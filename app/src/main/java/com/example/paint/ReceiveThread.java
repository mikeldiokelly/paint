package com.example.paint;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static com.example.paint.GameActivity.target;
import static java.lang.Integer.parseInt;

public class ReceiveThread extends Thread {
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
            int len;
            while ((len = socketInStream.read(inStreamBytes)) > 0) {
                String strReceive = new String(inStreamBytes, 0, len);
                //TODO parse...
                System.out.println("receive from Server：" + strReceive);
                char messageCode = strReceive.charAt(0);
                switch (messageCode) {
                    case 'U':
                        // messages from server starting with U are co-ordinates of a hit by some other color {U:<x>,<y>,<color>}
                        // update board
                        System.out.println("this one's update board");
                        String[] values_in_message = strReceive.split(":");
                        values_in_message = values_in_message[1].split(",");
                        int x_co = parseInt(values_in_message[0]);
                        int y_co = parseInt(values_in_message[1]);
                        int color = parseInt(values_in_message[2]);
                        System.out.println(x_co + " " + y_co + " " + color);
                        target[((y_co * GameActivity.target_sizeY) + x_co)] = color;
                        break;
                    case 'C':
                        //assign color
                        int assignedColor = parseInt(strReceive.split(":")[1]);
                        System.out.println("assigned color: " + assignedColor + " to me.");
                        MainActivity.color = assignedColor;
                        break;
                    case 'G':
                        System.out.println("server sends G");
                        GameActivity.game_start = true;
                        for (int i = 0; i < target.length; i++) {
                            target[i] = 0;
                        }

                        break;
                    case 'S':
                        System.out.println("server sends S !!!!!!!!!!!!!!!!!!!!!!!");
                        GameActivity.game_start = false;
                        break;
                    case 'R':
                        System.out.println("server sends reset!!");
                        for (int i = 0; i < target.length; i++) {
                            target[i] = 0;
                        }
                        System.out.println(target);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
