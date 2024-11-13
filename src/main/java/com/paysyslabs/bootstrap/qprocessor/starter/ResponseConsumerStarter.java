package com.paysyslabs.bootstrap.qprocessor.starter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.paysyslabs.bootstrap.qprocessor.consumers.ResponseConsumer;
import com.paysyslabs.bootstrap.qprocessor.service.StorageService;
import com.paysyslabs.bootstrap.qprocessor.service.TokenService;
import com.rabbitmq.client.Connection;

@Configuration
@ConditionalOnBean(value = { RequestConsumerStarter.class })
public class ResponseConsumerStarter {
    
    @Value("${queue.config.queue.response}")
    private String responseQueue;
    
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
        new ResponseConsumer(tokenService, storageService, "out-worker", prefetchCount, autoAck, queueConnection.createChannel(), responseQueue);
    }
}
