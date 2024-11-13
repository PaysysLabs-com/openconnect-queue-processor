package com.paysyslabs.bootstrap.qprocessor.starter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.paysyslabs.bootstrap.qprocessor.consumers.RequestConsumer;
import com.paysyslabs.bootstrap.qprocessor.service.StorageService;
import com.paysyslabs.bootstrap.qprocessor.service.TokenService;
import com.rabbitmq.client.Connection;

@Configuration
public class RequestConsumerStarter {
    
    @Value("${queue.config.queue.request}")
    private String requestQueue;
    
    @Value("${queue.config.prefetch.count}")
    private int prefetchCount;
    
    @Value("${queue.config.auto.ack}")
    private boolean autoAck;
    
    @Autowired
    private Connection queueConnection;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private StorageService storageService;

    @PostConstruct
    public void bootstrap() throws Exception {
        new RequestConsumer(tokenService, storageService, "in-worker", prefetchCount, autoAck, queueConnection.createChannel(), requestQueue);
    }
}
