package com.july.service;

import com.july.entity.Moment;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sherrypan on 16-5-27.
 */
public interface MomentService {

    boolean save(Moment moment);

    void deleteMomentById(BigInteger moment_id);
}
