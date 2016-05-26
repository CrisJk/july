package com.july.repository;

import com.july.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sherrypan on 16-5-25.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findByEmailAddress(String emailAddress);

    List<User> findByNickname(String nickname);

}
