package com.paysyslabs.bootstrap.qprocessor.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paysyslabs.bootstrap.qprocessor.entities.WSHostDetail;

@Repository
public interface WSHostDetailRepository extends CrudRepository<WSHostDetail, Integer> {

}
