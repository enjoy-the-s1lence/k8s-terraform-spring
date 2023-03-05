package com.spt.k8s.client.controller;

import com.spt.k8s.client.service.ServerCallingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class HttpController {

    @Value("${k8sApp.services.server.name}")
    private String serverName;

    private final DiscoveryClient discoveryClient;
    private final ServerCallingService schedulerService;

    @PostMapping("/periodic-job")
    public ResponseEntity<String> beginServerCalling() {
        var serverInstance = discoveryClient.getInstances(serverName).get(0);
        var serverServiceUrl = "http://" + serverInstance.getHost() + ":" + serverInstance.getPort() + "/";
        schedulerService.beginPeriodicHttpCalling(serverServiceUrl);
        schedulerService.beginPeriodicMessaging();

        return ResponseEntity.ok("Server http calling started");
    }

    @PutMapping("/periodic-job")
    public ResponseEntity<String> stopPeriodicJobs() {
        schedulerService.stopJobs();

        return ResponseEntity.ok("Periodic jobs suspended");
    }
}
