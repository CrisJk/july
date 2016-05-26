package com.july;

import com.july.entity.EmailAddress;
import com.july.entity.User;
import com.july.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by sherrypan on 16-5-26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(UserRepository.class)
public class UserRepositoryTests {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTests.class);

    @Autowired
    UserRepository userRepository;

    @Test
    public void saveTest() {
        User user1 = new User();
        user1.setEmailAddress(new EmailAddress("12345678@qq.com"));
        user1.setPassword("123456");
        user1.setNickname("Kitty");
        userRepository.save(user1);
        logger.info("user Kitty saved !");
    }
}
