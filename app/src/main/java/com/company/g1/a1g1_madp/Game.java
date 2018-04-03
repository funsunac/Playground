package com.company.g1.a1g1_madp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;

/**
 * Need to check if game object and handler is destroyed properly when pause and stop
 */

class Game {
    boolean  running;
    Context  context;
    GameView gameView;
    CollisionSystem collisionSystem;
    Spaceship spaceship;
    final Handler handler = new Handler();
    final int tick = 15;

    // Renderer runs separately in it's own thread
    // Good idea bad idea?
    Runnable gameLoop = new Runnable () {
        @Override
        public void run() {
            spaceship.update();
            for(Bullet bullet : Bullet.bullets)
                bullet.update();
            for(Enemy enemy : Enemy.enemies)
                enemy.update();
            collisionSystem.detectCollision();
            handler.postDelayed(this, tick);
        }
    };

    Game(Context context, int height, int width) {
        this.context = context;
        this.running = false;
        GameObject.LAYOUT_HEIGHT = height;
        GameObject.LAYOUT_WIDTH = width;
    }

    void start() {
        gameView = new GameView(context);
        ((ConstraintLayout)((Activity)context).findViewById(R.id.gameLayout)).addView(gameView);
        collisionSystem = new CollisionSystem();

        spaceship = new Spaceship();
        gameView.spaceship = this.spaceship;
    }

    void resume() {
        // Why is this part necessary?
        if (running) return;
        running = true;
        collisionSystem.setGridParams();
        Enemy.startSpawning();
        handler.postDelayed(gameLoop, tick);
        gameView.resume();
    }

    void pause() {
        if (!running) return;
        running = false;
        Enemy.stopSpawning();
        spaceship.stopFire();
        handler.removeCallbacks(gameLoop);
        gameView.pause();
    }
}
