package com.auber.Entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.auber.Entities.Behaviors.Node;
import com.auber.Entities.Behaviors.Pathfinding;
import com.auber.game.AuberGame;
import com.auber.rendering.GameScreen;
import com.auber.rendering.Renderable;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Enemy implements Renderable {
	/**
	 * The game screen the enemy is in
	 */
	private GameScreen gameScreen;

	/**
	 * The definition of the Enemy's position/bounding box
	 */
	private Body box2dBody;

	/**
	 * The path finding of the enemy
	 */
	private Pathfinding pathfinding;
	/**
	 * The path the enemy is taking
	 */
	private ArrayList<Node> path;

	/**
	 * Constructs the Enemy
	 * 
	 * @param screen The game screen the enemy is in
	 */
	public Enemy(GameScreen screen) {
		this.gameScreen = screen;

		defineEnemy();
		setPath(0, 1);

	}

	@Override
	public void update(float deltaTime) {
		move(path.get(0));
	}
	
	/**
	 * @param d
	 * @param decimalPlace
	 * @return The number [d] rounded to [decimalPlace] decimal places
	 */
	private static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	/**
	 * Moves the enemy to the specified node
	 * 
	 * @param node
	 */
	private void move(Node node) {
		// Checks whether the enemy has reached the node
		if (round(node.getWorldPosition().x, 2) == round(box2dBody.getPosition().x, 2)
				&& round(node.getWorldPosition().y, 2) == round(box2dBody.getPosition().y, 2)) {
			path.remove(0);
			if (path.size() == 0) {
				setPath(1, 2);
			}
		} else {
			// Checks if the enemy has reached the y coordinate of the node
			if (round(node.getWorldPosition().y, 2) - round(box2dBody.getPosition().y, 2) == 0) {
				//Checks if the node is to the right or left of the enemy, respectively
				if (isNodeToRightOfEnemy(node)) {
					box2dBody.setTransform(round(box2dBody.getPosition().x + 0.01f, 2),
							round(box2dBody.getPosition().y, 2), 0);
				} else {
					box2dBody.setTransform(round(box2dBody.getPosition().x - 0.01f, 2),
							round(box2dBody.getPosition().y, 2), 0);
				}
			} else {
				// Checks if the enemy is above the node or below it, respectively
				if (isNodeAboveEnemy(node)) {
					box2dBody.setTransform(round(box2dBody.getPosition().x, 2),
							round(box2dBody.getPosition().y + 0.01f, 2), 0);
				} else {
					box2dBody.setTransform(round(box2dBody.getPosition().x, 2),
							round(box2dBody.getPosition().y - 0.01f, 2), 0);
				}
			}

		}

	}

	/**
	 * @param node
	 * @return True if the specified node is on the right side of the enemy, False otherwise
	 */
	private boolean isNodeToRightOfEnemy(Node node) {
		return round(node.getWorldPosition().x, 2) - round(box2dBody.getPosition().x, 2) > 0;
	}
	
	/**
	 * @param node
	 * @return True if the specified node is above the enemy, False otherwise
	 */
	private boolean isNodeAboveEnemy(Node node) {
		return round(node.getWorldPosition().y, 2) - round(box2dBody.getPosition().y, 2) > 0;
	}
	
	/**
	 * Sets the path the enemy will take
	 * 
	 * @param start The index of the node where the path starts
	 * @param end The index of the node where the path will end
	 */
	public void setPath(int start, int end) {
		pathfinding = new Pathfinding();
		List<Node> interactables = gameScreen.getInteractables().getLocations();
		path = pathfinding.findPath(interactables.get(start), interactables.get(end), gameScreen.getPathfinder());
	}

	/**
	 * Defines the enemy's {@link #box2dBody}
	 */
	public void defineEnemy() {
		BodyDef bodyDefinition = new BodyDef();

		// Position and type
		bodyDefinition.position.set(gameScreen.getInteractables().getLocations().get(0).getWorldPosition().x,
				gameScreen.getInteractables().getLocations().get(0).getWorldPosition().y);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;
		
		// Collisions
		box2dBody = gameScreen.getWorld().createBody(bodyDefinition);
		FixtureDef fixtureDefinition = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(1 / AuberGame.PixelsPerMetre);

		fixtureDefinition.shape = shape;

		box2dBody.createFixture(fixtureDefinition);
	}

	@Override
	public float getX() {
		return box2dBody.getPosition().x;
	}

	@Override
	public float getY() {
		return box2dBody.getPosition().y;
	}

	@Override
	public float getWidth() {
		return (24f / AuberGame.PixelsPerMetre);
	}

	@Override
	public float getHeight() {
		return (24f / AuberGame.PixelsPerMetre);
	}

	@Override
	public String getTextureName() {
		if (!path.isEmpty()) {
			return "EnemyRun";
		}

		return "EnemyIdle";
	}
	
	@Override
	public boolean isMovingRight() {
		return isNodeToRightOfEnemy(path.get(0));
	}
	
}
