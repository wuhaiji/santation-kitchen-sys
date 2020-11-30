package com.yuntun.sanitationkitchen.constant;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/6
 */
public interface UserConstant {

    /**
     * 浏览器保存token key
     */
    String TOKEN_HEADER_KEY = "token";

    /**
     * 图片验证码放在session中的key
     */
    String CAPTCHA_ID_REDIS_KEY = "sk:captcha_id:";

    /**
     * 密匙对redis中的key
     */
    String RSA_KEYPAIR_REDIS_KEY = "sk:login_keypair:";

    /**
     * 未禁用
     */
    Integer NOT_DISABLED = 0;

    /**
     * 未禁用
     */
    Integer IS_DISABLED = 1;

}
