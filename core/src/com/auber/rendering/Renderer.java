package com.auber.rendering;

import com.auber.game.AuberGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

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
		setupCollisionBoxes(screen.getWorld(), map);
	}

	/**
	 * Sets up the collision boxes of the world
	 * @param world
	 * @param map
	 */
	private void setupCollisionBoxes(World world, TiledMap map) {
		BodyDef bodyDefinition = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDefinition = new FixtureDef();
		Body body;

		for (MapObject object : map.getLayers().get("Collision").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bodyDefinition.type = BodyDef.BodyType.StaticBody;
			bodyDefinition.position.set((rect.getX() + rect.getWidth() / 2) / AuberGame.PixelsPerMetre,
					(rect.getY() + rect.getHeight() / 2) / AuberGame.PixelsPerMetre);

			body = world.createBody(bodyDefinition);
			shape.setAsBox(rect.getWidth() / 2 / AuberGame.PixelsPerMetre,
					rect.getHeight() / 2 / AuberGame.PixelsPerMetre);
			fixtureDefinition.shape = shape;
			body.createFixture(fixtureDefinition);
		}
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
