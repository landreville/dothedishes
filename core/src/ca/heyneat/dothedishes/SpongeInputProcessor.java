package ca.heyneat.dothedishes;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Process touch inputs on the sponge.
 */
class SpongeInputProcessor implements InputProcessor {
    private final Random rand;
    private final OrthographicCamera camera;
    private final Sprite sponge;
    private final Array<Dirt> dirts;
    private boolean spongeActive = false;
    private final float spongeHalfWidth;
    private final float spongeHalfHeight;
    private Dirt activeDirt;
    private Vector3 dirtEntry;
    private final Array<Sound> scrubSounds;

    public SpongeInputProcessor(
            OrthographicCamera camera,
            Sprite sponge,
            Array<Dirt> dirts,
            Array<Sound> scrubSounds
    ) {
        this.camera = camera;
        this.sponge = sponge;
        this.dirts = dirts;
        this.spongeHalfWidth = sponge.getWidth() / 2;
        this.spongeHalfHeight = sponge.getHeight() / 2;
        this.scrubSounds = scrubSounds;
        this.rand = new Random();
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
            // They're touching the sponge!
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
        // Move the sponge to the new location (centred on it's centre)
        sponge.setX(vector.x - spongeHalfWidth);
        sponge.setY(vector.y - spongeHalfHeight);

        // Don't let the sponge fly off the screen
        if (sponge.getX() + sponge.getWidth() > DoTheDishes.RES_WIDTH) {
            sponge.setX(DoTheDishes.RES_WIDTH - sponge.getWidth());
        } else if (sponge.getX() < 0) {
            sponge.setX(0);
        }

        if (sponge.getY() + sponge.getWidth() > DoTheDishes.RES_HEIGHT) {
            sponge.setY(DoTheDishes.RES_HEIGHT - sponge.getHeight());
        } else if (sponge.getY() < 0) {
            sponge.setY(0);
        }

        // Clean some dirt
        swipeDirt(vector);

        return true;
    }

    /**
     * Make a cleaning swipe on a piece of dirt.
     *
     * @param vector
     */
    private void swipeDirt(Vector3 vector) {
        // Is the sponge over a piece of dirt.
        Dirt dirt = this.overDirt((int) vector.x, (int) vector.y);
        if (dirt == null && this.activeDirt != null) {
            // Finishing a swipe that crossed dirt.
            // Determine the swipe direction based on the current touch position
            // and the dirt location.
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
            // Starting swipe over dirt.
            this.activeDirt = dirt;
            this.dirtEntry = vector;
            // Make a random scrub sound effect as we begin to cross a dirt.
            this.scrubSounds.get(rand.nextInt(this.scrubSounds.size - 1)).play();
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

    /**
     * Check if the touch location is over a Dirt instance.
     *
     * Note this doesn't check if the sponge overlaps, but just the touched location. It works
     * well in practice.
     *
     * @param x Touched X location.
     * @param y Touched Y location.
     * @return Dirt instance or null.
     */
    private Dirt overDirt(int x, int y) {
        Dirt dirt;
        // Loop over all the dirts.
        // The implementation could be improved by removing dirts from the array as they
        // are cleaned so the loop doesn't have to check already cleaned dirts.
        // Iterate in reverse to get the top-most dirt.
        for (int i = dirts.size - 1; i >= 0; i--) {
            dirt = dirts.get(i);
            if (!dirt.isClean() && dirt.getBoundingRectangle().contains(x, y)) {
                return dirt;
            }
        }
        return null;
    }
}
