package com.yuntun.sanitationkitchen.model.entity;

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
 * 垃圾桶表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_trash_can")
public class TrashCan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 设施编号
     */
    private String facilityCode;

    /**
     * 设施类型0.餐厨垃圾桶 1.中转垃圾箱
     */
    private Integer facilityType;

    /**
     * 垃圾桶重量（单位：kg）
     */
    private Double weight;

    /**
     * 容量 单位升
     */
    private Integer capacity;

    /**
     * 当前垃圾储量升
     */
    private Integer reserve;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 垃圾桶rfid
     */
    private String rfid;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 联系人名称
     */
    private String contactPerson;

    /**
     * 联系人电话
     */
    private String contactPersonPhone;

    /**
     * 餐馆id
     */
    private Long restaurantId;

    /**
     * 经度，肯定是存储数据精度越高，最终位置定位越准确。
     * 但是考虑到数据库资源以及可定位到精准性，根据谷歌官方的建议，
     * 存储经纬度的时候，使用double(10,6)就可以了。
     * 这样，这些字段将会存储小数点后 6 位数以及小数点前最多 4 位数，如 -153.456783 度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

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
