package com.yuntun.sanitationkitchen.model.dto;

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
public class UserListDto {

    Integer pageSize;

    Integer pageNo;

    /**
     * uuid
     */
    private Long uid;

    private Integer disabled;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 环卫局id
     */
    private Long sanitationOfficeId;

}
