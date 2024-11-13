package com.paysyslabs.bootstrap.qprocessor.model;

public class ServiceResponse {

    private String code;
    private String description;

    public ServiceResponse(String code, String description) {
        super();
        this.code = code;
        this.description = description;
    }

    public ServiceResponse() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
