package com.auber.entities.behaviors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.auber.tools.MathsHelper;
import com.auber.tools.PathfindingWorldCreator;

public class Pathfinding {

	/**
	 * The depth the algorithm will search for the solution
	 */
	private float depth;

	/**
	 * The structure containing the data about the paths in the map
	 */
	PathfindingWorldCreator pathfinder;

	/**
	 * @param startPosition
	 * @param goalPosition
	 * @param pathfinder
	 * @return The shortest sequence of nodes that connects the nodes
	 *         [startPosition] and [endPosition]
	 */
	public ArrayList<Node> findPath(Node startPosition, Node goalPosition, PathfindingWorldCreator pathfinder) {
		this.pathfinder = pathfinder;
		Node startNode = startPosition;
		Node endNode = goalPosition;
		depth = 0f;

		ArrayList<Node> openSet = new ArrayList<Node>();
		HashSet<Node> closedSet = new HashSet<Node>();
		openSet.add(startNode);
		while (openSet.size() > 0) {
			Node currentNode = openSet.get(0);
			for (int i = 1; i < openSet.size(); i++) {
				if ((openSet.get(i).fCost() < currentNode.fCost()
						|| (openSet.get(i).fCost() == currentNode.fCost() && openSet.get(i).hCost < currentNode.hCost))
						&& depth > currentNode.fCost()) {
					currentNode = openSet.get(i);
					depth = currentNode.fCost();

				}
			}
			openSet.remove(currentNode);
			closedSet.add(currentNode);
			if (MathsHelper.round(currentNode.getWorldPosition().x, 2) == MathsHelper.round(endNode.getWorldPosition().x, 2)
					&& MathsHelper.round(currentNode.getWorldPosition().y, 2) == MathsHelper.round(endNode.getWorldPosition().y, 2)) {
				return retracePath(startNode, currentNode);
			}

			for (Node neighbour : pathfinder.getNeighbours(currentNode)) {
				if (closedSet.contains(neighbour)) {

					continue;

				}

				float newMovementCostToNeighbour = currentNode.gCost + getDistance(currentNode, neighbour);
				if (newMovementCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
					neighbour.gCost = newMovementCostToNeighbour;
					neighbour.hCost = getDistance(neighbour, endNode);
					neighbour.parent = currentNode;

					if (!openSet.contains(neighbour)) {
						openSet.add(neighbour);

					}
				}

			}

		}

		return openSet;

	}

	/**
	 * @param startNode
	 * @param endNode
	 * @return A sequence of nodes
	 */
	public ArrayList<Node> retracePath(Node startNode, Node endNode) {
		ArrayList<Node> path = new ArrayList<Node>();
		Node currentNode = endNode;
		while (currentNode != startNode) {
			path.add(currentNode);
			currentNode = currentNode.parent;
		}

		Collections.reverse(path);
		return path;
	}

	/**
	 * @param nodeA
	 * @param nodeB
	 * @return The distance between [nodeA] and [nodeB]
	 */
	public float getDistance(Node nodeA, Node nodeB) {
		float distanceX = MathsHelper.round(nodeA.getWorldPosition().x, 2) - MathsHelper.round(nodeB.getWorldPosition().x, 2);
		float distanceY = MathsHelper.round(nodeA.getWorldPosition().y, 2) - MathsHelper.round(nodeB.getWorldPosition().y, 2);

		return (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}
}
