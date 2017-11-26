package ca.heyneat.dothedishes;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Random;

public class DoTheDishes extends ApplicationAdapter {
    public static final int SINK_BOTTOM_LEFT_X = 421;
    public static final int SINK_BOTTOM_RIGHT_X = 737;
    public static final int SINK_BOTTOM_Y = 116;
	private static final int RES_WIDTH = 800;
	private static final int RES_HEIGHT = 480;
	private static final int SINK_TOP_RIGHT_X = 710;
	private static final int SINK_TOP_LEFT_X = 454;
	private static final int SINK_TOP_Y = 205;

	private Random rand;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Vector3 touchPos;

	private Sprite dirt1Tomato;
	private Sprite dirt2Bandaid;
	private Sprite dirt3Dots;
	private Sprite dirt4Dots;
	private Sprite dirt5Cheese;
	private Sprite dirt6Lips;
	private Sprite dirt7Smear;
	private Sprite dirt8Splat;
	private Sprite dirt9Splat;
	private Sprite dirt10Blood;
	private Sprite dirt11Red;
	private Sprite dirt12Burn;

	private Sprite plate1;
	private Sprite plate2;
	private Sprite plate3;
	private Sprite plate4;
	private Sprite plate5;
	private Sprite plate6;

	private Sprite grater;
	private Sprite pan1;
	private Sprite pan2;
	private Sprite strainer;
	private Sprite tray1;
	private Sprite tray2;
	private Sprite tray3;

	private Sprite utensil1;
	private Sprite utensil2;

	private Sprite sponge1;
	private Sprite sponge2;

	private Sprite background;
	private Sprite counterFg;
	private Sprite rackWire;

	private Array<Rectangle> rackWires;
	private Sprite backgroundSprite;

	private Array<Dish> dishes;

	private Array<Dish> allDish;
	private Array<Sprite> allDirt;
	
	@Override
	public void create () {
		rand = new Random();
		allDirt = new Array<Sprite>();
		dirt1Tomato = new Sprite(new Texture(Gdx.files.internal("dirt-1-16x32.png")));
		dirt2Bandaid = new Sprite(new Texture(Gdx.files.internal("dirt-2-64x16.png")));
		dirt3Dots = new Sprite(new Texture(Gdx.files.internal("dirt-3-16x32.png")));
		dirt4Dots = new Sprite(new Texture(Gdx.files.internal("dirt-4-16x32.png")));
		dirt5Cheese = new Sprite(new Texture(Gdx.files.internal("dirt-5-8x32.png")));
		dirt6Lips = new Sprite(new Texture(Gdx.files.internal("dirt-6-32x16.png")));
		dirt7Smear = new Sprite(new Texture(Gdx.files.internal("dirt-7-16x16.png")));
		dirt8Splat = new Sprite(new Texture(Gdx.files.internal("dirt-8-32x32.png")));
		dirt9Splat = new Sprite(new Texture(Gdx.files.internal("dirt-9-32x32.png")));
		dirt10Blood = new Sprite(new Texture(Gdx.files.internal("dirt-10-16x16.png")));
		dirt11Red = new Sprite(new Texture(Gdx.files.internal("dirt-11-16x32.png")));
		dirt12Burn = new Sprite(new Texture(Gdx.files.internal("dirt-12-32x32.png")));
		allDirt.add(dirt1Tomato);
		allDirt.add(dirt2Bandaid);
		allDirt.add(dirt3Dots);
		allDirt.add(dirt4Dots);
		allDirt.add(dirt5Cheese);
		allDirt.add(dirt6Lips);
		allDirt.add(dirt7Smear);
		allDirt.add(dirt8Splat);
		allDirt.add(dirt9Splat);
		allDirt.add(dirt10Blood);
		allDirt.add(dirt11Red);
		allDirt.add(dirt12Burn);

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

		touchPos = new Vector3();
		Gdx.input.setInputProcessor(new DishInputProcessor(camera, dishes));
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	private void initializeDishes(){
		float width;
		float height;
		float x;
		float y;
		float m = SINK_TOP_Y / (SINK_TOP_LEFT_X - SINK_BOTTOM_LEFT_X);
		int min;
		int max;
		int dishCount = 9;

		float deltaY = 0;

		for(int i=0; i<dishCount; i++) {
			Dish aDish = this.allDish.get(
				rand.nextInt(this.allDish.size)
			);

			width = aDish.getWidth();
			height = aDish.getHeight();

			y = SINK_TOP_Y - height - deltaY;
			deltaY += rand.nextInt((SINK_TOP_Y - SINK_BOTTOM_Y) / dishCount);
            if(y < SINK_BOTTOM_Y - height/2){
                y = SINK_BOTTOM_Y - height/2;
            }

			max = (int)(SINK_BOTTOM_RIGHT_X - y/m - width);
			min = (int)(SINK_BOTTOM_LEFT_X + y/m);
			if(min < SINK_BOTTOM_LEFT_X){
			    min = SINK_BOTTOM_LEFT_X;
            }
            if(max > SINK_BOTTOM_RIGHT_X){
			    max = SINK_BOTTOM_RIGHT_X;
            }

			x = rand.nextInt((max - min) + 1) + min;



			Dish dish = aDish.copy();
			dish.setX(x);
			dish.setY(y);
			for(int j=0; j<=rand.nextInt(5); j++) {
                dish.addDirt(allDirt.get(rand.nextInt(allDirt.size)));
            }
			dishes.add(dish);
		}
	}

	private void initializeDishRack() {
		rackWires = new Array<Rectangle>();
		int x = 139;
		int y = 122;
		for(int i=0; i<8; i++){

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
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		backgroundSprite.draw(batch);

		renderDishesAndRack(batch);


		batch.draw(counterFg, 416, 0);
		batch.end();
	}

	private void renderDishesAndRack(SpriteBatch batch){
        int i = 0;
        int dishesPerWire = dishes.size/rackWires.size;
        if(dishesPerWire == 0){
            dishesPerWire = 1;
        }
        Iterator<Rectangle> rackIter = rackWires.iterator();
        Iterator<Dish> dishIter = dishes.iterator();

        if(rackIter.hasNext()){
            Rectangle rackWireRect = rackIter.next();
            batch.draw(rackWire, rackWireRect.x, rackWireRect.y);
        }

		while(dishIter.hasNext()){
            // Interweave dishes and rack wires
            if(i % dishesPerWire == 0 && rackIter.hasNext()){
                Rectangle rackWireRect = rackIter.next();
                batch.draw(rackWire, rackWireRect.x, rackWireRect.y);
            }

			Dish dish = dishIter.next();
            dish.draw(batch);

            i++;
		}

		while(rackIter.hasNext()){
		    Rectangle rackWireRect = rackIter.next();
            batch.draw(rackWire, rackWireRect.x, rackWireRect.y);
        }
	}


	@Override
	public void dispose () {
		batch.dispose();
	}
}
