package com.xinlan.demo2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.xinlan.demo2.gameobjects.AbstractGameObject;
import com.xinlan.demo2.gameobjects.BunnyHead;
import com.xinlan.demo2.gameobjects.Clouds;
import com.xinlan.demo2.gameobjects.Feather;
import com.xinlan.demo2.gameobjects.GoldCoin;
import com.xinlan.demo2.gameobjects.Mountains;
import com.xinlan.demo2.gameobjects.Rock;
import com.xinlan.demo2.gameobjects.WaterOverlay;

public class Level {
	public static final String TAG = Level.class.getName();

	public Array<Rock> rocks;
	// decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;

	public BunnyHead bunnyHead;
	public Array<Feather> feathers = new Array<Feather>();
	public Array<GoldCoin> goldcoins = new Array<GoldCoin>();

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		rocks = new Array<Rock>();
		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// find matching color value to identify block type at (x,y)
				// point and create the corresponding game object if there is
				// a match
				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// do nothing
				} else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) { // rock
					if (lastPixel != currentPixel) {
						obj = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								* heightIncreaseFactor + offsetHeight);
						rocks.add((Rock) obj);
					} else {
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				} else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) { // player
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					bunnyHead = (BunnyHead) obj;
				} else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) { // feather
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					feathers.add((Feather) obj);
				} else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) { // gold
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y
							+ offsetHeight);
					goldcoins.add((GoldCoin) obj);
				} else {
				}
				lastPixel = currentPixel;
			}
		}
		// decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);
		// free memory
		pixmap.dispose();
	}

	public void update(float deltaTime) {
		bunnyHead.update(deltaTime);
		for (Rock rock : rocks)
			rock.update(deltaTime);
		for (GoldCoin goldCoin : goldcoins)
			goldCoin.update(deltaTime);
		for (Feather feather : feathers)
			feather.update(deltaTime);
		clouds.update(deltaTime);
	}

	public void render(SpriteBatch batch) {
		mountains.render(batch);
		for (Rock rock : rocks)
			rock.render(batch);
		for (GoldCoin goldCoin : goldcoins)
			goldCoin.render(batch);
		 for (Feather feather : feathers)
		        feather.render(batch);
		 bunnyHead.render(batch);
		waterOverlay.render(batch);
		clouds.render(batch);
	}

	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		ROCK(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		ITEM_FEATHER(255, 0, 255), // purple
		ITEM_GOLD_COIN(255, 255, 0); // yellow
		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}
}// end class
