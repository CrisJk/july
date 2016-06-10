package com.july.repository;

import com.july.entity.Account;
import com.july.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by sherrypan on 16-5-25.
 */
@Repository
public interface UserRepository extends MongoRepository<User, BigInteger> {

    List<User> findByEmail(String email);

    @Query("{'nickname' : { $regex : ?0, $options: 'i' } }")
    List<User> findByNickname(String nickname);

    List<User> findByFacebookAccount(Account account);

    List<User> findByGithubAccount(Account account);

    @Query("{'nickname' : { $regex : ?0, $options: 'i' } }")
    Page<User> findByNickname(String nickname,Pageable pageable);

    @Override
    void delete(User user);

}
