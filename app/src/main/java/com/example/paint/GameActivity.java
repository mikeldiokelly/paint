package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class GameActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[7][7];
    private int[][] color= new int[7][7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                //buttons[i][j] = findViewById(resID);
                //buttons[i][j].setOnClickListener(this::onClick);
            }
        }

        Button reset;
        reset=(Button) findViewById(R.id.reset);
        reset.setOnClickListener(this::onClick);
        reset.setBackgroundColor(Color.parseColor("red"));

    }

    //@Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.reset){
            resetarray();
        }
    }

    private  void resetarray(){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if ((i<4)&&(j<4)) {
                    color[i][j] = 0;
                    setboardcolor(i,j);
                }
                else {
                    color[i][j] = 1;
                    setboardcolor(i,j);
                }
            }
        }
    }

    private void setboardcolor(int i,int j){

        if(color[i][j]== 0) {
            buttons[i][j].setBackgroundColor(Color.parseColor("white"));
            buttons[i][j].setTextColor(Color.parseColor("white"));
            buttons[i][j].setText("x");

        }
        else {
            buttons[i][j].setBackgroundColor(Color.parseColor("black"));
            buttons[i][j].setTextColor(Color.parseColor("black"));
            buttons[i][j].setText("0");
        }

    }


}