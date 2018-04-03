package com.company.g1.a1g1_madp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;

/**
 * Need to check if game object and handler is destroyed properly when pause and stop
 */

class Game {
    private boolean  running;
    private Context  context;
    private GameView gameView;
    private GameUI   gameUI;
    private CollisionSystem collisionSystem;
    Spaceship spaceship;
    private Handler handler = new Handler();
    private final int tick = 15;

    // Renderer runs separately in it's own thread
    // Good idea bad idea?
    private Runnable gameLoop = new Runnable () {
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
        gameUI = new GameUI(context, this);
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
        spaceship.startFiring();
        handler.postDelayed(gameLoop, tick);
        gameView.resume();
    }

    void pause() {
        if (!running) return;
        running = false;
        Enemy.stopSpawning();
        spaceship.stopFiring();
        handler.removeCallbacks(gameLoop);
        gameView.pause();
    }
}
