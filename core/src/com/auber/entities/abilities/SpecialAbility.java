package com.auber.Entities.abilities;

import com.auber.Entities.Enemy;
import com.auber.Entities.Player;

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
