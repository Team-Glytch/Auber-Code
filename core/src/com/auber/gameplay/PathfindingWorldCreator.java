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

	private List<Node> locations;
	private ArrayList<ArrayList<Node>> locationBreakdown;

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
				int resultX = BinarySearch.search(locationBreakdown, checkN);
				if (resultX != -1) {
					int resultY = BinarySearch.searchY(locationBreakdown.get(resultX), checkN);
					if (resultY != -1) {
						neighbours.add(locationBreakdown.get(resultX).get(resultY));
					}
				}

			}
		}
		return neighbours;
	}
}
