package com.example.paint;
import android.app.Application;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ApplicationUtil extends Application {

    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 8888;

    private Socket socket;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    public void init() throws IOException, Exception {
        Runnable sendss = new Runnable(){
            //@Override
            public void run() { try {
                int msgNumber = 10000;
                socket = new Socket(ADDRESS, PORT);
                new ReceiveThread(socket).start();
                OutputStream outStream = socket.getOutputStream();
                for (int i = 0; i < msgNumber; i++) {
                    String sendMsg = "{" + "','time':'" + (i + 1)
                            + "','positionX':'" + ((i * 100003) % 1000)
                            + "','positionY':'" + ((i * 100003) % 800)
                            + "'}";
                    outStream.write(sendMsg.getBytes());
                    System.out.println("Send to server："+sendMsg);
                    Thread.sleep(10000);
                }
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
        };
        Thread thread = new Thread(sendss);
        thread.start();


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
