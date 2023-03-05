package com.spt.k8s.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messaging")
@Slf4j
public class HttpController {

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        log.debug("Got http request from client");
        return ResponseEntity.ok("PONG");
    }
}
