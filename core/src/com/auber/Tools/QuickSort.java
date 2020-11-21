package com.auber.tools;

import java.util.List;

import com.auber.entities.behaviors.Node;

class QuickSort {

	private static int partition(List<Node> path, int low, int high) {
		float pivot = path.get(high).getWorldPosition().x;
		float pivotY = path.get(high).getWorldPosition().y;
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			if (path.get(j).getWorldPosition().x < pivot
					|| (path.get(j).getWorldPosition().y < pivotY && path.get(j).getWorldPosition().x == pivot)) {
				i++;

				float tempX = path.get(i).getWorldPosition().x;
				float tempY = path.get(i).getWorldPosition().y;
				path.get(i).getWorldPosition().x = path.get(j).getWorldPosition().x;
				path.get(i).getWorldPosition().y = path.get(j).getWorldPosition().y;
				path.get(j).getWorldPosition().x = tempX;
				path.get(j).getWorldPosition().y = tempY;
			}
		}

		float tempX = path.get(i + 1).getWorldPosition().x;
		float tempY = path.get(i + 1).getWorldPosition().y;
		path.get(i + 1).getWorldPosition().x = path.get(high).getWorldPosition().x;
		path.get(i + 1).getWorldPosition().y = path.get(high).getWorldPosition().y;
		path.get(high).getWorldPosition().x = tempX;
		path.get(high).getWorldPosition().y = tempY;

		return i + 1;
	}

	/**
	 * Sorts the list [path] between the indices [low] and [high]
	 * 
	 * @param path
	 * @param low
	 * @param high
	 */
	private static void sort(List<Node> path, int low, int high) {
		if (low < high) {

			int pi = partition(path, low, high);

			// Recursively sort elements before
			// partition and after partition
			sort(path, low, pi - 1);
			sort(path, pi + 1, high);
		}

	}
	
	/**
	 * Quick sorts the provided node list
	 * 
	 * @param path
	 */
	public static void sort(List<Node> path) {
		sort(path, 0, path.size() - 1);
	}

}