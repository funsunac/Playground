package com.company.g1.g1extrateamlab;

import java.util.EnumSet;

abstract class GameObject{

    // Need new class for shared resources?
    static int xBound;
    static int yBound;

    enum Bound {
        LEFT, TOP, RIGHT, BOTTOM
    }

    float   x;
    float   y;
    float   height;
    float   width;
    float   rotation;
    float   radius;

    GameObject(float x, float y, float height, float width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.radius = height/2;
    }

    void onOutOfBound(EnumSet<Bound> bounds) {
        if(bounds.contains(Bound.LEFT))
            x = 0;
        if(bounds.contains(Bound.RIGHT))
            x = xBound;
        if(bounds.contains(Bound.TOP))
            y = 0;
        if(bounds.contains(Bound.BOTTOM))
            y = yBound;
    }

    void checkOutOfBound() {
        EnumSet<Bound> bounds = EnumSet.noneOf(Bound.class);
        if (x < 0)  {
            bounds.add(Bound.LEFT);
        } else if (x > xBound)
            bounds.add(Bound.RIGHT);
        if (y < 0)  {
            bounds.add(Bound.TOP);
        } else if (y > yBound)
            bounds.add(Bound.BOTTOM);
        if(!bounds.isEmpty())
            onOutOfBound(bounds);
    }

    float getRadius() {
        return  height / 2;
    }
}
