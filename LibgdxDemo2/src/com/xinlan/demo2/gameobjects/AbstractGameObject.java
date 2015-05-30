package com.xinlan.demo2.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {
	public Vector2 position;
	public Vector2 origin;
	public Vector2 dimension;
	public Vector2 scale;
	public float rotation;

	public Vector2 velocity = new Vector2();// 速度
	public Vector2 terminalVelocity = new Vector2();// 极限速度
	public Vector2 friction = new Vector2();// 摩擦力
	public Vector2 acceleration = new Vector2();// 加速度
	public Rectangle bounds = new Rectangle();

	public AbstractGameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
	}

	public void update(float deltaTime) {// 数据更新'
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
	}

	protected void updateMotionX(float deltaTime) {
		if (velocity.x != 0) {
			if (velocity.x > 0) {
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			} else {
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
			}
		}
		velocity.x += acceleration.x * deltaTime;
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x,
				terminalVelocity.x);
	}

	protected void updateMotionY(float deltaTime) {
		if (velocity.y != 0) {
			if (velocity.y > 0) {
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			} else {
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
			}
		}
		// Apply acceleration
		velocity.y += acceleration.y * deltaTime;
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y,
				terminalVelocity.y);
	}

	public abstract void render(SpriteBatch batch);// 渲染到屏幕
}// end class
