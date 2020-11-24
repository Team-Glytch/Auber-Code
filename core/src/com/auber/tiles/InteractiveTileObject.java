package com.auber.tiles;

import com.auber.entities.Player;
import com.auber.game.AuberGame;
import com.auber.gameplay.GameScreen;
import com.auber.gameplay.WorldContactListener;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class InteractiveTileObject {
	/**
	 * The fixture of the tile
	 */
	protected Fixture fixture;

	/**
	 * Creates the tile object
	 * 
	 * @param world The world the tile is in
	 * @param object The shape and positional data about the tile
	 */
	public InteractiveTileObject(World world, MapObject object) {
		Rectangle bounds = ((RectangleMapObject) object).getRectangle();

		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / AuberGame.PixelsPerMetre,
				(bounds.getY() + bounds.getHeight() / 2) / AuberGame.PixelsPerMetre);

		Body body = world.createBody(bdef);

		shape.setAsBox(bounds.getWidth() / 2 / AuberGame.PixelsPerMetre,
				bounds.getHeight() / 2 / AuberGame.PixelsPerMetre);
		fdef.shape = shape;

		fixture = body.createFixture(fdef);

		setCategoryFilter(WorldContactListener.TILE_BIT);
		fixture.setUserData(this);
	}

	/**
	 * Sets what the filter bit of the tile is
	 * 
	 * @param filterBit 
	 */
	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}

	/**
	 * Does the action when the player collides with the tile
	 * 
	 * @param screen
	 * @param player
	 */
	public abstract void collide(GameScreen screen, Player player);

}
