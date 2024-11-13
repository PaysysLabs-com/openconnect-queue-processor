package com.paysyslabs.bootstrap.qprocessor.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paysyslabs.bootstrap.qprocessor.entities.WSHostDetail;
import com.paysyslabs.bootstrap.qprocessor.model.Request;
import com.paysyslabs.bootstrap.qprocessor.model.ServiceResponse;
import com.paysyslabs.bootstrap.qprocessor.model.TransactionDetails;
import com.paysyslabs.bootstrap.qprocessor.service.StorageService;
import com.paysyslabs.bootstrap.qprocessor.service.TokenService;
import com.paysyslabs.bootstrap.qprocessor.utils.XMLUtils;
import com.paysyslabs.queue.QueueWorker;
import com.paysyslabs.queue.SimpleQueueWriter;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class RequestConsumer extends QueueWorker {
    
    private static final Logger LOG = LoggerFactory.getLogger(RequestConsumer.class);

    private final TokenService tokenService;
    private final StorageService storageService;
    
    public RequestConsumer(TokenService tokenService, StorageService storageService, String workerName, int prefetchCount, boolean autoAck, Channel channel, String queue) throws Exception {
        super(workerName, prefetchCount, autoAck, channel, queue);
        
        this.tokenService = tokenService;
        this.storageService = storageService;
    }

    @Override
    public void handle(long tag, BasicProperties properties, byte[] body) throws Exception {
        String message = new String(body);
        LOG.info("{}", message);
        Request request = new Request();
        
        try {
        	request = Request.parse(message);
            
            if (request == null)
                throw new Exception("failure parsing request");
            
            LOG.info("RequestConsumer - Tag:{} - Type:{} - Queue:{} - ReplyTo:{} - CorrelationID:{}", tag, request.getType(), request.getQueue(), properties.getReplyTo(), properties.getCorrelationId());
            TransactionDetails details = storageService.getTransactionDetail(request.getType());
            
            if (details == null)
                throw new Exception("failed to find TransactionDetails for type: " + request.getType());
            
            request.setQueue(details.getInQueue());
            
            tokenService.store(properties.getCorrelationId(), request);
            
            String xml = XMLUtils.create(request, details, storageService);
            
            LOG.info("[XML] {}", xml);
            
            if (details.isRemote()) {
                WSHostDetail hostDetail = storageService.getHostDetail(details.getHostId());
                LOG.info("writing {} bytes to remote {} @ {}", xml.length(), details.getInQueue(), hostDetail.getHost());
                SimpleQueueWriter writer = storageService.getWriterForHost(details.getHostId());
                
                if (!writer.isBeingConsumed(details.getInQueue()))
                    throw new Exception(String.format("remote consumer for '%s' not running", details.getInQueue()));
                
                writer.publish(details.getInQueue(), properties, xml.getBytes("UTF-8"));
            } else {
                LOG.info("writing {} bytes to {}", xml.length(), details.getInQueue());
                
                if (!isBeingConsumed(details.getInQueue()))
                    throw new Exception(String.format("consumer for '%s' not running", details.getInQueue()));
                
                respond(details.getInQueue(), properties, xml.getBytes("UTF-8"));
            }
        
        } catch (Exception e) {
            respond(properties.getReplyTo(), properties, XMLUtils.create(new ServiceResponse("500", e.getMessage())).getBytes());
        } finally {
        	acknowledge(tag);
        	LOG.info("RequestConsumer - Acknowledged --> Tag:{} - Type:{} - Queue:{} - ReplyTo:{} - CorrelationID:{}", tag, request.getType(), request.getQueue(), properties.getReplyTo(), properties.getCorrelationId());
        }
    }

}
