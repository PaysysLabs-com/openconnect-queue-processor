package com.paysyslabs.bootstrap.qprocessor.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paysyslabs.bootstrap.qprocessor.entities.WSTransactionParam;

@Repository
public interface WSTransactionParamRepository extends CrudRepository<WSTransactionParam, Long> {
    public List<WSTransactionParam> findByTransactionId(Integer transactionId);
}
