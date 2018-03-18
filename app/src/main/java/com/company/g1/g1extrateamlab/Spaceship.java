package com.company.g1.g1extrateamlab;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Really don't like how the view is also controlled by this class.
 * But can't yet think of a better but not overly convoluted implementation.
 */

public class Spaceship {

    private ImageView   ship;
    private float       aFactor = 5.0f;
    private float       rotation = 0f;

    // TODO Allow Spaceship and Bullet class to get ImageView resources themselves?

    Spaceship(ImageView ship) {
        this.ship = ship;
    }

    public void accelerate(float aX, float aY) {
        // Where is a better place to put this?
        // Cannot put in constructor because it is called in onCreate()
        int layoutWidth = ((View)ship.getParent()).getWidth();
        int layoutHeight = ((View)ship.getParent()).getHeight();

        float newX = ship.getX() - aFactor * aX;
        Log.d("", String.valueOf(layoutHeight));
        float newY = ship.getY() + aFactor * aY;
        if (newX < 0)
            newX = 0;
        else if (newX > layoutWidth - ship.getWidth())
            newX = layoutWidth - ship.getWidth();
        if (newY < 0)
            newY = 0;
        else if (newY > layoutHeight - ship.getHeight())
            newY = layoutHeight - ship.getHeight();
        ship.setX(newX);
        ship.setY(newY);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        ship.setRotation(rotation);
    }

    public float getRotation() {
        return rotation;
    }

}
