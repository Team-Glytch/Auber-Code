package com.auber.entities.abilities;

import com.auber.entities.Enemy;
import com.auber.entities.Player;

public interface SpecialAbility {

	void doAction(Enemy enemy, Player player);

	float getRange();

}
