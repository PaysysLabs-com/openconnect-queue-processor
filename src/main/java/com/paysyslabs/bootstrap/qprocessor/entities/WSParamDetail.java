package com.paysyslabs.bootstrap.qprocessor.entities;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.paysyslabs.bootstrap.qprocessor.model.TransactionDetails;

@Entity
@Table(name = "ws_req_param_details")
public class WSParamDetail {

    @Id
    private Long id;

    @Column(name = "tran_id")
    private Integer transactionId;

    @Column(name = "tran_type")
    private String transactionType;

    @Column(name = "req_params")
    private String requestParams;

    @Column(name = "queue_in")
    private String inboundQueue;

    @Column(name = "queue_type")
    private String queueType;

    @Column(name = "host_id")
    private Integer hostId;

    @Column(name = "from_ip")
    private String allowedIps;

    @Column(name = "reversal_api")
    private String reversalApi;
    
    public TransactionDetails getTransactionDetail() {
        TransactionDetails details = new TransactionDetails();
        details.setTransactionId(transactionId);
        
        if (allowedIps != null)
            details.setAllowedIps(Arrays.asList(allowedIps.split(",")));
        
        if (requestParams != null)
            details.setAllowedRequestParameters(Arrays.asList(requestParams.split(",")));
        
        details.setInQueue(inboundQueue);
        details.setQueueType(queueType);
        details.setHostId(hostId);
        details.setReversalApi(reversalApi);
        return details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getInboundQueue() {
        return inboundQueue;
    }

    public void setInboundQueue(String inboundQueue) {
        this.inboundQueue = inboundQueue;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getAllowedIps() {
        return allowedIps;
    }

    public void setAllowedIps(String allowedIps) {
        this.allowedIps = allowedIps;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

	public String getReversalApi() {
		return reversalApi;
	}

	public void setReversalApi(String reversalApi) {
		this.reversalApi = reversalApi;
	}
}
