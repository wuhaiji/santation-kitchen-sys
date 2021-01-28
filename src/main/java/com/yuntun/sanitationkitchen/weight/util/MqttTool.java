package com.yuntun.sanitationkitchen.weight.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p>
 * springboot整合Mqtt实现:https://blog.csdn.net/weixin_38261597/article/details/89510270
 * </p>
 *
 * @author whj
 * @since 2021/1/21
 */

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttTool implements MqttCallback {

    private String username;

    private String password;

    private String host;

    private MqttClient client;

    private boolean cleanSession = false;

    /**
     * 初始值为32位随机字符串，每次重新启动springboot服务，mqtt session就失效
     * 如果需要的话可以设置为固定的id，从而保证重新连接的session不变，
     * 也可以随机生成并存放在redis中
     */
    private String clientId = RandomUtil.randomString(32);

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void result() {
        int waitTime = 0;
        //默认保持会话
        for(int i=1;true;i++){
            if(connect()){
                break;
            }
            waitTime += i * 1000;
            ThreadUtil.sleep(i * 1000);
            if (waitTime >= 600_000) {
                log.warn("mqtt重连超时10分钟，放弃重连！！！");
                break;
            }
        }
    }

    /**
     * 连接mqtt
     * @return 连接是否成功
     */
    public boolean connect() {
        try {
            // host为主机名，clientId即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientId的保存形式，默认为以内存保存
            if (client == null) {
                client = new MqttClient(host, clientId, new MemoryPersistence());
            }
            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
            // 这里设置为true表示每次连接到服务器都以新的身份连接;
            // 如果设置为false代表 Client 希望建立一个持久会话的连接，Broker 将存储该 Client 订阅的主题和未接受的消息(QoS大于1)，
            // client离线重启后将收到这些消息；否则 Broker 不会存储这些数据，client离线重启后也不会收到消息。
            options.setCleanSession(cleanSession);
            // 设置连接的用户名
            options.setUserName(username);
            // 设置连接的密码
            options.setPassword(password.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔60秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(60);
            // 设置回调
            client.setCallback(this);
            client.connect(options);

            client.subscribe("test",MqttQoS.AT_LAST_ONCE.value());
            //订阅消息
            registerTheSubject();
            log.info("mqtt连接成功");
            return true;
        } catch (Exception e) {
            log.error("mqtt客户端连接异常，异常信息：", e);
            return false;
        }
    }

    /**
     * 通过注解注册主题和消息处理方法
     */
    private void registerTheSubject() {
    }

    public void publish(String topic, byte[] payload) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload);
        mqttMessage.setQos(MqttQoS.AT_ONLY_ONCE.value());
        mqttMessage.setRetained(false);
        try {
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            log.error("mqtt消息发送失败：", e);
        }
    }


    public void publish(String topic, MqttMessage mqttMessage) {
        try {
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            log.error("mqtt消息发送失败：", e);
        }
    }

    /**
     * 重连
     */
    public void reConnection() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            log.error("mqtt客户端断开连接:", e);
        }
        connect();
    }

    /**
     * 断连
     */
    public void disconnect() {
        try {
            client.disconnect();
            log.info("mqtt客户端断开连接成功");
        } catch (MqttException e) {
            log.error("mqtt客户端断开连接:", e);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("程序出现异常，MqttConsumer断线！正在重新连接...:");

        boolean connect;
        for (int i=0;i<10;i++){
            connect = connect();
            if (connect) {
                log.info("mqtt重新连接成功");
            }
            ThreadUtil.sleep(5000);
        }
        log.info("mqtt程序出现异常，重新连接10次均已失败!!!");
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.info("接收消息主题:" + topic);
        log.info("接收消息Qos:" + message.getQos());
        log.info("接收消息内容:\n" + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("消息发送成功");
    }

    public enum MqttQoS {
        AT_MOST_ONCE(0),
        AT_LAST_ONCE(1),
        AT_ONLY_ONCE(2),
        ;
        int qos;

        public int value() {
            return qos;
        }

        MqttQoS(int qos) {
            this.qos = qos;
        }
    }

}
