package com.oyxt.example.nio.groupchatv2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author 20190712713
 * @date 2020/1/15 9:23
 */
public class GroupChatServer {
    //监听端口号

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    //编写run方法，处理客户端的请求

    public void run() throws Exception {
        //创建线程组

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //获取PipeLine
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //向pipeLine中加入解码器
                            pipeline.addLast("decoder", new StringDecoder());

                            //向pipeLine中加入编码器
                            pipeline.addLast("encoder", new StringEncoder());

                            //加入自己的业务处理handler
                            pipeline.addLast(new GroupChatServerHandler());
                        }

                    });

            System.out.println("netty服务器启动...");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }

    public static void main(String[] args) throws Exception {
        new GroupChatServer(7000).run();
    }
}
