package com.paysyslabs.bootstrap.qprocessor.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paysyslabs.bootstrap.qprocessor.entities.WSParamDetail;

@Repository
public interface WSParamDetailRepository extends CrudRepository<WSParamDetail, Long> {

}
