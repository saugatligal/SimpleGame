package com.saugat984707.simplegame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by 984707 on 12/3/2015.
 */
public class GameView extends SurfaceView  {
    SurfaceHolder holder;
    Bitmap mouse;
    private int score = 0;
    private long timer = 0;
    private Paint scorePaint;
    // horizontal position (graphic is 205 pixels wide thus initialize right edge of graphic fall to left screen edge)
    private float mouseX = 205.0f;
    private float mouseY = 100.0f;
    private GameThread gthread = null;
    int min = 100;
    int max = 600;
    int min1 = 100;
    int max2 = 600;
    int startTime = 10000;
    int intervalTime = 1000;
    CountDownTimer maintimer ;





    public GameView(final Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mouse =BitmapFactory.decodeResource(getResources(), R.drawable.mouse);
                scorePaint = new Paint();
                scorePaint.setColor(Color.WHITE);
                scorePaint.setTextSize(50.0f);
                makeThread();

       maintimer=         new CountDownTimer(startTime,intervalTime){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timer = millisUntilFinished/1000;

                    }

                    @Override
                    public void onFinish() {


                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());



                        builder
                                .setMessage("Game over!!!!");


                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                score = 0;
                                makeThread();
maintimer.start();

                                gthread.start();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                killThread();
                                ((Activity)context).finish();
                            }
                        });
                        builder.show();



                    }
                };
                maintimer.start();

                gthread.setRunning(true);
                gthread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }



        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //  mouseX = mouseX + 4.0f;


        if(mouseX > getWidth()) { mouseX = -205.0f;}


        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mouse, mouseX, mouseY, null);
        canvas.drawText("Score: " + String.valueOf(score), 10.0f, 50.0f, scorePaint);
        canvas.drawText("TIME:"+ timer,getWidth()-200.0f,50.0f,scorePaint);

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
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void onDestroy()
    {
        mouse.recycle();
        mouse = null;
        System.gc();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        Random r = new Random();
        float i1 = r.nextInt(max - min + 1) + min;
        float i2 = r.nextInt(max - min + 1) + min;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (x > mouseX && x < mouseX + mouse.getWidth() && y > mouseY && y < mouseY + mouse.getHeight()) {
                Log.e("Coordinate",i1+" "+i2);
                mouseX = i1;
                mouseY = i2;

                score++;
                return true;
            }
        }
        return false;
    }


}
