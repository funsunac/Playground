package com.company.g1.g1extrateamlab;

import android.graphics.Rect;

import java.util.EnumSet;

abstract class MovableObject extends GameObject {

    static int test;
    float speed;    // This is speed right? My physics on99
    float theta;

    MovableObject(float x, float y, float height, float width) {
        super(x, y, height, width);
    }

    // Update per time unit, 1 time unit = 1 game tick
    void update() {
        float vX = (float)(speed * Math.cos(Math.toRadians(theta)));
        float vY = (float)(speed * Math.sin(Math.toRadians(theta)));
        x += vX;
        y += vY;
        checkOutOfBound();
    }

    void update(float aX, float aY) {

        float vX = (float)(speed * -aX);
        float vY = (float)(speed *  aY);
        x += vX;
        y += vY;
        checkOutOfBound();
    }

    void onOutOfBound(EnumSet<Bound> bounds) {
        if(bounds.contains(Bound.LEFT))
            x = 0;
        if(bounds.contains(Bound.RIGHT))
            x = xBound - width;
        if(bounds.contains(Bound.TOP))
            y = 0;
        if(bounds.contains(Bound.BOTTOM))
            y = yBound - height;
    }

    void checkOutOfBound() {
        EnumSet<Bound> bounds = EnumSet.noneOf(Bound.class);
        if (x < 0)  {
            bounds.add(Bound.LEFT);
        } else if (x + width > xBound)
            bounds.add(Bound.RIGHT);
        if (y < 0)  {
            bounds.add(Bound.TOP);
        } else if (y + height > yBound)
            bounds.add(Bound.BOTTOM);
        if(!bounds.isEmpty())
            onOutOfBound(bounds);
    }

    Rect getHitBox() {
        return new Rect((int)x,(int)y,(int)(x+width), (int)(y+height));
    }

}
