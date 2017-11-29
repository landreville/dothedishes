package ca.heyneat.dothedishes;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

/**
 * Represents a dish and all of the dirt on it.
 *
 * This is also a wrapper class for the dish sprite.
 */
public class Dish {
    private final static String TAG = "Dish";
    private final Random rand;
    private final Sprite dishSprite;
    private final Array<Dirt> dirts;
    // Area of this dish to place dirt in.
    private final Rectangle dirtArea;
    // Dish is currently drying
    private boolean drying = false;
    // Time when the dish was put in the drying rack
    private long dryingStart;
    // Dish is dropping into the sink
    private boolean droppingIn = false;
    // Final Y position for the dish dropping in
    private int dropToY;

    public Dish(Sprite dishSprite, Rectangle dirtArea) {
        this.dishSprite = dishSprite;
        this.dirts = new Array<Dirt>();
        this.dirtArea = dirtArea;
        this.rand = new Random();
    }

    /**
     * Instantiate a new dish based on this one. Use the same texture.
     * @return A new Dish instance.
     */
    public Dish copy() {
        return new Dish(
                new Sprite(dishSprite.getTexture()),
                this.dirtArea
        );
    }

    /**
     * Render the dish and the dirt on this dish.
     * @param batch SpriteBatch to draw the sprites with.
     */
    public void draw(SpriteBatch batch) {
        this.dishSprite.draw(batch);
        for (int i = 0; i < dirts.size; i++) {
            dirts.get(i).draw(batch);
        }
    }

    /**
     * Start dropping the dish from above the screen to the given y position.
     * @param dropToY Y position at which to stop moving the plate down.
     */
    public void startDropIn(int dropToY) {
        // Place the dish above the screen a random amount so it looks like the dishes
        // dropped at different times.
        this.setY(DoTheDishes.RES_HEIGHT + rand.nextInt(150));
        this.droppingIn = true;
        this.dropToY = dropToY;
    }

    // Wrapper methods for Sprite
    public Rectangle getBoundingRectangle() {
        return this.dishSprite.getBoundingRectangle();
    }

    public float getHeight() {
        return this.dishSprite.getHeight();
    }

    public float getWidth() {
        return this.dishSprite.getWidth();
    }

    public float getX() {
        return this.dishSprite.getX();
    }

    /**
     * Wrapper method for sprite. Also sets the X position for all dirt on this dish.
     * @param x
     */
    public void setX(float x) {
        float diff = x - this.getX();
        this.dishSprite.setX(x);
        this.moveDirtXY(diff, 0);
    }


    public float getY() {
        return this.dishSprite.getY();
    }

    /**
     * Wrapper method for sprite. Also sets the Y position for all dirt on this dish.
     * @param y
     */
    private void setY(float y) {
        float diff = y - this.getY();
        this.dishSprite.setY(y);
        this.moveDirtXY(0, diff);
    }

