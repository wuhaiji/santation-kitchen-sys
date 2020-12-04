package com.yuntun.sanitationkitchen.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@Accessors(chain = true)
public class UserGetVo {

    /**
     * uuid
     */
    private Long uid;

    /**
     * 用户名
     */
    private String username;


    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 所属角色名称
     */
    private String roleName;

    /**
     * 环卫局id
     */
    private Long sanitationOfficeId;

    /**
     * 环卫局名称
     */
    private String sanitationOfficeName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 最后一次登录时间
     */
    @DateTimeFormat(pattern = DateConst.dateTimePattern)
    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateConst.dateTimePattern)
    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime createTime;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 禁用人id
     */
    private Long disabledBy;





}
