package ca.heyneat.dothedishes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Random;

import javax.xml.soap.Text;

public class Dish {
    private final static String TAG = "Dish";
    private Random rand;
    private Sprite dishSprite;
    private Array<Sprite> dirtSprites;
    private Rectangle dirtArea;

    public Dish(Sprite dishSprite, Rectangle dirtArea){
        this.dishSprite = dishSprite;
        this.dirtSprites = new Array<Sprite>();
        this.dirtArea = dirtArea;
        this.rand = new Random();
    }

    public Dish copy(){
        return new Dish(
                new Sprite(dishSprite.getTexture()),
                this.dirtArea
        );
    }

    public void draw(SpriteBatch batch){
        this.dishSprite.draw(batch);
        for(int i=0; i<dirtSprites.size; i++){
            dirtSprites.get(i).draw(batch);
        }
    }

    public Rectangle getBoundingRectangle(){
        return this.dishSprite.getBoundingRectangle();
    }

    public float getHeight(){
        return this.dishSprite.getHeight();
    }

    public float getWidth(){
        return this.dishSprite.getWidth();
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

    public Sprite addDirt(Sprite dirt){
        return this.addDirt(dirt, true);
    }

    public Sprite addDirt(Sprite dirt, boolean randomizePosition){
        Sprite newDirt = new Sprite(dirt.getTexture());

        if(randomizePosition) {
            randomizeDirtPosition(newDirt);
        }
        this.dirtSprites.add(newDirt);
        return newDirt;
    }

    private void randomizeDirtPosition(Sprite dirt) {
        float scale = 1;
        float yscale = 1;

        if(this.dirtArea.getWidth() < dirt.getWidth()){
            scale = dirtArea.getWidth() / dirt.getWidth();
        }
        if(this.dirtArea.getHeight() < dirt.getHeight()){
            yscale = dirtArea.getHeight() / dirt.getHeight();
        }
        if(yscale < scale){
            scale = yscale;
        }

        int x;
        int y;
        int xmin = (int)(this.getX() + this.dirtArea.getX());
        int xmax = (int)(this.getX() + this.dirtArea.getX() + this.dirtArea.getWidth());
        int ymin = (int)(this.getY() + this.dirtArea.getY());
        int ymax = (int)(this.getY() + this.dirtArea.getY() + this.dirtArea.getHeight());

        xmax = xmax - ((int)(dirt.getWidth() * scale));
        ymax = ymax - ((int)(dirt.getHeight() * scale));


        if(ymin >= ymax){
            y = ymin;
        }else{
            y = this.rand.nextInt((ymax - ymin) + 1) + ymin;
        }
        if(xmin >= xmax){
            x = xmin;
        }else{
            x = this.rand.nextInt((xmax - xmin) + 1) + xmin;
        }


        dirt.setScale(scale);
        dirt.setX(x);
        dirt.setY(y);
        dirt.rotate(rand.nextInt(15) * (rand.nextBoolean() ? 1 : -1));
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
