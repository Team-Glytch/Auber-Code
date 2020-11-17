package com.davidalexanderkelly.game.Scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable{
	public Stage stage;
	private Viewport viewport;
	
	public Hud(SpriteBatch sb) {
		
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
	
	
}
