package com.auber.entities.abilities;

import com.auber.entities.Enemy;
import com.auber.entities.Player;
import com.auber.entities.projectile.DamageProjectile;
import com.auber.gameplay.GameScreen;
import com.badlogic.gdx.math.Vector2;

public class DamageProjectileAbility implements SpecialAbility {

	long cooldown = 0;

	GameScreen gameScreen;

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
