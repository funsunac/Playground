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

public class GameActivity extends AppCompatActivity {
    GameView gameView;

    Spaceship           spaceship;
    final int           PITCH_OFFSET = 5;   // Accelerometer Y-axis offset
    SensorManager       sensorManager;
    Sensor              accelerometer;
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) { }

        @Override
        public final void onSensorChanged(SensorEvent event) {
            spaceship.setAcceleration(event.values[0], event.values[1] - PITCH_OFFSET);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
//        setContentView(gameView);
        setContentView(R.layout.activity_game);
        ((ConstraintLayout)findViewById(R.id.gameLayout)).addView(gameView);
        final CollisionSystem collisionSystem = new CollisionSystem();

        gameView.post(new Runnable() {
            @Override
            public void run() {
                GameObject.LAYOUT_WIDTH = gameView.getWidth();
                GameObject.LAYOUT_HEIGHT = gameView.getHeight();
                collisionSystem.setGridParams();
            }
        });

        // PUT IN NEW CLASS
        spaceship = new Spaceship();
        gameView.spaceship = this.spaceship;
        Enemy.spawnEnemy();
        final Handler handler = new Handler();
        final int updateRate = 15;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spaceship.update();
                for(Bullet bullet : Bullet.bullets)
                    bullet.update();
                for(Enemy enemy : Enemy.enemies)
                    enemy.update();
                collisionSystem.detectCollision();
                handler.postDelayed(this, updateRate);
            }
        }, updateRate);
        // PUT IN NEW CLASS

//        gameLayout.setOnTouchListener(new OnRotationListener(){
//            @Override
//            public void onRotation() {
//                spaceship.setRotation(this.getAngle());
//            }
//        });

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else
            sensorManager = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        sensorManager.registerListener(
                sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}
