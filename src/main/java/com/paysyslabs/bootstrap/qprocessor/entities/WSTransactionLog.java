package com.paysyslabs.bootstrap.qprocessor.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transactions_log")
public class WSTransactionLog {

    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "queue_in", length = 50)
    private String inboundQueue;

    @Column(name = "req_params", length = 500)
    private String requestParams;

    @Column(name = "response_code", length = 10)
    private String responseCode;

    @Column(name = "response_data", length = 5000)
    private String responseData;

    @Column(name = "response_desc", length = 100)
    private String responseDescription;

    @Column(name = "rsp_in")
    private Date responseIn;

    @Column(name = "rsp_out")
    private Date responseOut;

    @Column(name = "transaction_reference", length = 13)
    private String transactionReference;

    @Column(name = "tran_type", length = 70)
    private String transactionType;

    @Column(name = "iden", length = 50)
    private String iden;

    @Column(name = "amount", length = 50)
    private String amount;

    @Column(name = "stan", length = 20)
    private String stan;

    @Column(name = "to_account", length = 50)
    private String toAccount;

    @Column(name = "client_tran_ref", length = 20)
    private String clientTranRef;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInboundQueue() {
        return inboundQueue;
    }

    public void setInboundQueue(String inboundQueue) {
        this.inboundQueue = inboundQueue;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Date getResponseIn() {
        return responseIn;
    }

    public void setResponseIn(Date responseIn) {
        this.responseIn = responseIn;
    }

    public Date getResponseOut() {
        return responseOut;
    }

    public void setResponseOut(Date responseOut) {
        this.responseOut = responseOut;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getIden() {
        return iden;
    }

    public void setIden(String iden) {
        this.iden = iden;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getClientTranRef() {
        return clientTranRef;
    }

    public void setClientTranRef(String clientTranRef) {
        this.clientTranRef = clientTranRef;
    }

}
