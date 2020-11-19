package com.auber.game.Tools;

import com.auber.game.AuberGame;
import com.auber.game.Screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DWorldCreator {
	public Box2DWorldCreator(PlayScreen screen) {
		World world = screen.getWorld();
		TiledMap map  = screen.getMap();
		
		BodyDef bodyDefinition = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDefinition = new FixtureDef();
		Body body;
		
		for(MapObject object : map.getLayers().get("Collision").getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			bodyDefinition.type = BodyDef.BodyType.StaticBody;
			bodyDefinition.position.set((rect.getX() + rect.getWidth() / 2) / AuberGame.PixelsPerMetre, (rect.getY() + rect.getHeight() / 2) / AuberGame.PixelsPerMetre);

			body = world.createBody(bodyDefinition);
			shape.setAsBox(rect.getWidth() / 2 / AuberGame.PixelsPerMetre, rect.getHeight() /2 / AuberGame.PixelsPerMetre);
			fixtureDefinition.shape = shape;
			body.createFixture(fixtureDefinition);
		};
	}
}
