package com.company.g1.g1extrateamlab;

abstract class MovableObject extends GameObject {

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

//    void update(float aX, float aY) {
//        float vX = (float)(speed * Math.cos(Math.toRadians(theta)) * aX);
//        float vY = (float)(speed * Math.sin(Math.toRadians(theta)) * aY);
//        x += vX;
//        y += vY;
//        checkOutOfBound();
//    }

    void update(float aX, float aY) {

        float vX = (float)(speed * -aX);
        float vY = (float)(speed *  aY);
        x += vX;
        y += vY;
        checkOutOfBound();
    }

}
