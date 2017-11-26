package ca.heyneat.dothedishes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Dirt {
    private static final String TAG = "Dirt";
    private Random rand;
    private Sprite dirtSprite;
    private int verticalSwipes;
    private int horizontalSwipes;
    private boolean clean = false;

    public Dirt(Sprite dirtSprite){
        this.rand = new Random();
        this.dirtSprite = dirtSprite;
        this.verticalSwipes = rand.nextInt(20);
        this.horizontalSwipes = rand.nextInt(20);
    }

    public void cleanSwipe(SwipeDirection direction){
        if(this.clean){
            return;
        }

        if(direction == SwipeDirection.VERTICAL){
            this.verticalSwipes -= 1;
        }else{
            this.horizontalSwipes -= 1;
        }

        Color c = this.dirtSprite.getColor();
        if(c.a > 0.25){
            c.a = (float)(c.a - (rand.nextFloat() * 0.1));
            this.dirtSprite.setColor(c);
        }

        if((verticalSwipes <= 0 && (horizontalSwipes <= 0 || verticalSwipes <= horizontalSwipes*5))
                || horizontalSwipes <= 0 && (verticalSwipes <=0 || horizontalSwipes <= verticalSwipes*5)){
            this.clean = true;
            this.dirtSprite.setAlpha(0);
        }
    }

    public boolean isClean(){
        return this.clean;
    }

    public void setPosition(float x, float y) {
        dirtSprite.setPosition(x, y);
    }

    public void setX(float x) {
        dirtSprite.setX(x);
    }

    public void setY(float y) {
        dirtSprite.setY(y);
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

    public float getY() {
        return dirtSprite.getY();
    }

    public float getWidth() {
        return dirtSprite.getWidth();
    }

    public float getHeight() {
        return dirtSprite.getHeight();
    }

    public Rectangle getBoundingRectangle(){
        return this.dirtSprite.getBoundingRectangle();
    }

}
