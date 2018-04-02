package com.company.g1.g1extrateamlab;

import android.os.Handler;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

public class Spaceship extends MovableObject {


    private Handler bulletHandler  = new Handler();
    private int     bulletFireRate = 200;
    private float aX, aY;
//    Class bulletClass = BouncyBullet.class;
    Class bulletClass = Bullet.class;

    Spaceship() {
        super(50,50,100,100);
        speed = 5;
        theta = -90f;
        setBulletHandler();
    }

    @Override
    void update() {
        super.update(aX, aY);
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

                // YOLOLOLOLOL
                Class ctorArgs[] = new Class[]{float.class, float.class, float.class};
                try {
                    bulletClass.getConstructor(ctorArgs).newInstance(bulletX, bulletY, theta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bulletHandler.postDelayed(this, bulletFireRate);
            }
        }, bulletFireRate);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
//        ship.setRotation(rotation);
    }

    public float getRotation() {
        return rotation;
    }

    public void setAcceleration(float aX, float aY) {
        this.aX = aX;
        this.aY = aY;
    }
}
