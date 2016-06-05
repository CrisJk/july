package com.july.repository;

import com.july.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * Created by sherrypan on 16-6-2.
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, BigInteger> {

}
