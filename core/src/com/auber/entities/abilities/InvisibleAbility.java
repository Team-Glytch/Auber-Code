package com.auber.entities.abilities;

import java.util.Timer;
import java.util.TimerTask;

import com.auber.entities.Enemy;
import com.auber.entities.Player;

public class InvisibleAbility implements SpecialAbility {

	long cooldown = 0;

	@Override
	public void doAction(final Enemy enemy, Player player) {
		if (System.currentTimeMillis() > cooldown) {
			if (enemy.isVisible()) {
				enemy.setVisible(false);
				cooldown = System.currentTimeMillis() + 10 * 1000;
				
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						enemy.setVisible(true);
						timer.cancel();
					}
				}, 2 * 1000);
			}
		}
	}

	@Override
	public float getRange() {
		return 25;
	}

}
