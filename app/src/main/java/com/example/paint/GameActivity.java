package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class GameActivity extends AppCompatActivity {

    private ImageButton[] buttons = new ImageButton[100];
    private int[] color= new int[100];
    CountDownTimer cTimer = null;
    boolean reloading = false;
    boolean game_start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                String buttonID = "imgBtn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[10*i+j] = (ImageButton)findViewById(resID);
            }
        }
        //every T seconds, update the board
        int[] start = new int[2];
        int[] board = new int[16];
        start [0] = 4;
        start [1] = 4;
        for (int i=0;i<16;i++){
            board[i] = 0;
        }
        if (game_start==true) {
            move_board (start);
        }
        update_board(start,board);
        Button reset;
        reset=(Button) findViewById(R.id.reset);
        reset.setOnClickListener(this::onClick);
        reset.setBackgroundColor(Color.parseColor("red"));

    }


    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.reset){
            resetarray();
            game_start = true;
        }
    }

    public void onClickGOO(View v) {
        int viewId = v.getId();
       // if(viewId == R.id.imgBtn_70||viewId == R.id.imgBtn_71||viewId == R.id.imgBtn_72||viewId == R.id.imgBtn_73) {
        if(!reloading) {
            ImageButton btn = (ImageButton) findViewById(viewId);
            btn.setImageResource(R.drawable.splash1black);

            //create timer
            reloading = true;
            TextView reloading_fire = (TextView) findViewById(R.id.reloading_fire);
            reloading_fire.setText("Reloading");
            startTimer();
        }
        else{
            //wait (maybe make the timer flash or something)
        }



    }

    void move_board (int [] start) {
        
    }

    private  void resetarray(){
        for (int i=0;i<10;i++){
            for (int j=0;j<8;j++){
                color[10 * i + j] = 1;
                setboardcolor(i,j);
            }
        }
    }


    private void setboardcolor(int i,int j){
        switch(color[10*i+j]) {
            case 0:
                buttons[10*i+j].setImageResource(R.drawable.white);
                break;
            case 1:
                buttons[10*i+j].setImageResource(R.drawable.lightgreen);
                break;
            case 2:
                buttons[10*i+j].setImageResource(R.drawable.spalsh1blue);
                break;
        }
    }

    void update_board(int [] start, int [] board) {
        resetarray();
        int a=0;
        for (int i = start[0]; i < start[0]+4; i++) {
            for (int j = start[1]; j < start[1]+4; j++) {
                color[10 * i + j] = board[a];
                setboardcolor(i,j);
                a++;
            }
        }
    }

    void startTimer() {

        cTimer = new CountDownTimer(5000, 500) {
            public void onTick(long millisUntilFinished) {
                int remaining_time = Math.round(millisUntilFinished/1000)+1;
                TextView reload_timer = (TextView) findViewById(R.id.reload_timer);
                reload_timer.setText(Long.toString(remaining_time)+" sec");
            }
            public void onFinish() {
                TextView reloading_fire = (TextView) findViewById(R.id.reloading_fire);
                reloading_fire.setText("Fire");
                TextView reload_timer = (TextView) findViewById(R.id.reload_timer);
                reload_timer.setText("");
                reloading = false;
            }
        };
        cTimer.start();
    }

    //cancel timer needs to be called when page is closed otherwise it will keep counting in the background
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}




