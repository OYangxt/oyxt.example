package com.oyxt.example.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author 20190712713
 * @date 2019/12/23 17:26
 */
public class GroupChatServer {
    //定义属性

    private Selector selector;
    private ServerSocketChannel listenChannel;

    private static final int PORT = 6667;

    //构造器，初始化工作

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //监听
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);

                            socketChannel.register(selector,SelectionKey.OP_READ);

                            //提示
                            System.out.println(socketChannel.getRemoteAddress() + " 上线");
                        }

                        //通道发送read事件，即通道是可读的状态
                        if (key.isReadable()) {
                            //处理读
                            readData(key);
                        }

                        //当前的key删除，防止重复处理
                        iterator.remove();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    //读取客户端消息

    private void readData(SelectionKey key) {
        SocketChannel channel = null;

        try {
            channel = (SocketChannel) key.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);

            if (read > 0) {
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" + msg);
                sendInfoToOtherClients(msg,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了。。。");
                //取消注册并关闭通道
                key.cancel();
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");

        //遍历所有注册到selector上的SocketChannel，并排除self
        for (SelectionKey key:selector.keys()) {
            Channel targetChannel = key.channel();

            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }

        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
