package com.spt.k8s.client.jms;

import com.spt.k8s.client.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SqsMessageProducer {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public SqsMessageProducer(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    public <T> void send(T message, Map<String, Object> headers, String queueName) {
        if (message == null) {
            log.warn("SQS Producer cant produce 'null' value");
            return;
        }

        try {
            queueMessagingTemplate.convertAndSend(queueName, message, headers);
            log.debug("Queue sent message: {}", message);
        } catch (MessagingException e) {
            throw new ClientException("An error occurred during message sending: " + e.getMessage());
        }
    }
}
