package com.auber.Tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.auber.Entities.Behaviors.Node;
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
		QuickSort ob = new QuickSort(locations);
		ob.sort(0, (locations.size()) - 1);
		locations = ob.path;
	}

	/**
	 * @return {@link #locations}
	 */
	public List<Node> getLocations() {
		return locations;
	}

	/**
	 * @param d
	 * @param decimalPlace
	 * @return The number [d] rounded to [decimalPlace] decimal places
	 */
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
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
				BinarySearch ob = new BinarySearch();
				int result = ob.binarySearch(locations, checkN);
				if (result != -1) {
					neighbours.add(locations.get(result));
				}

			}
		}
		return neighbours;
	}

}
