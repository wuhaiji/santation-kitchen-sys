package com.yuntun.sanitationkitchen.util;

import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.LoggerFactory;

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
     * token过期一个小时
     */
    public static final int TOKEN_TIMEOUT = 1800;
    /**
     * 刷新token，1天
     */
    public static final int REFRESH_TOKEN_TIME = 3600 * 24;
    public static final String SK_TOKEN = "sk:token:";
    public static final String SK_REFRESH_TOKEN = "sk:refresh_token:";


    /**
     * 生成uuid token
     *
     * @param userId    用户id
     * @param userAgent 用户浏览器信息
     * @return token
     */
    public TokenBody generalToken(String userId, String userAgent) {
        if (EptUtil.isEmpty(userId)) {
            log.error("AuthUtil:generalToken->userId 非法:" + userId);
            return null;
        }
        TokenBody tokenBody = new TokenBody();
        tokenBody.setToken(UUID.randomUUID().toString().replace("-", ""));
        tokenBody.setRefreshToken(UUID.randomUUID().toString().replace("-", ""));


        RedisUtils.setValueExpireSeconds(SK_TOKEN + tokenBody.getToken(), tokenBody, TOKEN_TIMEOUT);
        RedisUtils.setValueExpireSeconds(SK_REFRESH_TOKEN + tokenBody.getRefreshToken(), tokenBody, REFRESH_TOKEN_TIME);

        return tokenBody;
    }

    /**
     * 生成uuid token
     * @return token
     */
    public TokenBody validateToken(String token) {
        return null;
    }

    @Data
    @Accessors(chain = true)
    public static class TokenBody {
        String token;
        String refreshToken;
        Long creatTime;
        Long expireTime;

    }
}
