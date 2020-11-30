package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

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
public class UserGetDto {

    /**
     * uuid
     */
    private Long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;
}
