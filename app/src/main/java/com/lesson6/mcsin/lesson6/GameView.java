package com.lesson6.mcsin.lesson6;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by mcsin on 2015/12/2.
 */
public class GameView extends SurfaceView {

    private SurfaceHolder holder;
    private Bitmap red_target;
    private Bitmap bullet;
    private GameThread gthread = null;

    private float red_targetX = -150.0f;
    private float red_targetY = 100.0f; // vertical position
    private float bulletX = -50.0f;
    private float bulletY = -101.0f;
    private boolean bulletYActive = false;

    private int score = 0;
    private Paint scorePaint;


    public GameView(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback( new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                red_target = BitmapFactory.decodeResource(getResources(), R.drawable.target_bullseye);
                bullet = BitmapFactory.decodeResource(getResources(),R.drawable.bullet );

                scorePaint = new Paint();
                scorePaint.setColor(Color.BLACK);
                scorePaint.setTextSize(50.0f);

                makeThread();

                gthread.setRunning(true);
                gthread.start();


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        } );
    }

    public void makeThread()
    {
        gthread = new GameThread(this);

    }

    public void killThread()
    {
        boolean retry = true;
        gthread.setRunning(false);
        while(retry)
        {
            try
            {
                gthread.join();
                retry = false;
            } catch (InterruptedException e)
            {
            }
        }
    }



    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.WHITE);

        canvas.drawText("Score: " + String.valueOf(score), 10.0f, 50.0f, scorePaint);

        if(bulletYActive)
        {
            bulletY = bulletY - 10;
            if ( bulletY < 200 )
            {
                bulletX = -50.0f;
                bulletY = -101.0f;
                bulletYActive = false;
            }
            else
            {
                canvas.drawBitmap(bullet, bulletX, bulletY, null);
            }
        }

        red_targetX = red_targetX + 3.0f;
        if(red_targetX > getWidth()) red_targetX = -205.0f;

        canvas.drawBitmap(red_target, red_targetX, red_targetY, null);

        if ( bulletX >= red_targetX && bulletX <= red_targetX + red_target.getWidth()
                && bulletY <= red_targetY + red_target.getHeight() && bulletY >= red_targetY + red_target.getHeight() +25.0f )
        {
            score++;
            bulletYActive = false;
            bulletX = -50.0f;
            bulletY = -101.0f;

        }



    }

    public void giveFlower()
    {
        bulletYActive = true;
        bulletX = getWidth() / 2.0f - bullet.getWidth() / 2;
        bulletY = getHeight() - bullet.getHeight() - 25;


    }


    public void onDestroy()
    {
        red_target.recycle();
        red_target = null;
        System.gc();
    }
}
