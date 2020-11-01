package com.auber.rendering;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

public class Renderer {

	/**
	 * The asset handler for the game
	 */
	private AssetHandler handler;

	/**
	 * The objects that are needed to be rendered 
	 */
	private List<Renderable> renderables = new ArrayList<Renderable>();

	/**
	 * Sets up the renderer
	 * 
	 * @param texturePath The path to the directory where the textures are stored
	 */
	public Renderer(String texturePath) {
		handler = new AssetHandler(texturePath);
	}

	/**
	 * Renders the objects
	 * 
	 * @param batch The batch that draws the object to the screen
	 */
	public void render(Batch batch) {
		for (Renderable renderable : renderables) {
			batch.draw(handler.getTexture(renderable.getTextureName()), renderable.getX(), renderable.getY(),
					renderable.getWidth(), renderable.getHeight());
		}
	}

	/**
	 * Adds an object to be rendered
	 * 
	 * @param renderable
	 */
	public void add(Renderable renderable) {
		renderables.add(renderable);
	}

}
