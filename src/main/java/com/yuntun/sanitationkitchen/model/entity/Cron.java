package com.yuntun.sanitationkitchen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 定时任务表
 * </p>
 *
 * @author whj
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_cron")
public class Cron implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 定时任务完整类名
     */
    private String cronKey;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 任务描述
     */
    private String taskExplain;

    /**
     * 状态,1:正常;2:停用
     */
    private Integer status;


}
