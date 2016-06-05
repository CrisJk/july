package com.july.repository.Mongodb;

import com.july.entity.follow;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Crow on 2016/5/11.
 */
public interface FollowRepository extends MongoRepository<follow, String> {
}
