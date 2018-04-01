package com.company.g1.g1extrateamlab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View {

    Spaceship spaceship;
    Paint paint = new Paint();
    Paint paint2 = new Paint();

    public GameView(Context context) {
        super(context);
        paint.setColor(0xff00ff00);
        paint2.setColor(0xfc00ff00);
    }

    @Override
    public void onDraw(Canvas canvas) {
//        testThreadingEffect();
        canvas.drawCircle(spaceship.x,spaceship.y,spaceship.width/2, paint);
        for(Bullet bullet : Bullet.bullets) {
            if (bullet != null)
                canvas.drawCircle(bullet.x,bullet.y,spaceship.width/2, paint);
        }
        invalidate();
    }

    /**
     * Tests how separate threads for game physics and renderer
     * affects the game. Currently both run on main thread.
     * Therefore, "framerate" affects objects' speed, when ideally it shouldn't.
     */
    private void testThreadingEffect() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
