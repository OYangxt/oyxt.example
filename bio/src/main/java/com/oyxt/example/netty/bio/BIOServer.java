package com.oyxt.example.netty.bio;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 20190712713
 * @date 2019/12/20 8:56
 */
@Slf4j
public class BIOServer {
    public static void main(String[] args) throws IOException {
        log.info("create a threadPool");
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8888);

        while (true) {
            log.info("SERVICE STARTING...");

            final Socket socket = serverSocket.accept();
            log.info("Server gets a client ...");

            cachedThreadPool.execute(new Runnable() {
                public void run() {
                    communicateWithClient(socket);
                }
            });
        }


    }

    /**
     * 与客户端进行通信
     * @param socket
     */
    private static void communicateWithClient(Socket socket) {
        try {
            log.info("当前线程信息为： id={}，name={}",Thread.currentThread().getId(),Thread.currentThread().getName());

            byte[] bytes = new byte[1024];

            InputStream inputStream = socket.getInputStream();
            log.info("循环读取客户端的数据");

            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    String string = new String(bytes, 0, read);
                    System.out.println(string);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            log.info("关闭和client的连接");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
