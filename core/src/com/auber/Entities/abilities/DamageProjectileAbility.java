package com.auber.Entities.abilities;

import com.auber.Entities.Enemy;
import com.auber.Entities.Player;
import com.auber.Entities.projectile.DamageProjectile;
import com.auber.gameplay.GameScreen;
import com.badlogic.gdx.math.Vector2;

public class DamageProjectileAbility implements SpecialAbility {

	/**
	 * The timestamp of when the cooldown of the ability will finish
	 * Measured in milliseconds since the epoch
	 */
	long cooldown = 0;

	/**
	 * The screen the ability is being used on
	 */
	GameScreen gameScreen;

	/**
	 * Creates the DamageProjectileAbility
	 * 
	 * @param screen
	 */
	public DamageProjectileAbility(GameScreen screen) {
		this.gameScreen = screen;
	}

	@Override
	public void doAction(Enemy enemy, Player player) {
		if (System.currentTimeMillis() > cooldown) {
			new DamageProjectile(gameScreen, new Vector2(enemy.getX(), enemy.getY()),
					new Vector2(player.getX(), player.getY()));
			cooldown = (long) (System.currentTimeMillis() + 3 * 1000);
		}
	}

	@Override
	public float getRange() {
		return 30;
	}

}
