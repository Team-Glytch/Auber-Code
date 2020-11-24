package com.auber.entities.abilities;

import com.auber.entities.Enemy;
import com.auber.entities.Player;

public interface SpecialAbility {

	public void doAction(Enemy enemy, Player player);
	public float getRange();

}
