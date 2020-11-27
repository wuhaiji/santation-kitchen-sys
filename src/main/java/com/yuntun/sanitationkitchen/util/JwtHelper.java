package com.yuntun.sanitationkitchen.util;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.yuntun.sanitationkitchen.constant.JwtConstant;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.UserCode;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


/**
 * JWT工具类
 * 参考文献：${https://blog.csdn.net/qq_19734597/article/details/106342594}
 *
 * @author whj
 */
@Slf4j
@SuppressWarnings("restriction")
public class JwtHelper {

    public static final ConcurrentHashMap<String, String> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    public static final String USER_AGENT = "userAgent";
    public static final String USER_NAME = "userName";
    public static final String USER_ID = "userId";
    public static final String EXPIRE_TIME = "expireTime";
    public static final String SHA_256 = "SHA-c256";
    public static final int secret_length = 16;
    public static final String JWT = "JWT";
    public static final String TYP = "typ";
    public static final String ALG = "alg";
    public static final String SK_SIGNATURE_SECRET = "sk:signatureSecret:";
    public static final String SK_AES_SECRET_SECRET = "sk:aesSecret:";

    /**
     * 生成JWT字符串 格式：A.B.C A-header头信息 B-payload 有效负荷 C-signature 签名信息
     * 是将header和payload进行加密生成的
     *
     * @param userId 用户编号
     * @param agent  客户端信息，目前包含浏览器信息，用于客户端拦截器校验，防止跨域非法访问
     * @return
     */
    public static String generateJWT(String userId, String agent) {

        // 签名算法，选择SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 获取当前系统时间
        long nowTimeMillis = System.currentTimeMillis();
        // 添加Token过期时间
        Date expDate = new Date(nowTimeMillis + JwtConstant.EXPIRE_Mill);
        Date now = new Date(nowTimeMillis);
        //生成密匙,用于JWT加密userId的密匙(16位字符)
        String aesSecret = getRandomString(secret_length);
        // 将BASE64SECRET常量字符串使用base64解码成字节数组
        String signatureSecret = getRandomString(secret_length);
        byte[] signatureSecretBytes = DatatypeConverter.parseBase64Binary(signatureSecret);
        // 使用HmacSHA256签名算法生成一个HS256的签名秘钥Key
        Key signingKey = new SecretKeySpec(signatureSecretBytes, signatureAlgorithm.getJcaName());
        // 添加构成JWT的参数
        Map<String, Object> headMap = new HashMap<>(2);
        // Header { "alg": "HS256", "typ": "JWT" }
        headMap.put(ALG, SignatureAlgorithm.HS256.getValue());
        headMap.put(TYP, JWT);
        JwtBuilder builder = Jwts.builder().setHeader(headMap)
                // Payload { "userId": "1234567890", "userName": "vic", }
                // 加密后的客户编号
                .claim(USER_ID, AESUtil.encryptToStr(userId, aesSecret))
                // 客户名称
                // 客户端浏览器信息
                .claim(USER_AGENT, agent)
                // Signature
                .signWith(signatureAlgorithm, signingKey)
                //设置过期时间
                .setExpiration(expDate)
                .setNotBefore(now);
        String compact = builder.compact();
        RedisUtils.setValueExpireSeconds(SK_SIGNATURE_SECRET + SecureUtil.md5(compact), signatureSecret, JwtConstant.EXPIRE_Mill + 10000);
        RedisUtils.setValueExpireSeconds(SK_AES_SECRET_SECRET + SecureUtil.md5(compact), signatureSecret, JwtConstant.EXPIRE_Mill + 10000);
        return compact;
    }

