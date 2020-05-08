package com.taoyuanx.ca.auth.constants;

import com.taoyuanx.auth.TokenTypeEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @date 2020/5/6
 */
public enum AuthType {
    HMAC("hmac"),
    RSA("rsa"),
    SM2("sm2");
    public String value;

    AuthType(String value) {
        this.value = value;
    }

    public static AuthType type(String value) {
        value = value.toLowerCase();
        for (AuthType type : AuthType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
