package com.auber.rendering;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

	/**
	 * The name of the map
	 */
	private String mapName;

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

	/**
	 * The objects that are needed to be rendered
	 */
	private List<Renderable> renderables;

	public GameScreen(String mapName) {
		this.mapName = mapName;
		this.renderables = new ArrayList<Renderable>();
		camera = setupCamera();

		this.focusedRenderableIndex = -1;
	}

	/**
	 * @return The camera for the map
	 */
	private OrthographicCamera setupCamera() {
		OrthographicCamera camera = new OrthographicCamera();
		gamePort = new FitViewport(720, 720, camera);

		camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		camera.zoom = 0.225f;

		return camera;
	}
	
	/**
	 * @return {@link #camera}
	 */
	public OrthographicCamera getCamera() {
		return camera;
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
	 * @return {@link #mapName}
	 */
	public String getMapName() {
		return mapName;
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

	}

}