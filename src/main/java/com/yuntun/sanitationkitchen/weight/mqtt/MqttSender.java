package com.yuntun.sanitationkitchen.weight.mqtt;


import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * MQTT生产者消息发送接口
 * MessagingGateway 指定生产者消息通道的名称
 * @author tang
 */
public interface MqttSender {

    /**
     * 发送消息
     * @param data 发送的文本
     */
    void sendToMqtt(String data);

    /**
     * 发送消息
     * @param topic 主题名称
     * @param payload 消息主体
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 发送消息
     * @param topic 主题名称
     * @param qoc 消息质量（0，1，2）
     * @param payload 消息主体
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qoc, String payload);
}
