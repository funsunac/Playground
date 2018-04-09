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

    //
    private GameView        gameView;
    private GameUI          gameUI;
    private CollisionSystem collisionSystem;
    private InputSystem     inputSystem;

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
            // Omg omg
            gameUI.refreshUI();
            handler.postDelayed(this, tick);
        }
    };

    //

    static int money = 1000;

    Game(Context context, int height, int width) {
        this.context = context;
        this.running = false;
        GameObject.LAYOUT_HEIGHT = height;
        GameObject.LAYOUT_WIDTH = width;
    }

    void start() {
        gameUI          = new GameUI(context, this);
        gameView        = new GameView(context);
        collisionSystem = new CollisionSystem();
        inputSystem     = new InputSystem(this);

        ((ConstraintLayout)((Activity)context).findViewById(R.id.gameLayout)).addView(gameView);
        spaceship = new Spaceship();
        gameView.spaceship = this.spaceship;

        //
        new Tower(GameObject.LAYOUT_WIDTH/2,GameObject.LAYOUT_HEIGHT/2,150,150);
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

    //

    Tower findTower(float x, float y) {
        for(Tower tower : Tower.towers) {
            if(tower.getHitBox().contains((int)x,(int)y))
                return tower;
        }
        return null;
    }

    GameView getGameView() {
        return gameView;
    }
}
