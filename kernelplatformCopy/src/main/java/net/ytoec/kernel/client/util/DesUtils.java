/**
 * DesUtils.java
 * Created at Sep 29, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.client.util;

//import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import net.ytoec.kernel.common.Base64Utils;
//import net.ytoec.kernel.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * ClassName: DesUtils
 * </p>
 * <p>
 * Description: DES工具类
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Sep 29, 2013
 * </p>
 */
public class DesUtils {

    /**
     * <p>
     * Field LOG: 日志
     * </p>
     */
    private final static Logger LOG = LoggerFactory.getLogger(DesUtils.class);

    //    /**
    //     * <p>
    //     * Field CONFIGPATH: 配置文件路径
    //     * </p>
    //     */
    //    private static String CONFIGPATH;

    /**
     * <p>
     * Field Algorithm: 定义 加密算法,可用
     * </p>
     */
    private static final String TYPE = "DESede"; // 定义 加密算法,可用

    //    /**
    //     * <p>
    //     * Field FILENAME: 配置文件名称
    //     * </p>
    //     */
    //    private static final String FILENAME = "edi.properties";

    /**
     * <p>
     * Field keyValue: DES密钥
     * </p>
     */
    private static volatile String keyValue;

    /**
     * <p>
     * Description: DES密钥
     * </p>
     * 
     * @return the keyValue
     */
    public static String getKeyValue() {
        return keyValue;
    }

    /**
     * <p>
     * Description: DES密钥
     * </p>
     * 
     * @param keyValue the keyValue to set
     */
    public static void setKeyValue(String kv) {
        keyValue = kv;
    }

    //    /**
    //     * <p>
    //     * Description: 返回工程路径
    //     * </p>
    //     * 
    //     * @return 工程classes路径
    //     */
    //    private String getPath() {
    //        return Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("%20", " ");
    //    }
    //
    //    /**
    //     * <p>
    //     * Description: 获取当前项目的绝对路径
    //     * </p>
    //     * 
    //     * @param classesPath 工程路径
    //     * @return
    //     */
    //    private static String getPorjectPath(String classesPath) {
    //        String tempdir;
    //        String classPath[] = classesPath.split("webapp");
    //        tempdir = classPath[0];
    //        if (!"/".equals(tempdir.substring(tempdir.length()))) {
    //            tempdir+="resources/";
    //            tempdir += File.separator;
    //        }
    //        LOG.info("edi.properties  path------------：" + tempdir);
    //        return tempdir;
    //    }

    //    /**
    //     * <p>
    //     * Description: 获取键值
    //     * </p>
    //     * 
    //     * @param key 键
    //     * @return 值
    //     */
    //    public static String getCfgValue(String key) {
    //        DesUtils utils = new DesUtils();
    //        if (StringUtils.isBlank(CONFIGPATH)) {
    //            CONFIGPATH = getPorjectPath(utils.getPath()) + FILENAME;
    //            LOG.info("propertityPath：" + CONFIGPATH);
    //        }
    //        String kv = PropertiesUtil.readValue(CONFIGPATH, key);
    //        return kv;
    //    }

    /**
     * <p>
     * Description: 生成MD5Base64密钥
     * </p>
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static byte[] CreateMD5Base64CodeKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        LOG.info("CreateMD5Base64CodeKey begin ................");
        // 获取配置文件路径
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5key = md5.digest(keyValue.getBytes("UTF-8"));
        return Base64Utils.encodeToByteArray(md5key);
    }

    /**
     * <p>
     * Description: 加密
     * </p>
     * 
     * @param painText 明文
     * @return 密文
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String encryptMode(String painText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (StringUtils.isBlank(painText)) {
            return null;
        }
        LOG.info("encryptMode begin ................");
        byte[] keybyte = CreateMD5Base64CodeKey();
        String enCode = encryptMode(keybyte, painText.getBytes("UTF-8"));
        LOG.info("result :................" + enCode);
        return enCode;
    }

    /**
     * <p>
     * Description: 解密
     * </p>
     * 
     * @param enCode 密文
     * @return 明文
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String decryptMode(String enCode) throws NoSuchAlgorithmException, IOException {
        if (StringUtils.isBlank(enCode)) {
            return null;
        }
        LOG.info("decryptMode begin ................");
        byte[] keybyte = CreateMD5Base64CodeKey();
        String deCode = decryptMode(keybyte, enCode);
        LOG.info("result :................" + deCode);
        return deCode;

    }

    /**
     * <p>
     * Description: DES加密
     * </p>
     * 
     * @param keybyte 为加密密钥，长度为24字节
     * @param src为被加密的数据缓冲区（源）
     * @return
     */
    private static String encryptMode(byte[] keybyte, byte[] src) {

        try {
            SecretKey deskey = new SecretKeySpec(keybyte, TYPE);
            // 加密
            Cipher c1 = Cipher.getInstance(TYPE);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] b = c1.doFinal(src);
            return Base64Utils.encodeToString(b);

        } catch (java.security.NoSuchAlgorithmException e) {
            LOG.error("encryptMode error message ：" + e.getMessage());
            e.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e) {
            LOG.error("encryptMode error message ：" + e.getMessage());
            e.printStackTrace();
        } catch (java.lang.Exception e) {
            LOG.error("encryptMode error message ：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>
     * Description: DES解密
     * </p>
     * 
     * @param keybyte keybyte为加密密钥，长度为24字节
     * @param enCode 为加密后的缓冲区
     * @return
     * @throws IOException
     */
    private static String decryptMode(byte[] keybyte, String enCode) throws IOException {
        byte[] src = Base64Utils.decodeToByteArray(enCode.getBytes("UTF-8"));
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, TYPE);
            // 加密
            Cipher c1 = Cipher.getInstance(TYPE);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return new String(c1.doFinal(src));
        } catch (java.security.NoSuchAlgorithmException e) {
            LOG.error("decryptMode error message ：" + e.getMessage());
            e.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e) {
            LOG.error("decryptMode error message ：" + e.getMessage());
            e.printStackTrace();
        } catch (java.lang.Exception e) {
            LOG.error("decryptMode error message ：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
