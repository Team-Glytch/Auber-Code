package com.auber.game;

import com.auber.rendering.Renderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AuberGame extends ApplicationAdapter {
	SpriteBatch batch;
	Renderer renderer;
	
	@Override
	public void create() {
		renderer = new Renderer("assets/Sprites/");

		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		renderer.render(batch);

		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
	
}
