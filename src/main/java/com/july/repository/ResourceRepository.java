package com.july.repository;

import com.july.entity.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * Created by sherrypan on 16-6-4.
 */
@Repository
public interface ResourceRepository extends MongoRepository<Resource, BigInteger> {

}
