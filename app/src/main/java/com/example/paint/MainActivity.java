package com.example.paint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paint.client.src.BinClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class MainActivity extends Activity {
    private Socket socket;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    ApplicationUtil appUtil =  null;//(ApplicationUtil) this.getApplication();
    private BinClient client;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        appUtil =  (ApplicationUtil) this.getApplication();
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
        client = new BinClient();
        response = "Nothing yet";
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(response);
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

    public void onClickClientTest(View v){
        int viewId = v.getId();
        if(viewId == R.id.btnTest) {
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(response);
        }
    }

    public void onClickClientTestLogin(View v){
        int viewId = v.getId();
        if(viewId == R.id.btnTest2) {
            client.sendThread.unaryCommands(0); // LOGIN
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText("LOGIN");
        }
    }

    public void onClickClientTestUsername(View v){
        int viewId = v.getId();
        if(viewId == R.id.btnTest3) {
            EditText text = (EditText)findViewById(R.id.editText);
            String username = text.getText().toString();
            client.sendThread.sendNewNameCommand(username);
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText("USERNAME CHANGED TO: " + username);
        }
    }

    public void onClickClientTestNewRoom(View v){
        int viewId = v.getId();
        if(viewId == R.id.btnTest4) {
            client.sendThread.unaryCommands(2); //CREATE ROOM
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText("CREATE ROOM");
        }
    }

    public void onClickClientTestRandomRoom(View v){
        int viewId = v.getId();
        if(viewId == R.id.btnTest5) {
            client.sendThread.unaryCommands(3); // JOIN RANDOM ROOM
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText("JOIN RANDOM ROOM");
        }
    }

    public void onClickClientTestStartGame(View v){
        int viewId = v.getId();
        if(viewId == R.id.btnTest6) {
            client.sendThread.unaryCommands(4); // START GAME
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText("START GAME");
        }
    }

    public void onClickClientTestThrowPaint(View v){
        int viewId = v.getId();
        if(viewId == R.id.btnTest7) {
            EditText xText = (EditText)findViewById(R.id.inputX);
            EditText yText = (EditText)findViewById(R.id.inputY);
            int x = Integer.parseInt(xText.getText().toString());
            int y = Integer.parseInt(yText.getText().toString());
            client.sendThread.throwPaintCommand(x, y);
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText("THROW PAINT: " + x + " " + y);
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