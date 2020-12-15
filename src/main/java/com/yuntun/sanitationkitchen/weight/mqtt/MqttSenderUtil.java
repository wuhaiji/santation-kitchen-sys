package com.yuntun.sanitationkitchen.weight.mqtt;

import com.yuntun.sanitationkitchen.weight.util.SpringUtil;

/**
 * MqttSender
 * @author tang
 * @since 2020/9/15
 */
public class MqttSenderUtil {

    private static MqttSender mqttSender;

    public static MqttSender getMqttSender(){
        if(mqttSender==null){
            mqttSender= SpringUtil.getBean(MqttSender.class);
        }
        return mqttSender;
    }

}
