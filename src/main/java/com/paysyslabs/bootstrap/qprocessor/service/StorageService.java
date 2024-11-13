package com.paysyslabs.bootstrap.qprocessor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.paysyslabs.bootstrap.qprocessor.entities.WSHostDetail;
import com.paysyslabs.bootstrap.qprocessor.entities.WSTransactionLog;
import com.paysyslabs.bootstrap.qprocessor.entities.WSTransactionLogDetail;
import com.paysyslabs.bootstrap.qprocessor.entities.WSTransactionParam;
import com.paysyslabs.bootstrap.qprocessor.model.Request;
import com.paysyslabs.bootstrap.qprocessor.model.TransactionDetails;
import com.paysyslabs.bootstrap.qprocessor.repo.WSTransactionLogDetailRepository;
import com.paysyslabs.bootstrap.qprocessor.repo.WSTransactionLogRepository;
import com.paysyslabs.bootstrap.qprocessor.repo.WSTransactionParamRepository;
import com.paysyslabs.queue.QueueConfig;
import com.paysyslabs.queue.QueueConfigBuilder;
import com.paysyslabs.queue.SimpleQueueWriter;
import com.rabbitmq.client.Connection;

import net.jodah.lyra.config.RecoveryPolicies;
import net.jodah.lyra.config.RetryPolicies;
import net.jodah.lyra.util.Duration;

@Service
public class StorageService {

    protected static final Logger LOG = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    @Qualifier("transactionMap")
    private Map<String, TransactionDetails> transactionMap;

    @Autowired
    @Qualifier("hostDetailsMap")
    private Map<Integer, WSHostDetail> hostDetailsMap;

    @Autowired
    private WSTransactionLogRepository transactionLogRepository;

    @Autowired
    private WSTransactionLogDetailRepository transactionLogDetailRepository;

    @Autowired
    private WSTransactionParamRepository transactionParamRepository;

    private ConcurrentMap<Integer, SimpleQueueWriter> remoteWriters = new ConcurrentHashMap<>();

    @Value("${queue.config.heartbeat}")
    private Integer heartBeat;

    @Value("${queue.config.retry.interval}")
    private Integer retryInterval;

    public List<WSTransactionParam> getTransactionParameters(Integer transactionId) {
        return transactionParamRepository.findByTransactionId(transactionId);
    }

    public TransactionDetails getTransactionDetail(String type) {
        return transactionMap.get(type);
    }

    public WSTransactionLog saveLog(WSTransactionLog log) {
        return transactionLogRepository.save(log);
    }

    public void saveLog(List<WSTransactionLogDetail> wSTransactionLogDetailList) {
        transactionLogDetailRepository.saveAll(wSTransactionLogDetailList);
    }

    public WSHostDetail getHostDetail(Integer host) {
        return hostDetailsMap.get(host);
    }

    public String getLogColumnValue(List<WSTransactionParam> tranReqMap, TransactionDetails details,
            List<String> paramValueList, String columnName) {

        String identifier = null;

        try {
            WSTransactionParam tranReqIden = tranReqMap.stream()
                    .filter(x -> x.getLogColumn() !=null && x.getLogColumn().equals(columnName)).findAny()
                    .orElseThrow(NullPointerException::new);
            
            int idenIndex = details.getAllowedRequestParameters().indexOf(tranReqIden.getParameterName());
            
            return paramValueList.get(idenIndex);

        } catch (Exception ex) {
            LOG.error("getLogColumnValue error --> {} : {}", columnName, ex.toString());
        }

        return identifier;
    }

    @Async
    public void createAndSaveLog(String id, TokenService tokenService, Request request, byte[] body) {

        if (request == null)
            return;

        if (request.getParameters() == null)
            return;

        tokenService.remove(id);

        if (body == null)
            return;

        String message = new String(body);

        String responseCode = StringUtils.substringBetween(message, "<response_code>", "</response_code>");
        String responseDesc = StringUtils.substringBetween(message, "<response_desc>", "</response_desc>");
        String transactionRef = StringUtils.substringBetween(message, "<tran_ref>", "</tran_ref>");

        String requestData = request.getParameters().length() > 5000 ? request.getParameters().substring(0, 5000)
                : request.getParameters();
        String responseData = message.length() > 5000 ? message.substring(0, 5000) : message;

        TransactionDetails details = getTransactionDetail(request.getType());
        List<WSTransactionParam> tranReqMap = getTransactionParameters(details.getTransactionId());
        List<String> paramValueList = Arrays.asList(request.getParameters().split("/"));

        WSTransactionLog log = new WSTransactionLog();
        log.setInboundQueue(request.getQueue());
        log.setRequestParams(requestData);
        log.setResponseCode(responseCode);
        log.setResponseData(responseData);
        log.setResponseDescription(responseDesc);
        log.setResponseIn(request.getDate());
        log.setResponseOut(new Date());
        log.setTransactionReference(transactionRef);
        log.setTransactionType(request.getType());
        log.setIden(getLogColumnValue(tranReqMap, details, paramValueList, "iden"));
        log.setAmount(getLogColumnValue(tranReqMap, details, paramValueList, "amount"));
        log.setStan(getLogColumnValue(tranReqMap, details, paramValueList, "stan"));
        log.setToAccount(getLogColumnValue(tranReqMap, details, paramValueList, "to_account"));

        List<WSTransactionLogDetail> wSTransactionLogDetailList = new ArrayList<WSTransactionLogDetail>();

        int i = 0;
        for (String paramKey : details.getAllowedRequestParameters()) {

            WSTransactionLogDetail wSTransactionLogDetail = new WSTransactionLogDetail();

            wSTransactionLogDetail.setParamKey(paramKey);
            wSTransactionLogDetail.setParamValue(paramValueList.get(i));
            wSTransactionLogDetail.setTransactionsLog(log);

            wSTransactionLogDetailList.add(wSTransactionLogDetail);

            i++;
        }

        saveLog(log);
        saveLog(wSTransactionLogDetailList);
    }

    public synchronized SimpleQueueWriter getWriterForHost(Integer host) throws Exception {
        if (!remoteWriters.containsKey(host)) {

            WSHostDetail details = hostDetailsMap.get(host);

            if (details == null)
                throw new Exception("No connection details found for hostId: " + host);

            // create a new connection
            QueueConfig config = new QueueConfigBuilder().withHost(details.getHost())
                    .withUsername(details.getUsername()).withPassword(details.getPassword())
                    .withRequestedHeartbeat(heartBeat).withRecoveryPolicy(RecoveryPolicies.recoverAlways())
                    .withRetryPolicy(RetryPolicies.retryAlways().withInterval(Duration.seconds(retryInterval))).build();

            Connection connection = config.createConnection();

            remoteWriters.put(host, new SimpleQueueWriter(connection.createChannel()));
        }

        return remoteWriters.get(host);
    }

}
