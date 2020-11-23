package com.auber.gameplay;

import com.auber.entities.Enemy;
import com.auber.entities.Player;
import com.auber.rendering.Renderable;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class EnemyContactListener implements ContactListener {

	private GameScreen screen;
	
	public EnemyContactListener(GameScreen screen) {
		this.screen = screen;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		checkIfObjectsArePlayerAndEnemy(o1, o2);
		checkIfObjectsArePlayerAndEnemy(o2, o1);
	
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

	private void checkIfObjectsArePlayerAndEnemy(Object o1, Object o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		if (o1 instanceof Player && o2 instanceof Enemy) {
			screen.removeRenderable((Renderable) o2);
		}
		
	}
	
}
