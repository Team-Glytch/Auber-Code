package com.auber.game;

import com.auber.entities.Enemy;
import com.auber.entities.Player;
import com.auber.rendering.GameScreen;
import com.auber.rendering.Renderer;
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

		GameScreen mainScreen = new GameScreen("SpaceStation", renderer.getHandler());

		Enemy enemy = new Enemy(mainScreen);
		mainScreen.addRenderable(enemy);

		Player player = new Player(mainScreen.getWorld());
		mainScreen.setFocusedRenderable(player);
		renderer.setScreen(mainScreen);
	}

	/**
	 * Updates screen with movement
	 */
	@Override
	public void render() {
		if (this.getScreen() == null || renderer.getCurrentScreen().equals(this.getScreen())) {
			setScreen(renderer.getCurrentScreen());
		}

		renderer.getCurrentScreen().update(Gdx.graphics.getDeltaTime());

		renderer.getCurrentScreen().getWorld().step(1 / 60f, 6, 2);

		renderer.render();
	}

	/**
	 * Cleaning up
	 */
	@Override
	public void dispose() {
		renderer.dispose();
	}
}
