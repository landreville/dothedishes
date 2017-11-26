package ca.heyneat.dothedishes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Drawer {
    private final static String TAG = "Drawer";
    private Dish lastTouched;
    private Array<Dish> dishes;
    private Array<Rectangle> wires;
    private Sprite wire;

    public Drawer(Array<Dish> dishes, Array<Rectangle> wires, Sprite wire) {
        this.dishes = dishes;
        this.wires = wires;
        this.wire = wire;
    }

    public void setLastTouched(Dish dish) {
        this.lastTouched = dish;
    }

    public void drawInOrder(SpriteBatch batch) {

        // Bottom wire
        Rectangle wireRect = wires.get(0);
        batch.draw(wire, wireRect.x, wireRect.y);

        for (Dish dish : dishes) {
            if (dish.isDrying()) {
                if (dish.getY() > wires.get(1).getY()) {
                    dish.draw(batch);
                }
            }
        }

        for (int j = 1; j < wires.size; j++) {
            wireRect = wires.get(j);

            for (Dish dish : dishes) {
                if (dish.isDrying()) {
                    if (dish.getY() <= wireRect.y) {
                        dish.draw(batch);
                    }
                }
            }

            batch.draw(wire, wireRect.x, wireRect.y);
        }

        for (Dish dish : dishes) {
            if ((!dish.isDrying() || !dish.isClean()) && dish != lastTouched) {
                dish.draw(batch);
            }
        }

        if (lastTouched != null && !lastTouched.isDrying()) {
            lastTouched.draw(batch);
        }
    }
}
