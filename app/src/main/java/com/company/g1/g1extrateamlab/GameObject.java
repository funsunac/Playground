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
}
