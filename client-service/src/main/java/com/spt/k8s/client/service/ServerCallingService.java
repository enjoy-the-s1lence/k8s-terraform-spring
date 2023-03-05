package com.spt.k8s.client.service;

public interface ServerCallingService {

    void beginPeriodicHttpCalling(String url);

    void beginPeriodicMessaging();

    void stopJobs();
}
