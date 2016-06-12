package com.july.service.Impl;

import com.july.entity.Moment;
import com.july.repository.MomentRepository;
import com.july.service.MomentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

    @Override
    public Moment getMomentById(BigInteger id) {
        return  momentRepository.findOne(id) ;
    }


    @Override
    public void deleteMomentById(BigInteger moment_id) {
        Moment tmpMoment = momentRepository.findOne(moment_id);
        tmpMoment.setStatus(false);
        momentRepository.save(tmpMoment);
    }

    @Override
    public int findMomentCountByContentInPage(String content) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(content);
        List<Moment> momentList = momentRepository.findAllBy(criteria);
        if (momentList == null) {
            return 0;
        }
        return momentList.size();
    }

    @Override
    public Page<Moment> findMomentByContentInPage(String content, Pageable pageable) {
//        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(content);
        Page<Moment> momentPage = momentRepository.findAllBy(criteria, pageable);
        return momentPage;
    }

}
