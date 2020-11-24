package com.auber.gameplay;

import com.auber.entities.Enemy;
import com.auber.entities.Player;
import com.auber.entities.projectile.Projectile;
import com.auber.rendering.Renderable;
import com.auber.screens.GameScreen;
import com.auber.tiles.InteractiveTileObject;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {
	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short TILE_BIT = 4;
	public static final short ENEMY_BIT = 8;
	public static final short PROJECTILE_BIT = 16;

	/**
	 * The screen that the collisions happen in
	 */
	private GameScreen screen;

	/**
	 * Creates the contact listener
	 * 
	 * @param screen
	 */
	public WorldContactListener(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case PLAYER_BIT | TILE_BIT:
			if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
				((InteractiveTileObject) fixB.getUserData()).collide(screen, (Player) fixA.getBody().getUserData());
			} else {
				((InteractiveTileObject) fixA.getUserData()).collide(screen, (Player) fixB.getBody().getUserData());
			}
			break;
		case PLAYER_BIT | ENEMY_BIT:
			Enemy enemy = null;
			if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
				enemy = (Enemy) fixB.getBody().getUserData();
			} else {
				enemy = (Enemy) fixA.getBody().getUserData();
			}

			screen.getInteractables().jail(enemy);
			break;
		case PLAYER_BIT | PROJECTILE_BIT:
			Player player;
			Projectile projectile;
			
			if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
				player = (Player) fixA.getBody().getUserData();
				projectile = (Projectile) fixB.getBody().getUserData();
			} else {
				player = (Player) fixB.getBody().getUserData();
				projectile = (Projectile) fixA.getBody().getUserData();
			}
			projectile.collide(player);
			
			break;
		}
		
		if (fixA.getFilterData().categoryBits == PROJECTILE_BIT) {
			Renderable render = (Renderable) fixA.getBody().getUserData();
			screen.removeRenderable(render);
		} else if (fixB.getFilterData().categoryBits == PROJECTILE_BIT) {
			Renderable render = (Renderable) fixB.getBody().getUserData();
			screen.removeRenderable(render);
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
