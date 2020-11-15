package com.example.paint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class MainActivity extends Activity {
    private Socket socket;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    static ApplicationUtil appUtil =  null;//(ApplicationUtil) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //by default networking is not permitted in MainActivity !
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appUtil =  (ApplicationUtil) this.getApplication();
        try {
            appUtil.init();
//            socket = appUtil.getSocket();
//            dos = appUtil.getDos();
//            dis = appUtil.getDis();
//            appUtil.setMsg(Command.LOGIN);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // todo: start receiving thread
    }

    private int firstContactLogin() throws InterruptedException {
        EditText text = (EditText)findViewById(R.id.editText);
        String username = text.getText().toString();
        int statusCode = appUtil.sendNewNameCommand(username);

        if(statusCode != 0 ) {
            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
        }
        return statusCode;
    }

    public void onClickJoinRoom(View v) throws InterruptedException {
        int viewId = v.getId();
        if(viewId == R.id.btnJoin){
            if(joinRoom()){

                if(firstContactLogin() != 0 ) {
                    return;
                }
                Thread.sleep(500);

                int statusCode = appUtil.unaryCommands(3);
                if(statusCode != 0) {
                    Toast.makeText(getApplicationContext(), "Cannot join room", Toast.LENGTH_SHORT).show();
                    return;
                }
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


    public void onClickCreateRoom(View v) throws InterruptedException {
        int viewId = v.getId();
        if(viewId == R.id.btnCreate){
            createRoom();
        }
    }

    private void createRoom() throws InterruptedException {
        if(firstContactLogin() != 0 ) {
            return;
        }
        Thread.sleep(500);

        int statusCode = appUtil.unaryCommands(2);
        if(statusCode != 0) {
            Toast.makeText(getApplicationContext(), "Cannot create room", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }


}