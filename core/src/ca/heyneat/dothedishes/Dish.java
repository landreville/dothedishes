package ca.heyneat.dothedishes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import javax.xml.soap.Text;

public class Dish {
    private final static String TAG = "Dish";
    private Sprite dishSprite;
    private Array<Sprite> dirtSprites;

    public Dish(Sprite dishSprite){
        this.dishSprite = dishSprite;
        this.dirtSprites = new Array<Sprite>();
    }

    public void draw(SpriteBatch batch){
        this.dishSprite.draw(batch);
    }

    public Rectangle getBoundingRectangle(){
        return this.dishSprite.getBoundingRectangle();
    }

    public Sprite getSprite(){
        return this.dishSprite;
    }

    public void setX(float x){
        float diff = x - this.getX();
        this.dishSprite.setX(x);
        this.moveDirtXY(diff, 0);
    }

    public void setY(float y){
        float diff = y - this.getY();
        this.dishSprite.setY(y);
        this.moveDirtXY(0, diff);
    }

    public float getX(){
        return this.dishSprite.getX();
    }

    public float getY(){
        return this.dishSprite.getY();
    }

    public void moveX(int x){
        if(this.getY() < DoTheDishes.SINK_BOTTOM_Y &&
                this.getX() >= DoTheDishes.SINK_BOTTOM_LEFT_X &&
                this.getX() + x <= DoTheDishes.SINK_BOTTOM_LEFT_X){
            // Dish hits left side of sink.
            return;
        }
        if(this.getY() < DoTheDishes.SINK_BOTTOM_Y &&
                this.getX() + this.dishSprite.getWidth() <= DoTheDishes.SINK_BOTTOM_RIGHT_X &&
                this.getX() + this.dishSprite.getWidth() + x >= DoTheDishes.SINK_BOTTOM_RIGHT_X){
            // Dish hits right side of sink.
            return;
        }
        this.setX(this.getX() + x);
    }

    public void moveY(int y){
        if(this.getY() + y < DoTheDishes.SINK_BOTTOM_Y &&
                (this.getX() < DoTheDishes.SINK_BOTTOM_LEFT_X ||
                 this.getX() + this.dishSprite.getWidth() > DoTheDishes.SINK_BOTTOM_RIGHT_X)){
            // Hit the counter
            return;
        }
        if(this.getY() + this.dishSprite.getHeight()/2 + y < DoTheDishes.SINK_BOTTOM_Y){
            // Bottom of sink
            return;
        }
        this.setY(this.getY() + y);
    }

    public void addDirt(Sprite dirt){
        this.addDirt(dirt, true);
    }

    public void addDirt(Sprite dirt, boolean randomizePosition){
        if(randomizePosition) {
            // TODO: Randomize position on dish
            dirt.setX(getX() + this.dishSprite.getWidth() / 2);
            dirt.setY(getY() + this.dishSprite.getHeight() / 2);
        }
        this.dirtSprites.add(dirt);
    }

    public Array<Sprite> getDirts(){
        return this.dirtSprites;
    }

    public void removeDirt(int i){
        this.dirtSprites.removeIndex(i);
    }

    public void removeDirt(Sprite dirt){
        this.dirtSprites.removeValue(dirt, true);
    }

    private void moveDirtXY(float x, float y){
        Iterator<Sprite> iter = dirtSprites.iterator();
        while(iter.hasNext()){
            Sprite dirt = iter.next();
            dirt.setX(dirt.getX() + x);
            dirt.setY(dirt.getY() + y);
        }
    }
}
