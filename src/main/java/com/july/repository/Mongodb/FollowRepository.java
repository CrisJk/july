package com.july.repository.Mongodb;

import com.july.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Crow on 2016/5/11.
 */
public interface FollowRepository extends MongoRepository<Follow, String> {
}
