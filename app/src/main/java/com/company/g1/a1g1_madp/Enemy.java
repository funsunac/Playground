package com.company.g1.a1g1_madp;

import android.os.Handler;

import java.util.EnumSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Enemy extends MovableObject{

    private final static float  ENEMY_SPEED      = 10;
    private final static float  ENEMY_HEIGHT     = 50;
    private final static float  ENEMY_WIDTH      = 50;
    private final static long   ENEMY_SPAWN_RATE = 1000;
    static CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();

    private static Handler  spawnHandler  = new Handler();
    private static Runnable spawnRunnable = new Runnable(){
        @Override
        public void run() {
            float x = (float) (Math.random() * LAYOUT_WIDTH * 0.9);
            new Enemy(x, 0);
            spawnHandler.postDelayed(this, ENEMY_SPAWN_RATE);
        }
    };

    static void startSpawning() {
        spawnHandler.postDelayed(spawnRunnable, ENEMY_SPAWN_RATE);
    }

    static void stopSpawning() {
        spawnHandler.removeCallbacks(spawnRunnable);
    }

    Enemy(float x, float y) {
        super(x, y, ENEMY_HEIGHT, ENEMY_WIDTH, ENEMY_SPEED);
        theta = 90f;    // Moving downwards
        enemies.add(this);
    }

    void removeSelf() {
        enemies.remove(this);
    }

    @Override
    void onOutOfBound(EnumSet<BOUND> bounds) {
        removeSelf();
    }

    void onHit() {
        removeSelf();
    }
}
