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
    private Array<Sprite> dirts;
    private boolean spongeActive = false;
    private float spongeHalfWidth;
    private float spongeHalfHeight;


    public SpongeInputProcessor(OrthographicCamera camera, Sprite sponge, Array<Sprite> dirts){
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
        if(spongeRect.contains(vector.x, vector.y)){
            spongeActive = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!spongeActive){
            return false;
        }else{
            spongeActive = false;
            return true;
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!spongeActive){
            return false;
        }
        Vector3 vector = this.camera.unproject(new Vector3(screenX, screenY, 0));
        sponge.setX(vector.x - spongeHalfWidth);
        sponge.setY(vector.y - spongeHalfHeight);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
