package com.auber.entities.projectile;

import com.auber.entities.Player;
import com.badlogic.gdx.math.Vector2;

public interface Projectile {

	void collide(Player player);

	void fire(Vector2 target);

}
