package com.auber.gameplay;

import com.auber.entities.Player;
import com.auber.game.AuberGame;
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
	protected Rectangle bounds;
	protected Body body;
	protected MapObject object;

	protected Fixture fixture;

	public InteractiveTileObject(World world, MapObject object) {
		this.object = object;
		this.bounds = ((RectangleMapObject) object).getRectangle();

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / AuberGame.PixelsPerMetre,
				(bounds.getY() + bounds.getHeight() / 2) / AuberGame.PixelsPerMetre);

		body = world.createBody(bdef);

		shape.setAsBox(bounds.getWidth() / 2 / AuberGame.PixelsPerMetre,
				bounds.getHeight() / 2 / AuberGame.PixelsPerMetre);
		fdef.shape = shape;
		fixture = body.createFixture(fdef);

	}

	public abstract void collide(GameScreen screen, Player player);

	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}

}
