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
public class UserRoleListVo {

    /**
     * uuid
     */
    private Long uid;

    private Long roleId;

    private Long userId;

    private String username;

    private String roleName;


}
