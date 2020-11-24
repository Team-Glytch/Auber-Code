package com.auber.entities.behaviors;

import com.auber.game.AuberGame;
import com.badlogic.gdx.math.Vector2;

public class Node {

	/**
	 * The position of the node within the world
	 */
	private Vector2 worldPosition;

	/**
	 * The current path cost
	 */
	public float gCost;

	/**
	 * The heuristic function cost
	 */
	public float hCost;

	/**
	 * The parent node of this node
	 */
	public Node parent;

	/**
	 * Creates the node at the position specified
	 * 
	 * @param worldPosition
	 */
	public Node(Vector2 worldPosition) {
		this.worldPosition = worldPosition;
	}

	/**
	 * @return The position of the node in the world
	 */
	public Vector2 getWorldPosition() {
		return worldPosition;
	}

	/**
	 * @return The evaluation cost of the node
	 */
	public float fCost() {
		return gCost + hCost;
	}

	/**
	 * @param position
	 * @return The node closest to the position specified
	 */
	public static Node getNearestNode(Vector2 position) {
		float newX = getGridCoord(position.x);
		float newY = getGridCoord(position.y);

		return new Node(new Vector2(newX, newY));
	}

	/**
	 * @param coord
	 * @return The transformed coordinate, mapped onto the grid of the game
	 */
	private static float getGridCoord(float coord) {
		return (float) (Math.floor(coord * AuberGame.PixelsPerMetre / 16) * 16 - 8) / AuberGame.PixelsPerMetre;
	}

}
