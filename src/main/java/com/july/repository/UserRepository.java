package com.july.repository;

import com.july.entity.User;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by sherrypan on 16-5-25.
 */
public interface UserRepository extends Repository<User, Long> {

    List<User> findByEmailAddress();

    List<User> findByNickname();

}
