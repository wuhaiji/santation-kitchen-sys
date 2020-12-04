package com.yuntun.sanitationkitchen.auth;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.yuntun.sanitationkitchen.properties.IdProperties;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.RedisUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/26
 */

public class AuthUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    /**
     * token过期时间,1个小时
     */
    public static final int TOKEN_TIMEOUT = 3700_000;
    public static final int TOKEN_TIMEOUT_80_PERCENTAGE = 3600_000;
    /**
     * 刷新token，3天
     */
    public static final int REFRESH_TOKEN_TIME = 259200_000;

    /**
     * redis存储token的键前缀
     */
    public static final String SK_TOKEN = "sk:token:";
    /**
     * redis存储refresh_token的键前缀
     */
    public static final String SK_REFRESH_TOKEN = "sk:refresh_token:";


    /**
     * 生成uuid token
     *
     * @param userId 用户id
     * @return token
     */
    public static TokenInfo generalToken(Long userId) {
        if (EptUtil.isEmpty(userId)) {
            log.error("AuthUtil:generalToken->userId 非法:" + userId);
            return null;
        }
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(getUUID());
        tokenInfo.setRefreshToken(getUUID());
        tokenInfo.setCreatTime(System.currentTimeMillis());
        tokenInfo.setUserId(userId);
        //过期时间取80%防止token到达过期临界点
        tokenInfo.setExpireTime(System.currentTimeMillis() + TOKEN_TIMEOUT_80_PERCENTAGE);

        RedisUtils.setValueExpireMills(SK_TOKEN + tokenInfo.getToken(), JSON.toJSONString(tokenInfo), TOKEN_TIMEOUT);
        RedisUtils.setValueExpireMills(SK_REFRESH_TOKEN + tokenInfo.getRefreshToken(), JSON.toJSONString(tokenInfo), REFRESH_TOKEN_TIME);

        return tokenInfo;
    }

    /**
     * 通过refresh token获取t新的oken
     *
     * @param refreshToken refreshToken
     * @return token
     */
    public static TokenInfo refreshToken(String refreshToken) {
        if (EptUtil.isEmpty(refreshToken)) {
            log.error("AuthUtil:generalToken->refreshToken 非法:" + refreshToken);
            return null;
        }
        TokenInfo tokenInfo = RedisUtils.getObject(SK_REFRESH_TOKEN + refreshToken, TokenInfo.class);
        if (tokenInfo == null) {
            return null;
        }
        tokenInfo.setToken(getUUID());
        tokenInfo.setCreatTime(System.currentTimeMillis());
        //过期时间取80%防止token到达过期临界点
        tokenInfo.setExpireTime(System.currentTimeMillis() + TOKEN_TIMEOUT_80_PERCENTAGE);
        RedisUtils.setValueExpireMills(SK_TOKEN + tokenInfo.getToken(), JSON.toJSONString(tokenInfo), TOKEN_TIMEOUT);
        RedisUtils.setValueExpireMills(SK_REFRESH_TOKEN + tokenInfo.getRefreshToken(), JSON.toJSONString(tokenInfo), REFRESH_TOKEN_TIME);
        return tokenInfo;
    }

    /**
     * 验证token是否过期
     *
     * @return TokenBody token信息
     */
    public static TokenInfo validateToken(String token) {
        return RedisUtils.getObject(SK_TOKEN + token, TokenInfo.class);
    }

    /**
     * 注销登录
     */
    public static boolean removeToken(String token) {
        TokenInfo tokenInfo = RedisUtils.getObject(SK_TOKEN + token, TokenInfo.class);
        if (tokenInfo == null) {
            return false;
        }
        RedisUtils.delKey(SK_TOKEN + tokenInfo.getToken());
        RedisUtils.delKey(SK_REFRESH_TOKEN + tokenInfo.getToken());
        return true;
    }


    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
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

    /**
     * token信息类
     */
    @Data
    @Accessors(chain = true)
    public static class TokenInfo {
        String token;
        String refreshToken;
        Long creatTime;
        Long expireTime;
        Long userId;

    }
}
