package com.auber.gameplay;

import java.util.ArrayList;
import java.util.List;

import com.auber.Entities.Enemy;
import com.auber.Entities.Player;
import com.auber.game.AuberGame;
import com.auber.rendering.AssetHandler;
import com.auber.rendering.Renderable;
import com.auber.tiles.InteractableHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

	/**
	 * The name of the map
	 */
	private String mapName;

	/**
	 * The world of the map
	 */
	private World world;

	/**
	 * The camera in the map
	 */
	private OrthographicCamera camera;

	/**
	 * THe viewport of the map
	 */
	private Viewport gamePort;

	/**
	 * The index of the player in {@link #renderables}
	 */
	private int playerIndex;

	/**
	 * The pathfinder of the screen
	 */
	private PathfindingWorldCreator pathfinder;
	
	/**
	 * The interactable tiles in the screen
	 */
	private InteractableHandler interactables;

	/**
	 * The various rooms of the screen
	 */
	private Rooms rooms;

	/**
	 * The objects that are needed to be rendered
	 */
	private List<Renderable> renderables;

	private List<Renderable> renderableBuffer;

	public GameScreen(String mapName, AssetHandler handler) {
		this.mapName = mapName;
		this.renderables = new ArrayList<Renderable>();
		this.renderableBuffer = new ArrayList<Renderable>();

		this.world = new World(new Vector2(0, 0), true);
		this.world.setContactListener(new WorldContactListener(this));

		camera = setupCamera();

		this.playerIndex = -1;

		TiledMap map = handler.getMap(mapName);

		interactables = new InteractableHandler(map, world);

		rooms = new Rooms(interactables.getLocations().size());

		pathfinder = new PathfindingWorldCreator(map);
	}

	private boolean areEnemiesAlive() {
		for (Renderable r : renderables) {
			if (r instanceof Enemy && !((Enemy) r).isDead()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return The camera for the map
	 */
	private OrthographicCamera setupCamera() {
		OrthographicCamera camera = new OrthographicCamera();
		gamePort = new ExtendViewport(AuberGame.V_WIDTH / AuberGame.PixelsPerMetre,
				AuberGame.V_HEIGHT / AuberGame.PixelsPerMetre, camera);

		camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		camera.zoom = 0.2f;

		return camera;
	}

	/**
	 * @return {@link #camera}
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}

	public void update(float deltaTime) {
		renderables.addAll(renderableBuffer);
		renderableBuffer.clear();
		
		for (Renderable renderable : renderables) {
			renderable.update(deltaTime);
		}

		if (!areEnemiesAlive()) {
			System.out.println("YOU WIN");
			Gdx.app.exit();
		}

		if (rooms.getOperationalIDs().size() == 0 || getPlayer().getHealth() <= 0f) {
			System.out.println("YOU LOSE");
			Gdx.app.exit();
		}
	}

	/**
	 * Sets the renderable object that the camera is centred on
	 * 
	 * @param renderable
	 */
	public void setFocusedRenderable(Renderable renderable) {
		if (!this.renderables.contains(renderable)) {
			this.renderables.add(renderable);
			this.playerIndex = this.renderables.size() - 1;
		} else {
			this.playerIndex = this.renderables.indexOf(renderable);
		}
	}

	/**
	 * Adds a renderable object to the map
	 * 
	 * @param renderable
	 */
	public void addRenderable(Renderable renderable) {
		this.renderableBuffer.add(renderable);
	}

	/**
	 * @return {@link #renderables}
	 */
	public List<Renderable> getRenderables() {
		return renderables;
	}

	/**
	 * @return {@link #interactables}
	 */
	public InteractableHandler getInteractables() {
		return interactables;
	}

	/**
	 * @return {@link #rooms}
	 */
	public Rooms getRooms() {
		return rooms;
	}

	/**
	 * @return The player in the screen
	 */
	public Player getPlayer() {
		if (playerIndex == -1) {
			return null;
		}

		return (Player) renderables.get(playerIndex);
	}

	/**
	 * @return The pathfinder of the screen
	 */
	public PathfindingWorldCreator getPathfinder() {
		return pathfinder;
	}

	/**
	 * @return {@link #mapName}
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * @return {@link #world}
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Updates the camera
	 */
	public void updateCamera() {
		if (playerIndex != -1) {
			Renderable focusedRenderable = this.renderables.get(playerIndex);

			camera.position.x = focusedRenderable.getX();
			camera.position.y = focusedRenderable.getY();
			camera.update();
		}
	}

	/**
	 * Removes a renderable from the game
	 * 
	 * @param renderable
	 */
	public void removeRenderable(Renderable renderable) {
		int index = renderables.indexOf(renderable);

		if (index != -1) {
			renderables.remove(index);

			if (playerIndex == index) {
				playerIndex = -1;
			} else if (playerIndex > index) {
				playerIndex -= 1;
			}
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		this.world.dispose();
	}

}
