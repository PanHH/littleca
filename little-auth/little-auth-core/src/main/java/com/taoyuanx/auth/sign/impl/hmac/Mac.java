package com.taoyuanx.auth.sign.impl.hmac;

/**
 * @author dushitaoyuan
 * @date 2020/3/1519:20
 */
public interface Mac {

    byte[] doFinal(byte[] var1);
}
