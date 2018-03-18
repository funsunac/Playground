package com.company.g1.g1extrateamlab;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

public class Bullet{

    private ImageView bullet;
    private final int ANIM_PERIOD = 25;

    Bullet(Context context, float x, float y, int r, float theta) {
        bullet = new ImageView(context);
        bullet.setImageResource(R.drawable.bullet);

        final ConstraintLayout gameLayout = ((Activity)context).findViewById(R.id.gameLayout);
        gameLayout.addView(bullet);

        final int _r = r;
        final float _theta = theta;
        final Handler handler = new Handler();
        bullet.setX(x - bullet.getDrawable().getIntrinsicWidth()/2);
        bullet.setY(y);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bullet.setX((float)(bullet.getX() + _r * Math.cos(Math.toRadians(_theta))));
                bullet.setY((float)(bullet.getY() + _r * Math.sin(Math.toRadians(_theta))));
                if(bullet.getX() > 50 && bullet.getX() < gameLayout.getWidth() - 50
                        && bullet.getY() > 50 && bullet.getY() < gameLayout.getHeight() - 50)
                    handler.postDelayed(this, ANIM_PERIOD);
                else
                    gameLayout.removeView(bullet);
            }
        }, ANIM_PERIOD);
    }
}
