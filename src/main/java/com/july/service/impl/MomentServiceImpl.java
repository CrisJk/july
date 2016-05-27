package com.july.service.impl;

import com.july.entity.Moment;
import com.july.repository.MomentRepository;
import com.july.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sherrypan on 16-5-27.
 */
@Service
public class MomentServiceImpl implements MomentService {

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
