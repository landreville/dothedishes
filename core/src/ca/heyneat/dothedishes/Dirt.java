package ca.heyneat.dothedishes;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Dirt {
    private static final int MAX_SCRUBS = 15;
    private static final String TAG = "Dirt";
    private Random rand;
    private Sprite dirtSprite;
    private int verticalSwipes;
    private int horizontalSwipes;
    private boolean clean = false;

    public Dirt(Sprite dirtSprite) {
        this.rand = new Random();
        this.dirtSprite = dirtSprite;
        this.verticalSwipes = rand.nextInt(MAX_SCRUBS);
        this.horizontalSwipes = rand.nextInt(MAX_SCRUBS);
    }

    public void cleanSwipe(SwipeDirection direction) {
        if (this.clean) {
            return;
        }

        if (direction == SwipeDirection.VERTICAL) {
            this.verticalSwipes -= 1;
        } else {
            this.horizontalSwipes -= 1;
        }

        Color c = this.dirtSprite.getColor();
        if (c.a > 0.25) {
            c.a = (float) (c.a - (rand.nextFloat() * 0.15));
            this.dirtSprite.setColor(c);
        }

        if ((verticalSwipes <= 0 && (horizontalSwipes <= 0 || verticalSwipes <= horizontalSwipes * 2))
                || horizontalSwipes <= 0 && (verticalSwipes <= 0 || horizontalSwipes <= verticalSwipes * 2)) {
            this.clean = true;
            this.dirtSprite.setAlpha(0);
        }
    }

    public boolean isClean() {
        return this.clean;
    }

    public void setAlpha(float alpha) {
        this.dirtSprite.setAlpha(alpha);
    }

    public void setRotation(float degrees) {
        dirtSprite.setRotation(degrees);
    }

    public void setScale(float scaleXY) {
        dirtSprite.setScale(scaleXY);
    }

    public void draw(Batch batch) {
        dirtSprite.draw(batch);
    }

    public float getX() {
        return dirtSprite.getX();
    }

    public void setX(float x) {
        dirtSprite.setX(x);
    }

    public float getY() {
        return dirtSprite.getY();
    }

    public void setY(float y) {
        dirtSprite.setY(y);
    }

    public float getWidth() {
        return dirtSprite.getWidth();
    }

    public float getHeight() {
        return dirtSprite.getHeight();
    }

    public Rectangle getBoundingRectangle() {
        return this.dirtSprite.getBoundingRectangle();
    }

}
