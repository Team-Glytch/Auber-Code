package com.auber.tiles;

import java.util.List;

import com.auber.entities.Player;
import com.auber.entities.behaviors.Node;
import com.auber.gameplay.GameScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Teleporter extends InteractiveTileObject {

	/**
	 * The x coordinate of the teleporter
	 */
	private float x;
	
	/**
	 * The y coordinate of the teleporter
	 */
	private float y;

	/**
	 * Creates the teleporter
	 * 
	 * @param world The world the teleporter is in
	 * @param object The tile information of the teleporter
	 * @param x
	 * @param y
	 */
	public Teleporter(World world, MapObject object, float x, float y) {
		super(world, object);

		fixture.setUserData(this);
		this.x = x;
		this.y = y;
	}

	/**
	 * @return The position of the teleporter on the map
	 */
	public Vector2 getPosition() {
		return new Vector2(x, y);
	}

	@Override
	public void collide(GameScreen screen, Player player) {
		List<Teleporter> teleporters = screen.getInteractables().getTeleporters();
		int i = teleporters.indexOf(this);
		Teleporter otherTeleporter = null;
		
		if (i % 2 == 0) {
			otherTeleporter = teleporters.get(i + 1);
		} else {
			otherTeleporter = teleporters.get(i - 1);
		}
		
		Node node = new Node(otherTeleporter.getPosition());
		
		Node nextNode = screen.getPathfinder().getNeighbours(node).get(0);
		
		Vector2 dif = nextNode.getWorldPosition().cpy().sub(node.getWorldPosition());
		
		Vector2 newPosition = nextNode.getWorldPosition().cpy().add(dif.scl(0.5f));
		
		player.teleport(newPosition.x, newPosition.y);
	}

}
