package com.company.g1.a1g1_madp;

import android.graphics.Rect;

import java.util.EnumSet;

abstract class MovableObject extends GameObject {

    float speed;
    float theta;

    // Parameter so long! Eww.
    MovableObject(float x, float y, float height, float width, float speed) {
        super(x, y, height, width);
        this.speed = speed;
        this.theta = -90f;  // Pointing upwards by default
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

    void onOutOfBound(EnumSet<BOUND> bounds) {
        if(bounds.contains(BOUND.LEFT))
            x = 0;
        if(bounds.contains(BOUND.RIGHT))
            x = LAYOUT_WIDTH - width;
        if(bounds.contains(BOUND.TOP))
            y = 0;
        if(bounds.contains(BOUND.BOTTOM))
            y = LAYOUT_HEIGHT - height;
    }

    void checkOutOfBound() {
        EnumSet<BOUND> bounds = EnumSet.noneOf(BOUND.class);
        if (x < 0)  {
            bounds.add(BOUND.LEFT);
        } else if (x + width > LAYOUT_WIDTH)
            bounds.add(BOUND.RIGHT);
        if (y < 0)  {
            bounds.add(BOUND.TOP);
        } else if (y + height > LAYOUT_HEIGHT)
            bounds.add(BOUND.BOTTOM);
        if(!bounds.isEmpty())
            onOutOfBound(bounds);
    }

    Rect getHitBox() {
        return new Rect((int)x,(int)y,(int)(x+width), (int)(y+height));
    }

}
