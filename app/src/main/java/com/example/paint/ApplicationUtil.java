package com.example.paint;

import android.app.Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ApplicationUtil extends Application {

    public static final String ADDRESS = "176.34.64.145";
    public static final int PORT = 4000;

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public void init() throws IOException, Exception {

        try {
            int msgNumber = 10000;
            //socket = new Socket(ADDRESS, PORT);=
            socket = new Socket(ADDRESS, PORT);
            new ReceiveThread(socket).start();
            dos = new DataOutputStream(socket.getOutputStream());
            Thread.sleep(200);
            dos.writeInt(0);                                        // LOGIN here
            //            for (int i = 0; i < msgNumber; i++) {
//                String sendMsg = "{" + "','time':'" + (i + 1)
//                        + "','positionX':'" + ((i * 100003) % 1000)
//                        + "','positionY':'" + ((i * 100003) % 800)
//                        + "'}";
//                outStream.write(sendMsg.getBytes());
//                System.out.println("Send to server：" + sendMsg);
//                Thread.sleep(10000);
//            }
//            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Thread.sleep(5000);
            init();
        }
    }

    public int sendNewNameCommand(String stringArg) {
        try {
            dos.writeInt(1);
            Thread.sleep(200);
            dos.writeUTF(stringArg);
            dos.flush();
        System.out.println("appUtil line 51");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        System.out.println("appUtil line 57");
        return 0;
    }

    public int throwPaintCommand(int x, int y) {
        System.out.println("In throw paint with" + x + y );
        try {
            dos.writeInt(5);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(MainActivity.color);
            dos.flush();
            System.out.println("throw paint worked");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public int unaryCommands(int command) {
        try {
            dos.writeInt(command);
            dos.flush();
            System.out.println("appUtil line 78");
        } catch (Exception ex) {
            return -1;
        }
        System.out.println("appUtil line 82");
        return 0;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }


    public void setMsg(int command) {
        try {
            dos.writeUTF("?command=");
            dos.writeInt(command);
            dos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
