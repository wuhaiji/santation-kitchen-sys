package com.yuntun.sanitationkitchen.constant;

/**
 * jwt常量
 *
 * @author whj
 */
public interface JwtConstant {

    /**
     * 用户浏览器客户端信息key
     */
    String USER_AGENT_HEADER_KEY = "User-Agent";

    /**
     * 浏览器保存token key
     */
    String JWT_TOKEN_HEADER_KEY = "token";

    /**
     * jwt签名秘钥
     */
    String BASE64SECRET = "WDEqVUQoa0FzJmE1XiV6Zm0pQXVeQEZk";

    /**
     * jwt过期时间
     */
    int EXPIRE_Mill = 24 * 3600_000;

    /**
     * 用于JWT加密userId的密匙 ,16位
     */
    String AES_SECRET = "NwrFql59k7gr0HFV";

    /**
     * jwt payload userAgent
     */
    String USER_AGENT = "userAgent";
    /**
     * jwt payload userName
     */
    String USER_NAME = "userName";
    /**
     * jwt payload userId
     */
    String USER_ID = "userId";

    /**
     * 每次请求的id
     */
    String REQUEST_ID = "requestId";

}
