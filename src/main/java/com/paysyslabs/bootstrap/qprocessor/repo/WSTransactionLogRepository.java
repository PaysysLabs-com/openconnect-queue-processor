package com.paysyslabs.bootstrap.qprocessor.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paysyslabs.bootstrap.qprocessor.entities.WSTransactionLog;

@Repository
public interface WSTransactionLogRepository extends CrudRepository<WSTransactionLog, Long> {

}
