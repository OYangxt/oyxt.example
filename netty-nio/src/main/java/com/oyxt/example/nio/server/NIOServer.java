package com.oyxt.example.nio.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 20190712713
 * @date 2019/12/23 11:00
 */
@Slf4j
public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        Selector selector = Selector.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        log.info("将通道声明为非阻塞");
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector，事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        log.info("客户端注册成功");

        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);

                    System.out.println("from 客户端" + new String(buffer.array()));
                }
                //手动从集合中移动当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }



    }
}
