package com.nettystu.demo.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8000);
        // 接收新的连接线程
        new Thread(() -> {
            while (true) {
                try {
                    // 不断调用阻塞方法获取连接
                    Socket socket = serverSocket.accept();
                    new Thread(() -> {
                        try {
                            int len;
                            InputStream intStream = socket.getInputStream();
                            byte[] data = new byte[1024];
                            // 按字节流方式读取
                            while ( (len = intStream.read(data)) != -1) {
                                System.out.println(new String(data, 0, len));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
