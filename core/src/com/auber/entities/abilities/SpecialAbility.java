package com.auber.entities.abilities;

import com.auber.entities.Enemy;
import com.auber.entities.Player;

public interface SpecialAbility {

	/**
	 * Performs the special ability
	 * 
	 * @param enemy
	 * @param player
	 */
	public void doAction(Enemy enemy, Player player);
	
	/**
	 * @return How close a player can get to the enemy before they will use the ability
	 */
	public float getRange();

}
