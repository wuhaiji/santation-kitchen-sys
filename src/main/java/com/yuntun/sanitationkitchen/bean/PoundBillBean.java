package com.yuntun.sanitationkitchen.bean;

import com.yuntun.sanitationkitchen.model.entity.PoundBill;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerial;
import lombok.Data;

import java.util.List;


/**
 * 磅单返回bean
 * @author tang
 * @since 2020/12/14
 */
@Data
public class PoundBillBean extends PoundBill {

    private List<TrashWeightSerial> trashWeightSerials;

}
