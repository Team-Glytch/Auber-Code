package com.auber.gameplay;

import java.util.ArrayList;
import java.util.List;

public class Rooms {

	private boolean[] isInteractableOperational;
	
	public Rooms(int amountOfInteractables) {
		this.isInteractableOperational = new boolean[amountOfInteractables];
		
		for (int i = 0; i < this.isInteractableOperational.length; i++) {
			this.isInteractableOperational[i] = true;
		}
	}
	
	public void breakInteractable(int id) {
		this.isInteractableOperational[id] = false;
	}
	
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
