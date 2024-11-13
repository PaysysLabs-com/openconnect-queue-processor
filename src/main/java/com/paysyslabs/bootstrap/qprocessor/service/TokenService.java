package com.paysyslabs.bootstrap.qprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.paysyslabs.bootstrap.qprocessor.model.Request;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Service
public class TokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);

    private final Cache requestCache;

    @Autowired
    public TokenService(CacheManager cacheManager, @Value("${cache.name}") String cacheName) {
        super();
        this.requestCache = cacheManager.getCache(cacheName);
    }
    
    @Scheduled(fixedRateString = "${cache.expiry}")
    public void evictExpiredTokens() {
        LOG.info("Evicting expired tokens");
        requestCache.evictExpiredElements();
    }
    
    public void store(String key, Request value) {
        requestCache.put(new Element(key, value));
    }
    
    public boolean contains(String key) {
        return requestCache.get(key) != null;
    }

    public Request retrieve(String key) {
        return (Request) requestCache.get(key).getObjectValue();
    }
    
    public void remove(String key) {
        requestCache.remove(key);
    }
}
