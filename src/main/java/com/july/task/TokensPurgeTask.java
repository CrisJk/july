package com.july.task;

import com.july.entity.User;
import com.july.entity.VerificationToken;
import com.july.repository.UserRepository;
import com.july.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Created by sherrypan on 16-5-31.
 */
@Component
public class TokensPurgeTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoOperations mongoOperations;

    //delete expiryTokens everyday
    @Scheduled(initialDelay = 1000, fixedRate = 86400000)
    public void purgeExpired() {
        Date now = Date.from(Instant.now());
        boolean flag = false;
        List<VerificationToken> verificationTokens = mongoOperations.findAll(VerificationToken.class);
        for (int i = 0; i < verificationTokens.size(); i++) {
            VerificationToken vt = verificationTokens.get(i);
            if (vt.getExpiryDate().before(now) || vt.getExpiryDate().equals(now)) {
                User user = vt.getUser();
                if (user != null) {
                    userRepository.delete(user);
                    flag = true;
                }
                verificationTokenRepository.delete(vt);
            }
        }
        if (flag == true) {
            logger.info("Delete expired users !");
            logger.info("Delete expired verification tokens !");
        }
        logger.info("Purging tokens Task Finished !");
    }

}
