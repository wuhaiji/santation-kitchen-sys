package com.yuntun.sanitationkitchen.weight.adapter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * JT808协议
 * I/O数据读写处理类
 */
public class NettyServerChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    public static Logger logger = LoggerFactory.getLogger(NettyServerChannelInboundHandlerAdapter.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

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