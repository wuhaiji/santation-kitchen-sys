package com.yuntun.sanitationkitchen.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 油耗监测设备表
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Data
@Accessors(chain = true)
public class SanitationOfficeVo {

    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 用户名
     */
    private String name;

    /**
     * 管理员id
     */
    private Long managerId;

    /**
     * 创建人id
     */
    private Long creator;

    /**
     * 创建时间
     */
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

    /**
     * 禁用时间
     */
    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime disabledTime;

    /**
     * 修改者id
     */
    private Long updator;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = DateConst.dateTimePattern)
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
    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime deletedTime;


}
