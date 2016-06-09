package com.july.repository;

import com.july.entity.Moment;
import com.july.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by sherrypan on 16-5-27.
 */
@Repository
public interface MomentRepository extends MongoRepository<Moment, BigInteger> {
    List<Moment> findByCreater(User user) ;
}
