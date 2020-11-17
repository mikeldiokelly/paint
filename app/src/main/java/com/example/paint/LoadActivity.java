package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoadActivity extends AppCompatActivity {
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    ApplicationUtil appUtil =  (ApplicationUtil) this.getApplication();

    private int roomSize=3;
    private int gameLevel =3;

    private TextView RoomSizeResult;
    private TextView GameLevelResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        bindViews();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void bindViews() {
        SeekBar roomSizeBar = (SeekBar) findViewById(R.id.seekRoomSizeBar);
        RoomSizeResult = (TextView) findViewById(R.id.seekRoomSizeResult);
        roomSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                roomSize = progress;
                RoomSizeResult.setText("Set room size to:" + progress );
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar gameLevelBar = (SeekBar) findViewById(R.id.seekGameLevelBar);
        GameLevelResult = (TextView) findViewById(R.id.seekGameLevelResult);
        gameLevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gameLevel=progress;
                GameLevelResult.setText("Set game level to:" + progress );
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    public void onClickGo(View view) {
        int viewId = view.getId();
        if(viewId == R.id.btnGo){

            try {
                dos = appUtil.getDos();
                dis = appUtil.getDis();
                dos.writeUTF("command=");
                dos.writeInt(4);
                dos.writeUTF("Room Size=");
                dos.writeInt(roomSize);
                dos.writeUTF("Game Level=");
                dos.writeInt(gameLevel);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, GameActivity.class);
            Bundle b=new Bundle();
            b.putInt("roomSize",roomSize);
            b.putInt("gameLevel",gameLevel);
            intent.putExtras(b);

            startActivity(intent);
        }
    }
}