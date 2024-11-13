package com.paysyslabs.bootstrap.qprocessor.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactions_log_detail")
public class WSTransactionLogDetail {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "param_key", length = 50)
    private String paramKey;

    @Column(name = "param_value", length = 100)
    private String paramValue;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transactions_log_id")
    private WSTransactionLog transactionsLog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public WSTransactionLog getTransactionsLog() {
        return transactionsLog;
    }

    public void setTransactionsLog(WSTransactionLog transactionsLog) {
        this.transactionsLog = transactionsLog;
    }
        
}
