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


public class GameActivity extends AppCompatActivity {

    static final ImageButton[][] buttons = new ImageButton[10][8];
    static final private int[][] color = new int[10][8];
    static int target_sizeX = 4;
    static int target_sizeY = 4;
    final static int[] target = new int[target_sizeX * target_sizeY];
    static int[] current_board_position;
    CountDownTimer cTimer = null;
    boolean reloading = false;
    static boolean game_start = false;
    static int x_direction = 1; //1 = right, 0 = left
    static int y_direction = 1; //1 = down, 0 = up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //fill button array with buttons relating to those on the XML screen
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                String buttonID = "imgBtn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = (ImageButton) findViewById(resID);
            }
        }
        //initialize the target array
        for (int i = 0; i < target.length; i++) {
            target[i] = 0;
        }

        //every T seconds, update the board
        int[] start = new int[2];
        start[0] = 1; //y
        start[1] = 2; //x
        current_board_position = start; // initialize the current board  position

        update_board(start);
        Button reset;
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this::onClick);
        reset.setBackgroundColor(Color.parseColor("red"));

        Button actualReset;
        actualReset = (Button) findViewById(R.id.actualReset);
        actualReset.setOnClickListener(this::onClickReset);

        move_target(8, 10);

    }

    //
    public void onClick(View v) {

        if(!MainActivity.isClientHost){
            //only host gets to control the room
            return;
        }

        int viewId = v.getId();
        if (!game_start) {
            if (viewId == R.id.reset) {
                resetarray();
                MainActivity.appUtil.unaryCommands(4);
                game_start = true;
                TextView reset_stop_btn = (TextView) findViewById(R.id.reset);
                reset_stop_btn.setText("STOP");
            }
        } else {
            //stop the game
            MainActivity.appUtil.unaryCommands(7);
            game_start = false;
            TextView reset_stop_btn = (TextView) findViewById(R.id.reset);
            reset_stop_btn.setText("START");
        }
        //reset target
        for (int i = 0; i < target.length; i++) {
            target[i] = 0;
        }
    }

    public void onClickReset(View v) {
        MainActivity.appUtil.unaryCommands(8);
        for (int i = 0; i < target.length; i++) {
            target[i] = 0;
        }
    }

    public void onClickGOO(View v) {
        int viewId = v.getId();
        if (!reloading) {

            //create timer and update "Fire" message
            reloading = true;
            TextView reloading_fire = (TextView) findViewById(R.id.reloading_fire);
            reloading_fire.setText("Reloading");
            startTimer();

            //set the board int to the players colour
            String btn_id = v.getResources().getResourceName(v.getId()); //sample of ID "com.example.paint:id/imgBtn_53"
            String[] split_ID = btn_id.split("_");
            String[] split_digits = split_ID[1].split("(?!^)");
            color[Integer.parseInt(split_digits[0])][Integer.parseInt(split_digits[1])] = MainActivity.color;

            boolean target_hit = hit_target(Integer.parseInt(split_digits[0]), Integer.parseInt(split_digits[1]));
            if (target_hit) { // update the target board
                int y_co = Integer.parseInt(split_digits[0]) - current_board_position[0];
                int x_co = Integer.parseInt(split_digits[1]) - current_board_position[1];
                if (target[((y_co * target_sizeY) + x_co)] == 0) {
                    MainActivity.appUtil.throwPaintCommand(x_co, y_co);
                    target[((y_co * target_sizeY) + x_co)] = MainActivity.color;
                }
            }

        } else {
            //wait (maybe make the timer flash or something)
        }
    }

    boolean hit_target(int y, int x) {
        boolean hit = true;
        if (!(y >= current_board_position[0] && y < (current_board_position[0] + target_sizeY))) {
            hit = false;
        } else if (!(x >= current_board_position[1] && x < (current_board_position[1] + target_sizeX))) {
            hit = false;
        }
        return hit;
    }

    void move_target(int screen_sizeX, int screen_sizeY) {
        System.out.println(" in move_target");
        Handler myHandler = new Handler();
        final int[] delay = {1500};
        final int[] count = {0};


        myHandler.postDelayed(new Runnable() {
            public void run() {
                if (game_start) {
                    TextView reset_stop_btn = (TextView) findViewById(R.id.reset);
                    reset_stop_btn.setText("STOP");
                    determine_new_target_coordinates(target_sizeX, target_sizeY, screen_sizeX, screen_sizeY);
                    update_board(current_board_position);
                } else {
                    TextView reset_stop_btn = (TextView) findViewById(R.id.reset);
                    reset_stop_btn.setText("START");
                }
                if (count[0] == 2 && delay[0] > 550) {
                    delay[0] = delay[0] - 150;
                    count[0] = 0;
                }
                count[0]++;
                myHandler.postDelayed(this, delay[0]);
            }
        }, delay[0]);
    }

    static void determine_new_target_coordinates(int target_sizeX, int target_sizeY, int screen_sizeX, int screen_sizeY) {
        int space_between;

        if (x_direction == 1) { //check to see if we have hit the right side of the screen
            space_between = screen_sizeX - (current_board_position[1] + target_sizeX);
            if (space_between <= 0) {
                x_direction = 0;
            }
        } else { //check to see if we have hit the left side of the screen
            if (current_board_position[1] == 0) {
                x_direction = 1;
            }
        }
        if (y_direction == 1) { //check to see if we have hit the bottom of the screen
            space_between = screen_sizeY - (current_board_position[0] + target_sizeY);
            if (space_between <= 0) {
                y_direction = 0;
            }
        } else { //check to see if we have hit the top of the screen
            if (current_board_position[0] == 0) {
                y_direction = 1;
            }
        }

        //find new coordinates
        if (x_direction == 1) {
            current_board_position[1] = current_board_position[1] + 1;
        } else {
            current_board_position[1] = current_board_position[1] - 1;
        }
        if (y_direction == 1) {
            current_board_position[0] = current_board_position[0] + 1;
        } else {
            current_board_position[0] = current_board_position[0] - 1;
        }
    }

    private static void resetarray() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                color[i][j] = 1;
                setboardcolor(i, j);
            }
        }
    }


    private static void setboardcolor(int i, int j) {
        switch (color[i][j]) {
            case 0:
                buttons[i][j].setImageResource(R.drawable.white);
                break;
            case 1:
                buttons[i][j].setImageResource(R.drawable.lightgreen);
                break;
            case 2:
                buttons[i][j].setImageResource(R.drawable.spalsh1blue);
                break;
            case 3:
                buttons[i][j].setImageResource(R.drawable.splash1black);
                break;
            case 4:
                buttons[i][j].setImageResource(R.drawable.splash3green);
                break;
            case 5:
                buttons[i][j].setImageResource(R.drawable.splash1yellow);
                break;
        }
    }

    static void update_board(int[] start) {
        resetarray();
        int a = 0;
        for (int i = start[0]; i < start[0] + target_sizeY; i++) {
            for (int j = start[1]; j < start[1] + target_sizeX; j++) {
                color[i][j] = target[a];
                setboardcolor(i, j);
                a++;
            }
        }
    }

    void startTimer() {
        cTimer = new CountDownTimer(2000, 500) {
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
}




