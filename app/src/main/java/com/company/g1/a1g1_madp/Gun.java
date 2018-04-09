package com.company.g1.a1g1_madp;

import android.os.Handler;

class Gun {

    private final static int FIRE_RATE = 1000;
    private final static float  BULLET_OFFSET = 10;     // How far is the bullet spawned from the ship

    private GameObject parent;

    Gun(GameObject parent) {
        this.parent = parent;
    }

    private Handler bulletHandler  = new Handler();
    private Runnable bulletRunnable = new Runnable() {
        @Override
        public void run() {
            float shipCenterX = parent.x + parent.radius;
            float shipCenterY = parent.y + parent.radius;
            float bulletX = (float)(shipCenterX - Bullet.BULLET_WIDTH / 2
                    + (parent.radius + BULLET_OFFSET) * Math.cos(Math.toRadians(-90)));
            float bulletY = (float)(shipCenterY - Bullet.BULLET_HEIGHT / 2
                    + (parent.radius + BULLET_OFFSET) * Math.sin(Math.toRadians(-90)));
            new Bullet(bulletX, bulletY, -90);
            bulletHandler.postDelayed(this, FIRE_RATE);
        }
    };

    void startFiring() {
        bulletHandler.postDelayed(bulletRunnable, FIRE_RATE);
    }

    void stopFiring() {
        bulletHandler.removeCallbacks(bulletRunnable);
    }
}
