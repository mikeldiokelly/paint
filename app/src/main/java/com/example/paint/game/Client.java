package com.example.paint.game;
// package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Client {

    public static final String pollingThread = "POLLING_THREAD";
    public static final String actionThread = "ACTION_THREAD";
    public static final String disconnectThread = "STOP";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public String start() throws IOException {
        try{
            startConnection("176.34.64.145", 4000);
            String resp = sendMessage("hello server");
            // sendMessage(pollingThread);
            stopConnection();
            return resp;
        }
        catch(IOException e){
            System.out.println(e);
            return e.toString();
        }
    }
}
