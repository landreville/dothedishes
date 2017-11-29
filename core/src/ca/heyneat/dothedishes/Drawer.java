package ca.heyneat.dothedishes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Draws the dishs, dirt, and drying rack wires in the correct order.
 */
class Drawer {
    private final static String TAG = "Drawer";
    private Dish lastTouched;
    private final Array<Dish> dishes;
    private final Array<Rectangle> wires;
    private final Sprite wire;

    public Drawer(Array<Dish> dishes, Array<Rectangle> wires, Sprite wire) {
        this.dishes = dishes;
        this.wires = wires;
        this.wire = wire;
    }

    /**
     * Set the dish that was last touched so it can be drawn on top.
     *
     * @param dish Dish instance that was touched last.
     */
    public void setLastTouched(Dish dish) {
        this.lastTouched = dish;
    }

    public void drawInOrder(SpriteBatch batch) {

        // Bottom wire
        Rectangle wireRect = wires.get(0);
        batch.draw(wire, wireRect.x, wireRect.y);

        // Loop over the dishes and draw drying dishes that are in front of the
        // bottom rack wire.
        for (Dish dish : dishes) {
            if (dish.isDrying()) {
                if (dish.getY() > wires.get(1).getY()) {
                    dish.draw(batch);
                }
            }
        }

        // Draw dishes in between rack wires based on their Y position
        for (int j = 1; j < wires.size; j++) {
            wireRect = wires.get(j);

            // Nest loops :/
            for (Dish dish : dishes) {
                if (dish.isDrying()) {
                    if (dish.getY() <= wireRect.y) {
                        dish.draw(batch);
                    }
                }
            }

            batch.draw(wire, wireRect.x, wireRect.y);
        }

        // Draw dishes that are not drying (don't draw last touched yet)
        for (Dish dish : dishes) {
            if ((!dish.isDrying() || !dish.isClean()) && dish != lastTouched) {
                dish.draw(batch);
            }
        }

        // Draw the last touched dish on top
        if (lastTouched != null && !lastTouched.isDrying()) {
            lastTouched.draw(batch);
        }
    }

    /**
     * Return the top-most dish that overlaps the given x, y location.
     */
    public Dish getTopTouchedDish(int x, int y) {
        Dish dish = null;
        // It's probably the last touched dish.
        if (lastTouched != null && dishContains(lastTouched, x, y)) {
            return lastTouched;
        }

        // Dirty dishes are on top. Reverse order to get top to bottom.
        for (int i = dishes.size - 1; i >= 0; i--) {
            dish = dishes.get(i);
            if ((!dish.isDrying() || !dish.isClean()) && dishContains(dish, x, y)) {
                return dish;
            }
        }

        // Then check the other dishes. More loops!
        for (int i = dishes.size - 1; i >= 0; i--) {
            dish = dishes.get(i);
            if (dishContains(dish, x, y)) {
                return dish;
            }
        }

        return dish;
    }

    /**
     * Return True if the given dish contains the given X,Y co-ordinates
     */
    private boolean dishContains(Dish dish, int x, int y) {
        Rectangle dishRect = dish.getBoundingRectangle();
        return dishRect.contains(x, y);
    }
}
