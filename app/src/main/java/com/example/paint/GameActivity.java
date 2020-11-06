package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

public class GameActivity extends AppCompatActivity {

    private ImageButton[] buttons = new ImageButton[64];
    private int[] color= new int[64];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String buttonID = "imgBtn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[8*i+j] = (ImageButton)findViewById(resID);
            }
        }

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
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if ((i<4)&&(j<4)) {
                    color[8*i+j] = 0;
                    setboardcolor(i,j);
                }
                else {
                    color[8*i+j] = 1;
                    setboardcolor(i,j);
                }
            }
        }
    }

    private void setboardcolor(int i,int j){

        if(color[8*i+j]== 0) {
//            buttons[i][j].setBackgroundColor(Color.parseColor("white"));
//            buttons[i][j].setTextColor(Color.parseColor("white"));
//            buttons[i][j].setText("x");
            buttons[8*i+j].setImageResource(R.drawable.lightgreen);

        }
        else {
//            buttons[i][j].setBackgroundColor(Color.parseColor("black"));
//            buttons[i][j].setTextColor(Color.parseColor("black"));
//            buttons[i][j].setText("0");
            //buttons[i][j].setImageResource(R.drawable.splash3green);
        }

    }


}