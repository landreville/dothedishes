package ca.heyneat.dothedishes;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

class DishInputProcessor implements InputProcessor {
    private static final String TAG = "DishInputProcessor";
    private Dish currentDish;
    private int lastX;
    private int lastY;
    private final OrthographicCamera camera;
    private final Drawer drawer;

    public DishInputProcessor(OrthographicCamera camera, Drawer drawer) {
        this.camera = camera;
        this.currentDish = null;
        this.drawer = drawer;
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

        Dish dish = drawer.getTopTouchedDish((int) vector.x, (int) vector.y);
        if (dish == null) {
            return false;
        }

        currentDish = dish;
        drawer.setLastTouched(dish);
        lastX = (int) vector.x;
        lastY = (int) vector.y;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.currentDish == null) {
            return false;
        }


        this.currentDish = null;

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.currentDish == null) {
            return false;
        }
        Vector3 vector = this.camera.unproject(new Vector3(screenX, screenY, 0));

        currentDish.moveY((int) vector.y - lastY);
        currentDish.moveX((int) vector.x - lastX);
        lastX = (int) vector.x;
        lastY = (int) vector.y;

        currentDish.setDrying(
                inRack(
                        (int) currentDish.getX(),
                        (int) currentDish.getY()
                ) && currentDish.isClean()
        );

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

    private boolean inRack(int x, int y) {
        return (y < DoTheDishes.RACK_TOP_Y &&
                x + currentDish.getWidth() > DoTheDishes.RACK_BOTTOM_LEFT_X &&
                x < DoTheDishes.RACK_TOP_RIGHT_X
        );
    }
}
