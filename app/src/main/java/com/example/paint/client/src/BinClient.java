package com.example.paint.client.src;

public class BinClient {
    public SendThread sendThread;
    public BinClient(){
        sendThread = new SendThread();
    }
}