    /**
     *
     * @return True if this dish has no more dirt on it.
     */
    public boolean isClean() {
        // Loop over the dir to check if each is clean.
        // This could definitely be improved by saving the clean status when dirt is cleaned
        // instead of looping over all the dirt all the time.
        for (int i = 0; i < dirts.size; i++) {
            if (!dirts.get(i).isClean()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return True if the dish is drying.
     */
    public boolean isDrying() {
        return drying;
    }

    /**
     * Set whether the dish is drying or not.
     * @param drying True if the dish is drying.
     */
    public void setDrying(boolean drying) {
        this.drying = drying;
        this.dryingStart = TimeUtils.millis();
    }

    /**
     * How long has the dish been drying.
     * @return Time since setDrying(True) was called in milliseconds.
     */
    public long timeDrying() {
        if (this.dryingStart == 0) {
            return 0;
        }
        return TimeUtils.timeSinceMillis(this.dryingStart);
    }

    /**
     * Move the dish X position by the number provide.
     * @param x Delta to change x.
     */
    public void moveX(int x) {
        if (this.getY() < DoTheDishes.SINK_BOTTOM_Y &&
                this.getX() >= DoTheDishes.SINK_BOTTOM_LEFT_X &&
                this.getX() + x <= DoTheDishes.SINK_BOTTOM_LEFT_X) {
            // Dish hits left side of sink.
            return;
        }
        if (this.getY() < DoTheDishes.SINK_BOTTOM_Y &&
                this.getX() + this.dishSprite.getWidth() <= DoTheDishes.SINK_BOTTOM_RIGHT_X &&
                this.getX() + this.dishSprite.getWidth() + x >= DoTheDishes.SINK_BOTTOM_RIGHT_X) {
            // Dish hits right side of sink.
            return;
        }
        this.setX(this.getX() + x);
    }

    /**
     * Move the dish Y position by the number provide.
     * @param y Delta to change y.
     */
    public void moveY(int y) {

        if (this.getY() + y < DoTheDishes.SINK_BOTTOM_Y &&
                (this.getX() < DoTheDishes.SINK_BOTTOM_LEFT_X ||
                        this.getX() + this.dishSprite.getWidth() > DoTheDishes.SINK_BOTTOM_RIGHT_X)) {
            // Hit the counter
            return;
        }
        if (this.getY() + this.dishSprite.getHeight() / 2 + y < DoTheDishes.SINK_BOTTOM_Y) {
            // Bottom of sink
            return;
        }
        this.setY(this.getY() + y);
    }

    /**
     * Dispose the texture for this dish.
     */
    public void dispose() {
        this.dishSprite.getTexture().dispose();
    }

    /**
     * Add a new dirt using the provided texture. Place it randomly on the dish.
     *
     * @param dirt Texture to use for the dish.
     * @return A new Dirt instance.
     */
    public Dirt addDirt(Texture dirt) {
        Dirt newDirt = new Dirt(new Sprite(dirt));
        randomizeDirtPosition(newDirt);
        this.dirts.add(newDirt);
        return newDirt;
    }

    /**
     * Move the dish down by the delta amount until it gets to the Y position to drop to.
     *
     * @param delta Amount to move the dish down by.
     */
    public void dropIn(int delta) {
        if (this.getY() - delta < this.dropToY) {
            this.setY(this.dropToY);
            this.droppingIn = false;
        } else {
            this.moveY(-delta);
        }
    }

    /**
     * @return True if Dish is still dropping from the sky.
     */
    public boolean isDroppingIn() {
        return this.droppingIn;
    }

    /**
     * Move the dish up by the delta amount.
     *
     * @param delta Amount to move the dish up.
     */
    public void flyOut(int delta) {
        this.moveY(delta);
    }

    /**
     * Randomize the position of the given dirt on this dish. The dirt will also
     * be scaled if it can't fit in dish's dirt area.
     *
     * @param dirt Dirt instance to position.
     */
    private void randomizeDirtPosition(Dirt dirt) {
        float scale = 1;
        float yscale = 1;


        if (this.dirtArea.getWidth() < dirt.getWidth()) {
            // Dish is too wide to fit in dirt area
            scale = dirtArea.getWidth() / dirt.getWidth();
        }
        if (this.dirtArea.getHeight() < dirt.getHeight()) {
            // Dish is too tall to fit in dirt area
            yscale = dirtArea.getHeight() / dirt.getHeight();
        }
        // Use the smallest scaling out of the height and width
        if (yscale < scale) {
            scale = yscale;
        }

        int x;
        int y;
        int xmin = (int) (this.getX() + this.dirtArea.getX());
        int xmax = (int) (this.getX() + this.dirtArea.getX() + this.dirtArea.getWidth());
        int ymin = (int) (this.getY() + this.dirtArea.getY());
        int ymax = (int) (this.getY() + this.dirtArea.getY() + this.dirtArea.getHeight());

        xmax = xmax - ((int) (dirt.getWidth() * scale));
        ymax = ymax - ((int) (dirt.getHeight() * scale));


        if (ymin >= ymax) {
            // Dirt just barely fits on dish (probably scaled to fit exactly)
            y = ymin;
        } else {
            // Randomize the Y position
            y = this.rand.nextInt((ymax - ymin) + 1) + ymin;
        }
        if (xmin >= xmax) {
            // Dirt just barely fits on dish (probably scaled to fit exactly)
            x = xmin;
        } else {
            // Randomize X position
            x = this.rand.nextInt((xmax - xmin) + 1) + xmin;
        }


        dirt.setScale(scale);
        dirt.setX(x);
        dirt.setY(y);
        // Randomly rotate between -15 and 15 degrees
        dirt.setRotation(rand.nextInt(15) * (rand.nextBoolean() ? 1 : -1));
    }

    /**
     * Move all the dirt attached to this dish.
     *
     * @param x Delta amount to move X position.
     * @param y Delta amount to move Y position.
     */
    private void moveDirtXY(float x, float y) {
        for (Dirt dirt : dirts) {
            dirt.setX(dirt.getX() + x);
            dirt.setY(dirt.getY() + y);
        }
    }
}
