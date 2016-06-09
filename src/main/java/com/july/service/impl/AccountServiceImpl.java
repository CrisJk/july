package com.july.service.Impl;

import com.july.entity.Account;
import com.july.repository.AccountRepository;
import com.july.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by sherrypan on 16-6-1.
 */
@Service
public class AccountServiceImpl implements AccountService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public Account getByTypeAndIdentity(String type, String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(type).and("identity").is(id));
        return mongoOperations.findOne(query, Account.class);
    }

    @Override
    public Account saveOrUpdateAccount(String type, Map mp) {
        String identity, link, name;
        if (type == "github") {
            identity = mp.get("id").toString();
            link = (String) mp.get("html_url");
            name = (String) mp.get("name");
        } else {
            identity = (String) mp.get("id");
            link = (String) mp.get("link");
            name = (String) mp.get("name");
        }
        Account account = this.getByTypeAndIdentity(type, identity);
        if (account != null) {
            logger.info("Update the " + type + " account information.");
        } else {
            logger.info("Create a new " + type + " account information.");
            account = new Account();
        }
        account.setType(type);
        account.setIdentity(identity);
        account.setLink(link);
        account.setName(name);
        return accountRepository.save(account);
    }

}
