package com.yuntun.sanitationkitchen.weight.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wujihong
 */
@Data
public class TicketBill implements Serializable {

    private static final long serialVersionUID = -5809782578272943999L;

    // DTU绑定的手机号
    private String cardNo;

    // 打印时间
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime time;

    // 重量结果
    private String weight;

    // 驾驶人名字
    private String driverName;

    // 车牌号
    private String plateNo;

}
