package com.july.service;

import com.july.entity.Account;

import java.util.Map;

/**
 * Created by sherrypan on 16-6-1.
 */
public interface AccountService {

    Account getByTypeAndIdentity(String type, String id);

    Account saveOrUpdateAccount(String type, Map mp);

}
