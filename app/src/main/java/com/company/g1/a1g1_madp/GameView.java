package com.company.g1.a1g1_madp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    Spaceship spaceship;
    Paint bulletPaint = new Paint();
    Paint backgroundPaint = new Paint();
    Paint paint2 = new Paint();


    Bitmap spaceshipBitmap;
    Bitmap enemyBitmap;
    Bitmap towerBitmap;

    Thread renderThread = null;
    SurfaceHolder holder;
    Canvas canvas;
    volatile boolean running = false;
    Matrix rotationMatrix = new Matrix();

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        // Stupid
        bulletPaint.setColor(Color.BLUE);
        paint2.setColor(0xfc00ff00);
    }

    void loadImageResources() {
        Bitmap _bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
        spaceshipBitmap = Bitmap.createScaledBitmap(_bitmap,(int)spaceship.width,(int)spaceship.height,false);

        _bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        // TODO Remove hardcode
        enemyBitmap = Bitmap.createScaledBitmap(_bitmap,150,150,false);

        _bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tower);
        towerBitmap = Bitmap.createScaledBitmap(_bitmap,150,150,false);



        bulletPaint.setColor(getResources().getColor(R.color.colorBullet));
        backgroundPaint.setColor(getResources().getColor(R.color.colorBackground));
    }

    public void resume() {
        running = true;
        loadImageResources();
        renderThread = new Thread(this);
        renderThread.start();
    }

    @Override
    public void run() {
        while(running) {
            if(!holder.getSurface().isValid())  // What does this do?
                continue;
            canvas = holder.lockCanvas();
            draw(canvas);   // This part deviates from the copy source, is it ok?
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        // No idea what's going on
        while(true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(backgroundPaint.getColor());
        drawSpaceship();
        drawBullets();
        drawEnemies();
        drawTowers();
    }

    private void drawSpaceship() {
        rotationMatrix.setRotate(spaceship.getRotation()+90f, spaceship.radius, spaceship.radius);
        rotationMatrix.postTranslate(spaceship.x,spaceship.y);
        canvas.drawBitmap(spaceshipBitmap,rotationMatrix,null);
    }

    private void drawBullets() {
        for(Bullet bullet : Bullet.bullets) {
            float centerX = bullet.x + bullet.radius;
            float centerY = bullet.y + bullet.radius;
            canvas.drawCircle(centerX,centerY,bullet.radius, bulletPaint);
        }
    }

    private void drawEnemies() {
        for(Enemy enemy : Enemy.enemies) {
//            float centerX = enemy.x + enemy.radius;
//            float centerY = enemy.y + enemy.radius;
//            canvas.drawCircle(centerX,centerY,enemy.radius, bulletPaint);
            canvas.drawBitmap(enemyBitmap,enemy.x,enemy.y,null);
        }
    }

    private void drawTowers() {
        for(Tower tower : Tower.towers) {
            canvas.drawBitmap(towerBitmap,tower.x,tower.y,null);

        }
    }
}
