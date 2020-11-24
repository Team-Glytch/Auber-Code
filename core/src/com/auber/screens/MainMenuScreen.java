package com.auber.screens;

import java.util.ArrayList;
import java.util.List;

import com.auber.entities.Enemy;
import com.auber.entities.Player;
import com.auber.entities.abilities.DamageProjectileAbility;
import com.auber.entities.abilities.InvisibleAbility;
import com.auber.entities.abilities.SlowAbility;
import com.auber.entities.abilities.SpecialAbility;
import com.auber.rendering.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenuScreen implements Screen {

	/*
	 * Initialise menu stage where widgets wil go
	 */
	private Stage stage;

	/*
	 * Initialise skin which will map textures to widgets
	 */
	private Skin skin;

	/*
	 * Initialise table where widgets will be organised
	 */
	private Table table;

	/*
	 * Initialise menu buttons
	 */
	private Button buttonStart, buttonExit;

	/*
	 * reference to game
	 */
	private Renderer renderer;

	public MainMenuScreen(Renderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void show() {
		stage = new Stage();

		// look for inputs on stage
		Gdx.input.setInputProcessor(stage);

		// load skin for use in table
		table = new Table();
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Initialise button styler
		TextButtonStyle textButton = new TextButtonStyle();

		// Start button styling
		textButton.up = new TextureRegionDrawable(renderer.getHandler().getTexture("StartButton"));
		textButton.pressedOffsetX = 1;
		textButton.pressedOffsetX = -1;
		buttonStart = new Button(textButton);
		buttonStart.padTop(20);

		// Start button click event
		buttonStart.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
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
		});
		buttonStart.padTop(20);

		// Exit button styling
		textButton = new TextButtonStyle();
		textButton.up = new TextureRegionDrawable(renderer.getHandler().getTexture("ExitButton"));
		textButton.pressedOffsetX = 1;
		textButton.pressedOffsetX = -1;
		buttonExit = new Button(textButton);

		// Exit button click event
		buttonExit.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		buttonExit.padTop(20);

		// Add buttons to table
		table.add(buttonStart);
		table.row();
		table.add(buttonExit);
		table.debug();
		stage.addActor(table);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);

		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
