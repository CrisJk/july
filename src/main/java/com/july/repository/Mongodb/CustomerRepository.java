
//test mongodb connnect
package com.july.repository.Mongodb;

/**
 * Created by kuangjun on 2016/5/7.
 */
import java.util.List;

import com.july.entity.Timeline;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Timeline, String> {

    public Timeline findByFirstName(String firstName);
    public List<Timeline> findByLastName(String lastName);

}
