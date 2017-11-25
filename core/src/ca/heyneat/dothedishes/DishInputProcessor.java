package ca.heyneat.dothedishes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class DishInputProcessor implements InputProcessor{
    private static final String TAG = "DishInputProcessor";
    private Array<Dish> dishes;
    private Dish currentDish;
    private int lastX;
    private int lastY;
    private OrthographicCamera camera;

    public DishInputProcessor(OrthographicCamera camera, Array<Dish> dishes){
        this.camera = camera;
        this.dishes = dishes;
        currentDish = null;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 vector = this.camera.unproject(new Vector3(screenX, screenY, 0));

        Dish dish = this.touchedDish((int)vector.x, (int)vector.y);
        if(dish == null){
            return false;
        }

        currentDish = dish;
        lastX = (int)vector.x;
        lastY = (int)vector.y;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(this.currentDish == null){
            return false;
        }

        this.currentDish = null;

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(this.currentDish == null){
            return false;
        }
        Vector3 vector = this.camera.unproject(new Vector3(screenX, screenY, 0));

        currentDish.moveY((int)vector.y - lastY);
        currentDish.moveX((int)vector.x - lastX);
        lastX = (int)vector.x;
        lastY = (int)vector.y;

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private Dish touchedDish(int x, int y){
        Dish dish = null;

        for(int i=dishes.size-1; i>=0; i--){
            dish = dishes.get(i);
            Rectangle dishRect = dish.getBoundingRectangle();
            if(dishRect.contains(x, y)){
                return dish;
            }
        }
        return null;
    }
}
