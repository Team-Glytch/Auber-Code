package com.auber.gameplay;

import java.util.ArrayList;
import java.util.List;

public class Rooms {

	/**
	 * If value at [index] is true, then the corresponding interactable at [index]
	 * in interactables is operational
	 */
	private boolean[] isInteractableOperational;

	/**
	 * @param amountOfInteractables The amount of interactables in the map
	 */
	public Rooms(int amountOfInteractables) {
		this.isInteractableOperational = new boolean[amountOfInteractables];

		for (int i = 0; i < this.isInteractableOperational.length; i++) {
			this.isInteractableOperational[i] = true;
		}
	}

	/**
	 * Sets the interactable at index [id] to be inoperable
	 * 
	 * @param id
	 */
	public void breakInteractable(int id) {
		this.isInteractableOperational[id] = false;
	}

	/**
	 * @return The list of indices of the interactables that are operational
	 */
	public List<Integer> getOperationalIDs() {
		List<Integer> operationalIDs = new ArrayList<Integer>();

		for (int i = 0; i < this.isInteractableOperational.length; i++) {
			if (this.isInteractableOperational[i]) {
				operationalIDs.add(i);
			}
		}

		return operationalIDs;
	}

}
