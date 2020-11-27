package com.yuntun.sanitationkitchen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author whj
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbSkRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * uuid
     */
    private Integer uid;

    /**
     * 是否是超级管理员1：是，0：不是
     */
    private Boolean roleType;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 创建者id 
     */
    private Integer creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改者id
     */
    private Integer updator;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
