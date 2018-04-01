package com.company.g1.g1extrateamlab;


import android.os.Handler;
import java.util.ArrayList;

public class Bullet extends GameObject {

    static ArrayList<Bullet> bullets = new ArrayList<>();

    private final int ANIM_PERIOD = 25;

    Bullet(float _x, float _y, int r, float theta) {
        final int _r = r;
        final float _theta = theta;
        final Handler handler = new Handler();
        this.x = _x;
        this.y = _y;
        bullets.add(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                x = (float)(x + _r * Math.cos(Math.toRadians(_theta)));
                y = (float)(y + _r * Math.sin(Math.toRadians(_theta)));
                if(x > 50 && x < xBound - 50
                        && y > 50 && y < yBound - 50)
                    handler.postDelayed(this, ANIM_PERIOD);
                else
                    removeSelf();
            }
        }, ANIM_PERIOD);
    }

    private void removeSelf() {
        bullets.remove(this);
    }
}
