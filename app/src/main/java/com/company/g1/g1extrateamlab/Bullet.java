package com.company.g1.g1extrateamlab;


import java.util.EnumSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Bullet extends MovableObject {

    // Dev doc say using CopyOnWriteArrayList is costly
    // Ought to think of a better way to avoid ConcurrentModificationException
    static CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();

    Bullet(float x, float y, float theta) {
        super(x, y, 20, 20);
        speed = 15;
        this.theta = theta;
        this.x = x;
        this.y = y;
        checkOutOfBound();
        bullets.add(this);
    }

    void removeSelf() {
        bullets.remove(this);
    }

    @Override
    void onOutOfBound(EnumSet<Bound> bounds) {
        removeSelf();
    }
}
