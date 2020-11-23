package com.auber.tools;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.behaviors.Node;
import com.auber.game.AuberGame;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InteractableWorldCreator {
	
	/**
	 * The list of all of the interactable objects
	 */
	private ArrayList<Node> locations;	

	private ArrayList<Node> startLocations;
	
	/**
	 * Sets up the locations of the interactable objects
	 * 
	 * @param map
	 */
	public InteractableWorldCreator(TiledMap map) {
		
		this.locations = new ArrayList<Node>();
		
		for(MapObject object : map.getLayers().get("Interactables").getObjects().getByType(RectangleMapObject.class)){			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vector2 vPosition = new Vector2((rect.getX() + rect.getWidth() / 2) / AuberGame.PixelsPerMetre,(rect.getY() + rect.getHeight() / 2) / AuberGame.PixelsPerMetre);
			Node newNode = new Node(vPosition);
			locations.add(newNode);
		}
	
		this.startLocations = new ArrayList<Node>();
		for(MapObject object : map.getLayers().get("Start Locations").getObjects().getByType(RectangleMapObject.class)){			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vector2 vPosition = new Vector2((rect.getX() + rect.getWidth() / 2) / AuberGame.PixelsPerMetre,(rect.getY() + rect.getHeight() / 2) / AuberGame.PixelsPerMetre);
			Node newNode = new Node(vPosition);
			startLocations.add(newNode);
		}
	}
	
	public ArrayList<Node> getStartLocations() {
		return startLocations;
	}
	
	/**
	 * @return {@link #locations}
	 */
	public List<Node> getLocations() {
		return this.locations;
	}

}
