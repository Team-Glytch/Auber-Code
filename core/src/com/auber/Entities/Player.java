package com.auber.entities;

import com.auber.game.AuberGame;
import com.auber.gameplay.GameScreen;
import com.auber.gameplay.WorldContactListener;
import com.auber.rendering.Renderable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player implements Renderable {

	private GameScreen gameScreen;
	/**
	 * The box defining the player's position, and bounding box
	 */
	private Body box2dBody;

	private float speed;

	/**
	 * @param gameScreen.getWorld() The gameScreen.getWorld() the player is in
	 */
	public Player(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		this.box2dBody = definePlayer(gameScreen.getInteractables().getStartLocations().get(0).getWorldPosition());
	}

	/**
	 * @return The box defining the shape and position of the player
	 */
	private Body definePlayer(Vector2 loc) {

		// Position and Type
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.position.set(loc.x, loc.y);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;
		Body box2dBody = gameScreen.getWorld().createBody(bodyDefinition);
		box2dBody.setUserData(this);

		// Collision
		FixtureDef fixtureDefinition = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / AuberGame.PixelsPerMetre);
		fixtureDefinition.shape = shape;
		fixtureDefinition.filter.categoryBits = WorldContactListener.PLAYER_BIT;
		fixtureDefinition.filter.maskBits = WorldContactListener.ENEMY_BIT | WorldContactListener.TELEPORTER_BIT;
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

	public void teleport(final float x, final float y) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(gameScreen.getWorld().isLocked()) {
				}
				box2dBody.setTransform(x, y, 0);
			}
		}).start();
	}
}