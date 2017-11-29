package ca.heyneat.dothedishes;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class DoTheDishes extends ApplicationAdapter {
    // Background image size (resolution of the game)
    public static final int RES_WIDTH = 800;
    public static final int RES_HEIGHT = 480;
    // Key points on the background image
    public static final int SINK_BOTTOM_LEFT_X = 421;
    public static final int SINK_BOTTOM_RIGHT_X = 737;
    public static final int SINK_BOTTOM_Y = 116;
    public static final int RACK_TOP_LEFT_X = 180;
    public static final int RACK_TOP_RIGHT_X = 394;
    public static final int RACK_BOTTOM_LEFT_X = 140;
    public static final int RACK_BOTTOM_RIGHT_X = 348;
    public static final int RACK_TOP_Y = 200;
    private static final int SINK_TOP_RIGHT_X = 710;
    private static final int SINK_TOP_LEFT_X = 454;
    private static final int SINK_TOP_Y = 205;
    // How many dishes to create when starting the game
    private static final int MAX_DISHES = 12;
    private static final int MIN_DISHES = MAX_DISHES - 5;

    private Random rand;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    // Audio assets
    // Background ambient sink dish washing noises
    private Music backgroundSound;
    // A hand-curated variety of scrubbing sounds
    private Array<Sound> scrubSounds;

    // Sponges sprites
    private Sprite sponge1;
    private Sprite sponge2;
    // Points to the specific sponge to use (either sponge1 or sponge2)
    private Sprite sponge;
    // Text to show the time it took to finish the sinkload
    private BitmapFont finishedTimeFont;

    // Background sprites
    private Sprite background;
    // Part of the background is overlaid to make dishes look like they're in the sink
    private Sprite counterFg;
    // Each wire is separate to allow plates to fall in between them
    private Sprite rackWire;

    // Rack wire positions. Really only the x,y position is used.
    // These could be points instead or drawing the same sprite in multiple locations.
    private Array<Rectangle> rackWires;

    // All dish sprites. From this array is chosen the dishes to use each round.
    private Array<Dish> allDish;
    // All dirt sprites. From this array is chosen dirt to use in the round.
    private Array<Texture> allDirt;

    // Dishes that are in the current round. Chosen from the allDish array.
    private Array<Dish> dishes;
    // Dirt that's in the current. Chosen from the above allDirt array.
    private Array<Dirt> dirts;

    // "draw-er" draws the dishes in the correct order along with the drying rack wires
    // and the dirt on each dish.
    private Drawer drawer;

    // How long it took to finish the round
    private long finishedDuration;
    // Time that the current sinkload started at (used to figure out the duration
    // that the round took.
    private long startAt;
    // Indicates that the dishes are currently being cleared from the drying rack by
    // flying up through the ceiling.
    private boolean clearingDishes;
    // Time that the last dishrack full was cleared.
    // Used to have a pause between the dishes flying away and a new sinkload
    // falling into the sink.
    private long lastCleared;

    @Override
    public void create() {
        rand = new Random();
        dirts = new Array<Dirt>();
        dishes = new Array<Dish>();

        loadSounds();
        loadDirt();
        loadDishes();
        loadBackground();

        initializeDishRack();
        initializeDishes();
        drawer = new Drawer(dishes, rackWires, rackWire);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, RES_WIDTH, RES_HEIGHT);
        batch = new SpriteBatch();

        InputMultiplexer inputProcessor = new InputMultiplexer();
        // The sponge gets a change to capture input events first
        inputProcessor.addProcessor(new SpongeInputProcessor(camera, sponge, dirts, scrubSounds));
        // Then the dishes
        inputProcessor.addProcessor(new DishInputProcessor(camera, drawer));

        Gdx.input.setInputProcessor(inputProcessor);
        Gdx.app.setLogLevel(Application.LOG_NONE);
    }

    /**
     * Instantiate music and sound effects.
     */
    private void loadSounds() {
        backgroundSound = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
        scrubSounds = new Array<Sound>();
        scrubSounds.add(
                Gdx.audio.newSound(Gdx.files.internal("audio/scrub1.mp3"))
        );
        scrubSounds.add(
                Gdx.audio.newSound(Gdx.files.internal("audio/scrub3.mp3"))
        );
        scrubSounds.add(
                Gdx.audio.newSound(Gdx.files.internal("audio/scrub4.mp3"))
        );
        backgroundSound.play();
        backgroundSound.setLooping(true);
    }

    /**
     * Instantiate sprites for the background scene.
     */
    private void loadBackground() {
        sponge1 = new Sprite(new Texture(Gdx.files.internal("sponge-1-64x64.png")));
        sponge2 = new Sprite(new Texture(Gdx.files.internal("sponge-2-64x64.png")));
        // Randomly choose one of the sponges
        sponge = rand.nextBoolean() ? sponge1 : sponge2;
        sponge.setX(735);
        sponge.setY(140);

        background = new Sprite(new Texture(Gdx.files.internal("background-800x480.png")));
        counterFg = new Sprite(new Texture(Gdx.files.internal("counter-fg-321x115.png")));
        rackWire = new Sprite(new Texture(Gdx.files.internal("rack-wire-227x35.png")));

        this.finishedTimeFont = new BitmapFont();
        this.finishedTimeFont.setColor(new Color(0.75f, 0.75f, 0.75f, 1f));
    }

    /**
     * Instantiate dish sprites
     */
    private void loadDishes() {
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
                new Rectangle(15, 15, 60, 60)
        ));
        allDish.add(new Dish(
                new Sprite(new Texture(Gdx.files.internal("plate-6-96x96.png"))),
                new Rectangle(15, 15, 60, 60)
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
    }

    /**
     * Instantiate dirt sprites
     */
    private void loadDirt() {
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
    }

    /**
     * Choose the dishes for this round and their positions.
     */
    private void initializeDishes() {
        this.startAt = 0;
        // Random number of dishes within MAX_DISHES to MIN_DISHES
        int dishCount = rand.nextInt((MAX_DISHES - MIN_DISHES) + 1) + MIN_DISHES;

        float deltaY = 0;

        for (int i = 0; i < dishCount; i++) {
            // Place each succeeding dish in a slightly lower y position
            deltaY += rand.nextInt((SINK_TOP_Y - SINK_BOTTOM_Y) / dishCount);
            dishes.add(placeDishInSink(deltaY));
        }
    }

    /**
     * Choose a random dish, a random x position, and add some random dirt to it.
     * @param deltaY How far from the top of the sink to place this dish.
     * @return The newly added Dish instance.
     */
    private Dish placeDishInSink(float deltaY) {
        float width;
        float height;
        float y;
        int max;
        int min;
        float x;
        // Get the slope of the sink perspective to ensure dishes are placed within the sink
        float m = SINK_TOP_Y / (SINK_TOP_LEFT_X - SINK_BOTTOM_LEFT_X);
        // Get a random dish out of all the dish sprites
        Dish aDish = this.allDish.get(
                rand.nextInt(this.allDish.size)
        );

        width = aDish.getWidth();
        height = aDish.getHeight();

        // Set the y placement
        y = SINK_TOP_Y - height - deltaY;

        // If the y placement is too low, place it at the bottom of the sink
        if (y < SINK_BOTTOM_Y - height / 2) {
            y = SINK_BOTTOM_Y - height / 2;
        }

        // Ensure the x placement is within the sink based on the
        // side of the sink (determined using the slope m and y-position)
        // and the width of the plate.
        max = (int) (SINK_BOTTOM_RIGHT_X - y / m - width);
        min = (int) (SINK_BOTTOM_LEFT_X + y / m);
        if (min < SINK_BOTTOM_LEFT_X) {
            min = SINK_BOTTOM_LEFT_X;
        }
        if (max > SINK_BOTTOM_RIGHT_X) {
            max = SINK_BOTTOM_RIGHT_X;
        }

        x = rand.nextInt((max - min) + 1) + min;

        // Copy the dish sprite so we can place it independently.
        // The proper method may be to draw the existing sprite instance
        // in the appropriate locations instead of instantiating multiple sprites.
        // At least the sprite copy uses the same texture instance.
        Dish dish = aDish.copy();
        dish.setX(x);
        // Start dropping the dish from above the top of the frame
        dish.startDropIn((int) y);
        // Add some random dirt to the dish
        for (int j = 0; j <= rand.nextInt(5); j++) {
            dirts.add(dish.addDirt(allDirt.get(rand.nextInt(allDirt.size))));
        }

        return dish;
    }

    /**
     * Place the dish rack wires.
     */
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
        // Always clear your gl
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        newLevel();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        background.draw(batch);

        dropInDishes();
        drawer.drawInOrder(batch);

        batch.draw(counterFg, 416, 0);
        drawFinishedDuration(batch);
        sponge.draw(batch);
        batch.end();
    }

    /**
     * If all the dishes are clean and in the drying rack, then start a new round.
     * This needs to be called over multiple frames because starting a new round
     * involves moving the dishes up and out of the screen and then dropping in
     * a new set of dishes from the sky.
     */
    private void newLevel() {
        // Wait until all dishes are drying for at least one second.
        // There's a lot of loops over the round's dishes and these should be
        // reduced by emitting a signal when each dish is put into the drying rack (or
        // just the last dish) instead of looping through the dishes every frame.
        for (Dish dish : dishes) {
            if (!dish.isDrying() || dish.timeDrying() < 1000) {
                return;
            }
        }

        // Only set the finish time the first time we find that all dishes are drying and clean
        if (!this.clearingDishes) {
            // Subtract 1 second to account for wait time above.
            this.finishedDuration = TimeUtils.timeSinceMillis(this.startAt) - 1000;
        }

        // Start clearing the dishes by moving them up and out of the screen.
        boolean done = clearDishes();

        if (done) {
            if (this.lastCleared == 0) {
                // Set the time that he dishes are cleared, so we can wait one second below
                this.lastCleared = TimeUtils.millis();
            }

            // Pause for one second after clearing before bringing in new dishes
            if (TimeUtils.timeSinceMillis(this.lastCleared) > 1000) {
                this.lastCleared = 0;
                // Create a new batch of dishes and place them randomly in the sink.
                initializeDishes();
                this.clearingDishes = false;
            }
        }
    }

    /**
     * Move the dishes in the drying rack up and out of the screen.
     *
     * @return True if the dishes are off the screen now.
     */
    private boolean clearDishes() {
        this.clearingDishes = true;
        boolean done = true;

        for (Dish dish : dishes) {
            if (dish.getY() < DoTheDishes.RES_HEIGHT) {
                // This one isn't off the screen yet
                done = false;
            }
            // Move it on up.
            dish.flyOut((int) (Gdx.graphics.getDeltaTime() * 1000));
        }
        if (done) {
            // Clear out the array of the last rounds dishes.
            dishes.clear();
        }
        return done;
    }

    /**
     * Move the new batch of dishes down from the sky.
     */
    private void dropInDishes() {
        if (this.startAt != 0) {
            // Dishes have already been dropped.
            return;
        }

        boolean done = true;
        // I love looping through this array of dishes.
        for (Dish dish : dishes) {
            if (dish.isDroppingIn()) {
                // Move the dish down some more.
                dish.dropIn((int) (Gdx.graphics.getDeltaTime() * 1000));
                done = false;
            }
        }

        if (done) {
            // Start time is when all the dishes have dropped into the sink.
            this.startAt = TimeUtils.millis();
        }
    }

    /**
     * Render the round duration
     * @param batch
     */
    private void drawFinishedDuration(SpriteBatch batch) {
        if (this.finishedDuration == 0) {
            // Haven't completed a round yet.
            return;
        }
        int minutes = (int) Math.floor(this.finishedDuration / 1000 / 60);
        int seconds = Math.round(this.finishedDuration / 1000 % 60);
        this.finishedTimeFont.draw(batch, String.format("%d:%02d", minutes, seconds), 698, 368);
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
        backgroundSound.dispose();
        for (Sound sound : scrubSounds) {
            sound.dispose();
        }
    }
}
