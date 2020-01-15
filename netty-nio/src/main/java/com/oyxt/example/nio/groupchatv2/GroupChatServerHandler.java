package com.oyxt.example.nio.groupchatv2;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 20190712713
 * @date 2020/1/15 9:56
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded表示连接建立，一旦连接，第一个被执行
    // 将当前channel加入到channelGroup

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + sdf.format(new Date()) + "\n");
        channelGroup.add(channel);
    }

    //断开连接，将用户离线信息推送给其他在线用户

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
        System.out.println("channelGroup SIZE: " + channelGroup.size());
    }

    //表示channel处于活动状态，提示客户端上线

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了...");
    }

    // 表示channel处于不活动状态，提示离线了

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了...");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();

        //这时我们遍历channelGroup,根据不同的情况，回送不同的消息
        channelGroup.forEach(ch->{
            if (channel != ch) {
                ch.writeAndFlush("[用户]" + channel.remoteAddress() + "发送了消息" + msg + "\n" );
            }else {
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
    }
}
