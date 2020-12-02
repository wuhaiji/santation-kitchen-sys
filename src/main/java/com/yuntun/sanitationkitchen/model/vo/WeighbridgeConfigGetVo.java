package com.yuntun.sanitationkitchen.model.vo;

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
 * 地磅配置vo
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class WeighbridgeConfigGetVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 称重公差值
     */
    private Double weighingTolerance;

}
