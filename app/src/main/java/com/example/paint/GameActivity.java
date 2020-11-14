package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class GameActivity extends AppCompatActivity {

    final private ImageButton[][] buttons = new ImageButton[10][8];
    final private int[][] color= new int[10][8];
    int target_sizeX = 4;
    int target_sizeY = 3;
    int [][] target = new int [target_sizeY][target_sizeX];
    int[] current_board_position;
    CountDownTimer cTimer = null;
    boolean reloading = false;
    boolean game_start = false;
    int x_direction = 1; //1 = right, 0 = left
    int y_direction = 1; //1 = down, 0 = up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //fill button array with buttons relating to those on the XML screen
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                String buttonID = "imgBtn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = (ImageButton)findViewById(resID);
            }
        }
        //initialize the target array
        for (int i = 0; i < target_sizeY; i++) {
            for (int j = 0; j < target_sizeX; j++) {
                target[i][j] = 0;
            }
        }

        //every T seconds, update the board
        int[] start = new int[2];
        int[] board = new int[16];
        start [0] = 1; //y
        start [1] = 2; //x
        current_board_position = start; // initialize the current board  position
        for (int i=0;i<16;i++){
            board[i] = 0;
        }

        update_board(start,board);
        Button reset;
        reset=(Button) findViewById(R.id.reset);
        reset.setOnClickListener(this::onClick);
        reset.setBackgroundColor(Color.parseColor("red"));


        int test_level =1;
//        if (game_start) { //happens before onClick and then doesn't occur again so never works
        move_target(start, test_level, target_sizeX, target_sizeY, 8, 10, board);
//        }
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

            //create timer and update "Fire" message
            reloading = true;
            TextView reloading_fire = (TextView) findViewById(R.id.reloading_fire);
            reloading_fire.setText("Reloading");
            startTimer();

            //set the board int to the players colour
            String btn_id = v.getResources().getResourceName(v.getId()); //sample of ID "com.example.paint:id/imgBtn_53"
            String [] split_ID = btn_id.split("_");
            String [] split_digits = split_ID[1].split("(?!^)");
//            color[Integer.parseInt(split_digits[0])][Integer.parseInt(split_digits[1])] = 2;

            boolean target_hit = hit_target(Integer.parseInt(split_digits[0]), Integer.parseInt(split_digits[1]));
            if(target_hit){ // update the target board
                int y_co =  Integer.parseInt(split_digits[0]) - current_board_position[0];
                int x_co =  Integer.parseInt(split_digits[1]) - current_board_position[1];
                target[y_co][x_co] = 2; //fill in colour variable when we have one
            }

        }
        else{
            //wait (maybe make the timer flash or something)
        }
    }

    boolean hit_target(int y,int  x){
        boolean hit = true;
            if(!(y >= current_board_position[0] && y < (current_board_position[0] + target_sizeY))){
                hit = false;
            }
            else if(!(x >= current_board_position[1] && x < (current_board_position[1] + target_sizeX))){
                hit = false;
            }
            else if(target[y][x] != 0){ // space occupied
                hit = false;
            }
        return hit;
    }

    void move_target(int [] start, int level, int target_sizeX, int target_sizeY, int screen_sizeX, int screen_sizeY, int[] board) {
        double speed = 0.5*level;
        Handler myHandler = new Handler();
        int delay = (int)(speed*1000);


        myHandler.postDelayed(new Runnable() {
            public void run() {
                determine_new_target_coordinates(target_sizeX, target_sizeY, screen_sizeX, screen_sizeY);
                update_board(current_board_position, board);
                myHandler.postDelayed(this, delay);
            }
        }, delay);
    }

    void determine_new_target_coordinates(int target_sizeX, int target_sizeY, int screen_sizeX, int screen_sizeY) {
        int space_between;

        if (x_direction == 1) { //check to see if we have hit the right side of the screen
            space_between = screen_sizeX - (current_board_position[1] + target_sizeX + 1);
            if(space_between <= 0) {
                x_direction = 0;
            }
        }
        else { //check to see if we have hit the left side of the screen
            if(current_board_position[1] == 0){
                x_direction = 1;
            }
        }
        if (y_direction == 1){ //check to see if we have hit the bottom of the screen
            space_between = screen_sizeY - (current_board_position[0] + target_sizeY + 1);
            if(space_between <= 0) {
                y_direction = 0;
            }
        }
        else{ //check to see if we have hit the top of the screen
            if(current_board_position[0] == 0){
                y_direction = 1;
            }
        }

        //find new coordinates
        if(x_direction == 1){
            current_board_position[1] = current_board_position[1] + 1;
        }
        else {
            current_board_position[1] = current_board_position[1] - 1;
        }
        if(y_direction ==1){
            current_board_position[0] = current_board_position[0] + 1;
        }
        else{
            current_board_position[0] = current_board_position[0] - 1;
        }
    }

    private  void resetarray(){
        for (int i=0;i<10;i++){
            for (int j=0;j<8;j++){
                color[i][j] = 1;
                setboardcolor(i,j);
            }
        }
    }


    private void setboardcolor(int i,int j){
        switch(color[i][j]) {
            case 0:
                buttons[i][j].setImageResource(R.drawable.white);
                break;
            case 1:
                buttons[i][j].setImageResource(R.drawable.lightgreen);
                break;
            case 2:
                buttons[i][j].setImageResource(R.drawable.spalsh1blue);
                break;
        }
    }

    void update_board(int [] start, int [] board) {
        resetarray();
        int a=0;
        for (int i = start[0]; i < start[0]+4; i++) {
            for (int j = start[1]; j < start[1]+4; j++) {
                color[i][j] = board[a];
                setboardcolor(i,j);
                a++;
            }
        }
    }

    void startTimer() {
        cTimer = new CountDownTimer(5000, 500) {
            public void onTick(long millisUntilFinished) {
                int remaining_time = Math.round(millisUntilFinished / 1000) + 1;
                TextView reload_timer = (TextView) findViewById(R.id.reload_timer);
                reload_timer.setText(Long.toString(remaining_time) + " sec");
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




