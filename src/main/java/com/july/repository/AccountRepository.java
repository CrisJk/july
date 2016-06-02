package com.july.repository;

import com.july.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sherrypan on 16-6-2.
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, Long> {

}
