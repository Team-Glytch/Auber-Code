package com.auber.Entities.projectile;

import com.auber.Entities.Player;
import com.auber.game.AuberGame;
import com.auber.gameplay.GameScreen;
import com.auber.gameplay.WorldContactListener;
import com.auber.rendering.Renderable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class DamageProjectile implements Renderable, Projectile {
	
	/**
	 * The screen the projectile is fired on
	 */
	private GameScreen gameScreen;
	/**
	 * The location and bounding box of the projectile
	 */
	private Body box2dBody;

	/**
	 * Creates the projectile
	 * 
	 * @param screen
	 * @param pos The position of the projectile
	 * @param target Where the projectile is fired at
	 */
	public DamageProjectile(GameScreen screen, Vector2 pos, Vector2 target) {
		this.gameScreen = screen;
		defineEnemy(pos);
		fire(target);
		screen.addRenderable(this);
	}

	/**
	 * Defines the position and bounding box of the projectile
	 * 
	 * @param loc
	 */
	public void defineEnemy(Vector2 loc) {
		// Position and type
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.position.set(loc.x, loc.y);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;
		box2dBody = gameScreen.getWorld().createBody(bodyDefinition);

		// Collisions
		FixtureDef fixtureDefinition = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / AuberGame.PixelsPerMetre);
		fixtureDefinition.shape = shape;
		// Prevents enemies from colliding with each other
		fixtureDefinition.filter.categoryBits = WorldContactListener.PROJECTILE_BIT;
		fixtureDefinition.filter.maskBits = WorldContactListener.DEFAULT_BIT | WorldContactListener.PLAYER_BIT;
		box2dBody.createFixture(fixtureDefinition);

		box2dBody.setUserData(this);
	}

	@Override
	public void collide(final Player player) {
		float dmg = 2.5f;

		player.setHealth(player.getHealth() - dmg);

	}
	
	@Override
	public void fire(Vector2 target) {
		Vector2 dif = target.cpy().sub(box2dBody.getPosition());
		float magnitude = dif.len();
		float unitX = dif.x / magnitude;
		float unitY = dif.y / magnitude;

		float speed = 0.8f;

		Vector2 vel = new Vector2(unitX * speed, unitY * speed);

		this.box2dBody.setLinearVelocity(vel);

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
		return 10 / AuberGame.PixelsPerMetre;
	}

	@Override
	public float getHeight() {
		return 10 / AuberGame.PixelsPerMetre;
	}

	@Override
	public String getTextureName() {
		return "DamageProjectile";
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public boolean isMovingRight() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return true;
	}
}
