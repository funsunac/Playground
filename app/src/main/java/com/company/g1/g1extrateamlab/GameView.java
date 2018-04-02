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
    volatile boolean running = false;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        // Stupid
        paint.setColor(Color.BLUE);
        paint2.setColor(0xfc00ff00);
    }

    void loadImageResources() {
        bitmap = decodeSampledBitmapFromResource(
                getResources(), R.drawable.circle_vector_2,
                (int)spaceship.width, (int)spaceship.height);
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
            loadImageResources();

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

        drawSpaceship(canvas);
        drawBullet(canvas);
//        drawEnemy();



        for(Enemy enemy : Enemy.enemies)
            canvas.drawCircle(enemy.x,enemy.y,enemy.width/2, paint2);
    }

    private void drawSpaceship(Canvas canvas) {
        canvas.drawBitmap(bitmap,spaceship.x,spaceship.y,null);
    }

    private void drawBullet(Canvas canvas) {
        for(Bullet bullet : Bullet.bullets)
            canvas.drawCircle(bullet.x,bullet.y,bullet.width/2, paint);
    }



    // Apparently all code below are necessary just to scale the size of a bitmap...
    // https://developer.android.com/topic/performance/graphics/load-bitmap.html
    // Really? WTF!?

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
