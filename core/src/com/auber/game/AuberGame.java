package com.auber.game;

import com.auber.rendering.GameScreen;
import com.auber.rendering.Renderer;
import com.auber.tmp.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class AuberGame extends Game {
	/**
	 * The renderer of the game
	 */
	private Renderer renderer;

	/*
	 * Used mainly for testing, will be cleaned up
	 */
	Player player;
	World world;

	@Override
	public void create() {
		world = new World(new Vector2(0, 0), true);
		this.player = new Player(world);

		renderer = new Renderer();
		renderer.addTextures("assets/Sprites/");
		renderer.addMaps("assets/Maps/");

		GameScreen mainScreen = new GameScreen("SpaceShip");
		mainScreen.setFocusedRenderable(this.player);
		renderer.setScreen(mainScreen);
	}

	@Override
	public void render() {
		if (this.getScreen() == null || renderer.getCurrentScreen().equals(this.getScreen())) {
			setScreen(renderer.getCurrentScreen());
		}

		handleKeys();
		world.step(1 / 60f, 6, 2);

		renderer.render();
	}

	@Override
	public void dispose() {
		world.dispose();
		renderer.dispose();
	}

	private void handleKeys() {
		boolean moveUp = (Gdx.input.isKeyPressed(Input.Keys.UP));
		boolean moveDown = (Gdx.input.isKeyPressed(Input.Keys.DOWN));
		boolean moveRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT));
		boolean moveLeft = (Gdx.input.isKeyPressed(Input.Keys.LEFT));

		float velX = 0;
		float velY = 0;

		float speed = 300;

		if (moveUp) {
			velY += speed;
		}

		if (moveDown) {
			velY -= speed;
		}

		if (moveLeft) {
			velX -= speed;
		}

		if (moveRight) {
			velX += speed;
		}

		player.box2dBody.setLinearVelocity(velX, velY);

	}

}
