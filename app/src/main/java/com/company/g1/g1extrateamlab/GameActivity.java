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
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {

    Spaceship           spaceship;
    final int           PITCH_OFFSET = 5;   // Accelerometer Y-axis offset
    SensorManager       sensorManager;
    Sensor              accelerometer;
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) { }

        @Override
        public final void onSensorChanged(SensorEvent event) {
            spaceship.accelerate(event.values[0], event.values[1] - PITCH_OFFSET);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ConstraintLayout    gameLayout = findViewById(R.id.gameLayout);
        final ImageView ship = findViewById(R.id.spaceship);
        spaceship = new Spaceship(ship);

        gameLayout.setOnTouchListener(new OnRotationListener(){
            @Override
            public void onRotation() {
                spaceship.setRotation(this.getAngle());
            }
        });

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else {
            sensorManager = null;
        }

        final Handler handler = new Handler();
        final int ANIM_PERIOD = 200;
        final Context context = this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView ship = findViewById(R.id.spaceship);
                final float theta = spaceship.getRotation() - 90f;

                int offset = 10;
                int shipRadius = ship.getHeight() / 2;
                float shipCenterX = ship.getX() + shipRadius;
                float shipCenterY = ship.getY() + shipRadius;
                float bulletX = (float)(shipCenterX + (shipRadius + offset) * Math.cos(Math.toRadians(theta)));
                float bulletY = (float)(shipCenterY + (shipRadius + offset) * Math.sin(Math.toRadians(theta)));

                new Bullet(context, bulletX, bulletY, 15, theta);
                handler.postDelayed(this, ANIM_PERIOD);
            }
        }, ANIM_PERIOD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}
