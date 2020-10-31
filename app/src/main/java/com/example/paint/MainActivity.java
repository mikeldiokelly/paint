package com.example.paint;
import com.example.paint.Command;
import com.example.paint.ApplicationUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        try {
            appUtil.init();
            socket = appUtil.getSocket();
            dos = appUtil.getDos();
            dis = appUtil.getDis();
            appUtil.setMsg(Command.LOGIN);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // todo: start receiving thread
    }

    public static final String EXTRA_MESSAGE = "Test";
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void onClickJoinRoom(View v) {
        int viewId = v.getId();
        if(viewId == R.id.btnJoin){
            if(joinRoom()){
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
            }
        }
    }

    private void findRoom() {
        appUtil.setMsg(Command.FINDROOM);
        // todo: receive existing rooms
    }

    private boolean joinRoom() {
        appUtil.setMsg(Command.JOINRANDOM);
        // todo: server return succeed or not
        return true;
    }


    public void onClickCreateRoom(View v) {
        int viewId = v.getId();
        if(viewId == R.id.btnCreate){
            createRoom();
        }
    }

    private void createRoom() {
        Intent intent = new Intent(this, LoadActivity.class);
        startActivity(intent);
    }


}