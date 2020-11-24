package com.auber.Entities.projectile;

import com.auber.Entities.Player;
import com.badlogic.gdx.math.Vector2;

public interface Projectile {

	/**
	 * Handles the event where a projectile collides with a player
	 * 
	 * @param player
	 */
	public void collide(Player player);

	/**
	 * Fires the projectile towards the target specified
	 * 
	 * @param target
	 */
	public void fire(Vector2 target);
	
}