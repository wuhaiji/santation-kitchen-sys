package com.yuntun.sanitationkitchen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 后台管理系统用户表
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    /**
     * 最后一次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 创建人id
     */
    private Long creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 禁用人id
     */
    private Long disabledBy;

    /**
     * 禁用时间
     */
    private LocalDateTime disabledTime;

    /**
     * 修改者id
     */
    private Long updator;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除状态
     */
    private Integer deleted;

    /**
     * 删除人
     */
    private Long deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;


}
