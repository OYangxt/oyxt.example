package com.oyxt.example.nio.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * 1 SimpleChannelInboudHandler 是 ChannelInboudHandlerAdapter
 * 2 HttpObject 客户端和服务器端相互通讯的数据被封装成HttpObject
 * @author 20190712713
 * @date 2020/1/9 15:37
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端的数据
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
        //先判断msg是不是httpRequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("PipeLine的hashcode为：" +  channelHandlerContext.pipeline().hashCode());
            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址" + channelHandlerContext.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri，过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                log.info("请求了 favicon.ico, 但不做响应");
                return;
            }

            //回复信息给浏览器【http协议】
            ByteBuf content = Unpooled.copiedBuffer("Hello, 我是服务器", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的reponse返回
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