    /**
     * 生成JWT uuid token 格
     *
     * @param userId 用户编号
     * @param agent  客户端信息，目前包含浏览器信息，用于客户端拦截器校验，防止跨域非法访问
     * @return
     */
    public static String generateJWTUUID(String userId, String agent) {

        // 签名算法，选择SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 获取当前系统时间
        long nowTimeMillis = System.currentTimeMillis();
        // 添加Token过期时间
        Date expDate = new Date(nowTimeMillis + JwtConstant.EXPIRE_Mill);
        Date now = new Date(nowTimeMillis);
        //生成密匙,用于JWT加密userId的密匙(16位字符)
        String aesSecret = getRandomString(secret_length);
        // 将BASE64SECRET常量字符串使用base64解码成字节数组
        String signatureSecret = getRandomString(secret_length);
        byte[] signatureSecretBytes = DatatypeConverter.parseBase64Binary(signatureSecret);
        // 使用HmacSHA256签名算法生成一个HS256的签名秘钥Key
        Key signingKey = new SecretKeySpec(signatureSecretBytes, signatureAlgorithm.getJcaName());
        // 添加构成JWT的参数
        Map<String, Object> headMap = new HashMap<>(2);
        // Header { "alg": "HS256", "typ": "JWT" }
        headMap.put(ALG, SignatureAlgorithm.HS256.getValue());
        headMap.put(TYP, JWT);
        JwtBuilder builder = Jwts.builder().setHeader(headMap)
                // Payload { "userId": "1234567890", "userName": "vic", }
                // 加密后的客户编号
                .claim(USER_ID, AESUtil.encryptToStr(userId, aesSecret))
                // 客户名称
                // 客户端浏览器信息
                .claim(USER_AGENT, agent)
                // Signature
                .signWith(signatureAlgorithm, signingKey)
                //设置过期时间
                .setExpiration(expDate)
                .setNotBefore(now);
        String compact = builder.compact();
        RedisUtils.setValueExpireSeconds(SK_SIGNATURE_SECRET + SecureUtil.md5(compact), signatureSecret, JwtConstant.EXPIRE_Mill + 10000);
        RedisUtils.setValueExpireSeconds(SK_AES_SECRET_SECRET + SecureUtil.md5(compact), signatureSecret, JwtConstant.EXPIRE_Mill + 10000);
        return compact;
    }

    /**
     * 生成随机字符串
     *
     * @param length 要生成的字符串长度
     * @return
     */
    public static String getRandomString(int length) {
        //1.  定义一个字符串（A-Z，a-z，0-9,1-9对应键盘符号）即62个数字字母；
        String str = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890!@#$%^&*()";
        //2.  由Random生成随机数
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        //3.  长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //从62个的数字或字母中选择
            int number = random.nextInt(str.length());
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        //生成AES_SECRET
        // String randomString = getRandomString(16);
        // System.out.println(randomString);
        // //生成BASE64SECRET
        // String randomString2 = getRandomString(24);
        // String string = Base64.getEncoder().encodeToString(randomString2.getBytes());
        // System.out.println(string);
        String s = SecureUtil.md5("admin");
        System.out.println(s);
    }

    /**
     * 解析JWT 返回Claims对象,从redis中获取secret进行解密，如果获取为空，说明jwt已经失效
     *
     * @param jsonWebToken token
     * @return
     */
    public static Claims parseJWT(String jsonWebToken) {
        try {
            // 解析jwt
            String signatureSecret = RedisUtils.getString(SK_SIGNATURE_SECRET + SecureUtil.md5(jsonWebToken));
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(signatureSecret))
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException:", e);
            log.error("[JWTHelper]-JWT解析异常：token已经超时");
            throw new ServiceException(UserCode.LOGIN_FAILED_TIME_OUT);
        } catch (Exception e) {
            log.error("Exception:", e);
            log.error("[JWTHelper]-JWT解析异常：token非法token");
            throw new ServiceException(UserCode.LOGIN_FAILED_ILLEGAL_TOKEN);
        }
    }


    /**
     * 校验JWT是否有效 返回json字符串的demo:
     * {"freshToken":"A.B.C","userName":"vic","userId":"123", "userAgent":"xxxx"}
     * freshToken-刷新后的jwt userName-客户名称 userId-客户编号 userAgent-客户端浏览器信息
     *
     * @param jsonWebToken
     * @return
     */
    public static JSONObject validateLogin(String jsonWebToken) {

        if (EptUtil.isEmpty(jsonWebToken)) {
            log.error("[JWTHelper]-json web token 为空");
            throw new ServiceException(UserCode.LOGIN_FAILED_TOKEN_IS_EMPTY);
        }
        JSONObject retMap = new JSONObject();
        Claims claims = parseJWT(jsonWebToken);
        if (claims == null) {
            return retMap;
        }
        String aesSecret = RedisUtils.getString(SK_AES_SECRET_SECRET + SecureUtil.md5(jsonWebToken));
        // 解密用户id编号
        String value = AESUtil.decryptToStr((String) claims.get(USER_ID), aesSecret);
        retMap.put(USER_ID, value);
        // 客户端浏览器信息
        retMap.put(USER_AGENT, claims.get(USER_AGENT));
        new JwtBody().setUserAgent((String) claims.get(USER_AGENT)).setUserId(Long.valueOf(value));
        // 刷新JWT
        // retMap.put("freshToken", generateJWT(decryptUserId, (String) claims.get("userName"),
        //         (String) claims.get("userAgent"), (String) claims.get("domainName")));
        return retMap;
    }
    @Data
    @Accessors(chain = true)
    public static class JwtBody{
        Long UserId;
        String userAgent;
    }
}
