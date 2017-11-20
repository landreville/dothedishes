package ca.heyneat.dothedishes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Dirt {
    private Texture texture;
    private Rectangle rectangle;

    public Dirt(Texture texture){
        this.texture = texture;
        this.rectangle = new Rectangle();
        this.rectangle.setHeight(texture.getHeight());
        this.rectangle.setWidth(texture.getWidth());
    }

    public float getX() {
        return this.rectangle.x;
    }

    public float getY() {
        return this.rectangle.y;
    }

    public void setX(float x){
        this.rectangle.setX(x);
    }

    public void setY(float y){
        this.rectangle.setY(y);
    }

    public float getHeight(){
        return this.rectangle.getHeight();
    }

    public float getWidth(){
        return this.rectangle.getWidth();
    }

    public void moveX(float x){
        this.rectangle.setX(this.rectangle.getX() + x);
    }

    public void moveY(float y){
        this.rectangle.setY(this.rectangle.getY() + y);
    }
}

