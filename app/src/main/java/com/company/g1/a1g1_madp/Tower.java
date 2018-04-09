package com.company.g1.a1g1_madp;

import java.util.concurrent.CopyOnWriteArrayList;

public class Tower extends GameObject{

    static CopyOnWriteArrayList<Tower> towers = new CopyOnWriteArrayList<>();

    Gun gun;

    Tower(float x, float y, float height, float width) {
        super(x, y, height, width);
        gun = new Gun(this);
        gun.startFiring();
        towers.add(this);
    }

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }
}
