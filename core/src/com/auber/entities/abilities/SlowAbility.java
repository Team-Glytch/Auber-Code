package com.auber.entities.abilities;

import com.auber.entities.Enemy;
import com.auber.entities.Player;
import com.auber.entities.projectile.SlowProjectile;
import com.auber.screens.GameScreen;
import com.badlogic.gdx.math.Vector2;

public class SlowAbility implements SpecialAbility {
	
	/**
	 * The timestamp of when the cooldown of the ability will finish
	 * Measured in milliseconds since the epoch
	 */
	private long cooldown;
	
	/**
	 * The screen the ability is being used on
	 */
	GameScreen gameScreen;

	/**
	 * Creates the SlowAbility
	 * 
	 * @param screen
	 */
	public SlowAbility(GameScreen screen) {
		this.gameScreen = screen;
	}
	
	@Override
	public void doAction(final Enemy enemy, final Player player) {
		if (System.currentTimeMillis() > cooldown) {
			cooldown = System.currentTimeMillis() + 10 * 1000;

			new SlowProjectile(gameScreen, new Vector2(enemy.getX(), enemy.getY()),
					new Vector2(player.getX(), player.getY()));

		}
	}

	@Override
	public float getRange() {
		return 35;
	}
	
}
