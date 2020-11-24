package com.auber.game;

import com.auber.rendering.Renderer;
import com.auber.screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class AuberGame extends Game {
	/**
	 * The renderer of the game
	 */
	private Renderer renderer;

	/**
	 * How many pixels there are per metre
	 */
	public static final float PixelsPerMetre = 100;
	/**
	 * Width of the screen
	 */
	public static final float V_WIDTH = 720;
	/**
	 * Height of the screen
	 */
	public static final float V_HEIGHT = 720;

	/**
	 * Initialise world
	 */
	@Override
	public void create() {
		renderer = new Renderer();
		renderer.addTextures("assets/Sprites/");
		renderer.addMaps("assets/Maps/");

		setScreen(new MainMenuScreen(renderer));
	}

	/**
	 * Updates screen with movement
	 */
	@Override
	public void render() {
		if (renderer.getCurrentScreen() != null) {
			if (this.getScreen() == null || !renderer.getCurrentScreen().equals(this.getScreen())) {
				setScreen(renderer.getCurrentScreen());
			}

			renderer.getCurrentScreen().update(Gdx.graphics.getDeltaTime());

			renderer.getCurrentScreen().getWorld().step(1 / 60f, 6, 2);

			renderer.render();
		} else {
			super.render();
		}
	}

	/**
	 * Cleaning up
	 */
	@Override
	public void dispose() {
		renderer.dispose();
	}

}
