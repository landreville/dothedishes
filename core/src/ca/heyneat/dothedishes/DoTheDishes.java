package ca.heyneat.dothedishes;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class DoTheDishes extends ApplicationAdapter {
    public static final int SINK_BOTTOM_LEFT_X = 421;
    public static final int SINK_BOTTOM_RIGHT_X = 737;
    public static final int SINK_BOTTOM_Y = 116;
    public static final int RACK_TOP_LEFT_X = 180;
    public static final int RACK_TOP_RIGHT_X = 394;
    public static final int RACK_BOTTOM_LEFT_X = 140;
    public static final int RACK_BOTTOM_RIGHT_X = 348;
    public static final int RACK_TOP_Y = 200;
    public static final int RACK_BOTTOM_Y = 120;
    public static final int RACK_HEIGHT = 35;
    public static final int RES_WIDTH = 800;
    public static final int RES_HEIGHT = 480;
    private static final int SINK_TOP_RIGHT_X = 710;
    private static final int SINK_TOP_LEFT_X = 454;
    private static final int SINK_TOP_Y = 205;
    private Random rand;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Sprite sponge1;
    private Sprite sponge2;
    private Sprite sponge;

    private Sprite background;
    private Sprite counterFg;
    private Sprite rackWire;

    private Array<Rectangle> rackWires;
    private Sprite backgroundSprite;

    private Array<Dish> dishes;
    private Array<Dirt> dirts;

    private Array<Dish> allDish;
    private Array<Texture> allDirt;

    private Dish leaving;
    private Dish coming;

    private Drawer drawer;

    @Override
    public void create() {
        dirts = new Array<Dirt>();
        rand = new Random();

        allDirt = new Array<Texture>();
        allDirt.add(new Texture(Gdx.files.internal("dirt-1-16x32.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-2-64x16.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-3-16x32.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-4-16x32.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-5-8x32.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-6-32x16.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-7-16x16.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-8-32x32.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-9-32x32.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-10-16x16.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-11-16x32.png")));
        allDirt.add(new Texture(Gdx.files.internal("dirt-12-32x32.png")));

        allDish = new Array<Dish>();
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("plate-1-128x128.png"))),
                new Rectangle(20, 20, 88, 88)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("plate-2-128x128.png"))),
                new Rectangle(20, 20, 88, 88)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("plate-3-128x128.png"))),
                new Rectangle(20, 20, 88, 88)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("plate-4-128x128.png"))),
                new Rectangle(20, 20, 88, 88)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("plate-5-96x96.png"))),
                new Rectangle(15, 15, 65, 65)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("plate-6-96x96.png"))),
                new Rectangle(15, 15, 65, 65)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("grater-64x92.png"))),
                new Rectangle(12, 0, 40, 80)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("pan-1-128x128.png"))),
                new Rectangle(19, 19, 85, 85)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("pan-2-128x128.png"))),
                new Rectangle(19, 19, 85, 85)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("strainer-128x128.png"))),
                new Rectangle(29, 19, 85, 85)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("tray-1-256x128.png"))),
                new Rectangle(21, 7, 214, 113)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("tray-2-192x96.png"))),
                new Rectangle(21, 7, 107, 56)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("tray-3-128x64.png"))),
                new Rectangle(3, 3, 121, 58)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("utensil-1-32x128.png"))),
                new Rectangle(5, 96, 22, 27)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("utensil-2-32x128.png"))),
                new Rectangle(1, 95, 30, 32)
        ));

        sponge1 = new Sprite(new Texture(Gdx.files.internal("sponge-1-64x64.png")));
        sponge2 = new Sprite(new Texture(Gdx.files.internal("sponge-2-64x64.png")));
        sponge = rand.nextBoolean() ? sponge1 : sponge2;
        sponge.setX(735);
        sponge.setY(140);

        background = new Sprite(new Texture(Gdx.files.internal("background-800x480.png")));
        counterFg = new Sprite(new Texture(Gdx.files.internal("counter-fg-321x115.png")));
        rackWire = new Sprite(new Texture(Gdx.files.internal("rack-wire-227x35.png")));

        dishes = new Array<Dish>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, RES_WIDTH, RES_HEIGHT);

        batch = new SpriteBatch();

        backgroundSprite = new Sprite(background);

        initializeDishRack();
        initializeDishes();

        drawer = new Drawer(dishes, rackWires, rackWire);

        InputMultiplexer inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(new SpongeInputProcessor(camera, sponge, dirts));
        inputProcessor.addProcessor(new DishInputProcessor(camera, dishes, drawer));

        Gdx.input.setInputProcessor(inputProcessor);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    private void initializeDishes() {
        float width;
        float height;
        float x;
        float y;

        int min;
        int max;
        int dishCount = 9;

        float deltaY = 0;

        for (int i = 0; i < dishCount; i++) {
            deltaY += rand.nextInt((SINK_TOP_Y - SINK_BOTTOM_Y) / dishCount);
            dishes.add(placeDishInSink(deltaY));
        }
    }

    private Dish placeDishInSink(float deltaY) {
        float width;
        float height;
        float y;
        int max;
        int min;
        float x;
        float m = SINK_TOP_Y / (SINK_TOP_LEFT_X - SINK_BOTTOM_LEFT_X);
        Dish aDish = this.allDish.get(
                rand.nextInt(this.allDish.size)
        );

        width = aDish.getWidth();
        height = aDish.getHeight();

        y = SINK_TOP_Y - height - deltaY;

        if (y < SINK_BOTTOM_Y - height / 2) {
            y = SINK_BOTTOM_Y - height / 2;
        }

        max = (int) (SINK_BOTTOM_RIGHT_X - y / m - width);
        min = (int) (SINK_BOTTOM_LEFT_X + y / m);
        if (min < SINK_BOTTOM_LEFT_X) {
            min = SINK_BOTTOM_LEFT_X;
        }
        if (max > SINK_BOTTOM_RIGHT_X) {
            max = SINK_BOTTOM_RIGHT_X;
        }

        x = rand.nextInt((max - min) + 1) + min;


        Dish dish = aDish.copy();
        dish.setX(x);
        dish.setY(y);
        for (int j = 0; j <= rand.nextInt(5); j++) {
            dirts.add(dish.addDirt(allDirt.get(rand.nextInt(allDirt.size))));
        }

        return dish;
    }

    private void initializeDishRack() {
        rackWires = new Array<Rectangle>();
        int x = 139;
        int y = 122;
        for (int i = 0; i < 8; i++) {

            rackWires.add(new Rectangle(
                    x,
                    y,
                    165,
                    26
            ));
            x += 5;
            y += 10;
        }
        rackWires.reverse();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        backgroundSprite.draw(batch);

        cycleDishes();
        drawer.drawInOrder(batch);

        batch.draw(counterFg, 416, 0);
        sponge.draw(batch);
        batch.end();
    }

    private void cycleDishes() {
        Array<Dish> cleanDishes = new Array<Dish>();
        boolean gone;
        boolean here;
        Dish dryingDish;

        for (Dish dish : dishes) {
            if (dish.isDrying() && dish.isClean()) {
                cleanDishes.add(dish);
            }
        }

        if (cleanDishes.size > dishes.size / 2 && leaving == null) {
            dryingDish = cleanDishes.get(rand.nextInt(cleanDishes.size - 1));
            if (dryingDish.timeDrying() > 3000) {
                this.leaving = dryingDish;
            }
        }

        if (leaving != null) {
            gone = leaving.fadeOut(Gdx.graphics.getDeltaTime() * 0.3f);
            if (gone) {
                dishes.removeValue(leaving, true);
                leaving = null;
            }
        }

        if (leaving != null && coming == null) {
            coming = placeDishInSink(0);
            dishes.insert(0, coming);
        }

        if (coming != null) {
            here = coming.fadeIn(Gdx.graphics.getDeltaTime() * 0.3f);
            if (here) {
                coming = null;
            }
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        for (int i = 0; i < allDirt.size; i++) {
            allDirt.get(i).dispose();
        }
        for (int i = 0; i < allDish.size; i++) {
            allDish.get(i).dispose();
        }
        sponge1.getTexture().dispose();
        sponge2.getTexture().dispose();
        background.getTexture().dispose();
        counterFg.getTexture().dispose();
        rackWire.getTexture().dispose();
    }
}
