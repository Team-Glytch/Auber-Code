package com.auber.rendering;

import com.auber.Tools.Box2DWorldCreator;
import com.auber.game.AuberGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Renderer {

	/**
	 * The tool used to render the sprites
	 */
	private SpriteBatch batch;
	/**
	 * The asset handler for the game
	 */
	private AssetHandler handler;

	/**
	 * The renderer of the maps
	 */
	private OrthogonalTiledMapRenderer mapRenderer;

	/**
	 * The current screen of the game
	 */
	private GameScreen currentScreen;

	/**
	 * Sets up the renderer
	 * 
	 * @param texturePath The path to the directory where the textures are stored
	 */
	public Renderer() {
		batch = new SpriteBatch();
		handler = new AssetHandler();
		mapRenderer = new OrthogonalTiledMapRenderer(null, 1 / AuberGame.PixelsPerMetre);
	}

	public AssetHandler getHandler() {
		return handler;
	}
	
	/**
	 * @return {@link #currentScreen}
	 */
	public GameScreen getCurrentScreen() {
		return currentScreen;
	}

	/**
	 * Adds the maps to the render
	 * 
	 * @param directoryPath The directory where the maps are stored
	 */
	public void addMaps(String directoryPath) {
		handler.loadAllMaps(directoryPath);
	}

	/**
	 * Loads all of the textures from the directory specified
	 * 
	 * @param directoryPath The path to the directory containing the textures
	 */
	public void addTextures(String directoryPath) {
		handler.loadAllTextures(directoryPath);
	}

	/**
	 * Renders the objects
	 * 
	 * @param batch The batch that draws the object to the screen
	 */
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		mapRenderer.setView(currentScreen.getCamera());
		mapRenderer.render();

		currentScreen.updateCamera();

		for (Renderable renderable : currentScreen.getRenderables()) {
			float x = renderable.getX();
			float y = renderable.getY();
			float width = renderable.getWidth();
			float height = renderable.getHeight();

			TextureRegion texture = handler.getTexture(renderable.getTextureName());
			
			if (!renderable.isMovingRight()) {
				texture.flip(true, false);
			}
			
			batch.draw(texture, x - (width / 2), y - (height / 2), width,
					height);
			
			if (!renderable.isMovingRight()) {
				texture.flip(true, false);
			}
		}
		batch.setProjectionMatrix(currentScreen.getCamera().combined);
		batch.end();
	}

	/**
	 * Sets the current screen
	 * 
	 * @param screen
	 */
	public void setScreen(GameScreen screen) {
		this.currentScreen = screen;
		TiledMap map = handler.getMap(screen.getMapName());
		
		mapRenderer.setMap(map);
		new Box2DWorldCreator(screen.getWorld(), map);
	}

	/**
	 * Disposes of the renderer's tools
	 */
	public void dispose() {
		currentScreen.dispose();
		mapRenderer.dispose();
		batch.dispose();
	}

}
