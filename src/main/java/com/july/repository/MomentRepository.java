package com.july.repository;

import com.july.entity.Moment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sherrypan on 16-5-27.
 */
@Repository
public interface MomentRepository extends MongoRepository<Moment, Long> {

}
