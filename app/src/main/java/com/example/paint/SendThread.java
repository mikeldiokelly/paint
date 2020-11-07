package com.example.paint;
import java.io.OutputStream;
import java.net.Socket;

public class SendThread implements Runnable {
    @Override
    public void run() {
        try {
            int msgNumber = 10000;
            //server Port
            int serverPort = 8888;
            //server address
            String inetAddress = "127.0.0.1";
            Socket socket = new Socket(inetAddress, serverPort);
            new ReceiveThread(socket).start();
            OutputStream outStream = socket.getOutputStream();

            for (int i = 0; i < msgNumber; i++) {

                // construct json data
                String sendMsg = "{" + "','time':'" + (i + 1)
                        + "','positionX':'" + ((i * 100003) % 1000)
                        + "','positionY':'" + ((i * 100003) % 800)
                        + "'}";

                // send msg
                outStream.write(sendMsg.getBytes());

                // seng msg debug
                System.out.println("Send to server："+sendMsg);

                //
                Thread.sleep(10000);
            }

            // tell the server the connection end
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
