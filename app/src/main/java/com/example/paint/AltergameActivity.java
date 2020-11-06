package com.example.paint;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.paint.game.GameView;

public class AltergameActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altergame);
        gameView = (GameView)findViewById(R.id.gameViewView);
        // gameView.start(bitmapIds);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameView != null){
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gameView != null){
            gameView.destroy();
        }
        gameView = null;
    }



    public void onClickGo(View v) {
        int viewId = v.getId();
        int[] bitmapIds = {
                R.drawable.arrow,
                R.drawable.splash0black,
                R.drawable.splash1black,
                R.drawable.splash4black,
                R.drawable.splash6black,
                R.drawable.splash7black
        };
        if(viewId == R.id.button_go){
            gameView.start(bitmapIds);
            ObjectAnimator animation = ObjectAnimator.ofFloat(gameView, "translationY", 0,400f,0,400f,0,400f,0,400f,0,400f,0,400f);
            animation.setDuration(10000);
            animation.start();

        }
    }

}