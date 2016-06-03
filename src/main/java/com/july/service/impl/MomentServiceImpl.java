package com.july.service.impl;

import com.july.entity.Moment;
import com.july.repository.MomentRepository;
import com.july.service.MomentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sherrypan on 16-5-27.
 */
@Service
public class MomentServiceImpl implements MomentService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MomentRepository momentRepository;

    @Override
    public boolean save(Moment moment) {
        if(moment != null){
            momentRepository.save(moment);
            return true;
        }
        return false;
    }

}
