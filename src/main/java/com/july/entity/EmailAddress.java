package com.july.entity;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * Created by sherrypan on 16-5-24.
 */
public final class EmailAddress {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    @Field("email")
    private final String value;

    public EmailAddress(String emailAddress) {
        Assert.isTrue(isValid(emailAddress), "Invalid email address!");
        this.value = emailAddress;
    }

    public static boolean isValid(String candidate) {
        return candidate == null ? false : PATTERN.matcher(candidate).matches();
    }

}
