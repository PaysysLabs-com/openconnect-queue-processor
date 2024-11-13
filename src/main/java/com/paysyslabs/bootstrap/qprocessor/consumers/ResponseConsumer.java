package com.paysyslabs.bootstrap.qprocessor.consumers;

import com.paysyslabs.bootstrap.qprocessor.model.Request;
import com.paysyslabs.bootstrap.qprocessor.service.StorageService;
import com.paysyslabs.bootstrap.qprocessor.service.TokenService;
import com.paysyslabs.queue.QueueWorker;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class ResponseConsumer extends QueueWorker {

    private final TokenService tokenService;
    private final StorageService storageService;

    public ResponseConsumer(TokenService tokenService, StorageService storageService, String workerName, int prefetchCount, boolean autoAck, Channel channel, String queue) throws Exception {
    	super(workerName, prefetchCount, autoAck, channel, queue);
    	
        this.tokenService = tokenService;
        this.storageService = storageService;
    }

    @Override
    public void handle(long tag, BasicProperties properties, byte[] body) throws Exception {
        Request request = new Request();
        
        try {
        	request = tokenService.retrieve(properties.getCorrelationId());        	
        	
        	LOG.info("ResponseConsumer - Tag:{} - Type:{} - Queue:{} - ReplyTo:{} - CorrelationID:{}", tag, request.getType(), request.getQueue(), properties.getReplyTo(), properties.getCorrelationId());
        	LOG.info("inserting in transactions_log for correlation_id='{}'", properties.getCorrelationId() );
            storageService.createAndSaveLog(properties.getCorrelationId(), tokenService, request, body);
            LOG.info("inserted in transactions_log for correlation_id='{}'", properties.getCorrelationId() );
            
            respond(properties.getReplyTo(), properties, body);
            
        } catch (Exception ex) {
        	LOG.error("{}", ex);
        } finally {
        	acknowledge(tag);
        	LOG.info("ResponseConsumer - Acknowledged --> Tag:{} - Type:{} - Queue:{} - ReplyTo:{} - CorrelationID:{}", tag, request.getType(), request.getQueue(), properties.getReplyTo(), properties.getCorrelationId());
        } 
        
    }

}
