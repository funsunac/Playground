package com.company.g1.g1extrateamlab;

import android.os.Handler;

import java.util.EnumSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Enemy extends MovableObject{

    static CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    static int spawnRate = 1000;
    static Handler spawner = new Handler();

    static void spawnEnemy() {
        spawner.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Enemy(50,50,40,40);
                spawner.postDelayed(this, spawnRate);
            }
        }, spawnRate);
    }

    Enemy(float x, float y, float height, float width) {
        super(x, y, height, width);
        speed = 10;
        theta = 90f;
        enemies.add(this);
    }

    void removeSelf() {
        enemies.remove(this);
    }

    @Override
    void onOutOfBound(EnumSet<Bound> bounds) {
        removeSelf();
    }

    void onHit() {
        removeSelf();
    }
}
