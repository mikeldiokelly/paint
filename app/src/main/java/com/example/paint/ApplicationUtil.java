package com.example.paint;

import android.app.Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ApplicationUtil extends Application {

    public static final String ADDRESS = "176.34.64.145";
    public static final int PORT = 4000;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public void init() throws Exception {

        try {
            socket = new Socket(ADDRESS, PORT);
            new ReceiveThread(socket).start();
            dos = new DataOutputStream(socket.getOutputStream());
            Thread.sleep(200);
            dos.writeInt(0);
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
        System.out.println("In throw paint with" + x + y);
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

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

}
