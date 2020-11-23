package com.auber.entities.behaviors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.auber.entities.Player;
import com.auber.game.AuberGame;
import com.auber.tools.MathsHelper;
import com.auber.tools.PathfindingWorldCreator;
import com.badlogic.gdx.math.Vector2;

public class Pathfinding {

	/**
	 * @param startPosition
	 * @param goalPosition
	 * @param pathfinder
	 * @return The shortest sequence of nodes that connects the nodes
	 *         [startPosition] and [endPosition]
	 */
	public static ArrayList<Node> findPath(Node startPosition, Node goalPosition, Player player,
			PathfindingWorldCreator pathfinder) {
		Node startNode = startPosition;
		Node endNode = goalPosition;
		float depth = 0f;

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
			if (MathsHelper.round(currentNode.getWorldPosition().x, 2) == MathsHelper
					.round(endNode.getWorldPosition().x, 2)
					&& MathsHelper.round(currentNode.getWorldPosition().y, 2) == MathsHelper
							.round(endNode.getWorldPosition().y, 2)) {
				return retracePath(startNode, currentNode);
			}

			for (Node neighbour : pathfinder.getNeighbours(currentNode)) {
				if (closedSet.contains(neighbour) || isNodeInRangeOfPlayer(player, neighbour)) {
					continue;
				}

				float distance = MathsHelper.distanceBetween(currentNode.getWorldPosition(),
						neighbour.getWorldPosition());
				float newMovementCostToNeighbour = currentNode.gCost + distance;
				if (newMovementCostToNeighbour < neighbour.gCost || !openSet.contains(neighbour)) {
					neighbour.gCost = newMovementCostToNeighbour;
					neighbour.hCost = MathsHelper.distanceBetween(neighbour.getWorldPosition(),
							endNode.getWorldPosition());
					neighbour.parent = currentNode;

					if (!openSet.contains(neighbour)) {
						openSet.add(neighbour);

					}
				}

			}

		}

		return openSet;

	}

	public static boolean isNodeInRangeOfPlayer(Player player, Node node) {
		float radius = 20 / AuberGame.PixelsPerMetre;

		if (player != null) {
			float distance = MathsHelper.distanceBetween(new Vector2(player.getX(), player.getY()),
					node.getWorldPosition());

			if (distance < radius) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param startNode
	 * @param endNode
	 * @return A sequence of nodes
	 */
	public static ArrayList<Node> retracePath(Node startNode, Node endNode) {
		ArrayList<Node> path = new ArrayList<Node>();
		Node currentNode = endNode;
		while (currentNode != startNode) {
			path.add(currentNode);
			currentNode = currentNode.parent;
		}

		Collections.reverse(path);
		return path;
	}

}
