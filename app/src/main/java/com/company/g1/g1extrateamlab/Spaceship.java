package com.company.g1.g1extrateamlab;

import android.os.Handler;

import java.util.List;

public class Spaceship extends GameObject {

    private float       aFactor  = 5.0f;

    private Handler bulletHandler  = new Handler();
    private int     bulletFireRate = 200;

    // Where and how to get layout bounds?
    Spaceship() {
        setBulletHandler();
    }

    private void setBulletHandler() {
        bulletHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final float theta = rotation - 90f;
                int   offset = 10;
                float shipCenterX = x + radius;
                float shipCenterY = y + radius;
                float bulletX = (float)(shipCenterX + (radius + offset) * Math.cos(Math.toRadians(theta)));
                float bulletY = (float)(shipCenterY + (radius + offset) * Math.sin(Math.toRadians(theta)));

                new Bullet(bulletX, bulletY, 15, theta);
                bulletHandler.postDelayed(this, bulletFireRate);
            }
        }, bulletFireRate);
    }

    public void accelerate(float aX, float aY) {
        x = x - aFactor * aX;
        y = y + aFactor * aY;

        // TODO create onOutOfBound() in parent class
        if (x < 0)
            x = 0;
        else if (x > xBound - width)
            x = xBound - width;
        if (y < 0)
            y = 0;
        else if (y > yBound - height)
            y = yBound - height;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
//        ship.setRotation(rotation);
    }

    public float getRotation() {
        return rotation;
    }
}
