package com.auber.gameplay;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.behaviors.Node;
import com.auber.game.AuberGame;
import com.auber.tools.BinarySearch;
import com.auber.tools.QuickSort;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PathfindingWorldCreator {

	/**
	 * The path finding nodes on the map
	 */
	private List<Node> locations;

	/**
	 * The structure of the nodes on the map All the nodes on the same x-coordinate
	 * are stored in the internal
	 */
	private ArrayList<ArrayList<Node>> locationBreakdown;

	/**
	 * Creates the pathfinding from the tiled map
	 * 
	 * @param map
	 */
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
		float previousX = locations.get(0).getWorldPosition().x;
		locationBreakdown = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> tempLocation = new ArrayList<Node>();

		for (Node n : locations) {
			if (n.getWorldPosition().x == previousX) {
				tempLocation.add(n);
			} else {
				locationBreakdown.add(tempLocation);
				tempLocation = new ArrayList<Node>();
				tempLocation.add(n);
				previousX = n.getWorldPosition().x;

			}

		}

		locationBreakdown.add(tempLocation);

	}

	/**
	 * @param node
	 * @return The 4 direct neighbours of [node]
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
				int[] result = BinarySearch.search(locationBreakdown, checkN);
				if (result[0] != -1) {
					neighbours.add(locationBreakdown.get(result[0]).get(result[1]));
				}

			}
		}
		return neighbours;
	}
}
