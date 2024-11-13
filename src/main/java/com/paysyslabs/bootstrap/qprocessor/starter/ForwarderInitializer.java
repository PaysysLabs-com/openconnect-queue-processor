package com.paysyslabs.bootstrap.qprocessor.starter;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.paysyslabs.queue.QueueForwarder;
import com.rabbitmq.client.Connection;

@Configuration
public class ForwarderInitializer {

    @Value("#{'${queue.config.forwarders.from}'.split(',')}")
    private List<String> fromQueues;
    
    @Value("${queue.config.forwarders.to}")
    private String toQueue;
    
    @Value("${queue.config.request.forwarders.from}")
    private String requestFromQueue;
    
    @Value("#{'${queue.config.request.forwarders.to}'.split(',')}")
    private List<String> requestToQueues;
    
    @Value("${queue.config.forwarders.prefetch.count}")
    private int prefetchCount;
    
    @Value("${queue.config.forwarders.auto.ack}")
    private boolean autoAck;
    
    @Autowired
    private Connection queueConnection;

    @PostConstruct
    public void bootstrap() throws Exception {
        if (fromQueues != null)
            for (String queue : fromQueues) {
                new QueueForwarder(
                        toQueue,
                        String.format("%s-forwarder", queue.toLowerCase()),
                        prefetchCount,
                        autoAck,
                        queueConnection.createChannel(),
                        queue
                    );
            }
    }
    
}
