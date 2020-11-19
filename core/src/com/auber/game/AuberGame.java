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

	public static final float PixelsPerMetre = 100;
	public static final float V_WIDTH = 720;
	public static final float V_HEIGHT = 720;

	/*
	 * Used mainly for testing, will be cleaned up
	 */
	Player player;
	World world;

	/**
	 * Initialise world
	 */
	@Override
	public void create() {
		world = new World(new Vector2(0, 0), true);
		this.player = new Player(world);

		renderer = new Renderer();
		renderer.addTextures("assets/Sprites/");
		renderer.addMaps("assets/Maps/");

		GameScreen mainScreen = new GameScreen("SpaceStation", world);
		mainScreen.setFocusedRenderable(this.player);
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

		handleKeys();
		world.step(1 / 60f, 6, 2);

		renderer.render();
	}

	/**
	 * Key mapping and speed changes
	 */
	private void handleKeys() {
		boolean moveUp = (Gdx.input.isKeyPressed(Input.Keys.UP));
		boolean moveDown = (Gdx.input.isKeyPressed(Input.Keys.DOWN));
		boolean moveRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT));
		boolean moveLeft = (Gdx.input.isKeyPressed(Input.Keys.LEFT));
		
		if (moveUp && player.box2dBody.getLinearVelocity().y < 0.7f && !moveDown) {
			player.box2dBody.applyLinearImpulse(new Vector2(0f, 0.15f), player.box2dBody.getWorldCenter(), true);
		} else if (moveDown && player.box2dBody.getLinearVelocity().y > -0.7f && !moveUp) {
			player.box2dBody.applyLinearImpulse(new Vector2(0f, -0.15f), player.box2dBody.getWorldCenter(), true);
		} else if (moveUp == moveDown) {
			player.box2dBody.setLinearVelocity(player.box2dBody.getLinearVelocity().x, 0f);
		}

		if (moveRight && player.box2dBody.getLinearVelocity().x < 0.7f && !moveLeft) {
			player.box2dBody.applyLinearImpulse(new Vector2(0.15f, 0f), player.box2dBody.getWorldCenter(), true);
		} else if (moveLeft && player.box2dBody.getLinearVelocity().x > -0.7f && !moveRight) {
			player.box2dBody.applyLinearImpulse(new Vector2(-0.15f, 0f), player.box2dBody.getWorldCenter(), true);
		} else if (moveRight == moveLeft) {
			player.box2dBody.setLinearVelocity(0f, player.box2dBody.getLinearVelocity().y);
		}

	}

	/**
	 * Cleaning up
	 */
	@Override
	public void dispose() {
		world.dispose();
		renderer.dispose();
	}
}
