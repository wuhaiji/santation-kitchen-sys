package com.yuntun.sanitationkitchen.weight.mqtt.config;

import com.yuntun.sanitationkitchen.weight.mqtt.MqttSender;
import com.yuntun.sanitationkitchen.weight.mqtt.constant.MqttTopicConst;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import java.util.UUID;

/**
 * mqtt 配置
 * @author tang
 */
@Configuration
public class MqttConfig {

    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    private String clientIdPrefix= UUID.randomUUID().toString();


/*-------------------------------------客户端工厂----------------------------------------*/

    @Bean
    public MqttConnectOptions mqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{host});
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        // 2秒
        mqttConnectOptions.setKeepAliveInterval(2);
        // 超时时间
        mqttConnectOptions.setConnectionTimeout(5);
        return mqttConnectOptions;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

/*-------------------------------------生产----------------------------------------*/

    @Bean
    public GatewayProxyFactoryBean gateway() {
        GatewayProxyFactoryBean gateway = new GatewayProxyFactoryBean(MqttSender.class);
        gateway.setDefaultRequestChannel(outBoundChannel());
        return gateway;
    }
    @Bean
    public MessageChannel outBoundChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "outBoundChannel")
    public MessageHandler outBound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                clientIdPrefix+"-producer",
                mqttClientFactory());
        messageHandler.setAsync(true);
        return messageHandler;
    }

/*-------------------------------------消费----------------------------------------*/

    @Bean
    public MessageChannel inBoundChannel(){
        return new DirectChannel();
    }
    // 订阅主题
    @Bean
    public MessageProducer inBound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientIdPrefix+"-consumer",
                        mqttClientFactory()
                );
        adapter.addTopics(new String[]{
                MqttTopicConst.VEHICLE_MESSAGE,
                MqttTopicConst.TRASH_MESSAGE,
                MqttTopicConst.UDC_DIED
        } , new int[]{1,1,1});
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(inBoundChannel());
        return adapter;
    }
    @Bean
    @ServiceActivator(inputChannel = "inBoundChannel")
    public MessageHandler handler(MessageHandler mqttMsgHandler) {
        return mqttMsgHandler;
    }

}
