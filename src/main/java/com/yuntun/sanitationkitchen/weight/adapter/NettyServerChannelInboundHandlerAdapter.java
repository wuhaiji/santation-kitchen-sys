package com.yuntun.sanitationkitchen.weight.adapter;

import com.alibaba.fastjson.JSONObject;
import com.sun.el.stream.Stream;
import com.yuntun.sanitationkitchen.config.Scheduled.ScheduledTask;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.entity.Driver;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.util.RedisUtils;
import com.yuntun.sanitationkitchen.weight.config.UDCDataHeaderType;
import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
import com.yuntun.sanitationkitchen.weight.entity.TicketBill;
import com.yuntun.sanitationkitchen.weight.mqtt.MqttSenderUtil;
import com.yuntun.sanitationkitchen.weight.mqtt.constant.MqttTopicConst;
import com.yuntun.sanitationkitchen.weight.propertise.TrashDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.service.CommonService;
import com.yuntun.sanitationkitchen.weight.service.TrashCanTask;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import com.yuntun.sanitationkitchen.weight.util.UDCDataResponse;
import com.yuntun.sanitationkitchen.weight.util.UDCDataUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * JT808协议
 * I/O数据读写处理类
 */
public class NettyServerChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    public static CommonService myService = SpringUtil.getBean(CommonService.class);

    public static TrashDataPackageFormat trashDataPackageFormat = SpringUtil.getBean(TrashDataPackageFormat.class);

    public static UDCDataUtil udcDataUtil = SpringUtil.getBean(UDCDataUtil.class);

    public static Logger logger = LoggerFactory.getLogger(NettyServerChannelInboundHandlerAdapter.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 定时任务池
    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(4);

    // 任务
    private static ConcurrentHashMap<String, ScheduledFuture> task = new ConcurrentHashMap<>();


    /**
     * ①
     * 将上线的终端设备记录下来
     *
     * @param ctx
     * @author wujihong
     * @since 14:36 2020-11-11
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());//加入ChannelGroup
        System.out.println(ctx.channel().id() + " 设备上线," + "Online: " + channels.size());
    }

    /**
     * ②
     *
     * @param ctx
     * @author wujihong
     * @since 15:05 2020-11-12
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
    }

    /**
     * ③
     * 客户端与服务端第一次建立连接时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelActive(ctx);
        ctx.channel().read();
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int port = insocket.getPort();
        //此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
        System.out.println("channelActive clientAddress:" + clientIp + ":" + port + " " + ctx.name());
    }

    /**
     * ④
     * 从客户端收到新的数据时，这个方法会在收到消息时被调用
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bytebuf = (ByteBuf) msg;
        byte[] bytes = new byte[bytebuf.readableBytes()];
        bytebuf.readBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            if (i == 0) {
                System.out.print("{ ");
            }

            System.out.print(bytes[i]);
            if (i + 1 == bytes.length) {
                System.out.println(" }");
            } else {
                System.out.print(", ");
            }

        }

        // 判断它是否是UDC协议
        if (udcDataUtil.getFlag(bytes) == UDCDataHeaderType.PACKAGE_SYMBOL) {
            // 判断它是否是登录
            if (udcDataUtil.getDataPackageType(bytes) == UDCDataHeaderType.LOGIN_PACKAGE) {
                System.out.println("登录响应！");
                ctx.write(Unpooled.copiedBuffer(UDCDataResponse.loginResponse(bytes)));
            }

            // 判断它是否是数据上报
            if (udcDataUtil.getDataPackageType(bytes) == UDCDataHeaderType.UPLOAD_PACKAGE) {
                String deviceNumber = udcDataUtil.getDeviceNumber(bytes);
                logger.info("数据上报！");
                // 解析数据
                SKDataBody resolve = myService.resolve(udcDataUtil.getDataBody(bytes));
                Set<String> epcs = resolve.getEpcs();
                logger.info("resolve:{}", resolve);

                for (String epc:epcs) {
                    String rfidType;
                    try {
                        rfidType = myService.getRFIDType(epc);
                        logger.info("rfidType:{}", rfidType);
                    } catch (ServiceException ex) {
                        logger.error(ex.getMsg());
                        throw ex;
                    }

                    // 根据EPC号 处理数据
                    myService.disposeEPC(ctx,bytes,deviceNumber,epc,rfidType);
                }

                // 垃圾桶重量
                Double trashWeight = resolve.getTrashWeight();
                if (trashWeight != null) {
                    // 将垃圾桶重量存储到redis,并设置有效时间10s（超过时间后，就算完成称重）
                    RedisUtils.setValue("sk:"+deviceNumber, "valid");
                    Integer weightWaitTime = trashDataPackageFormat.getWeightWaitTime();
                    RedisUtils.expireSeconds("sk:"+deviceNumber, weightWaitTime);
                    RedisUtils.listPush("sk:"+deviceNumber+"_weight",trashWeight);

                    List<Double> weightList = RedisUtils.listGetAll("sk:"+deviceNumber + "_weight").stream().mapToDouble(weight -> Double.parseDouble(weight.toString()))
                            .boxed().collect(Collectors.toList());
                    logger.info("缓存区中的垃圾桶重量：{}", weightList);
                }

                // 地磅车辆重量
                Double boundWeight = resolve.getBoundWeight();
                if (boundWeight != null) {
                    RedisUtils.setValue("sk:"+deviceNumber+"_boundWeight", boundWeight);
                    TicketBill ticketBill = (TicketBill)RedisUtils.getValue("sk:" + deviceNumber + "_ticketBill");

                    ticketBill.setWeight(boundWeight+"kg");
                    logger.info("地磅称重结果：{}", ticketBill);
                    RedisUtils.setValue("sk:"+deviceNumber+"_ticketBill", ticketBill);

                }

                Object redisBoundWeight = RedisUtils.getValue("sk:" + deviceNumber + "_boundWeight");
                if (redisBoundWeight != null) {
                    // 生成地磅流水
                    String vehicleEPC = RedisUtils.getString("sk:"+deviceNumber + "_vehicleEPC");
                    String driverEPC = RedisUtils.getString("sk:" + deviceNumber + "_driverEPC");
                    if (vehicleEPC != null && driverEPC != null) {
                        String driverName = myService.getDriverInfo(driverEPC).getName();
                        TicketBill ticketBill = (TicketBill)RedisUtils.getValue("sk:" + deviceNumber + "_ticketBill");
                        ticketBill.setDriverName(driverName);
                        String ticketBillStr = JSONObject.toJSONStringWithDateFormat(ticketBill, "yyyy-MM-dd HH:mm:ss");
                        myService.generatePoundBill(deviceNumber,vehicleEPC,driverEPC,Double.valueOf(redisBoundWeight.toString()));

                        // 清空此次地磅称重数据
                        RedisUtils.delKey("sk:"+deviceNumber+"_vehicleEPC");
                        RedisUtils.delKey("sk:"+deviceNumber+"_driverEPC");
                        RedisUtils.delKey("sk:"+deviceNumber+"_ticketBill");
                        RedisUtils.delKey("sk:" + deviceNumber + "_boundWeight");

                        // 发送打印小票机请求
                        MqttSenderUtil.getMqttSender().sendToMqtt(MqttTopicConst.TICKET_MACHINE, ticketBillStr);
                    }

                }

                ScheduledFuture future = task.get(deviceNumber);
                // trashCanTareWeight为垃圾桶皮重
                String trashCanEPC = RedisUtils.getString("sk:"+deviceNumber + "_trashCanEPC");
                String driverEPC = RedisUtils.getString("sk:"+deviceNumber + "_driverEPC");
                List<Object> redisWeight = RedisUtils.listGetAll("sk:"+deviceNumber + "_weight");
                Object redisTicketBill = RedisUtils.getValue("sk:"+deviceNumber + "_ticketBill");
                logger.info("trashCanEPC：{}", trashCanEPC);
                logger.info("driverEPC：{}", driverEPC);
                logger.info("future：{}", future);
                logger.info("redisTicketBill：{}", redisTicketBill);
                logger.info("redisWeight：{}", redisWeight);

                // 当垃圾桶重量和车辆人员信息都不为空时，在执行（完成垃圾桶的称重）
                if (redisWeight != null && redisWeight.size() != 0 && redisTicketBill != null && future == null) {
                    ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() ->
                    {
                        List<Double> weightList = RedisUtils.listGetAll("sk:"+deviceNumber + "_weight").stream().mapToDouble(weight -> Double.parseDouble(weight.toString()))
                                .boxed().collect(Collectors.toList());
                        Long expire = RedisUtils.getTTl("sk:"+deviceNumber);
                        TicketBill ticketBill = (TicketBill)RedisUtils.getValue("sk:" + deviceNumber + "_ticketBill");
                        logger.info("称重倒计时：{}", expire);
                        logger.info("bill：{}", ticketBill);

                        // 当expire = -2，一种是：redis不存在key；一种是：该key过期了
                        if ( expire == -2 ) {
                            logger.info("垃圾桶称重集合：{}", weightList);
                            TrashCanTask.resolveTrashCan(task,deviceNumber,trashCanEPC,driverEPC,weightList,ticketBill);
                        }
                    }, 0, 1, TimeUnit.SECONDS);
                    task.put(deviceNumber, scheduledFuture);
                }
            }

            // 判断它是否是心跳
            if (udcDataUtil.getDataPackageType(bytes) == UDCDataHeaderType.HEART_PACKAGE) {
                System.out.println("进入了心跳响应！");
                ctx.write(Unpooled.copiedBuffer(UDCDataResponse.heartResponse(bytes)));
            }

            // 判断它是否是主动下线报文
            if (udcDataUtil.getDataPackageType(bytes) == UDCDataHeaderType.OFFLINE_PACKAGE) {
                System.out.println("进入了下线响应！");
                MqttSenderUtil.getMqttSender().sendToMqtt(MqttTopicConst.UDC_DIED, "设备离线");
                ctx.write(Unpooled.copiedBuffer(UDCDataResponse.offlineResponse(bytes)));
            }

        } else {
            System.out.println("非法访问···！");
            logger.error("非法访问···！");
        }

    }

    /**
     * ⑤
     * 从客户端收到新的数据、读取完成时调用
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
        logger.info("channel-Read-Complete");
        ctx.flush();
    }

    /**
     * ⑥
     * 客户端与服务端 断连时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelInactive(ctx);
        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
    }

    /**
     * ⑦
     *
     * @param ctx
     * @author wujihong
     * @since 15:18 2020-11-12
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel-Unregistered");
    }

    /**
     * ⑧
     * 将离线的终端设备移除
     *
     * @param context
     * @author wujihong
     * @since 14:36 2020-11-11
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        System.out.println(context.channel().id() + " 设备离线," + "Online: " + channels.size());
    }

    /**
     * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
        logger.error("cause message:{}", cause.getMessage());
        logger.error("{} ,occurred into error", ctx.channel().id());
        ctx.close();//抛出异常，断开与客户端的连接
    }

    /**
     * 服务端当read超时, 会调用这个方法
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception, IOException {
        super.userEventTriggered(ctx, evt);
        logger.error("user-Event-Triggered:{}", ctx.channel().id());
        ctx.close();//超时时断开连接
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel-Writability-Changed");
    }

}
