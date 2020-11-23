package com.auber.entities;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.behaviors.Node;
import com.auber.entities.behaviors.Pathfinding;
import com.auber.game.AuberGame;
import com.auber.gameplay.GameScreen;
import com.auber.rendering.Renderable;
import com.auber.tools.MathsHelper;
import com.badlogic.gdx.math.Vector2;
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
	 * The path the enemy is taking
	 */
	private ArrayList<Node> path;

	int destinationID;

	boolean isDead = false;

	/**
	 * Constructs the Enemy
	 * 
	 * @param screen The game screen the enemy is in
	 */
	public Enemy(GameScreen screen, int index) {
		this.gameScreen = screen;

		Node startLoc = gameScreen.getInteractables().getStartLocations().get(index + 1);
		defineEnemy(startLoc);

		setPath(startLoc, index);

	}

	public boolean isDead() {
		return isDead;
	}

	public void kill() {
		this.isDead = true;
		path.clear();
	}

	@Override
	public void update(float deltaTime) {
		checkCurrentPath();

		if (!path.isEmpty()) {
			move(path.get(0));
		}
	}

	public void checkCurrentPath() {
		if (path != null && !path.isEmpty()) {
			Node node = path.get(0);
			Player player = gameScreen.getPlayer();

			if (Pathfinding.isNodeInRangeOfPlayer(player, node)) {
				setPath(node, destinationID);
			}
		}
	}

	/**
	 * Moves the enemy to the specified node
	 * 
	 * @param node
	 */
	private void move(Node node) {
		// Checks whether the enemy has reached the node
		if (MathsHelper.round(node.getWorldPosition().x, 2) == MathsHelper.round(box2dBody.getPosition().x, 2)
				&& MathsHelper.round(node.getWorldPosition().y, 2) == MathsHelper.round(box2dBody.getPosition().y, 2)) {
			path.remove(0);
			if (path.size() == 0) {
				setNewPath();
			}
		} else {
			// Checks if the enemy has reached the y coordinate of the node
			if (MathsHelper.round(node.getWorldPosition().y, 2)
					- MathsHelper.round(box2dBody.getPosition().y, 2) == 0) {
				// Checks if the node is to the right or left of the enemy, respectively
				if (isNodeToRightOfEnemy(node)) {
					box2dBody.setTransform(MathsHelper.round(box2dBody.getPosition().x + 0.01f, 2),
							MathsHelper.round(box2dBody.getPosition().y, 2), 0);
				} else {
					box2dBody.setTransform(MathsHelper.round(box2dBody.getPosition().x - 0.01f, 2),
							MathsHelper.round(box2dBody.getPosition().y, 2), 0);
				}
			} else {
				// Checks if the enemy is above the node or below it, respectively
				if (isNodeAboveEnemy(node)) {
					box2dBody.setTransform(MathsHelper.round(box2dBody.getPosition().x, 2),
							MathsHelper.round(box2dBody.getPosition().y + 0.01f, 2), 0);
				} else {
					box2dBody.setTransform(MathsHelper.round(box2dBody.getPosition().x, 2),
							MathsHelper.round(box2dBody.getPosition().y - 0.01f, 2), 0);
				}
			}

		}

	}

	/**
	 * @param node
	 * @return True if the specified node is on the right side of the enemy, False
	 *         otherwise
	 */
	private boolean isNodeToRightOfEnemy(Node node) {
		return MathsHelper.round(node.getWorldPosition().x, 2) - MathsHelper.round(box2dBody.getPosition().x, 2) > 0;
	}

	/**
	 * @param node
	 * @return True if the specified node is above the enemy, False otherwise
	 */
	private boolean isNodeAboveEnemy(Node node) {
		return MathsHelper.round(node.getWorldPosition().y, 2) - MathsHelper.round(box2dBody.getPosition().y, 2) > 0;
	}

	public void setNewPath() {
		int startID = destinationID;
		gameScreen.getRooms().breakInteractable(startID);

		List<Integer> operationalIDs = gameScreen.getRooms().getOperationalIDs();

		int nextDest = -1;
		float dist = Float.MAX_VALUE;

		for (int i = 0; i < operationalIDs.size(); i++) {
			float distance = MathsHelper.distanceBetween(box2dBody.getPosition(),
					gameScreen.getInteractables().getLocations().get(operationalIDs.get(i)).getWorldPosition());

			if (distance < dist) {
				dist = distance;
				nextDest = i;
			}

		}

		if (nextDest != -1) {
			setPath(startID, operationalIDs.get(nextDest));
		}
	}

	public void setPath(Node fromNode, int end) {
		List<Node> interactables = gameScreen.getInteractables().getLocations();
		path = Pathfinding.findPath(fromNode, interactables.get(end), gameScreen.getPlayer(),
				gameScreen.getPathfinder());
		destinationID = end;
	}

	/**
	 * Sets the path the enemy will take
	 * 
	 * @param start The index of the node where the path starts
	 * @param end   The index of the node where the path will end
	 */
	public void setPath(int start, int end) {
		setPath(gameScreen.getInteractables().getLocations().get(start), end);
	}

	/**
	 * Defines the enemy's {@link #box2dBody}
	 * 
	 * @param startLoc
	 */
	public void defineEnemy(Node startLoc) {
		Vector2 loc = startLoc.getWorldPosition();

		// Position and type
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.position.set(loc.x, loc.y);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;
		box2dBody = gameScreen.getWorld().createBody(bodyDefinition);

		// Collisions
		FixtureDef fixtureDefinition = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / AuberGame.PixelsPerMetre);
		fixtureDefinition.shape = shape;
		// Prevents enemies from colliding with each other
		fixtureDefinition.filter.categoryBits = 0x0001;
		fixtureDefinition.filter.maskBits = 0x0010;
		box2dBody.createFixture(fixtureDefinition);

		box2dBody.setUserData(this);
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
		return path.isEmpty() || isNodeToRightOfEnemy(path.get(0));
	}

}
