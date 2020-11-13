package com.example.paint.client.src;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ReceiveThread extends Thread{
    private Socket newSocket;
    private InputStream socketInStream;
    public ReceiveThread(Socket newSocket) throws IOException {
        this.newSocket = newSocket;
        this.socketInStream = newSocket.getInputStream();
    }
    @Override
    public void run() {
        try {
            byte[] inStreamBytes = new byte[1024];
            int len ;
            while ((len = socketInStream.read(inStreamBytes)) > 0) {
                String strReceive = new String(inStreamBytes, 0, len);
                //TODO parse...
                //System.out.println("receive from Server："+strReceive);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
