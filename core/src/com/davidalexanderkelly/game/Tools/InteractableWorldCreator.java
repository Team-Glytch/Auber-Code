package com.davidalexanderkelly.game.Tools;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.davidalexanderkelly.game.SpaceGamePrototype;
import com.davidalexanderkelly.game.Entities.Behaviors.Node;

public class InteractableWorldCreator {
	
	private ArrayList<Node> locations;	
	private TiledMap map;

public InteractableWorldCreator(TiledMap map) {
		
		this.map = map;		
	}
	
	public void setLocations() {
		
		this.locations = new ArrayList<Node>();
		
		for(MapObject object : map.getLayers().get("Interactables").getObjects().getByType(RectangleMapObject.class)){			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vector2 vPosition = new Vector2((rect.getX() + rect.getWidth() / 2) / SpaceGamePrototype.PixelsPerMetre,(rect.getY() + rect.getHeight() / 2) / SpaceGamePrototype.PixelsPerMetre);
			Node newNode = new Node(vPosition);
			locations.add(newNode);
		}
	}
	
	public List<Node> getLocations() {
		return this.locations;
	}

}
