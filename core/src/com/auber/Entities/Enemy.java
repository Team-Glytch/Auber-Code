package com.auber.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.auber.entities.abilities.SpecialAbility;
import com.auber.entities.behaviors.Node;
import com.auber.entities.behaviors.Pathfinding;
import com.auber.game.AuberGame;
import com.auber.gameplay.WorldContactListener;
import com.auber.rendering.Renderable;
import com.auber.screens.GameScreen;
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

	/**
	 * The ID of the location of the interactable being travelled to
	 */
	int destinationID;

	/**
	 * True if the enemy is visible to the player, False otherwise
	 */
	boolean isVisible = true;

	/**
	 * True if the player has killed the enemy, False otherwise
	 */
	boolean isDead = false;

	/**
	 * True if the enemy can move, False otherwise
	 */
	boolean canMove = true;

	/**
	 * The special ability the enemy can use
	 */
	private SpecialAbility ability;

	/**
	 * The ID of the enemy
	 */
	private int id;

	/**
	 * Constructs the Enemy
	 * 
	 * @param screen The game screen the enemy is in
	 */
	public Enemy(GameScreen screen, SpecialAbility ability, int index) {
		this.gameScreen = screen;
		this.ability = ability;
		this.id = index;

		Node startLoc = gameScreen.getInteractables().getStartLocations().get(index + 1);
		defineEnemy(startLoc);

		path = new ArrayList<Node>();

		setPath(startLoc, index);

	}

	/**
	 * @return {@link #id}
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return {@link #isDead}
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * Kills the enemy
	 */
	public void kill() {
		this.isDead = true;
		path.clear();
	}

	@Override
	public void update(float deltaTime) {
		if (!isDead && canMove) {
			updateAbility();
			updatePathing();
		}
	}

	/**
	 * Handles the special ability behaviour of the enemy
	 */
	private void updateAbility() {
		if (ability != null) {
			Player player = gameScreen.getPlayer();

			if (MathsHelper.distanceBetween(box2dBody.getPosition(),
					new Vector2(player.getX(), player.getY())) <= ability.getRange() / AuberGame.PixelsPerMetre) {
				ability.doAction(this, player);
			}
		}
	}

	/**
	 * Handles the pathing behaviour of the enemy
	 */
	private void updatePathing() {
		checkCurrentPath();

		if (!path.isEmpty()) {
			move(path.get(0));
		} else {
			int nextDest = getNextDestID();

			if (nextDest != -1) {
				setPath(Node.getNearestNode(this.box2dBody.getPosition()), nextDest);
			}
		}
	}

	/**
	 * Checks if the player has blocked the enemy's path
	 */
	private void checkCurrentPath() {
		if (path != null && !path.isEmpty()) {
			if (shouldRedoPath()) {
				setPath(path.get(0), destinationID);
			}
		}
	}

	/**
	 * @return True if the player is in range of the next 3 nodes infront of the
	 *         player, False otherwise
	 */
	private boolean shouldRedoPath() {
		for (int i = 0; i < 3 && i < path.size(); i++) {
			if (Pathfinding.isNodeInRangeOfPlayer(gameScreen.getPlayer(), path.get(0))) {
				return true;
			}

		}

		return false;
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
				canMove = false;

				setNewPath();

				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						canMove = true;
						timer.cancel();
					}
				}, 5000);
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

	/**
	 * Sets a new path when the enemy has reached the interactable
	 */
	private void setNewPath() {
		int startID = destinationID;
		gameScreen.getRooms().breakInteractable(startID);

		List<Integer> operationalIDs = gameScreen.getRooms().getOperationalIDs();

		int nextDest = getNextDestID();

		if (nextDest != -1) {
			setPath(startID, operationalIDs.get(nextDest));
		}
	}

	/**
	 * @return The ID of the closest operable interactable the enemy can reach
	 */
	private int getNextDestID() {
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

		return nextDest;
	}

	/**
	 * Sets a path from a node to an interactable
	 * 
	 * @param fromNode The node the enemy is standing on
	 * @param end      The ID of the interactable the enemy wants to go to
	 */
	private void setPath(Node fromNode, int end) {
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
		fixtureDefinition.filter.categoryBits = WorldContactListener.ENEMY_BIT;
		fixtureDefinition.filter.maskBits = WorldContactListener.DEFAULT_BIT | WorldContactListener.PLAYER_BIT;
		box2dBody.createFixture(fixtureDefinition);

		box2dBody.setUserData(this);
	}

	/**
	 * Teleports the enemy to a specified location
	 * 
	 * @param x
	 * @param y
	 */
	public void teleport(final float x, final float y) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (gameScreen.getWorld().isLocked()) {
				}
				box2dBody.setTransform(x, y, 0);
			}
		}).start();
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

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

}
