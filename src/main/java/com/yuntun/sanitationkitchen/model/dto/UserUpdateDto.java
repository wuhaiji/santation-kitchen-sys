package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
public class UserUpdateDto {

    /**
     * uuid
     */
    private Long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;
    // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    /**
     * 禁用时间
     */
    private Integer disabled;


    private Long roleId;
    /**
     * 环卫局id
     */
    private Long sanitationOfficeId;


}
