package ca.heyneat.dothedishes;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class Dish {
    private final static String TAG = "Dish";
    private Random rand;
    private Sprite dishSprite;
    private Array<Dirt> dirts;
    private Rectangle dirtArea;
    private boolean drying = false;
    private float alpha = 1;
    private long dryingStart;
    private boolean fadingIn = false;

    public Dish(Sprite dishSprite, Rectangle dirtArea) {
        this.dishSprite = dishSprite;
        this.dirts = new Array<Dirt>();
        this.dirtArea = dirtArea;
        this.rand = new Random();
    }

    public Dish copy() {
        return new Dish(
                new Sprite(dishSprite.getTexture()),
                this.dirtArea
        );
    }

    public void draw(SpriteBatch batch) {
        this.dishSprite.draw(batch);
        for (int i = 0; i < dirts.size; i++) {
            dirts.get(i).draw(batch);
        }
    }

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

    public void setX(float x) {
        float diff = x - this.getX();
        this.dishSprite.setX(x);
        this.moveDirtXY(diff, 0);
    }

    public float getY() {
        return this.dishSprite.getY();
    }

    public void setY(float y) {
        float diff = y - this.getY();
        this.dishSprite.setY(y);
        this.moveDirtXY(0, diff);
    }

    public boolean isClean() {
        for (int i = 0; i < dirts.size; i++) {
            if (!dirts.get(i).isClean()) {
                return false;
            }
        }
        return true;
    }

    public boolean isDrying() {
        return drying;
    }

    public void setDrying(boolean drying) {
        this.drying = drying;
        this.dryingStart = TimeUtils.millis();
    }

    public long timeDrying() {
        if (this.dryingStart == 0) {
            return 0;
        }
        return TimeUtils.timeSinceMillis(this.dryingStart);
    }

    public void moveX(int x) {
        if (!canMove()) {
            return;
        }

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

    public void moveY(int y) {
        if (!canMove()) {
            return;
        }

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

    public void dispose() {
        this.dishSprite.getTexture().dispose();
    }

    public Dirt addDirt(Texture dirt) {
        Dirt newDirt = new Dirt(new Sprite(dirt));
        randomizeDirtPosition(newDirt);
        this.dirts.add(newDirt);
        return newDirt;
    }

    public boolean fadeOut(float delta) {
        Color color = this.dishSprite.getColor();
        if (alpha - delta <= 0) {
            alpha = 0;
        } else {
            alpha -= delta;
        }

        color.a = alpha;
        this.dishSprite.setColor(color);

        return alpha == 0;
    }

    public boolean fadeIn(float delta) {
        if (!this.fadingIn) {
            this.fadingIn = true;
            this.setAlpha(0);
        }
        Color color = this.dishSprite.getColor();
        if (alpha + delta >= 1) {
            alpha = 1;
            this.fadingIn = false;
        } else {
            alpha += delta;
        }
        color.a = alpha;
        this.dishSprite.setColor(color);

        for (Dirt dirt : dirts) {
            dirt.setAlpha(alpha);
        }

        return alpha == 1;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.dishSprite.setAlpha(alpha);
    }

    public boolean canMove() {
        return alpha == 1;
    }

    private void randomizeDirtPosition(Dirt dirt) {
        float scale = 1;
        float yscale = 1;

        if (this.dirtArea.getWidth() < dirt.getWidth()) {
            scale = dirtArea.getWidth() / dirt.getWidth();
        }
        if (this.dirtArea.getHeight() < dirt.getHeight()) {
            yscale = dirtArea.getHeight() / dirt.getHeight();
        }
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
            y = ymin;
        } else {
            y = this.rand.nextInt((ymax - ymin) + 1) + ymin;
        }
        if (xmin >= xmax) {
            x = xmin;
        } else {
            x = this.rand.nextInt((xmax - xmin) + 1) + xmin;
        }


        dirt.setScale(scale);
        dirt.setX(x);
        dirt.setY(y);
        dirt.setRotation(rand.nextInt(15) * (rand.nextBoolean() ? 1 : -1));
    }

    private void moveDirtXY(float x, float y) {
        for (Dirt dirt : dirts) {
            dirt.setX(dirt.getX() + x);
            dirt.setY(dirt.getY() + y);
        }
    }
}
