package com.yuntun.sanitationkitchen.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 后台管理系统角色表
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@Data
@Accessors(chain = true)
public class RoleOptionsVo {


    private Long uid;


    private Boolean roleType;

    /**
     * 角色
     */
    private String roleName;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm")
    private LocalDateTime createTime;

    /**
     * 禁用状态
     */
    private Integer disabled;



}
