package com.auber.Entities.Behaviors;

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
	public float fCost(){
		return gCost + hCost;
	}
	
}
