package com.example.paint;
import android.app.Application;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ApplicationUtil extends Application {

    public static final String ADDRESS = "237.0.0.1";
    public static final int PORT = 8080;

    private Socket socket;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    public void init() throws IOException, Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Build Connection to the Server
                try {
                    socket = new Socket(ADDRESS, PORT);
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


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


    public void setMsg(int command){
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
