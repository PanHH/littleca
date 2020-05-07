package com.taoyuanx.test;

import com.taoyuanx.auth.SimpleTokenManager;
import com.taoyuanx.auth.mac.HMacAlgorithms;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.sign.impl.HMacSign;
import com.taoyuanx.auth.sign.impl.RsaSign;
import com.taoyuanx.auth.sign.impl.Sm2Sign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.auth.utils.JSONUtil;
import com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthRequestDTO;
import com.taoyuanx.ca.auth.dto.EncodeRequestDTO;
import com.taoyuanx.ca.core.api.impl.SM2;
import com.taoyuanx.ca.core.util.CertUtil;
import com.taoyuanx.ca.core.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.File;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author dushitaoyuan
 * @desc 认证请求构造
 * @date 2020/2/25
 */
public class LittleAuthRequestTest {


    @Test
    public  void tokenCanceltest(){
        String m="eyJ0eXBlIjoxLCJjIjozLCJ1IjoiZHVzaGl0YW95dWFuLWhtYWMiLCJzIjoxNTg4ODM1ODkxNDI2LCJlIjoxNTg4ODQxMjkxNDI2fQ.QH_7HECy5wwr_kt81-WBn_zXVhzbDYA6y-55Xe-v8UA";
        HMacSign hMacSign=new HMacSign(HMacAlgorithms.HMAC_SHA_256,"dushitaoyuan".getBytes());
        SimpleTokenManager simpleTokenManager=new SimpleTokenManager(hMacSign);
        Token token = simpleTokenManager.parseToken(m);
        System.out.println(token.getEndTime());
        System.out.println(DigestUtils.md5Hex(m));
    }
    @Test
    public void hmacAuthRequestBuildTest() throws Exception {
        String apiAccount = "dushitaoyuan-hmac";
        String apiSercet = "dushitaoyuan";
        ISign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, apiSercet.getBytes("UTF-8"));
        AuthRequestDTO authRequestDTO = newAuthRequest(hMacSign, apiAccount);
        System.out.println(JSONUtil.toJsonString(authRequestDTO));
    }

    @Test
    public void hmacAuthRefreshRequestBuildTest() throws Exception {
        String apiSercet = "dushitaoyuan";
        ISign hMacSign = new HMacSign(HMacAlgorithms.HMAC_SHA_256, apiSercet.getBytes("UTF-8"));
        String refreshToken = "eyJhcGlJZCI6MSwiYXBpQWNjb3VudCI6ImR1c2hpdGFveXVhbiIsImNyZWF0ZVRpbWUiOjE1ODI2OTA4MTQ2NTYsImVuZFRpbWUiOjE1ODI2OTgwMTQ2NTYsInR5cGUiOjJ9.MYfi9e7ygm2ATy3GcRTdo7CUwM3Sg0NDNemWDGBjqjM";
        AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(hMacSign, refreshToken);
        System.out.println(JSONUtil.toJsonString(authRefreshRequestDTO));
    }

    @Test
    public void rsaAuthRequestBuildTest() throws Exception {
        String apiAccount = "dushitaoyuan-rsa";
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/rsa/client/client.p12", p12Password);
        RSAPrivateKey privateKey = (RSAPrivateKey) CertUtil.getPrivateKey(keyStore, p12Password, null);
        RSAPublicKey publicKey = (RSAPublicKey) CertUtil.getPublicKey(keyStore, null);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyPem("d:/cert/p12/rsa/server/server_pub.pem");
        ISign rsaSign = new RsaSign(publicKey, privateKey, signAlg);
        AuthRequestDTO authRequestDTO = newAuthRequest(rsaSign, apiAccount);
        String data = JSONUtil.toJsonString(authRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(RSAUtil.encryptByPublicKey(data, serverPublicKey));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    @Test
    public void rsaAuthRefreshRequestBuildTest() throws Exception {
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        String refreshToken = "xxxxx";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/rsa/client/client.p12", p12Password);
        RSAPrivateKey privateKey = RSAUtil.getPrivateKey(keyStore, p12Password);
        RSAPublicKey publicKey = RSAUtil.getPublicKey(keyStore);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyPem("d:/cert/p12/rsa/server/server_pub.pem");
        ISign rsaSign = new RsaSign(publicKey, privateKey, signAlg);
        AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(rsaSign, refreshToken);
        String data = JSONUtil.toJsonString(authRefreshRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(RSAUtil.encryptByPublicKey(data, serverPublicKey));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }

    @Test
    public void sm2AuthRequestBuildTest() throws Exception {
        String apiAccount = "dushitaoyuan-sm2";
        String p12Password = "123456";
        String signAlg = "SM3WITHSM2";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/sm2/client/client.p12", p12Password);
        PrivateKey privateKey = CertUtil.getPrivateKey(keyStore, p12Password, null);
        PublicKey publicKey = CertUtil.getPublicKey(keyStore, null);
        PublicKey serverPublicKey = CertUtil.readPublicKeyPem("d:/cert/p12/sm2/server/server_pub.pem");
        ISign sm2Sign = new Sm2Sign(publicKey, privateKey, signAlg);
        SM2 sm2 = new SM2();
        AuthRequestDTO authRequestDTO = newAuthRequest(sm2Sign, apiAccount);
        byte[] data = JSONUtil.toJsonBytes(authRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(Base64.encodeBase64URLSafeString(sm2.encrypt(data, serverPublicKey)));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }
    @Test
    public void sm2AuthRefreshRequestBuildTest() throws Exception {
        String p12Password = "123456";
        String signAlg = "SHA256WITHRSA";
        String refreshToken = "xxxx";
        KeyStore keyStore = CertUtil.readKeyStore("d:/cert/p12/sm2/client/client.p12", p12Password);
        RSAPrivateKey privateKey = RSAUtil.getPrivateKey(keyStore, p12Password);
        RSAPublicKey publicKey = RSAUtil.getPublicKey(keyStore);
        RSAPublicKey serverPublicKey = (RSAPublicKey) CertUtil.readPublicKeyPem("d:/cert/sm2/rsa/server/server_pub.pem");
        ISign rsaSign = new RsaSign(publicKey, privateKey, signAlg);
        AuthRefreshRequestDTO authRefreshRequestDTO = newAuthRefreshRequest(rsaSign, refreshToken);
        String data = JSONUtil.toJsonString(authRefreshRequestDTO);
        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();
        encodeRequestDTO.setData(RSAUtil.encryptByPublicKey(data, serverPublicKey));
        System.out.println(JSONUtil.toJsonString(encodeRequestDTO));
    }
    public AuthRequestDTO newAuthRequest(ISign signImpl, String apiAccount) throws Exception {
        String random = RandomCodeUtil.getRandCode(16);
        Long timestamp = System.currentTimeMillis();
        AuthRequestDTO authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setApiAccount(apiAccount);
        authRequestDTO.setRandom(random);
        authRequestDTO.setTimestamp(timestamp);
        String signStr = random + apiAccount + timestamp;
        String sign = signImpl.sign(signStr);
        authRequestDTO.setSign(sign);
        return authRequestDTO;
    }

    public AuthRefreshRequestDTO newAuthRefreshRequest(ISign signImpl, String refreshToken) throws Exception {
        AuthRefreshRequestDTO authRefreshRequestDTO = new AuthRefreshRequestDTO();
        authRefreshRequestDTO.setRefreshToken(refreshToken);
        String signStr = refreshToken;
        String sign = signImpl.sign(signStr);
        authRefreshRequestDTO.setSign(sign);
        return authRefreshRequestDTO;
    }
}
