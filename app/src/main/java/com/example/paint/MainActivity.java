package com.example.paint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class MainActivity extends Activity {
    private Socket socket;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    ApplicationUtil appUtil =  (ApplicationUtil) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try {
//            appUtil.init();
//            socket = appUtil.getSocket();
//            dos = appUtil.getDos();
//            dis = appUtil.getDis();
//            appUtil.setMsg(Command.LOGIN);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // todo: start receiving thread
    }

    public void onClickJoinRoom(View v) {
        int viewId = v.getId();
        if(viewId == R.id.btnJoin){
            if(joinRoom()){
                Intent intent = new Intent(this, AltergameActivity.class);
                startActivity(intent);
            }
        }
    }

    private void findRoom() {
        // todo:
    }

    private boolean joinRoom() {
        // todo:
        return true;
    }


    public void onClickCreateRoom(View v) {
        int viewId = v.getId();
        if(viewId == R.id.btnCreate){
            createRoom();
        }
    }

    private void createRoom() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }


}