package com.yuntun.sanitationkitchen.weight.service;

import com.alibaba.fastjson.JSONObject;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.entity.Driver;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.util.RedisUtils;
import com.yuntun.sanitationkitchen.weight.entity.TicketBill;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author wujihong
 */
public class TrashCanTask {

    public static Logger logger = LoggerFactory.getLogger(TrashCanTask.class);

    public static CommonService myService = SpringUtil.getBean(CommonService.class);

    /**
     * 用于处理垃圾桶数据
     * 1.生成垃圾桶流水
     * 2.mqtt推送小票机内容
     *
     * @param task
     * @param deviceNumber
     * @param trashCanEPC
     * @param driverEPC
     * @param weightList
     * @param ticketBill
     */
    public static void resolveTrashCan(ConcurrentHashMap<String, ScheduledFuture<?>> task, String deviceNumber, String trashCanEPC, String driverEPC,
                                       List<Double> weightList, TicketBill ticketBill) {
        try {
            // 根据DTU设备号，获取车牌号
            Vehicle vehicleByDTU = myService.getVehicleByDTU(deviceNumber);
            if (vehicleByDTU != null) {
                ticketBill.setPlateNo(vehicleByDTU.getNumberPlate());
            }

            // 获取垃圾桶信息
            TrashCan trashCanInfo = myService.getTrashCanInfo(trashCanEPC);
            // 获取司机信息
            Driver driverInfo = myService.getDriverInfo(driverEPC);
            // 获取垃圾桶称重结果
            Double weightResult = myService.getTrashWeight(weightList, trashCanInfo);
            ticketBill.setWeight(weightResult + "kg");
            String ticketBillStr = JSONObject.toJSONStringWithDateFormat(ticketBill, "yyyy-MM-dd HH:mm:ss");

            logger.info("小票机打印垃圾桶称重结果：{}", ticketBillStr);

            // 生成垃圾桶流水单
            myService.generateTrashWeightSerial(weightList, trashCanInfo, driverInfo);

            // mqtt推送小票机内容
            myService.send2TicketText(ticketBill, deviceNumber);
        } catch (ServiceException ex) {
            logger.error(ex.getMsg());
        } finally {
            // 清空此次称重缓存
            RedisUtils.delKey("sk:" + deviceNumber);
            RedisUtils.delKey("sk:" + deviceNumber + "_trashCanEPC");
            RedisUtils.delKey("sk:" + deviceNumber + "_driverEPC");
            RedisUtils.delKey("sk:" + deviceNumber + "_weight");
            RedisUtils.delKey("sk:" + deviceNumber + "_ticketBill");

            // 取消任务(完成称重)
            if (task.get(deviceNumber) != null) {
                task.get(deviceNumber).cancel(false);
            }
            task.remove(deviceNumber);
        }
    }


}
