package com.company.g1.g1extrateamlab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView implements Runnable {

    Spaceship spaceship;
    Paint paint = new Paint();
    Paint paint2 = new Paint();

    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        // Stupid
        paint.setColor(0xff00ff00);
        paint2.setColor(0xfc00ff00);
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    @Override
    public void run() {
        while(running) {
            if(!holder.getSurface().isValid())  // What does this do?
                continue;
            Canvas canvas = holder.lockCanvas();
            draw(canvas);   // This part deviates from the copy source, is it ok?
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        // No idea what's going on
        while(true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        testThreadingEffect();
        canvas.drawCircle(spaceship.x,spaceship.y,spaceship.width/2, paint);
        for(Bullet bullet : Bullet.bullets)
            canvas.drawCircle(bullet.x,bullet.y,spaceship.width/2, paint2);
        for(Enemy enemy : Enemy.enemies)
            canvas.drawCircle(enemy.x,enemy.y,enemy.width/2, paint2);
    }

    /**
     * Tests how separate threads for game physics and renderer
     * affects the game. Currently graphics is rendered on separate thread
     * Therefore, "framerate" should, in theory, has no affect object speed.
     * However, the use of CopyOnWriteArrayList kinda defeats the purpose? I dunno... Confused...
     */
    private void testThreadingEffect() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
