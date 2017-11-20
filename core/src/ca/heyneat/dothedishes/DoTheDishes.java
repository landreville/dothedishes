package ca.heyneat.dothedishes;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Random;

public class DoTheDishes extends ApplicationAdapter {
	private static final int RES_WIDTH = 800;
	private static final int RES_HEIGHT = 480;
	private static final int SINK_BOTTOM_Y = 116;
	private static final int SINK_BOTTOM_LEFT_X = 421;
	private static final int SINK_BOTTOM_RIGHT_X = 735;
	private static final int SINK_TOP_RIGHT_X = 710;
	private static final int SINK_TOP_LEFT_X = 456;

	private Random rand;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Vector3 touchPos;

	private Texture dirt1Tomato;
	private Texture dirt2Bandaid;
	private Texture dirt3Dots;
	private Texture dirt4Dots;
	private Texture dirt5Cheese;
	private Texture dirt6Lips;
	private Texture dirt7Smear;
	private Texture dirt8Splat;
	private Texture dirt9Splat;
	private Texture dirt10Blood;
	private Texture dirt11Red;
	private Texture dirt12Burn;

	private Texture plate1;
	private Texture plate2;
	private Texture plate3;
	private Texture plate4;
	private Texture plate5;
	private Texture plate6;

	private Texture grater;
	private Texture pan1;
	private Texture pan2;
	private Texture strainer;
	private Texture tray1;
	private Texture tray2;
	private Texture tray3;

	private Texture utensil1;
	private Texture utensil2;

	private Texture sponge1;
	private Texture sponge2;

	private Texture background;
	private Texture counterFg;
	private Texture rackWire;

	private Array<Rectangle> rackWires;
	private Rectangle sponge;
	private Sprite backgroundSprite;

	private Array<Dish> dishes;

	private Array<Texture> allDishTextures;
	private Array<Texture> allDirtTextures;
	
	@Override
	public void create () {
		rand = new Random();
		allDirtTextures = new Array<Texture>();
		dirt1Tomato = new Texture(Gdx.files.internal("dirt-1-16x32.png"));
		dirt2Bandaid = new Texture(Gdx.files.internal("dirt-2-64x16.png"));
		dirt3Dots = new Texture(Gdx.files.internal("dirt-3-16x32.png"));
		dirt4Dots = new Texture(Gdx.files.internal("dirt-4-16x32.png"));
		dirt5Cheese = new Texture(Gdx.files.internal("dirt-5-8x32.png"));
		dirt6Lips = new Texture(Gdx.files.internal("dirt-6-32x16.png"));
		dirt7Smear = new Texture(Gdx.files.internal("dirt-7-16x16.png"));
		dirt8Splat = new Texture(Gdx.files.internal("dirt-8-32x32.png"));
		dirt9Splat = new Texture(Gdx.files.internal("dirt-9-32x32.png"));
		dirt10Blood = new Texture(Gdx.files.internal("dirt-10-16x16.png"));
		dirt11Red = new Texture(Gdx.files.internal("dirt-11-16x32.png"));
		dirt12Burn = new Texture(Gdx.files.internal("dirt-12-32x32.png"));
		allDirtTextures.add(dirt1Tomato);
		allDirtTextures.add(dirt2Bandaid);
		allDirtTextures.add(dirt3Dots);
		allDirtTextures.add(dirt4Dots);
		allDirtTextures.add(dirt5Cheese);
		allDirtTextures.add(dirt6Lips);
		allDirtTextures.add(dirt7Smear);
		allDirtTextures.add(dirt8Splat);
		allDirtTextures.add(dirt9Splat);
		allDirtTextures.add(dirt10Blood);
		allDirtTextures.add(dirt11Red);
		allDirtTextures.add(dirt12Burn);

		plate1 = new Texture(Gdx.files.internal("plate-1-128x128.png"));
		plate2 = new Texture(Gdx.files.internal("plate-2-128x128.png"));
		plate3 = new Texture(Gdx.files.internal("plate-3-128x128.png"));
		plate4 = new Texture(Gdx.files.internal("plate-4-128x128.png"));
		plate5 = new Texture(Gdx.files.internal("plate-5-96x96.png"));
		plate6 = new Texture(Gdx.files.internal("plate-6-96x96.png"));

		grater = new Texture(Gdx.files.internal("grater-64x92.png"));
		pan1 = new Texture(Gdx.files.internal("pan-1-128x128.png"));
		pan2 = new Texture(Gdx.files.internal("pan-2-128x128.png"));
		strainer = new Texture(Gdx.files.internal("strainer-128x128.png"));
		tray1 = new Texture(Gdx.files.internal("tray-1-256x128.png"));
		tray2 = new Texture(Gdx.files.internal("tray-2-192x96.png"));
		tray3 = new Texture(Gdx.files.internal("tray-3-128x64.png"));
		utensil1 = new Texture(Gdx.files.internal("utensil-1-32x128.png"));
		utensil2 = new Texture(Gdx.files.internal("utensil-2-32x128.png"));

		allDishTextures = new Array<Texture>();
		allDishTextures.add(plate1);
		allDishTextures.add(plate2);
		allDishTextures.add(plate3);
		allDishTextures.add(plate4);
		allDishTextures.add(plate5);
		allDishTextures.add(plate6);
		allDishTextures.add(grater);
		allDishTextures.add(pan1);
		allDishTextures.add(pan2);
		allDishTextures.add(strainer);
		allDishTextures.add(tray1);
		allDishTextures.add(tray2);
		allDishTextures.add(tray3);
		allDishTextures.add(utensil1);
		allDishTextures.add(utensil2);

		sponge1 = new Texture(Gdx.files.internal("sponge-1-64x64.png"));
		sponge2 = new Texture(Gdx.files.internal("sponge-2-64x64.png"));

		background = new Texture(Gdx.files.internal("background-800x480.png"));
		counterFg = new Texture(Gdx.files.internal("counter-fg-321x115.png"));
		rackWire = new Texture(Gdx.files.internal("rack-wire-227x35.png"));

		dishes = new Array<Dish>();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, RES_WIDTH, RES_HEIGHT);

		batch = new SpriteBatch();

		backgroundSprite = new Sprite(background);

		locateRackWires();
		locateInitialDishes();


		touchPos = new Vector3();
	}

	private void locateInitialDishes(){
		int i = rand.nextInt(this.allDishTextures.size);
		Texture texture = this.allDishTextures.get(i);
		Dish dish = new Dish(
				texture,
				SINK_BOTTOM_LEFT_X,
				SINK_BOTTOM_Y - texture.getHeight() / 2
		);
		dishes.add(dish);
	}

	private void locateRackWires() {
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

		renderDryingRack();
		renderDishes();


		batch.draw(counterFg, 416, 0);
		batch.end();
	}

	private void renderDishes(){
		Iterator<Dish> iter = dishes.iterator();
		while(iter.hasNext()){
			Dish dish = iter.next();
			batch.draw(dish.getTexture(), dish.getX(), dish.getY());
		}
	}

	private void renderDryingRack() {
		Iterator<Rectangle> iter = rackWires.iterator();
		while(iter.hasNext()){
			Rectangle rackWireRect = iter.next();
			batch.draw(rackWire, rackWireRect.x, rackWireRect.y);
		}
	}


	@Override
	public void dispose () {
		batch.dispose();
	}
}
