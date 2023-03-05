package com.spt.k8s.client.service;

import com.spt.k8s.client.ClientException;
import com.spt.k8s.client.jms.SqsMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerCallingServiceImpl implements ServerCallingService {

    private static final String HTTP_CALLING_TASK = "HTTP periodic calling";
    private static final String MESSAGING_TASK = "SQS periodic messaging";
    private static final Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

    private final RestTemplate restTemplate;
    private final ScheduledExecutorService executorService;
    private final SqsMessageProducer messageProducer;

    @Value("${ping-pong.queue.name}")
    private String pingPongQ;

    @Override
    public void beginPeriodicHttpCalling(String targetUrl) {
        cancelIfRunning(tasks.get(HTTP_CALLING_TASK));
        var task = executorService.scheduleAtFixedRate(() -> {
            pingServer(targetUrl);
        }, 0, 5, TimeUnit.SECONDS);
        tasks.put(HTTP_CALLING_TASK, task);
    }

    @Override
    public void beginPeriodicMessaging() {
        cancelIfRunning(tasks.get(MESSAGING_TASK));
        var task = executorService.scheduleAtFixedRate(this::sendPingMessage, 0, 5, TimeUnit.SECONDS);

        tasks.put(MESSAGING_TASK, task);
    }

    @Override
    public void stopJobs() {
        tasks.forEach((k, v) -> v.cancel(false));
    }

    private void cancelIfRunning(ScheduledFuture<?> task) {
        if (Objects.nonNull(task)) {
            if (!task.isCancelled()) {
                task.cancel(true);
            }
        }
    }

    private void pingServer(String url) {
        ResponseEntity<String> response;
        try {
            log.debug("Client http request to server: PING");
            response = restTemplate.getForEntity(url + "messaging/ping", String.class);
            if (!HttpStatus.OK.equals(response.getStatusCode()) || Objects.isNull(response.getBody())) {
                throw new ClientException("Bad response from server");
            }
            log.debug("Server http response: {}", response.getBody());
        } catch (RestClientException | ClientException e) {
            log.error("An error occurred during server http ping: {}", e.getMessage());
        }
    }

    private void sendPingMessage() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Message-Type", "PING-PONG");
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        messageProducer.send("PING", headers, pingPongQ);
    }
}
