package com.paysyslabs.bootstrap.qprocessor.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paysyslabs.bootstrap.qprocessor.entities.WSHostDetail;
import com.paysyslabs.bootstrap.qprocessor.entities.WSParamDetail;
import com.paysyslabs.bootstrap.qprocessor.model.TransactionDetails;
import com.paysyslabs.bootstrap.qprocessor.repo.WSHostDetailRepository;
import com.paysyslabs.bootstrap.qprocessor.repo.WSParamDetailRepository;

@Configuration
public class ServiceConfig {
    
    @Autowired
    private WSParamDetailRepository paramDetailRepository;
    
    @Autowired
    private WSHostDetailRepository hostDetailRepository;

    @Bean("transactionMap")
    public Map<String, TransactionDetails> prepareTransactionMap() {
        Map<String, TransactionDetails> map = new HashMap<>();
        
        for (WSParamDetail detail : paramDetailRepository.findAll()) {
            map.put(detail.getTransactionType(), detail.getTransactionDetail());
        }
        
        return map;
    }
    
    @Bean("hostDetailsMap")
    public Map<Integer, WSHostDetail> prepareHostDetailMap() {
        Map<Integer, WSHostDetail> map = new HashMap<>();
        
        for (WSHostDetail detail : hostDetailRepository.findAll()) {
            map.put(detail.getId(), detail);
        }
        
        return map;
    }
    
}
