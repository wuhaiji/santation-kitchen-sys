package com.yuntun.sanitationkitchen.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/7
 */

@Data
@Accessors(chain = true)
public class RoleListVo {
    /**
     * uuid
     */
    private Long uid;

    /**
     * 是否是超级管理员1.不是，0.是
     */
    private Boolean roleType;

    /**
     * 角色
     */
    private String roleName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime createTime;

    /**
     * 禁用状态
     */
    private Integer disabled;

}
