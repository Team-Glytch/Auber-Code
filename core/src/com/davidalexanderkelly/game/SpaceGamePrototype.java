package com.davidalexanderkelly.game;



import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.davidalexanderkelly.game.Screens.PlayScreen;

public class SpaceGamePrototype extends Game {
	
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 720;
	public static final float PixelsPerMetre = 100;
	
	public SpriteBatch batch;
	
	
	@Override
	public void create () {
		// Sets level
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void dispose() {
		//Clean up
		super.dispose();
		batch.dispose();
	}
	
	@Override
	public void render () {
		super.render();
		
	}
	
}
