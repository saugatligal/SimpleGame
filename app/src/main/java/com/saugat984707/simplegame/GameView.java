package com.saugat984707.simplegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by 984707 on 12/3/2015.
 */
public class GameView extends SurfaceView {
    SurfaceHolder holder;
    Bitmap mouse;
    // horizontal position (graphic is 205 pixels wide thus initialize right edge of graphic fall to left screen edge)
    private float mouseX = -205.0f;
    private float mouseY = 100.0f;
    private GameThread gthread = null;
    public GameView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mouse =BitmapFactory.decodeResource(getResources(), R.drawable.mouse);
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
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mouseX = mouseX + 4.0f;

        if(mouseX > getWidth()) {}
            mouseX = -205.0f;

        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mouse, mouseX, mouseY, null);

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
}
