package com.auber.gameplay;

import com.auber.entities.Enemy;
import com.auber.entities.Player;
import com.auber.rendering.Renderable;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {
	public static final short ENEMY_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short TELEPORTER_BIT = 4;

	private GameScreen screen;

	public WorldContactListener(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case PLAYER_BIT | TELEPORTER_BIT:
			if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
				((InteractiveTileObject) fixB.getUserData()).collide(screen, (Player) fixA.getBody().getUserData());
			} else {
				((InteractiveTileObject) fixA.getUserData()).collide(screen, (Player) fixB.getBody().getUserData());
			}
			break;
		case PLAYER_BIT | ENEMY_BIT:
			if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
				Enemy enemy = (Enemy) fixB.getBody().getUserData();
				screen.removeRenderable((Renderable) enemy);
			} else {
				Enemy enemy = (Enemy) fixA.getBody().getUserData();
				screen.removeRenderable((Renderable) enemy);
			}
			break;
		}

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

}
