package com.company.g1.g1extrateamlab;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    SensorManager aSensorM;

    Sensor aAccSensor;
    SensorEventListener myListener = new SensorEventListener() {
        float accFactor = 5.0f;

        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) { }

        @Override
        public final void onSensorChanged(SensorEvent event) {

            ImageView  ship = findViewById(R.id.spaceship);
            ConstraintLayout    gameLayout = findViewById(R.id.gameLayout);

            float newX = ship.getX() - accFactor * event.values[0];
            float newY = ship.getY() + accFactor * ((event.values[1]) - 5);
            if (newX < 0) newX = 0;
            else if (newX > gameLayout.getWidth() - ship.getWidth())
                newX = gameLayout.getWidth()-ship.getWidth();
            if (newY<0) newY = 0;
            else if (newY > gameLayout.getHeight()-ship.getHeight())
                newY = gameLayout.getHeight()-ship.getHeight();
            ship.setX(newX);
            ship.setY(newY);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        aSensorM.registerListener(myListener, aAccSensor, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        aSensorM.unregisterListener(myListener);
    }


    public class Bullet{

        ImageView bullet;

        Bullet(Context context, float x, float y, int r, float theta) {
            bullet = new ImageView(context);

            bullet.setImageResource(R.drawable.bullet);

            ((ConstraintLayout)findViewById(R.id.gameLayout)).addView(bullet);

            final int _r = r;
            final float _theta = theta;
            final Handler handler = new Handler();
            final int ANIM_PERIOD = 25;
            bullet.setX(x - bullet.getDrawable().getIntrinsicWidth()/2);
            bullet.setY(y);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bullet.setX((float)(bullet.getX() + _r * Math.cos(Math.toRadians(_theta))));
                    bullet.setY((float)(bullet.getY() + _r * Math.sin(Math.toRadians(_theta))));
                    // Still not very sure about how handler works, just worry about memory leaks
                    // LOL sorry not sorry
                   if(bullet.getX() > 50 && bullet.getX() < (findViewById(R.id.gameLayout)).getWidth() - 50 && bullet.getY() > 50 && bullet.getY() < (findViewById(R.id.gameLayout)).getHeight() - 50)
                       handler.postDelayed(this, ANIM_PERIOD);
                   else
                       ((ConstraintLayout)findViewById(R.id.gameLayout)).removeView(bullet);
                }
            }, ANIM_PERIOD);
        }
    }
    float rotation = 0f;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ConstraintLayout    gameLayout = findViewById(R.id.gameLayout);

        gameLayout.setOnTouchListener(new View.OnTouchListener() {

            int     ptr1_old_x, ptr1_old_y,
                    ptr2_old_x, ptr2_old_y,
                    ptr1_new_x, ptr1_new_y,
                    ptr2_new_x, ptr2_new_y,
                    mid_pt_x,   mid_pt_y;

            // For debugging only
//            ImageView centerSpot = findViewById(R.id.centerSpot);

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        ptr1_old_x = (int)event.getX(0);
                        ptr1_old_y = (int)event.getY(0);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:

                        if(event.getPointerCount() == 2) {
                            ptr2_old_x = (int)event.getX(1);
                            ptr2_old_y = (int)event.getY(1);
                            mid_pt_x = (ptr1_old_x + ptr2_old_x) / 2;
                            mid_pt_y = (ptr1_old_y + ptr2_old_y) / 2;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(event.getPointerCount() == 2) {
                            ptr1_new_x = (int)event.getX(0);
                            ptr1_new_y = (int)event.getY(0);
                            ptr2_new_x = (int)event.getX(1);
                            ptr2_new_y = (int)event.getY(1);

                            // For debug only
//                            centerSpot.setX(mid_pt_x);
//                            centerSpot.setY(mid_pt_y);

                            double ptr1_old_theta = Math.atan2(ptr1_old_x - mid_pt_x, ptr1_old_y - mid_pt_y);
                            double ptr1_new_theta = Math.atan2(ptr1_new_x - mid_pt_x, ptr1_new_y - mid_pt_y);
                            double ptr2_old_theta = Math.atan2(ptr2_old_x - mid_pt_x, ptr2_old_y - mid_pt_y);
                            double ptr2_new_theta = Math.atan2(ptr2_new_x - mid_pt_x, ptr2_new_y - mid_pt_y);

                            double delta_theta_1 = Math.toDegrees(ptr1_old_theta - ptr1_new_theta);
                            double delta_theta_2 = Math.toDegrees(ptr2_old_theta - ptr2_new_theta);

                            if(delta_theta_1 > 180) delta_theta_1 -= 360;
                            if(delta_theta_1 < -180 ) delta_theta_1 += 360;

                            if(delta_theta_2 > 180) delta_theta_2 -= 360;
                            if(delta_theta_2 < -180 ) delta_theta_2 += 360;

                            rotation += delta_theta_1 + delta_theta_2;

                            findViewById(R.id.spaceship).setRotation(rotation);
                            ptr1_old_x = ptr1_new_x;
                            ptr1_old_y = ptr1_new_y;
                            ptr2_old_x = ptr2_new_x;
                            ptr2_old_y = ptr2_new_y;
                        }
                        break;
                }
                return true;
            }
        });

        aSensorM = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // get sensor
        if (aSensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){ // Success!
            aAccSensor = aSensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else {
            aSensorM = null;
        }

        //
        final Handler handler = new Handler();
        final int ANIM_PERIOD = 200;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView ship = findViewById(R.id.spaceship);
                final float theta = rotation - 90f;

                int offset = 10;
                int shipRadius = ship.getHeight() / 2;
//                Log.d("", String.valueOf(shipRadius));
                float shipCenterX = ship.getX() + shipRadius;
                float shipCenterY = ship.getY() + shipRadius;
                float bulletX = (float)(shipCenterX + (shipRadius + offset) * Math.cos(Math.toRadians(theta)));
                float bulletY = (float)(shipCenterY + (shipRadius + offset) * Math.sin(Math.toRadians(theta)));

                new Bullet(getApplicationContext(), bulletX, bulletY, 15, theta);
                handler.postDelayed(this, ANIM_PERIOD);
            }
        }, ANIM_PERIOD);
    }
}
