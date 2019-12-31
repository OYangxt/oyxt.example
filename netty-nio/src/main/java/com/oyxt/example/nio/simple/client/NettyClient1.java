package com.oyxt.example.nio.simple.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 20190712713
 * @date 2019/12/25 14:49
 */
public class NettyClient1 {
    public static void main(String[] args) {
        EventLoopGroup bossgroup = new NioEventLoopGroup();
        EventLoopGroup workergroup = new NioEventLoopGroup();


        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossgroup,workergroup)
                .channel(NioServerSocketChannel.class);
    }
}
