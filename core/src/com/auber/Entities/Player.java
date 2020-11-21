package com.auber.entities;

import com.auber.game.AuberGame;
import com.auber.rendering.Renderable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player implements Renderable {

	/**
	 * The world the player is in
	 */
	private World world;
	/**
	 * The box defining the player's position, and bounding box
	 */
	public Body box2dBody;

	/**
	 * @param world The world the player is in
	 */
	public Player(World world) {
		this.world = world;
		this.box2dBody = definePlayer();
	}

	/**
	 * @return The box defining the shape and position of the player
	 */
	private Body definePlayer() {
		// Position and Type
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.position.set(780 / AuberGame.PixelsPerMetre, 1250 / AuberGame.PixelsPerMetre);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;

		// Collision
		Body box2dBody = world.createBody(bodyDefinition);
		FixtureDef fixtureDefinition = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5 / AuberGame.PixelsPerMetre);

		fixtureDefinition.shape = shape;

		box2dBody.createFixture(fixtureDefinition);

		return box2dBody;
	}

	/**
	 * @return True if the player is moving, False otherwise
	 */
	private boolean isMoving() {
		return box2dBody.getLinearVelocity().x != 0 || box2dBody.getLinearVelocity().y != 0;
	}

	/**
	 * Key mapping and speed changes
	 */
	private void handleKeys() {
		boolean moveUp = (Gdx.input.isKeyPressed(Input.Keys.UP));
		boolean moveDown = (Gdx.input.isKeyPressed(Input.Keys.DOWN));
		boolean moveRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT));
		boolean moveLeft = (Gdx.input.isKeyPressed(Input.Keys.LEFT));

		// Whether the player should move up
		if (moveUp && this.box2dBody.getLinearVelocity().y < 0.7f && !moveDown) {
			this.box2dBody.applyLinearImpulse(new Vector2(0f, 0.15f), this.box2dBody.getWorldCenter(), true);
			
		// Whether the player should move down
		} else if (moveDown && this.box2dBody.getLinearVelocity().y > -0.7f && !moveUp) {
			this.box2dBody.applyLinearImpulse(new Vector2(0f, -0.15f), this.box2dBody.getWorldCenter(), true);
			
		// Whether the player's up and down motions cancel each other out
		} else if (moveUp == moveDown) {
			this.box2dBody.setLinearVelocity(this.box2dBody.getLinearVelocity().x, 0f);
		}

		// Whether the player should move right
		if (moveRight && this.box2dBody.getLinearVelocity().x < 0.7f && !moveLeft) {
			this.box2dBody.applyLinearImpulse(new Vector2(0.15f, 0f), this.box2dBody.getWorldCenter(), true);
			
		// Whether the player should move left
		} else if (moveLeft && this.box2dBody.getLinearVelocity().x > -0.7f && !moveRight) {
			this.box2dBody.applyLinearImpulse(new Vector2(-0.15f, 0f), this.box2dBody.getWorldCenter(), true);
			
		// Whether the player's left and right motions cancel each other out
		} else if (moveRight == moveLeft) {
			this.box2dBody.setLinearVelocity(0f, this.box2dBody.getLinearVelocity().y);
		}
	}

	@Override
	public void update(float deltaTime) {
		handleKeys();
	}

	@Override
	public float getX() {
		return box2dBody.getPosition().x;
	}

	@Override
	public float getY() {
		return box2dBody.getPosition().y;
	}

	@Override
	public float getWidth() {
		return (20f / AuberGame.PixelsPerMetre);
	}

	@Override
	public float getHeight() {
		return (20f / AuberGame.PixelsPerMetre);
	}

	@Override
	public String getTextureName() {
		if (isMoving()) {
			return "PlayerRun";
		} else {
			return "PlayerIdle";
		}

	}

	@Override
	public boolean isMovingRight() {
		return !isMoving() || box2dBody.getLinearVelocity().x >= 0;
	}
}
