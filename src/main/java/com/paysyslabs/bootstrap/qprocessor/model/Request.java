package com.paysyslabs.bootstrap.qprocessor.model;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Request implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4986959487628007513L;
    
    private String type;
    private String parameters;
    private Date date;
    private String queue;
    
    public static Request parse(String message) {
        try {
            String t = message.substring(0, message.indexOf('/'));
            String p = message.substring(message.indexOf("/") + 1);
            return new Request(t, p);
        } catch (Exception e) {
            
        }
        return null;
    }

    public Request() {
        super();
    }

    public Request(String type, String parameters) {
        super();
        this.type = type;
        this.parameters = parameters;
        this.date = new Date();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public List<String> getValueList() {
		List<String> decoded = new ArrayList<>();
		for (String encoded : parameters.split("/")) {
			try {
				decoded.add(URLDecoder.decode(encoded, "UTF-8"));
			} catch (Exception e) {
				
			}
		}
		return decoded;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
