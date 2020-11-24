package com.auber.tiles;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.Enemy;
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

	/**
	 * The nodes at which the enemies and player first spawn in
	 */
	private ArrayList<Node> startLocations;

	/**
	 * The locations where the enemies are sent to when captured
	 */
	private ArrayList<Node> brigLocations;

	/**
	 * The locations of the teleporters in the map. 
	 * Teleporters are paired with their neighbour, e.g. 0-1, 2-3, etc.
	 */
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

		this.brigLocations = new ArrayList<Node>();
		for (MapObject object : map.getLayers().get("Brig").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vector2 vPosition = getPosition(rect);
			Node newNode = new Node(vPosition);
			brigLocations.add(newNode);
		}

		RectangleMapObject infirmiryRect = map.getLayers().get("Infirmary").getObjects()
				.getByType(RectangleMapObject.class).first();
		new Infirmary(world, infirmiryRect);

	}

	/**
	 * Jails the specified enemy
	 * 
	 * @param enemy
	 */
	public void jail(Enemy enemy) {
		Vector2 loc = brigLocations.get(enemy.getID()).getWorldPosition();

		enemy.teleport(loc.x, loc.y);
		enemy.kill();
	}

	/**
	 * @param rect
	 * @return The position of [rect] as a vector
	 */
	private Vector2 getPosition(Rectangle rect) {
		return new Vector2((rect.getX() + rect.getWidth() / 2) / AuberGame.PixelsPerMetre,
				(rect.getY() + rect.getHeight() / 2) / AuberGame.PixelsPerMetre);
	}

	/**
	 * @return {@link #startLocations}
	 */
	public ArrayList<Node> getStartLocations() {
		return startLocations;
	}

	/**
	 * @return {@link #locations}
	 */
	public List<Node> getLocations() {
		return this.locations;
	}

	/**
	 * @return {@link #teleporters}
	 */
	public ArrayList<Teleporter> getTeleporters() {
		return teleporters;
	}

}
