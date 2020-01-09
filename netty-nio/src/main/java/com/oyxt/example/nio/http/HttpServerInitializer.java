package com.oyxt.example.nio.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author 20190712713
 * @date 2020/1/9 15:30
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //得到管道，向管道加入处理器
        ChannelPipeline pipeline = socketChannel.pipeline();
        //加入一个netty提供的httpServerCodec codec => [coder - decoder]
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());
    }
}
