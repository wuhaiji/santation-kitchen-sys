package com.yuntun.sanitationkitchen.model.vo;

import com.yuntun.sanitationkitchen.auth.AuthUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/30
 */
@Data
@Accessors(chain = true)
public class UserLoginVo {
    String username;
    String phone;
    Long uid;
    AuthUtil.TokenInfo tokenInfo;
}
