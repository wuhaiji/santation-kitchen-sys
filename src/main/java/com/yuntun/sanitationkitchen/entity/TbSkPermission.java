package com.yuntun.sanitationkitchen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author whj
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbSkPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限tag
     */
    private String permissionTag;

    /**
     * 权限父级id  0:顶级菜单
     */
    private Integer parentId;

    /**
     * 权限类型：1.菜单 2.按钮
     */
    private Integer permissionType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者id
     */
    private Long creator;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 修改者id
     */
    private Long updator;


}
