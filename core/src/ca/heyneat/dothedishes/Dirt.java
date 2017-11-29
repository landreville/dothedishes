package ca.heyneat.dothedishes;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Represents Dirt to be placed on a plate.
 *
 * This is also a wrapper class for Sprite.
 */
public class Dirt {
    // Maximum number of scrubs to take to clean dirt
    private static final int MAX_SCRUBS = 15;
    private static final String TAG = "Dirt";
    private final Random rand;
    private final Sprite dirtSprite;
    // Number of vertical swipes required to clean dirt
    private int verticalSwipes;
    // Number of horizontal swipes required to clean dirt
    private int horizontalSwipes;
    private boolean clean = false;

    public Dirt(Sprite dirtSprite) {
        this.rand = new Random();
        this.dirtSprite = dirtSprite;
        // Randomly set the number of swipes required to clean the dirt
        this.verticalSwipes = rand.nextInt(MAX_SCRUBS);
        this.horizontalSwipes = rand.nextInt(MAX_SCRUBS);
    }

    /**
     * Perform a cleaning swipe.
     *
     * The number of vertical or horizontal swipes left to clean the dirt will be decremented.
     * The dirt will be reduced in opacity by a random amount to a minimum of 25%.
     *
     * @param direction Direction of the cleaning swipe.
     */
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
        // Don't set the colour lower than 25% alpha, to ensure it's still visible to be cleaned.
        if (c.a > 0.25) {
            c.a = (float) (c.a - (rand.nextFloat() * 0.15));
            this.dirtSprite.setColor(c);
        }

        // If the horizontal and vertical swipe requirements have hit zero then
        // the dirt is cleaned.
        // If the swipes in either direction is the maximum number of swipes (plus the original
        // requirement) then it's considered clean. To prevent people from being
        // frustrated by making swipes in only one direction.
        if (
            (verticalSwipes <= 0 &&
                (horizontalSwipes <= 0 || verticalSwipes <= -1 * MAX_SCRUBS)) ||
            (horizontalSwipes <= 0 &&
                 (verticalSwipes <= 0 || horizontalSwipes <= -1 * MAX_SCRUBS))
        ) {
            this.clean = true;
            this.dirtSprite.setAlpha(0);
        }
    }

    public boolean isClean() {
        return this.clean;
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
