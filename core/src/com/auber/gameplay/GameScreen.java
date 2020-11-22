package com.auber.gameplay;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.Enemy;
import com.auber.game.AuberGame;
import com.auber.rendering.AssetHandler;
import com.auber.rendering.Renderable;
import com.auber.tools.InteractableWorldCreator;
import com.auber.tools.PathfindingWorldCreator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
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
	 * The index of the renderable object that the camera is centred on
	 */
	private int focusedRenderableIndex;

	private PathfindingWorldCreator pathfinder;
	private InteractableWorldCreator interactables;

	private Rooms rooms;

	public Rooms getRooms() {
		return rooms;
	}

	/**
	 * The objects that are needed to be rendered
	 */
	private List<Renderable> renderables;

	public GameScreen(String mapName, AssetHandler handler) {
		this.mapName = mapName;
		this.renderables = new ArrayList<Renderable>();

		this.world = new World(new Vector2(0, 0), true);
		this.world.setContactListener(new EnemyContactListener(this));

		camera = setupCamera();

		this.focusedRenderableIndex = -1;

		TiledMap map = handler.getMap(mapName);

		interactables = new InteractableWorldCreator(map);

		rooms = new Rooms(interactables.getLocations().size());

		pathfinder = new PathfindingWorldCreator(map);
	}

	public boolean containsEnemies() {
		for (Renderable r : renderables) {
			if (r instanceof Enemy) {
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
		gamePort = new FitViewport(AuberGame.V_WIDTH / AuberGame.PixelsPerMetre,
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
		for (Renderable renderable : renderables) {
			renderable.update(deltaTime);
		}

		if (!containsEnemies()) {
			System.out.println("YOU WIN");
			Gdx.app.exit();
		}

		if (rooms.getOperationalIDs().size() == 0) {
			System.out.println("GAME OVER");
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
			this.focusedRenderableIndex = this.renderables.size() - 1;
		} else {
			this.focusedRenderableIndex = this.renderables.indexOf(renderable);
		}
	}

	/**
	 * Adds a renderable object to the map
	 * 
	 * @param renderable
	 */
	public void addRenderable(Renderable renderable) {
		this.renderables.add(renderable);
	}

	/**
	 * @return {@link #renderables}
	 */
	public List<Renderable> getRenderables() {
		return renderables;
	}

	/**
	 * @return The interactable objects in the screen's world
	 */
	public InteractableWorldCreator getInteractables() {
		return interactables;
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

	public World getWorld() {
		return world;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
	}

	/**
	 * Updates the camera
	 */
	public void updateCamera() {
		if (focusedRenderableIndex != -1) {
			Renderable focusedRenderable = this.renderables.get(focusedRenderableIndex);

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

			if (focusedRenderableIndex == index) {
				focusedRenderableIndex = -1;
			} else if (focusedRenderableIndex > index) {
				focusedRenderableIndex -= 1;
			}
		}
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
