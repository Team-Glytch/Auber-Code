package com.auber.gameplay;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.behaviors.Node;
import com.auber.game.AuberGame;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class InteractableHandler {

	/**
	 * The list of all of the interactable objects
	 */
	private ArrayList<Node> locations;

	private ArrayList<Node> startLocations;

	private ArrayList<Teleporter> teleporters;

	/**
	 * Sets up the locations of the interactable objects
	 * 
	 * @param map   The map the interactables are defined in
	 * @param world The world the interactables will act in
	 */
	public InteractableHandler(TiledMap map, World world) {
		this.locations = new ArrayList<Node>();

		for (MapObject object : map.getLayers().get("Interactables").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vector2 vPosition = getPosition(rect);
			Node newNode = new Node(vPosition);
			locations.add(newNode);
		}

		this.startLocations = new ArrayList<Node>();
		for (MapObject object : map.getLayers().get("Start Locations").getObjects()
				.getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vector2 vPosition = getPosition(rect);
			Node newNode = new Node(vPosition);
			startLocations.add(newNode);
		}

		Array<RectangleMapObject> teleporterRects = map.getLayers().get("Teleporters").getObjects()
				.getByType(RectangleMapObject.class);
		this.teleporters = new ArrayList<Teleporter>();

		for (int i = 0; i < teleporterRects.size; i++) {
			this.teleporters.add(null);
		}

		for (MapObject object : teleporterRects) {
			RectangleMapObject rect = ((RectangleMapObject) object);

			int index = Integer.parseInt(rect.getName());

			Vector2 vPosition = getPosition(rect.getRectangle());
			Teleporter teleporter = new Teleporter(world, object, vPosition.x, vPosition.y);
			teleporters.set(index, teleporter);
		}
	}

	private Vector2 getPosition(Rectangle rect) {
		return new Vector2((rect.getX() + rect.getWidth() / 2) / AuberGame.PixelsPerMetre,
				(rect.getY() + rect.getHeight() / 2) / AuberGame.PixelsPerMetre);
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

	public ArrayList<Teleporter> getTeleporters() {
		return teleporters;
	}

}
