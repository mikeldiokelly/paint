package com.example.paint.game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.paint.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends View {

    private Paint paint;
    private Paint textPaint;
    private Arrow combatAircraft = null;
    private List<SinglePaint> sprites = new ArrayList<SinglePaint>();
    private List<SinglePaint> spritesNeedAdded = new ArrayList<SinglePaint>();
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private float density = getResources().getDisplayMetrics().density;//屏幕密度
    public static final int STATUS_GAME_STARTED = 1;//游戏开始
    public static final int STATUS_GAME_PAUSED = 2;//游戏暂停
    public static final int STATUS_GAME_OVER = 3;//游戏结束
    public static final int STATUS_GAME_DESTROYED = 4;//游戏销毁
    private int status = STATUS_GAME_DESTROYED;//初始为销毁状态
    private long frame = 0;//总共绘制的帧数
    private long score = 0;//总得分
    private float fontSize = 12;//默认的字体大小，用于绘制左上角的文本
    private float fontSize2 = 20;//用于在Game Over的时候绘制Dialog中的文本
    private float borderSize = 2;//Game Over的Dialog的边框
    private Rect continueRect = new Rect();//"继续"、"重新开始"按钮的Rect

    //触摸事件相关的变量
    private static final int TOUCH_MOVE = 1;//移动
    private static final int TOUCH_SINGLE_CLICK = 2;//单击
    private static final int TOUCH_DOUBLE_CLICK = 3;//双击
    //一次单击事件由DOWN和UP两个事件合成，假设从down到up间隔小于200毫秒，我们就认为发生了一次单击事件
    private static final int singleClickDurationTime = 200;
    //一次双击事件由两个点击事件合成，两个单击事件之间小于300毫秒，我们就认为发生了一次双击事件
    private static final int doubleClickDurationTime = 300;
    private long lastSingleClickTime = -1;//上次发生单击的时刻
    private long touchDownTime = -1;//触点按下的时刻
    private long touchUpTime = -1;//触点弹起的时刻
    private float touchX = -1;//触点的x坐标
    private float touchY = -1;//触点的y坐标

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameView, defStyle, 0);
        a.recycle();
        //初始化paint
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        //设置textPaint，设置为抗锯齿，且是粗体
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        textPaint.setColor(0xff000000);
        fontSize = textPaint.getTextSize();
        fontSize *= density;
        fontSize2 *= density;
        textPaint.setTextSize(fontSize);
        borderSize *= density;
    }

    public void start(int[] bitmapIds){
        destroy();
        for(int bitmapId : bitmapIds){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
            bitmaps.add(bitmap);
        }
        startWhenBitmapsReady();
    }

    private void startWhenBitmapsReady(){
        combatAircraft = new Arrow(bitmaps.get(0));
        status = STATUS_GAME_STARTED;
        postInvalidate();
    }

    private void restart(){
        destroyNotRecyleBitmaps();
        startWhenBitmapsReady();
    }

    public void pause(){ status = STATUS_GAME_PAUSED; }

    private void resume(){
        status = STATUS_GAME_STARTED;
        postInvalidate();
    }

    private long getScore(){return score; }

    /*-------------------------------draw-------------------------------------*/

    @Override
    protected void onDraw(Canvas canvas) {
        if(isSingleClick()){
            createRandomSprites(canvas.getWidth());
            onSingleClick(touchX, touchY);
        }

        super.onDraw(canvas);

        if(status == STATUS_GAME_STARTED){
            drawGameStarted(canvas);
        }else if(status == STATUS_GAME_PAUSED){
        }else if(status == STATUS_GAME_OVER){
            drawGameOver(canvas);
        }
    }


    private void drawGameStarted(Canvas canvas){
        if(frame == 0){
            float centerX = canvas.getWidth() / 2;
            float centerY = canvas.getHeight() - combatAircraft.getHeight() / 2;
            combatAircraft.centerTo(centerX, centerY);
        }
        if(spritesNeedAdded.size() > 0){
            sprites.addAll(spritesNeedAdded);
            spritesNeedAdded.clear();
        }
        frame++;
        Iterator<SinglePaint> iterator = sprites.iterator();
        while (iterator.hasNext()){
            SinglePaint s = iterator.next();
            s.draw(canvas, paint, this);
        }

        if(combatAircraft != null){
            combatAircraft.draw(canvas, paint, this);
            if(combatAircraft.isDestroyed()){
                status = STATUS_GAME_OVER;
            }
            postInvalidate();
        }
    }


    //绘制结束状态的游戏
    private void drawGameOver(Canvas canvas){
        drawScoreDialog(canvas, "重新开始");

        if(lastSingleClickTime > 0){
            postInvalidate();
        }
    }

    private void drawScoreDialog(Canvas canvas, String operation){
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        //存储原始值
        float originalFontSize = textPaint.getTextSize();
        Paint.Align originalFontAlign = textPaint.getTextAlign();
        int originalColor = paint.getColor();
        Paint.Style originalStyle = paint.getStyle();
        /*
        W = 360
        w1 = 20
        w2 = 320
        buttonWidth = 140
        buttonHeight = 42
        H = 558
        h1 = 150
        h2 = 60
        h3 = 124
        h4 = 76
        */
        int w1 = (int)(20.0 / 360.0 * canvasWidth);
        int w2 = canvasWidth - 2 * w1;
        int buttonWidth = (int)(140.0 / 360.0 * canvasWidth);

        int h1 = (int)(150.0 / 558.0 * canvasHeight);
        int h2 = (int)(60.0 / 558.0 * canvasHeight);
        int h3 = (int)(124.0 / 558.0 * canvasHeight);
        int h4 = (int)(76.0 / 558.0 * canvasHeight);
        int buttonHeight = (int)(42.0 / 558.0 * canvasHeight);

        canvas.translate(w1, h1);
        //绘制背景色
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFD7DDDE);
        Rect rect1 = new Rect(0, 0, w2, canvasHeight - 2 * h1);
        canvas.drawRect(rect1, paint);
        //绘制边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF515151);
        paint.setStrokeWidth(borderSize);
        //paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawRect(rect1, paint);
        //绘制文本"飞机大战分数"
        textPaint.setTextSize(fontSize2);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("飞机大战分数", w2 / 2, (h2 - fontSize2) / 2 + fontSize2, textPaint);
        //绘制"飞机大战分数"下面的横线
        canvas.translate(0, h2);
        canvas.drawLine(0, 0, w2, 0, paint);
        //绘制实际的分数
        String allScore = String.valueOf(getScore());
        canvas.drawText(allScore, w2 / 2, (h3 - fontSize2) / 2 + fontSize2, textPaint);
        //绘制分数下面的横线
        canvas.translate(0, h3);
        canvas.drawLine(0, 0, w2, 0, paint);
        //绘制按钮边框
        Rect rect2 = new Rect();
        rect2.left = (w2 - buttonWidth) / 2;
        rect2.right = w2 - rect2.left;
        rect2.top = (h4 - buttonHeight) / 2;
        rect2.bottom = h4 - rect2.top;
        canvas.drawRect(rect2, paint);
        //绘制文本"继续"或"重新开始"
        canvas.translate(0, rect2.top);
        canvas.drawText(operation, w2 / 2, (buttonHeight - fontSize2) / 2 + fontSize2, textPaint);
        continueRect = new Rect(rect2);
        continueRect.left = w1 + rect2.left;
        continueRect.right = continueRect.left + buttonWidth;
        continueRect.top = h1 + h2 + h3 + rect2.top;
        continueRect.bottom = continueRect.top + buttonHeight;

        //重置
        textPaint.setTextSize(originalFontSize);
        textPaint.setTextAlign(originalFontAlign);
        paint.setColor(originalColor);
        paint.setStyle(originalStyle);
    }


    //生成随机的Sprite
    private void createRandomSprites(int canvasWidth){
        int splatType = (int)Math.ceil(3*Math.random());
        SinglePaint sprite = new SinglePaint(bitmaps.get(splatType));

        if(sprite != null){
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            float x = touchX;//(float)((canvasWidth - spriteWidth)*Math.random());
            float y = touchY;//100;//-spriteHeight;
            sprite.setX(x);
            sprite.setY(y);
            addSprite(sprite);
        }
    }

    /*-------------------------------touch------------------------------------*/

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int touchType = resolveTouchType(event);
        if(status == STATUS_GAME_STARTED){
            if(touchType == TOUCH_MOVE){
                if(combatAircraft != null){
                    combatAircraft.centerTo(touchX, touchY);
                }
            }
        }else if(status == STATUS_GAME_PAUSED){
            if(lastSingleClickTime > 0){
                postInvalidate();
            }
        }else if(status == STATUS_GAME_OVER){
            if(lastSingleClickTime > 0){
                postInvalidate();
            }
        }
        return true;
    }

    //合成我们想要的事件类型
    private int resolveTouchType(MotionEvent event){
        int touchType = -1;
        int action = event.getAction();
        touchX = event.getX();
        touchY = event.getY();
        if(action == MotionEvent.ACTION_MOVE){
            long deltaTime = System.currentTimeMillis() - touchDownTime;
            if(deltaTime > singleClickDurationTime){
                touchType = TOUCH_MOVE;
            }
        }else if(action == MotionEvent.ACTION_DOWN){
            //触点按下
            touchDownTime = System.currentTimeMillis();
        }else if(action == MotionEvent.ACTION_UP){
            touchUpTime = System.currentTimeMillis();
            long downUpDurationTime = touchUpTime - touchDownTime;
            if(downUpDurationTime <= singleClickDurationTime){
                long twoClickDurationTime = touchUpTime - lastSingleClickTime;
                if(twoClickDurationTime <=  doubleClickDurationTime){
                    touchType = TOUCH_DOUBLE_CLICK;
                    lastSingleClickTime = -1;
                    touchDownTime = -1;
                    touchUpTime = -1;
                }else{
                    lastSingleClickTime = touchUpTime;
                }
            }
        }
        return touchType;
    }

    //在onDraw方法中调用该方法，在每一帧都检查是不是发生了单击事件
    private boolean isSingleClick(){
        boolean singleClick = false;
        //我们检查一下是不是上次的单击事件在经过了doubleClickDurationTime毫秒后满足触发单击事件的条件
        if(lastSingleClickTime > 0){
            //计算当前时刻距离上次发生单击事件的时间差
            long deltaTime = System.currentTimeMillis() - lastSingleClickTime;

            if(deltaTime >= doubleClickDurationTime){
                //如果时间差超过了一次双击事件所需要的时间差，
                //那么就在此刻延迟触发之前本该发生的单击事件
                singleClick = true;
                //重置变量
                lastSingleClickTime = -1;
                touchDownTime = -1;
                touchUpTime = -1;
            }
        }
        return singleClick;
    }

    private void onSingleClick(float x, float y){
        if(status == STATUS_GAME_STARTED){
        }else if(status == STATUS_GAME_PAUSED){
        }else if(status == STATUS_GAME_OVER){
            if(isClickRestartButton(x, y)){
                restart();
            }
        }
    }
    //是否单击了GAME OVER状态下的“重新开始”按钮
    private boolean isClickRestartButton(float x, float y){
        return continueRect.contains((int)x, (int)y);
    }

    private RectF getPauseBitmapDstRecF(){
        Bitmap pauseBitmap = status == STATUS_GAME_STARTED ? bitmaps.get(9) : bitmaps.get(10);
        RectF recF = new RectF();
        recF.left = 15 * density;
        recF.top = 15 * density;
        recF.right = recF.left + pauseBitmap.getWidth();
        recF.bottom = recF.top + pauseBitmap.getHeight();
        return recF;
    }

    /*-------------------------------destroy------------------------------------*/

    private void destroyNotRecyleBitmaps(){
        status = STATUS_GAME_DESTROYED;
        frame = 0;
        score = 0;
        if(combatAircraft != null){
            combatAircraft.destroy();
        }
        combatAircraft = null;
        for(SinglePaint s : sprites){
            s.destroy();
        }
        sprites.clear();
    }

    public void destroy(){
        destroyNotRecyleBitmaps();
        for(Bitmap bitmap : bitmaps){
            bitmap.recycle();
        }
        bitmaps.clear();
    }

    /*-------------------------------public methods-----------------------------------*/

    public void addSprite(SinglePaint sprite){
        spritesNeedAdded.add(sprite);
    }

    //添加得分
    public void addScore(int value){
        score += value;
    }

    public int getStatus(){
        return status;
    }

    public float getDensity(){
        return density;
    }



}