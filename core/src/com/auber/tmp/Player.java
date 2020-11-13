package com.auber.tmp;

import com.auber.rendering.Renderable;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player implements Renderable {

	/**
	 * The world the player is in
	 */
	private World world;
	/**
	 * The box defining the player's shape and position
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
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.position.set(100, 100);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;
		
		Body box2dBody = world.createBody(bodyDefinition);
		
		return box2dBody;
	}
	
	/**
	 * @return True if the player is moving, False otherwise
	 */
	private boolean isMoving() {
		return box2dBody.getLinearVelocity().x != 0 || box2dBody.getLinearVelocity().y != 0;
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
	public int getWidth() {
		return 32;
	}

	@Override
	public int getHeight() {
		return 32;
	}

	@Override
	public String getTextureName() {
		if (isMoving()) {
			return "PlayerRun";
		} else {
			return "PlayerIdle";
		}
	
	}

}