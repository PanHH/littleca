package com.taoyuanx.ca.auth.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @desc 认证请求参数
 * @date 2020/2/17
 */
@Data
public class AuthRequestDTO implements com.taoyuanx.ca.auth.dto.ISignDTO {
    /**
     * 随机数
     */
    private String random;
    /**
     * api 账户
     */
    private String apiAccount;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * random+apiAccount+timestamp 签名
     */
    private String sign;

    @Override
    public String toSignStr() {
        return random + apiAccount + timestamp;
    }
}
