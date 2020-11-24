package com.auber.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.auber.game.AuberGame;


public class MainMenuScreen implements Screen {
	
	private static final int EXIT_BUTTON_WIDTH = 250;
	private static final int EXIT_BUTTON_HEIGHT = 120;
	private static final int PLAY_BUTTON_WIDTH = 300;
	private static final int PLAY_BUTTON_HEIGHT = 120;
	private static final int EXIT_BUTTON_Y = 100;
	private static final int PLAY_BUTTON_Y = 230;
	private static final int LOGO_WIDTH = 400;
	private static final int LOGO_HEIGHT = 250;
	private static final int LOGO_Y = 450;
	
	final AuberGame game;
	
	Texture playButtonActive;
	Texture playButtonInactive;
	Texture exitButtonActive;
	Texture exitButtonInactive;
	Texture logo;
	
	public MainMenuScreen (final AuberGame game) {
		this.game = game;
		playButtonActive = new Texture("assets/Images/play_button_active.png");
		playButtonInactive = new Texture("assets/Images/play_button_inactive.png");
		exitButtonActive = new Texture("assets/Images/exit_button_active.png");
		exitButtonInactive = new Texture("assets/Images/exit_button_inactive.png");
		logo = new Texture("assets/Images/logo.png");
				
		final MainMenuScreen mainMenuScreen = this;
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				System.out.println(game.cam.getInputInGameWorld().x);
				
				//Exit button
				float x = AuberGame.V_WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
				if (game.cam.getInputInGameWorld().x < (x*1.15) + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > (x*1.15) && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y < (EXIT_BUTTON_Y*1.75) + EXIT_BUTTON_HEIGHT && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y > (EXIT_BUTTON_Y*1.75)) {
					mainMenuScreen.dispose();
					Gdx.app.exit();
				}
				
				//Play game button
				x = AuberGame.V_WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
				if (game.cam.getInputInGameWorld().x < (x*1.15) + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > (x*1.15) && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y < (PLAY_BUTTON_Y*1.75) + PLAY_BUTTON_HEIGHT && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y > (PLAY_BUTTON_Y*1.75)) {
					mainMenuScreen.dispose();
					
				}
				
				return super.touchUp(screenX, screenY, pointer, button);
			}
			
		});
	}
	
	@Override
	public void show () {
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		
	
		float x = AuberGame.V_WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
		if (game.cam.getInputInGameWorld().x < (x*1.15) + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > (x*1.15) && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y < (EXIT_BUTTON_Y*1.75) + EXIT_BUTTON_HEIGHT && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y > (EXIT_BUTTON_Y*1.75)) {
			game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		} else {
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		}
		
		x = AuberGame.V_WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
		if (game.cam.getInputInGameWorld().x < (x*1.15) + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > (x*1.15) && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y < (PLAY_BUTTON_Y*1.75) + PLAY_BUTTON_HEIGHT && AuberGame.V_HEIGHT - game.cam.getInputInGameWorld().y > (PLAY_BUTTON_Y*1.75)) {
			game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		} else {
			game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		}
		
		game.batch.draw(logo, AuberGame.V_WIDTH / 2 - LOGO_WIDTH / 2 , LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);
		
		game.batch.end();
	}

	@Override
	public void resize (int V_WIDTH, int V_HEIGHT) {
		
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

}
