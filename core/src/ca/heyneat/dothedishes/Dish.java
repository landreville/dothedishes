package ca.heyneat.dothedishes;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import javax.xml.soap.Text;

public class Dish {
    private Texture texture;
    private Rectangle rectangle;
    private Array<Dirt> dirts;

    public Dish(Texture texture, float x, float y){
        this.texture = texture;
        this.rectangle = new Rectangle();
        this.rectangle.setX(x);
        this.rectangle.setY(y);
        this.rectangle.setHeight(texture.getHeight());
        this.rectangle.setWidth(texture.getWidth());
        this.dirts = new Array<Dirt>();
    }

    public Texture getTexture(){
        return this.texture;
    }

    public float getX() {
        return this.rectangle.getX();
    }

    public float getY() {
        return this.rectangle.getY();
    }

    public float getHeight(){
        return this.rectangle.getHeight();
    }

    public float getWidth(){
        return this.rectangle.getWidth();
    }

    public void setX(float x){
        float diff = x - this.getX();
        this.rectangle.setX(x);
        this.moveDirtXY(diff, 0);
    }

    public void setY(float y){
        float diff = y - this.getY();
        this.rectangle.setY(y);
        this.moveDirtXY(0, diff);
    }

    public void moveX(float x){
        this.rectangle.setX(this.rectangle.getX() + x);
        this.moveDirtXY(x, 0);
    }

    public void moveY(float y){
        this.rectangle.setY(this.rectangle.getY() + y);
        this.moveDirtXY(0, y);
    }

    public void moveXY(float x, float y){
        this.moveX(x);
        this.moveY(y);
        this.moveDirtXY(x, y);
    }

    public void addDirt(Dirt dirt){
        // TODO: Randomize position on dish
        dirt.setX(getX() + getWidth() / 2);
        dirt.setY(getY() + getHeight() / 2);
        this.dirts.add(dirt);
    }

    public Array<Dirt> getDirts(){
        return this.dirts;
    }

    public void removeDirt(int i){
        this.dirts.removeIndex(i);
    }

    public void removeDirt(Dirt dirt){
        this.dirts.removeValue(dirt, true);
    }

    private void moveDirtXY(float x, float y){
        Iterator<Dirt> iter = dirts.iterator();
        while(iter.hasNext()){
            Dirt dirt = iter.next();
            dirt.moveX(x);
            dirt.moveY(y);
        }
    }
}
