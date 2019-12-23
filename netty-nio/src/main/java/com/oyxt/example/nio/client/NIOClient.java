package com.oyxt.example.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author 20190712713
 * @date 2019/12/23 11:38
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作...");
            }
        }

        //如果连接成功，就发送数据

        String str = "Hello, netty";

        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将byteBuffer数据写入到channel
        socketChannel.write(byteBuffer);
        System.in.read();


    }
}
