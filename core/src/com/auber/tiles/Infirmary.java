package com.auber.tiles;

import java.util.Timer;
import java.util.TimerTask;

import com.auber.Entities.Player;
import com.auber.gameplay.GameScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;

public class Infirmary extends InteractiveTileObject {

	/**
	 * Creates the infirmary tile
	 * 
	 * @param world The world the tile in in
	 * @param object The information about the shape and position of the tile
	 */
	public Infirmary(World world, MapObject object) {
		super(world, object);
	}

	@Override
	public void collide(GameScreen screen, final Player player) {
		player.setSpeed(0f);

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (player.getHealth() == 10f) {
					player.setSpeed(1f);
					timer.cancel();
				} else {
					float newHealth = player.getHealth() + 0.5f;
					
					if (newHealth > 10f) {
						newHealth = 10f;
					}
					
					player.setHealth(newHealth);
					
				}
			}
		}, 0, 1000);

	}

}
