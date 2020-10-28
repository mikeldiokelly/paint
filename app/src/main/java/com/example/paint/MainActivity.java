package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;

        import java.net.Socket;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // todo: connect to the server (socket/http1.1)

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
            findRoom();
        }
    }

    private void findRoom() {
        // todo: request existing rooms
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