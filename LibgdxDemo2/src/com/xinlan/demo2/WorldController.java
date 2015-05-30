package com.xinlan.demo2;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.xinlan.demo2.asset.Assets;
import com.xinlan.demo2.game.Level;
import com.xinlan.demo2.gameobjects.BunnyHead;
import com.xinlan.demo2.gameobjects.BunnyHead.JUMP_STATE;
import com.xinlan.demo2.gameobjects.Feather;
import com.xinlan.demo2.gameobjects.GoldCoin;
import com.xinlan.demo2.gameobjects.Rock;

public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();

	public Sprite[] testSprites;
	public int selectedSprite;

	public CameraHelper cameraHelper;

	public Level level;
	public int lives;
	public int score;

	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	private float timeLeftGameOverDelay;

	public WorldController() {
		init();
	}

	public void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		// initTestObjects();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		lives = Constants.LIVES_START;
		initLevel();
	}

	private void initLevel() {
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
	}

	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		// updateTestObjects(deltaTime);
		handleInputGame(deltaTime);
		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0)
				init();
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		} else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null
					: level.bunnyHead);
			Gdx.app.debug(TAG,
					"Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}

	private void handleInputGame(float deltaTime) {
		if (cameraHelper.hasTarget(level.bunnyHead)) {
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			} else {
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}
			}
			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
				level.bunnyHead.setJumping(true);
			else
				level.bunnyHead.setJumping(false);
		}
	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;
		// Selected Sprite Controls
		// float sprMoveSpeed = 5 * deltaTime;
		// if (Gdx.input.isKeyPressed(Keys.A))
		// moveSelectedSprite(-sprMoveSpeed, 0);
		// if (Gdx.input.isKeyPressed(Keys.D))
		// moveSelectedSprite(sprMoveSpeed, 0);
		// if (Gdx.input.isKeyPressed(Keys.W))
		// moveSelectedSprite(0, sprMoveSpeed);
		// if (Gdx.input.isKeyPressed(Keys.S))
		// moveSelectedSprite(0, -sprMoveSpeed);

		// Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP))
			moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
			cameraHelper.setPosition(0, 0);

		// ÉãÏñ»ú¿ØÖÆ
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	private void moveSelectedSprite(float x, float y) {
		testSprites[selectedSprite].translate(x, y);
	}

	private void updateTestObjects(float deltaTime) {
		float rotation = testSprites[selectedSprite].getRotation();
		rotation += 90 * deltaTime;
		rotation %= 360;
		testSprites[selectedSprite].setRotation(rotation);
	}

	private void initTestObjects() {
		testSprites = new Sprite[5];
		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(Assets.instance.bunny.head);
		regions.add(Assets.instance.feather.feather);
		regions.add(Assets.instance.goldCoin.goldCoin);

		for (int i = 0; i < testSprites.length; i++) {
			Sprite spr = new Sprite(regions.random());
			spr.setSize(1, 1);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			float randomX = MathUtils.random(-2.0f, 2.0f);
			float randomY = MathUtils.random(-2.0f, 2.0f);
			spr.setPosition(randomX, randomY);
			testSprites[i] = spr;
		}// end for i

		selectedSprite = 0;
	}

	private void initTestObjects2() {
		testSprites = new Sprite[5];
		int width = 32;
		int height = 32;

		Pixmap pixmap = createProceduralPixmap(width, height);
		Texture texture = new Texture(pixmap);
		for (int i = 0, len = testSprites.length; i < len; i++) {
			Sprite spr = new Sprite(texture);
			spr.setSize(1, 1);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			float randomX = MathUtils.random(-2.0f, 2.0f);
			float randomY = MathUtils.random(-2.0f, 2.0f);
			spr.setPosition(randomX, randomY);
			testSprites[i] = spr;
		}// end for i

		selectedSprite = 0;
	}

	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	private void testCollisions() {
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
				level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		// Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width,
					rock.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}
		// Test collision: Bunny Head <-> Gold Coins
		for (GoldCoin goldcoin : level.goldcoins) {
			if (goldcoin.collected)
				continue;
			r2.set(goldcoin.position.x, goldcoin.position.y,
					goldcoin.bounds.width, goldcoin.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}
		// Test collision: Bunny Head <-> Feathers
		for (Feather feather : level.feathers) {
			if (feather.collected)
				continue;
			r2.set(feather.position.x, feather.position.y,
					feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}
	}

	private void onCollisionBunnyHeadWithRock(Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y
				- (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitLeftEdge) {
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}
		switch (bunnyHead.jumpState) {
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height
					+ bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height
					+ bunnyHead.origin.y;
			break;
		}
	}

	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {
		goldcoin.collected = true;
		score += goldcoin.getScore();
	}

	private void onCollisionBunnyWithFeather(Feather feather) {
		feather.collected = true;
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
	}

	public boolean isGameOver() {
		return lives < 0;
	}

	public boolean isPlayerInWater() {
		return level.bunnyHead.position.y < -5;
	}
}// end class
