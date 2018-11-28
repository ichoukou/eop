/**
 * RsaUtils.java
 * Created at Sep 29, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.client.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import net.ytoec.kernel.common.Base64Utils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * ClassName: RsaUtils
 * </p>
 * <p>
 * Description: RSA数字认证工具类
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Sep 29, 2013
 * </p>
 */
public class RsaUtils {

    /**
     * <p>
     * Field LOG: 日志
     * </p>
     */
    private final static Logger LOG = LoggerFactory.getLogger(RsaUtils.class);

    /**
     * <p>
     * Field modulePuk: 公钥module节点
     * </p>
     */
    private static final String MODULEPUK = "2cuxmVHFfWTI4EXEGOZRnUaCFhcC4x5uL+gMk8V8g6Rp4p64TWHx/ykUr18Ma6fkGU9xLd24UNgUPazhnIJ8PzFiSvSU6AWU1wO0DDsYB5o9b3QPYD4I9ZUwz6XOyE9oaWIh20sfWf0iQ5lcjcVpwcvteKI2e1Q5pzJKwUAD5hc=";

    /**
     * <p>
     * Field exponentPublic: 公钥Exponent节点
     * </p>
     */
    private static final String EXPONENTPUK = "AQAB";

    /**
     * <p>
     * Field modulePrk: 私钥module节点
     * </p>
     */
    private static final String MODULEPRK = "treJ6Z1FraRgPP5V0PzzWD0tdgX+hlzvte9Sy7lL4ryPw73iDePJeSNxAhgjTWuIjXrca5ICQdx/O9/Ll3wC+yq2jBfSIrS2LOOwG50sfP8JFwQJLCDjmQrvW3k3WX96xO8tzlZJ768/xPJah+bc8gW2+ELrJ9K+DjzDZhLMWXM=";

    /**
     * <p>
     * Field exponetPrivate: exponetPrivate 公钥 D节点
     * </p>
     */
    private static final String EXPONENTPRK = "JSC0eHEJyJf/KkWwIHBV7lc4FqRvLNRTrU99LoJvhho0yFycQ6BGv0PRYdCP09qG++C8S2t24/UvMXZvQ0/ittfs1yagFyK3PE5MbRgNZbh561BfV6PGLazSum77oFNk8jLQfICdJ2/SoZO42evIeTmfx1sPS3O5dF3RXTSBuzk=";

    /**
     * <p>
     * Description: 生成公钥
     * </p>
     * 
     * @return PublicKey
     */
    public static PublicKey getPublicKey() {
        LOG.info("create PublicKey begin.................");
        byte[] modulusBytes;
        PublicKey pubKey = null;
        try {
            modulusBytes = Base64.decode(MODULEPUK);
            byte[] exponentBytes = Base64.decode(EXPONENTPUK);
            BigInteger modulus = new BigInteger(1, modulusBytes);
            BigInteger exponent = new BigInteger(1, exponentBytes);
            RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            pubKey = fact.generatePublic(rsaPubKey);
        } catch (InvalidKeySpecException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        }
        LOG.info("create PublicKey end.................");
        return pubKey;
    }

    /**
     * <p>
     * Description: 生成私钥
     * </p>
     * 
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey() {
        LOG.info("create PrivateKey begin.................");
        byte[] modulusBytes;
        PrivateKey priKey = null;
        try {
            modulusBytes = Base64.decode(MODULEPRK);
            byte[] exponentBytes = Base64.decode(EXPONENTPRK);
            BigInteger modulus = new BigInteger(1, modulusBytes);
            BigInteger exponent = new BigInteger(1, exponentBytes);
            RSAPrivateKeySpec rsaPriKey = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            priKey = fact.generatePrivate(rsaPriKey);
        } catch (InvalidKeySpecException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        }
        LOG.info("create PrivateKey end.................");
        return priKey;
    }

    /**
     * <p>
     * Description: RSA 签名方法 私钥签名
     * </p>
     * 
     * @param plainText 要签名的明文
     * @return 签名后的密文
     */
    public static String sign(String plainText) {
        LOG.info("sign begin.................");
        try {
            // 明文MD5加密转Base64再签名
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            plainText = Base64Utils.encodeToString(md5.digest(plainText.getBytes("GB2312")));
            PrivateKey key = getPrivateKey();
            java.security.Signature sign;
            sign = java.security.Signature.getInstance("SHA1withRSA");
            sign.initSign(key);
            sign.update(plainText.getBytes());
            return Base64Utils.encodeToString(sign.sign());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (SignatureException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        }
        LOG.info("sign end.................");
        return null;
    }

    /**
     * <p>
     * Description: RSA验证方法，公钥验证
     * </p>
     * 
     * @param plainText 明文
     * @param signed 签名
     * @return
     */
    public static boolean verify(String plainText, String signed) {
        LOG.info("verify begin.................");
        boolean ret = false;
        try {
            // 明文MD5加密转Base64再签名
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            plainText = Base64Utils.encodeToString(md5.digest(plainText.getBytes("GB2312")));
            PublicKey key = getPublicKey();

            java.security.Signature signatureChecker;
            signatureChecker = java.security.Signature.getInstance("SHA1withRSA");
            signatureChecker.initVerify(key);

            byte[] decodebase64Sign = Base64Utils.decodeToByteArray(signed.getBytes("GB2312"));

            signatureChecker.update(plainText.getBytes());
            if (signatureChecker.verify(decodebase64Sign)) {
                ret = true;
            }
        } catch (NoSuchAlgorithmException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (SignatureException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            LOG.error("error message:" + e.getMessage());
            e.printStackTrace();
        }
        LOG.info("verify end.................");
        return ret;
    }
}
