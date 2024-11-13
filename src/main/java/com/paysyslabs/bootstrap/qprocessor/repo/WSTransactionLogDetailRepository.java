package com.paysyslabs.bootstrap.qprocessor.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paysyslabs.bootstrap.qprocessor.entities.WSTransactionLogDetail;

@Repository
public interface WSTransactionLogDetailRepository extends CrudRepository<WSTransactionLogDetail, Long> {

}
