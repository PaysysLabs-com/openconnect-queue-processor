package com.paysyslabs.bootstrap.qprocessor.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.paysyslabs.bootstrap.qprocessor.entities.WSTransactionParam;
import com.paysyslabs.bootstrap.qprocessor.model.Request;
import com.paysyslabs.bootstrap.qprocessor.model.ServiceResponse;
import com.paysyslabs.bootstrap.qprocessor.model.TransactionDetails;
import com.paysyslabs.bootstrap.qprocessor.service.StorageService;

public class XMLUtils {
    
    public static String create(ServiceResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("<response>");
        builder.append(String.format("<response_code>%s</response_code>", response.getCode()));
        builder.append(String.format("<response_desc>%s</response_desc>", response.getDescription()));
        builder.append("</response>");
        return builder.toString();
    }

    public static String create(Request request, TransactionDetails details, StorageService service) {
        StringBuilder builder = new StringBuilder();
        List<WSTransactionParam> parameters = service.getTransactionParameters(details.getTransactionId());

        Map<String, String> kvMap = new HashMap<>();
        
        List<String> values = request.getValueList();
        
        for (int i=0; i<details.getAllowedRequestParameters().size() && i<values.size(); i++) {
            kvMap.put(details.getAllowedRequestParameters().get(i), values.get(i));
        }
        
        builder.append("<Request>");
        builder.append(String.format("<%s>%d</%s>", "tran_id", details.getTransactionId(), "tran_id"));
        builder.append(String.format("<%s>%s</%s>", "queue_in", details.getInQueue(), "queue_in"));
        
        for (WSTransactionParam param : parameters) {
            String val = kvMap.containsKey(param.getParameterName()) ? kvMap.get(param.getParameterName()) : param.getValue();
            val = StringUtils.rightPad(val, param.getMaxLength());
            builder.append(String.format("<%s>%s</%s>", param.getParameterName(), val, param.getParameterName()));
        }
        
        builder.append("</Request>");
        return builder.toString();
    }
    
}
