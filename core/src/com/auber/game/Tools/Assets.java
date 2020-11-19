package com.auber.game.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	public AssetManager manager;
	
	public void load() {
		if(manager == null) {
			manager = new AssetManager();
		}
		manager.load("assets/Sprites/Spritepack.atlas", TextureAtlas.class);
		manager.load("assets/Sounds/stepSound.wav",  Sound.class);
	}
	
}
