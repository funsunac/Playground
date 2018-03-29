package com.company.g1.g1extrateamlab;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Really don't like how the view is also controlled by this class.
 * But can't yet think of a better but not overly convoluted implementation.
 */

public class Spaceship {

    private ImageView   ship;
    private float       aFactor = 5.0f;
    private float       rotation = 0f;

    private Handler bulletHandler  = new Handler();
    private int     bulletFireRate = 200;

    // TODO Allow Spaceship and Bullet class to get ImageView resources themselves?

    Spaceship(ImageView ship) {
        this.ship = ship;
        setBulletHandler();
    }

    private void setBulletHandler() {
        bulletHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //ImageView ship = findViewById(R.id.spaceship);
                final float theta = rotation - 90f;
                int   offset = 10;
                int   shipRadius  = ship.getHeight() / 2;
                float shipCenterX = ship.getX() + shipRadius;
                float shipCenterY = ship.getY() + shipRadius;
                float bulletX = (float)(shipCenterX + (shipRadius + offset) * Math.cos(Math.toRadians(theta)));
                float bulletY = (float)(shipCenterY + (shipRadius + offset) * Math.sin(Math.toRadians(theta)));

                new Bullet(ship.getContext(), bulletX, bulletY, 15, theta);
                bulletHandler.postDelayed(this, bulletFireRate);
            }
        }, bulletFireRate);
    }

    public void accelerate(float aX, float aY) {
        // Where is a better place to put this?
        // Cannot put in constructor because it is called in onCreate()
        int layoutWidth = ((View)ship.getParent()).getWidth();
        int layoutHeight = ((View)ship.getParent()).getHeight();

        float newX = ship.getX() - aFactor * aX;
        Log.d("", String.valueOf(layoutHeight));
        float newY = ship.getY() + aFactor * aY;
        if (newX < 0)
            newX = 0;
        else if (newX > layoutWidth - ship.getWidth())
            newX = layoutWidth - ship.getWidth();
        if (newY < 0)
            newY = 0;
        else if (newY > layoutHeight - ship.getHeight())
            newY = layoutHeight - ship.getHeight();
        ship.setX(newX);
        ship.setY(newY);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        ship.setRotation(rotation);
    }

    public float getRotation() {
        return rotation;
    }


}
