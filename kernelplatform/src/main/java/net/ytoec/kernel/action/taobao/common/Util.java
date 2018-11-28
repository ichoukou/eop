/**
 * Util.java Wangyong 2011-9-21 下午02:19:43
 */
package net.ytoec.kernel.action.taobao.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.dataobject.User;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sun.misc.BASE64Encoder;

/**
 * 淘宝客户免登录：<br>
 * 淘宝开放平台授权签名验证、参数解析、MD5编码、base64编码
 * 
 * @author Wangyong
 * @2011-9-21 net.ytoec.kernel.action.taobao.common
 */
public class Util {

    private static Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * 验证TOP回调地址的签名是否合法。要求所有参数均为已URL反编码的。
     * 
     * @param topParams TOP私有参数（未经BASE64解密）
     * @param topSession TOP私有会话码
     * @param topSign TOP回调签名
     * @param appKey 应用公钥
     * @param appSecret 应用密钥
     * @return 验证成功返回true，否则返回false
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean verifyTopResponse(String topParams, String topSession, String topSign, String appKey,
                                            String appSecret) throws NoSuchAlgorithmException, IOException {
        StringBuilder result = new StringBuilder();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        result.append(appKey).append(topParams).append(topSession).append(appSecret);
        byte[] bytes = md5.digest(result.toString().getBytes("UTF-8"));
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes).equals(topSign);
    }

    /**
     * 解析top_parameters
     * 
     * @param top_parameters
     * @param key 需要解析的键
     * @return
     */
    public static String resolvingParameters(String top_parameters, String parameterKey) {
        String valueResutl = null;
        Map<String, String> map = convertBase64StringtoMap(top_parameters);
        Iterator keyValuePairs = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            Map.Entry entry = (Map.Entry) keyValuePairs.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            logger.debug("淘宝参数键值：" + key + "淘宝参数值：" + value);
            if (key.equals(parameterKey)) {
                valueResutl = (String) value;
            }
        }
        return valueResutl;
    }

    /**
     * 把经过BASE64编码的字符串转换为Map对象
     * 
     * @param str
     * @return
     * @throws Exception
     */
    private static Map<String, String> convertBase64StringtoMap(String str) {
        if (StringUtils.isEmpty(str)) return Collections.EMPTY_MAP;
        String keyvalues = StringUtils.EMPTY;
        try {
            keyvalues = new String(Base64.decodeBase64(str.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        }
        String[] keyvalueArray = keyvalues.split("\\&");
        Map<String, String> map = new HashMap<String, String>();
        for (String keyvalue : keyvalueArray) {
            String[] s = keyvalue.split("\\=");
            if (s == null || s.length != 2) {
                continue;
            }
            map.put(s[0], s[1]);
        }
        return map;
    }

    /**
     * 把经过BASE64编码的字符串转换为中文字符串
     * 
     * @param str
     * @return
     * @throws Exception
     */
    public static String convertBase64String(String str) {
        if (StringUtils.isEmpty(str)) return StringUtils.EMPTY;
        String keyvalues = StringUtils.EMPTY;
        try {
            keyvalues = new String(Base64.decodeBase64(str.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        }
        return keyvalues;
    }

    /**
     * 验证时间戳是否在允许的范围内（官方建议误差在5分钟以内，最长不超过30分钟）
     * 
     * @param timestamp 时间戳字符串(有ts,iframe,visitor_id,visitor_nick四种参数)
     * @param timelag 时间戳范围
     * @return
     */
    public static boolean validateTimestamp(String timestamp, int timelag) {
        boolean flag = false;
        long original = Long.valueOf(timestamp);
        Date oriDate = new Date(original);
        logger.debug("TOP上的时间：" + oriDate);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        int minute = cal.get(Calendar.MINUTE);
        // 在当前分钟上加timelag分钟
        cal.add(Calendar.MINUTE, timelag);
        long formertime = cal.getTimeInMillis();
        cal.add(Calendar.MINUTE, -timelag);
        // 在当前分钟上减timelag分钟
        cal.add(Calendar.MINUTE, -timelag);
        long aftertime = cal.getTimeInMillis();
        logger.debug(original + "===" + formertime + "===" + aftertime);
        if (aftertime < original && original < formertime) flag = true;
        logger.debug(flag + "======================");
        return flag;
    }
    
    public static String validateTimestampResult(String timestamp, int timelag) {
        boolean flag = false;
        long original = Long.valueOf(timestamp);
        Date oriDate = new Date(original);
        logger.debug("TOP上的时间：" + oriDate);
        return "TOP上的时间："+oriDate;
    }

    /**
     * 新的md5签名，首尾放secret。
     * 
     * @param secret 分配给您的APP_SECRET
     */
    public static String md5Signature(TreeMap<String, String> params, String secret) {

        String result = null;

        StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));

        if (orgin == null)

        return result;

        orgin.append(secret);

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));

        } catch (Exception e) {

            throw new java.lang.RuntimeException("sign error !");

        }

        return result;

    }
    
    /**
     * 新的md5签名，首尾放secret。
     * @param target 加密目标字符串
     * @param secret 给目标字符串加密用的key
     */
    public static String md5Signature(String target, String secretKey) {

        String result = null;

        if (StringUtils.isBlank(target) || StringUtils.isBlank(secretKey))
            return result;
        
        StringBuffer orgin = new StringBuffer(target);
        orgin.append(secretKey);

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));

        } catch (Exception e) {

            throw new java.lang.RuntimeException("sign error !");

        }

        return result;

    }

    /**
     * 二行制转字符串
     */
    private static String byte2hex(byte[] b) {

        StringBuffer hs = new StringBuffer();

        String stmp = "";

        for (int n = 0; n < b.length; n++) {

            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

            if (stmp.length() == 1)

            hs.append("0").append(stmp);

            else

            hs.append(stmp);

        }

        return hs.toString().toUpperCase();

    }

    /**
     * 添加参数的封装方法
     */
    private static StringBuffer getBeforeSign(TreeMap<String, String> params, StringBuffer orgin) {
        if (params == null) return null;
        Map<String, String> treeMap = new TreeMap<String, String>();
        treeMap.putAll(params);
        Iterator<String> iter = treeMap.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append(name).append(params.get(name));
        }
        return orgin;
    }

    /**
     * 连接到TOP服务器并获取数据
     */
    public static String getResult(String urlStr, String content) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(content.getBytes("utf-8"));
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            logger.error("连接到TOP服务器并获取数据时出错："+e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * 将一个xml串转化为输入流
     * 
     * @param xmlString
     * @return
     */
    public static InputStream getInputStream(String xmlString) {
        byte[] bytes = null;
        try {
            bytes = xmlString.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return bais;
    }

    /**
     * 创建用户对象
     * 
     * @param inputStream
     */
    public static User createObject(InputStream inputStream) {
        User user = new User();
        DocumentReader documentReader = new DocumentReader();
        Document document = documentReader.getDocument(inputStream);
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList childList = node.getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                Node childNode = childList.item(j);
                if ("email".equals(childNode.getNodeName())) {
                    user.setMail(childNode.getTextContent());
                }
                if ("nick".equals(childNode.getNodeName())) {
                    user.setShopAccount(childNode.getTextContent());
                }
                if ("has_shop".equals(childNode.getNodeName())) {
                    user.setHasShpo(childNode.getTextContent());
                }
            }
        }
        return user;
    }

    public static String estimateHasShop(InputStream inputStream) throws IOException {
        String hasShop = "";
        DocumentReader documentReader = new DocumentReader();
        Document document = documentReader.getDocument(inputStream);
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList childList = node.getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                Node childNode = childList.item(j);
                if ("has_shop".equals(childNode.getNodeName())) {
                    hasShop = childNode.getTextContent();
                    inputStream.close();
                }
            }
        }
        return hasShop;
    }

}
