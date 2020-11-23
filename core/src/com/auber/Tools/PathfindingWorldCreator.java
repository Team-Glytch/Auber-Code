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

public class PathfindingWorldCreator {

	/**
	 * The locations of the nodes within the map
	 */
	private List<Node> locations;

	public PathfindingWorldCreator(TiledMap map) {
		this.locations = new ArrayList<Node>();

		for (MapObject object : map.getLayers().get("Ai Pathing").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vector2 vPosition = new Vector2((rect.getX() + rect.getWidth() / 2) / AuberGame.PixelsPerMetre,
					(rect.getY() + rect.getHeight() / 2) / AuberGame.PixelsPerMetre);
			Node newNode = new Node(vPosition);
			locations.add(newNode);

		}

		QuickSort.sort(locations);
	}

	/**
	 * @return {@link #locations}
	 */
	public List<Node> getLocations() {
		return locations;
	}

	/**
	 * @param node
	 * @return A list of the neighbour nodes of [node]
	 */
	public ArrayList<Node> getNeighbours(Node node) {
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == y || (x + y) == 0)
					continue;

				float checkX = node.getWorldPosition().x + (x * 0.16f);
				float checkY = node.getWorldPosition().y + (y * 0.16f);
				Vector2 checkV = new Vector2(checkX, checkY);
				Node checkN = new Node(checkV);
				int result = BinarySearch.search(locations, checkN);

				if (result != -1) {
					neighbours.add(locations.get(result));
				}

			}
		}
		return neighbours;
	}

}
