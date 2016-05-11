package com.july.repository.Mongodb;

import com.july.entity.Timeline;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Crow on 2016/5/11.
 */
public interface TimelineRepository  extends MongoRepository<Timeline, String> {

}
