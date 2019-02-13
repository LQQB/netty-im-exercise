package com.nettystu.demo.io;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {

            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while(true) {
                    try {
                        socket.getOutputStream().write((new Date() + "  :  您好").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) { }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }).start();
    }
}
