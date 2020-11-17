package com.davidalexanderkelly.game.Entities.Behaviors;

import com.badlogic.gdx.math.Vector2;

public class Node {
	
	private Vector2 worldPosition;
	
	public float gCost;
	public float hCost;
	public Node parent;
	
	public Node(Vector2 worldPosition) {
		this.worldPosition = worldPosition;
	}
	
	
	
	public Vector2 getWorldPosition() {
		return worldPosition;
	}
	
	public float fCost(){
		return gCost + hCost;
	}
	
}
