package com.oyxt.example.nio.groupchatv2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 20190712713
 * @date 2020/1/15 11:02
 */
public class GroupChatClientHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println(msg);
    }
}
