package com.auber.rendering;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetHandler {

	/**
	 * The amount of time each frame has in an animation, in milliseconds
	 */
	private final long frameDurationMs = 125l;

	/**
	 * The storage of the textures that can be retrieved using the texture name
	 * 
	 * The textures are stored as a group of animation frames in an array
	 */
	private Map<String, TextureRegion[]> textureMap;

	/**
	 * Sets up the asset handler
	 * @param textureLocation The path to where the textures are being stored
	 */
	public AssetHandler(String textureLocation) {
		textureMap = new HashMap<String, TextureRegion[]>();

		loadAllTextures(textureLocation);
	}

	/**
	 * Loads all of the textures in the textureLocation
	 * 
	 * @param textureLocation The file path of the directory where the textures are stored
	 */
	private void loadAllTextures(String textureLocation) {
		File file = new File(textureLocation);
		
		for (String childPath : file.list()) {
			textureMap.put(childPath.replace(".png", ""), loadTexture(textureLocation + childPath));
		}
		
	}
	
	/**
	 * Loads an animation texture into an array so it can be stored into {@link #textureMap}
	 * 
	 * @param filePath The path of the file that contains the animation sequence
	 * @return An array containing the singular frames of an animation
	 */
	private TextureRegion[] loadTexture(String filePath) {
		Texture texture = new Texture(Gdx.files.internal(filePath));

		TextureRegion region = new TextureRegion(texture);

		int amountOfAnimationFrames = getAmountOfFrames(filePath);

		TextureRegion[] animationTextures = region.split(region.getRegionWidth() / amountOfAnimationFrames,
				region.getRegionHeight())[0];

		return animationTextures;
	}

	/**
	 * Gets the amount of frames there are in the animation
	 * 
	 * @param filePath The location of the file containing the animation sequence
	 * @return The amount of frames the animation consists of
	 */
	private int getAmountOfFrames(String filePath) {
		int frameAmount = 0;

		Pixmap map = new Pixmap(Gdx.files.internal(filePath));

		boolean wasLastBlankLine = true;

		for (int x = 0; x < map.getWidth(); x++) {
			boolean isLineBlank = true;

			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getPixel(x, y) != 0) {
					isLineBlank = false;
					wasLastBlankLine = false;
					break;
				}
			}

			if (isLineBlank && !wasLastBlankLine) {
				frameAmount += 1;
				wasLastBlankLine = true;
			}
		}

		return frameAmount;
	}

	/**
	 * Gets a texture frame of the animation specified by textureName
	 * The exact animation frame is selected automatically
	 * 
	 * @param textureName The name of the texture desired
	 * @return The texture specified by textureName
	 */
	public TextureRegion getTexture(String textureName) {
		TextureRegion[] textureRegions = textureMap.get(textureName);

		int framePtr = (int) ((System.currentTimeMillis() % (frameDurationMs * textureRegions.length))
				/ frameDurationMs);

		return textureRegions[framePtr];
	}

}
