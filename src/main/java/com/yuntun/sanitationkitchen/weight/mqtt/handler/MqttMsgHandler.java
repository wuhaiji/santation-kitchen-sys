package com.yuntun.sanitationkitchen.weight.mqtt.handler;

import com.yuntun.sanitationkitchen.weight.mqtt.constant.MqttTopicConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;


/**
 * Mqtt 消息处理
 * @author tang
 * @since 2020/9/15
 */
@Component
public class MqttMsgHandler implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic=(String)message.getHeaders().get("mqtt_receivedTopic");
        String payload=(String) message.getPayload();
        try {
            if(MqttTopicConst.VEHICLE_MESSAGE.equals(topic)){
                System.out.println("mqtt发送地磅设备信息！");
            }

            if(MqttTopicConst.TRASH_MESSAGE.equals(topic)){
                System.out.println("mqtt发送垃圾桶设备消息！");
            }

            if(MqttTopicConst.UDC_DIED.equals(topic)){
                System.out.println("mqtt发送设备离线消息！");
            }

            if (MqttTopicConst.TICKET_MACHINE.equals(topic)) {
                System.out.println(payload);
                System.out.println("mqtt发送小票机设备消息！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
