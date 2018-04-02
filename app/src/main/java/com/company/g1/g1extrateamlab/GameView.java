package com.company.g1.g1extrateamlab;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

public class GameView extends SurfaceView implements Runnable {

    Spaceship spaceship;
    Paint paint = new Paint();
    Paint paint2 = new Paint();
    Bitmap bitmap;
    Thread renderThread = null;
    SurfaceHolder holder;
    Canvas canvas;
    volatile boolean running = false;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        // Stupid
        paint.setColor(Color.BLUE);
        paint2.setColor(0xfc00ff00);
    }

    void loadImageResources() {
        Bitmap _bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circle_vector_2);
        bitmap = Bitmap.createScaledBitmap(_bitmap,(int)spaceship.width,(int)spaceship.height,false);
    }

    public void resume() {
        running = true;
        loadImageResources();
        renderThread = new Thread(this);
        renderThread.start();
    }

    @Override
    public void run() {
        while(running) {
            if(!holder.getSurface().isValid())  // What does this do?
                continue;
            canvas = holder.lockCanvas();
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
        drawSpaceship();
        drawBullets();
        drawEnemies();
    }

    private void drawSpaceship() {
        canvas.drawBitmap(bitmap,spaceship.x,spaceship.y,null);
//        float centerX = spaceship.x + spaceship.radius;
//        float centerY = spaceship.y + spaceship.radius;
//        canvas.drawCircle(centerX,centerY,spaceship.radius,paint);
    }

    private void drawBullets() {
        for(Bullet bullet : Bullet.bullets) {
            float centerX = bullet.x + bullet.radius;
            float centerY = bullet.y + bullet.radius;
            canvas.drawCircle(centerX,centerY,bullet.radius, paint);
        }
    }

    private void drawEnemies() {
        for(Enemy enemy : Enemy.enemies) {
            float centerX = enemy.x + enemy.radius;
            float centerY = enemy.y + enemy.radius;
            canvas.drawCircle(centerX,centerY,enemy.radius, paint);
        }
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
