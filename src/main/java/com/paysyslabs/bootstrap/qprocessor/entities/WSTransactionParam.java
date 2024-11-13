package com.paysyslabs.bootstrap.qprocessor.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tran_req_map")
public class WSTransactionParam {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "tran_id")
    private Integer transactionId;

    @Column(name = "param_name")
    private String parameterName;

    @Column(name = "value")
    private String value;

    @Column(name = "max_length")
    private String maxLengthString;

    @Column(name = "log_column")
    private String logColumn;

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

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMaxLengthString() {
        return maxLengthString;
    }

    public void setMaxLengthString(String maxLengthString) {
        this.maxLengthString = maxLengthString;
    }

    public Integer getMaxLength() {
        try {
            return Integer.parseInt(maxLengthString);
        } catch (Exception e) {
        }
        return 0;
    }

    public String getLogColumn() {
        return logColumn;
    }

    public void setLogColumn(String logColumn) {
        this.logColumn = logColumn;
    }

}
