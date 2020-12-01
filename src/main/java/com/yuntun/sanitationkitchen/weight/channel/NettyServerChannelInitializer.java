package com.yuntun.sanitationkitchen.weight.channel;

import com.yuntun.sanitationkitchen.weight.adapter.NettyServerChannelInboundHandlerAdapter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 通道初始化
 */
public class NettyServerChannelInitializer<SocketChannel> extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {

        // 设定40秒后，直接断开连接
        ch.pipeline().addLast(new IdleStateHandler(40, 0, 0, TimeUnit.SECONDS));

        /**
         * 自定义ChannelInboundHandlerAdapter
         */
        ch.pipeline().addLast(new NettyServerChannelInboundHandlerAdapter());
    }

}
