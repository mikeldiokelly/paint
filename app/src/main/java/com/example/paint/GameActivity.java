package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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
        }
    }

    public void onClickGOO(View v) {
        int viewId = v.getId();
       // if(viewId == R.id.imgBtn_70||viewId == R.id.imgBtn_71||viewId == R.id.imgBtn_72||viewId == R.id.imgBtn_73) {
            ImageButton btn = (ImageButton) findViewById(viewId);
            btn.setImageResource(R.drawable.splash1black);



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
}




