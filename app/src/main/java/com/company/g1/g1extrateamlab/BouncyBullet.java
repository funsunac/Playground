package com.company.g1.g1extrateamlab;

import java.util.EnumSet;

public class BouncyBullet extends Bullet {

    int bouncedCount = 0;

    BouncyBullet(float x, float y, float theta) {
        super(x, y, theta);
    }

    @Override
    void onOutOfBound(EnumSet<Bound> bounds) {
        if(bouncedCount == 1)
            removeSelf();
        else {
            theta -= 180f;
            bouncedCount++;
        }
    }
}
