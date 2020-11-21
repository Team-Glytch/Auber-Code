package com.auber.Tools;

import java.math.BigDecimal;
import java.util.List;

import com.auber.Entities.Behaviors.Node;

class BinarySearch {

	/**
	 * @param locations
	 * @param x
	 * @return The index of node [x] within [locations]
	 */
	public static int search(List<Node> locations, Node x) {
		int lowerBound = 0, upperBound = (locations.size() - 1);
		boolean foundX = false;
		int mid = 0;
		while (lowerBound <= upperBound) {
			if (foundX == false) {
				mid = (lowerBound + upperBound) / 2;
			}

			if (round(locations.get(mid).getWorldPosition().x, 2) == round(x.getWorldPosition().x, 2)) {
				foundX = true;
				if (round(locations.get(mid).getWorldPosition().y, 2) == round(x.getWorldPosition().y, 2)) {

					return mid;
				}

				if (round(locations.get(mid).getWorldPosition().y, 2) < round(x.getWorldPosition().y, 2)) {
					mid += 1;
					lowerBound += 1;
				} else {
					mid -= 1;
					upperBound -= 1;
				}

			} else if (foundX == true) {
				return -1;
			}

			if (round(locations.get(mid).getWorldPosition().x, 2) < round(x.getWorldPosition().x, 2)) {
				lowerBound = mid + 1;

			} else {
				upperBound = mid - 1;
			}

		}

		return -1;
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

}
