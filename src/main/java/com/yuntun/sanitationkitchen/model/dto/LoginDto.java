package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/3
 */
@Data
@Accessors(chain = true)
public class LoginDto {
    String username;
    String password;
    String code;
    String publickey;
    String captchaId;
}
