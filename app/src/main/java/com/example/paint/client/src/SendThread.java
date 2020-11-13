package com.example.paint.client.src;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SendThread {

    private Socket socket;
    private DataOutputStream dout;
    private Thread mainSendThread;
    public SendThread(){
        try {
            int serverPort = 4000;
            String inetAddress = "176.34.64.145";
            socket = new Socket(inetAddress, serverPort);
            new ReceiveThread(socket).start();
            dout = new DataOutputStream(socket.getOutputStream());
//            while(true) {
//
//            }
//            socket.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private DataOutputStream reInit(){
        try {
            int serverPort = 4000;
            String inetAddress = "176.34.64.145";
            Socket sTemp = new Socket(inetAddress, serverPort);
            dout = new DataOutputStream(sTemp.getOutputStream());
            return dout;
        }
        catch (Exception e) {
            return null;
        }

    }

    public void sendNewNameCommand(String stringArg){
        try {
            mainSendThread = new Thread() {
                public void run(){
                    try {
                        dout = reInit();
                        dout.writeInt(1);
                        dout.writeUTF(stringArg);
                        dout.flush();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            mainSendThread.start();
        }
        catch (Exception e){
            return;
        }
    }

    public void throwPaintCommand(int x, int y){
        try {
            mainSendThread = new Thread() {
                public void run(){
                    try {
                        dout = reInit();
                        dout.writeInt(5);
                        dout.writeInt(x);
                        dout.writeInt(y);
                        dout.flush();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            mainSendThread.start();
        }
        catch (Exception e){
            return;
        }
    }

    public void unaryCommands(int command) {
        try {
            mainSendThread = new Thread() {
                public void run() {
                    try {
                        dout = reInit();
                        dout.writeInt(command);
                        dout.flush();
                    } catch (Exception ex) {
                        return;
                    }
                }
            };
            mainSendThread.start();
        }
        catch (Exception e) {
            return;
        }
    }

}
