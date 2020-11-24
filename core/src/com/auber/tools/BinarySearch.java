package com.auber.tools;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.behaviors.Node;

public class BinarySearch {

	/**
	 * int[] { index of outer array list, index of inner array list }
	 * 
	 * @param locationBreakdown
	 * @param node
	 * @return The indices of the node within location breakdown
	 */
	public static int[] search(ArrayList<ArrayList<Node>> locationBreakdown, Node node) {
		int resultX = BinarySearch.searchX(locationBreakdown, node);
		if (resultX != -1) {
			int resultY = BinarySearch.searchY(locationBreakdown.get(resultX), node);
			if (resultY != -1) {
				return new int[] { resultX, resultY };
			}
		}

		return new int[] { -1, -1 };
	}

	/**
	 * @param locationBreakdown
	 * @param x
	 * @return The index of node [x] within [locations]
	 */
	private static int searchX(ArrayList<ArrayList<Node>> locationBreakdown, Node x) {
		int lowerBound = 0, upperBound = (locationBreakdown.size() - 1);
		int mid = 0;
		while (lowerBound <= upperBound) {
			mid = (lowerBound + upperBound) / 2;

			if (MathsHelper.round(locationBreakdown.get(mid).get(0).getWorldPosition().x, 2) == MathsHelper
					.round(x.getWorldPosition().x, 2)) {
				return mid;
			}
			try {
				if (MathsHelper.round(locationBreakdown.get(mid).get(0).getWorldPosition().x, 2) < MathsHelper
						.round(x.getWorldPosition().x, 2)) {
					lowerBound = mid + 1;
				} else {
					upperBound = mid - 1;
				}
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}

		}

		return -1;
	}

	/**
	 * The search is done in terms of the node's y coordinate
	 * 
	 * @param locations
	 * @param y
	 * @return The index of the node within the locations
	 */
	private static int searchY(List<Node> locations, Node y) {
		int lowerBound = 0, upperBound = (locations.size() - 1);
		int mid = 0;
		while (lowerBound <= upperBound) {
			mid = (lowerBound + upperBound) / 2;

			if (MathsHelper.round(locations.get(mid).getWorldPosition().y, 2) == MathsHelper
					.round(y.getWorldPosition().y, 2)) {
				return mid;
			}
			try {
				if (MathsHelper.round(locations.get(mid).getWorldPosition().y, 2) < MathsHelper
						.round(y.getWorldPosition().y, 2)) {
					lowerBound = mid + 1;
				} else {
					upperBound = mid - 1;
				}
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}

		}

		return -1;
	}

}