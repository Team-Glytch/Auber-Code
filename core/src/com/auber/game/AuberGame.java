package com.auber.game;

import java.util.ArrayList;
import java.util.List;

import com.auber.Entities.Enemy;
import com.auber.Entities.Player;
import com.auber.Entities.abilities.DamageProjectileAbility;
import com.auber.Entities.abilities.InvisibleAbility;
import com.auber.Entities.abilities.SlowAbility;
import com.auber.Entities.abilities.SpecialAbility;
import com.auber.gameplay.GameScreen;
import com.auber.rendering.MenuCamera;
import com.auber.rendering.Renderer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


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
	
	public SpriteBatch batch;
	public MenuCamera cam;

	/**
	 * Initialise world
	 */
	@Override
	public void create() {
		renderer = new Renderer();
		renderer.addTextures("assets/Sprites/");
		renderer.addMaps("assets/Maps/");

		GameScreen mainScreen = new GameScreen("SpaceStation", renderer.getHandler());

		Player player = new Player(mainScreen);
		mainScreen.setFocusedRenderable(player);

		List<Enemy> enemyList = new ArrayList<Enemy>();

		for (int i = 0; i < 8; i++) {
			SpecialAbility ability = null;

			switch (i % 3) {
			case 0:
				ability = new InvisibleAbility();
				break;
			case 1:
				ability = new SlowAbility(mainScreen);
				break;
			case 2:
				ability = new DamageProjectileAbility(mainScreen);
				break;
			default:
				break;
			}
			
			Enemy enemy = new Enemy(mainScreen, ability, i);
			enemyList.add(enemy);
			mainScreen.addRenderable(enemy);
		}

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
