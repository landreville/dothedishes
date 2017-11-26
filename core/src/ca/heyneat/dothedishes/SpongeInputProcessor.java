package ca.heyneat.dothedishes;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class SpongeInputProcessor implements InputProcessor {
    private OrthographicCamera camera;
    private Sprite sponge;
    private Array<Dirt> dirts;
    private boolean spongeActive = false;
    private float spongeHalfWidth;
    private float spongeHalfHeight;
    private Dirt activeDirt;
    private Vector3 dirtEntry;


    public SpongeInputProcessor(OrthographicCamera camera, Sprite sponge, Array<Dirt> dirts) {
        this.camera = camera;
        this.sponge = sponge;
        this.dirts = dirts;
        this.spongeHalfWidth = sponge.getWidth() / 2;
        this.spongeHalfHeight = sponge.getHeight() / 2;
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
        Rectangle spongeRect = sponge.getBoundingRectangle();
        if (spongeRect.contains(vector.x, vector.y)) {
            spongeActive = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!spongeActive) {
            return false;
        } else {
            spongeActive = false;
            return true;
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!spongeActive) {
            return false;
        }
        Vector3 vector = this.camera.unproject(new Vector3(screenX, screenY, 0));
        sponge.setX(vector.x - spongeHalfWidth);
        sponge.setY(vector.y - spongeHalfHeight);

        if(sponge.getX() + sponge.getWidth() > DoTheDishes.RES_WIDTH){
            sponge.setX(DoTheDishes.RES_WIDTH - sponge.getWidth());
        }else if(sponge.getX() < 0){
            sponge.setX(0);
        }

        if(sponge.getY() + sponge.getWidth() > DoTheDishes.RES_HEIGHT){
            sponge.setY(DoTheDishes.RES_HEIGHT - sponge.getHeight());
        }else if(sponge.getY() < 0){
            sponge.setY(0);
        }

        swipeDirt(vector);
        
        return true;
    }

    private void swipeDirt(Vector3 vector) {
        Dirt dirt = this.overDirt((int) vector.x, (int) vector.y);
        if (dirt == null && this.activeDirt != null) {
            // Ending swipe
            SwipeDirection direction;
            if (Math.abs(vector.x - dirtEntry.x) >= Math.abs(vector.y - dirtEntry.y)) {
                direction = SwipeDirection.HORIZONTAL;
            } else {
                direction = SwipeDirection.VERTICAL;
            }
            this.activeDirt.cleanSwipe(direction);
            this.activeDirt = null;
            this.dirtEntry = null;
        } else if (dirt != null && this.activeDirt == null) {
            // Starting swipe
            this.activeDirt = dirt;
            this.dirtEntry = vector;
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private Dirt overDirt(int x, int y) {
        Dirt dirt;

        for (int i = dirts.size - 1; i >= 0; i--) {
            dirt = dirts.get(i);
            if (!dirt.isClean() && dirt.getBoundingRectangle().contains(x, y)) {
                return dirt;
            }
        }
        return null;
    }
}
