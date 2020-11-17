package com.davidalexanderkelly.game.Tools;

import com.davidalexanderkelly.game.Entities.Enemy;

public class EnemyUpdater extends Thread {
	
	Enemy enemy;
	float deltaTime;
	
	public EnemyUpdater(Enemy enemy, float deltaTime) {
		this.enemy = enemy;
		this.deltaTime = deltaTime;
	}
	
	public void run() {
		enemy.update(deltaTime);
	}
}
