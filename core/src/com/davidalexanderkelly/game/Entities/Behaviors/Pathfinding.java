package com.davidalexanderkelly.game.Entities.Behaviors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.davidalexanderkelly.game.Screens.PlayScreen;
import com.davidalexanderkelly.game.Tools.PathfindingWorldCreator;

public class Pathfinding {
	
	private float depth;
	PathfindingWorldCreator pathfinder;
	
	public static float round(float d, int decimalPlace) {
	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
	
	public ArrayList<Node> findPath(Node startPosition,Node goalPosition, PathfindingWorldCreator pathfinder) {
		this.pathfinder = pathfinder;
		Node startNode = startPosition;
		Node endNode =  goalPosition;
		depth = 0f;
		
	    ArrayList<Node> openSet = new ArrayList<Node>();
	    HashSet<Node> closedSet = new HashSet<Node>();
	    openSet.add(startNode);
	    while(openSet.size() > 0) {
	    	Node currentNode = openSet.get(0);
	    	for(int i = 1; i < openSet.size(); i++) {
	    		if((openSet.get(i).fCost() < currentNode.fCost() || (openSet.get(i).fCost() == currentNode.fCost() && openSet.get(i).hCost < currentNode.hCost)) && depth > currentNode.fCost()) {
	    			currentNode = openSet.get(i);
	    			depth = currentNode.fCost();


	    			
	    		}
	    	}
	    	openSet.remove(currentNode);
	    	closedSet.add(currentNode);
	    	if(round(currentNode.getWorldPosition().x,2) == round(endNode.getWorldPosition().x,2) && round(currentNode.getWorldPosition().y,2) == round(endNode.getWorldPosition().y,2)) {
	    		ArrayList<Node> returnPath = new ArrayList<Node>();
	    		returnPath = retracePath(startNode,currentNode);
	    		
	    		return returnPath;
	    		
	    	}
	    	
	    	for(Node neighbour : pathfinder.getNeighbours(currentNode)){
	    		if(closedSet.contains(neighbour)) {
	    			
	    			continue;
	    			
	    		}

	    		float newMovementCostToNeighbour = currentNode.gCost + getDistance(currentNode,neighbour);
	    		if(newMovementCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
	    			neighbour.gCost = newMovementCostToNeighbour;
	    			neighbour.hCost = getDistance(neighbour,endNode);
	    			neighbour.parent = currentNode;
	    			
	    			if(!openSet.contains(neighbour)) {
	    				openSet.add(neighbour);
	    				
	    				
	    			}
	    		}
	    		
	    	}
	    	
	    }
	    
		return openSet;
		
	}
	
	public ArrayList<Node> retracePath(Node startNode, Node endNode) {
		
		ArrayList<Node> path = new ArrayList<Node>();
		Node currentNode = endNode;
		while(currentNode != startNode) {
			path.add(currentNode);
			currentNode = currentNode.parent;
		}
		
		Collections.reverse(path);
		return path;
	}
	
	public float getDistance(Node nodeA, Node nodeB) {
		
		float distanceX = round(nodeA.getWorldPosition().x,2) - round(nodeB.getWorldPosition().x,2);
		float distanceY = round(nodeA.getWorldPosition().y,2) - round(nodeB.getWorldPosition().y,2);
		
		if(distanceX > distanceY)
			return 14*distanceY + 10*(distanceX-distanceY);
		return 14*distanceX + 10*(distanceY-distanceX);
	}
}	
