package com.july.service;

import com.july.entity.Moment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sherrypan on 16-5-27.
 */
public interface MomentService {

    boolean save(Moment moment);

    void deleteMomentById(BigInteger moment_id);

    int findMomentCountByContentInPage(String content);

    Page<Moment> findMomentByContentInPage(String content, Pageable pageable);

}
