package com.davidalexanderkelly.game.Tools;

import java.util.List;

import com.davidalexanderkelly.game.Entities.Behaviors.Node;

class QuickSort {
	
	public List<Node> path;
	
	public QuickSort(List<Node> path) {
		this.path = path;
	}

    public int partition(int low, int high) 
    { 
        float pivot = path.get(high).getWorldPosition().x;
        float pivotY = path.get(high).getWorldPosition().y;
        int i = (low-1);
        for (int j=low; j<high; j++) 
        { 
            if (path.get(j).getWorldPosition().x < pivot ||(path.get(j).getWorldPosition().y < pivotY && path.get(j).getWorldPosition().x == pivot)) 
            { 
                i++; 
  
                float tempX = path.get(i).getWorldPosition().x;
                float tempY = path.get(i).getWorldPosition().y;
                path.get(i).getWorldPosition().x = path.get(j).getWorldPosition().x;
                path.get(i).getWorldPosition().y = path.get(j).getWorldPosition().y; 
                path.get(j).getWorldPosition().x = tempX;
                path.get(j).getWorldPosition().y = tempY; 
            } 
        } 
  

        float tempX = path.get(i+1).getWorldPosition().x;
        float tempY = path.get(i+1).getWorldPosition().y;
        path.get(i+1).getWorldPosition().x = path.get(high).getWorldPosition().x;
        path.get(i+1).getWorldPosition().y = path.get(high).getWorldPosition().y;
        path.get(high).getWorldPosition().x = tempX; 
        path.get(high).getWorldPosition().y = tempY; 
  
        return i+1; 
    } 
  
    public void sort(int low, int high) 
    { 
        if (low < high) 
        { 

            int pi = partition(low, high); 
  
            // Recursively sort elements before 
            // partition and after partition 
            sort(low, pi-1); 
            sort(pi+1, high); 
        } 

    } 
  
} 