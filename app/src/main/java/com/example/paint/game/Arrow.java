package com.example.paint.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.List;

public class Arrow extends SinglePaint{

    public Arrow(Bitmap bitmap){
        super(bitmap);
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
        if(!isDestroyed()){
            validatePosition(canvas);
        }
    }

    private void validatePosition(Canvas canvas){
        if(getX() < 0){
            setX(0);
        }
        if(getY() < 0){
            setY(0);
        }
        RectF rectF = getRectF();
        int canvasWidth = canvas.getWidth();
        if(rectF.right > canvasWidth){
            setX(canvasWidth - getWidth());
        }
        int canvasHeight = canvas.getHeight();
        if(rectF.bottom > canvasHeight){
            setY(canvasHeight - getHeight());
        }
    }

}