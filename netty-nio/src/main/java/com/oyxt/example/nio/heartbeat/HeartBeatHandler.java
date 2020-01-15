package com.oyxt.example.nio.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 20190712713
 * @date 2020/1/15 14:28
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventState = null;

            switch (event.state()) {
                case READER_IDLE:
                    eventState = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventState = "写空闲";
                    break;
                case ALL_IDLE:
                    eventState = "读写空闲";
                    break;

            }
            log.info("服务器发生{}，主动断开连接...",eventState);
            ctx.close();

        }
    }
}
