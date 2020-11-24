package com.auber.entities;

import com.auber.game.AuberGame;
import com.auber.gameplay.WorldContactListener;
import com.auber.rendering.Renderable;
import com.auber.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player implements Renderable {

	/**
	 * The screen the player is on
	 */
	private GameScreen gameScreen;
	
	/**
	 * The box defining the player's position, and bounding box
	 */
	private Body box2dBody;

	/**
	 * The relative speed of the player, default is 1.0f
	 */
	private float speed;

	/**
	 * The health of the player, max is 10.0f
	 */
	private float health;
	
	/**
	 * Controls the direction which the player is facing
	 */
	private boolean movingRight;
	
	/**
	 * @param gameScreen.getWorld() The gameScreen.getWorld() the player is in
	 */
	public Player(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		this.box2dBody = definePlayer(gameScreen.getInteractables().getStartLocations().get(0).getWorldPosition());
		this.speed = 1.0f;
		this.health = 10f;
		this.movingRight = true;
		
		
	}

	/**
	 * Sets the health of the player
	 * 
	 * @param health
	 */
	public void setHealth(float health) {
		this.health = health;
	}
	
	/**
	 * @return {@link #health}
	 */
	public float getHealth() {
		return health;
	}
	
	/**
	 * Sets the relative speed of the player
	 * 
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * @return {@link #speed}
	 */
	public float getSpeed() {
		return speed;
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
		shape.setRadius(6 / AuberGame.PixelsPerMetre);
		fixtureDefinition.shape = shape;
		fixtureDefinition.filter.categoryBits = WorldContactListener.PLAYER_BIT;
		fixtureDefinition.filter.maskBits = WorldContactListener.DEFAULT_BIT | WorldContactListener.ENEMY_BIT
				| WorldContactListener.TILE_BIT | WorldContactListener.PROJECTILE_BIT;
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

		float movementSpeed = this.speed * 0.15f;

		// Whether the player should move up
		if (moveUp && this.box2dBody.getLinearVelocity().y < 0.7f && !moveDown) {
			this.box2dBody.applyLinearImpulse(new Vector2(0f, movementSpeed), this.box2dBody.getWorldCenter(), true);

			// Whether the player should move down
		} else if (moveDown && this.box2dBody.getLinearVelocity().y > -0.7f && !moveUp) {
			this.box2dBody.applyLinearImpulse(new Vector2(0f, -movementSpeed), this.box2dBody.getWorldCenter(), true);

			// Whether the player's up and down motions cancel each other out
		} else if (moveUp == moveDown) {
			this.box2dBody.setLinearVelocity(this.box2dBody.getLinearVelocity().x, 0f);
		}

		// Whether the player should move right
		if (moveRight && this.box2dBody.getLinearVelocity().x < 0.7f && !moveLeft) {
			this.box2dBody.applyLinearImpulse(new Vector2(movementSpeed, 0f), this.box2dBody.getWorldCenter(), true);
			movingRight = true;

			// Whether the player should move left
		} else if (moveLeft && this.box2dBody.getLinearVelocity().x > -0.7f && !moveRight) {
			this.box2dBody.applyLinearImpulse(new Vector2(-movementSpeed, 0f), this.box2dBody.getWorldCenter(), true);
			movingRight = false;

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
		return (30f / AuberGame.PixelsPerMetre);
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
		return movingRight;
	}

	public void teleport(final float x, final float y) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (gameScreen.getWorld().isLocked()) {
				}
				box2dBody.setTransform(x, y, 0);
			}
		}).start();
	}

	@Override
	public boolean isVisible() {
		return true;
	}
}