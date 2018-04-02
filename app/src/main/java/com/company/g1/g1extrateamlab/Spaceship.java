package com.company.g1.g1extrateamlab;

import android.os.Handler;
import android.util.Log;

class Spaceship extends MovableObject {

    // Static field should suffice.
    // However, they can't be overridden,
    // superclass method will be messed up
    // Therefore, need to retain and assign to the instance field.
    // A better solution is much much much welcomed!

    private final static float  SHIP_SPEED    = 10;
    private final static float  SHIP_HEIGHT   = 100;
    private final static float  SHIP_WIDTH    = 100;
    private       static long   FIRE_RATE     = 200;
    private final static float  BULLET_OFFSET = 10;     // How far is the bullet spawned from the ship
    private float               aX;
    private float               aY;
//    private Class               bulletClass = Bullet.class;
    private Handler bulletHandler  = new Handler();

    Spaceship() {
        super(LAYOUT_WIDTH / 2 - SHIP_WIDTH / 2,
                LAYOUT_HEIGHT - SHIP_HEIGHT * 1.5f,
                SHIP_HEIGHT,SHIP_WIDTH, SHIP_SPEED);
        fire();
    }

    private void fire() {
        bulletHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.d("rad", String.valueOf(radius));
                float shipCenterX = x + radius;
                float shipCenterY = y + radius;
                float bulletX = (float)(shipCenterX - Bullet.BULLET_WIDTH / 2
                        + (radius + BULLET_OFFSET) * Math.cos(Math.toRadians(theta)));
                float bulletY = (float)(shipCenterY - Bullet.BULLET_HEIGHT / 2
                        + (radius + BULLET_OFFSET) * Math.sin(Math.toRadians(theta)));
                new Bullet(bulletX, bulletY, theta);
                bulletHandler.postDelayed(this, FIRE_RATE);
            }
        }, FIRE_RATE);
    }

    void setAcceleration(float aX, float aY) {
        this.aX = aX;
        this.aY = aY;
    }

    @Override
    void update() {
        super.update(aX, aY);
    }

    //    public void  setRotation(float rotation) { this.rotation = rotation; }
    //    public float getRotation()               { return rotation; }
}
