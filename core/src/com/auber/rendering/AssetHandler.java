package com.auber.rendering;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetHandler {

	/**
	 * The amount of time each frame has in an animation, in milliseconds
	 */
	private static final long frameDurationMs = 125l;

	/**
	 * The storage of the textures that can be retrieved using the texture name
	 * 
	 * The textures are stored as a group of animation frames in an array
	 */
	private Map<String, TextureRegion[]> textureMap;

	/**
	 * The storage of the maps in the games that can be retrieved using the map's
	 * name
	 */
	private Map<String, TiledMap> mapMap;

	/**
	 * Sets up the asset handler
	 * 
	 * @param textureLocation The path to where the textures are being stored
	 */
	public AssetHandler() {
		textureMap = new HashMap<String, TextureRegion[]>();
		mapMap = new HashMap<String, TiledMap>();
	}

	/**
	 * Loads all the maps of the map
	 * 
	 * @param mapsLocation
	 */
	public void loadAllMaps(String mapsLocation) {
		FileHandle handle = Gdx.files.internal(mapsLocation);

		TmxMapLoader mapLoader = new TmxMapLoader();

		for (FileHandle child : handle.list()) {
			if (child.path().contains(".tmx")) {
				mapMap.put(child.name().replace(".tmx", ""), mapLoader.load(child.path()));
			}
		}

	}

	/**
	 * Loads all of the textures in the textureLocation
	 * 
	 * @param texturesLocation The file path of the directory where the textures are
	 *                         stored
	 */
	public void loadAllTextures(String texturesLocation) {
		FileHandle handle = Gdx.files.internal(texturesLocation);

		FileHandle[] directory = handle.list();

		for (FileHandle child : directory) {
			if (child.path().contains(".png")) {
				textureMap.put(child.name().replace(".png", ""), loadTexture(child));
			}
		}

	}

	/**
	 * Loads an animation texture into an array so it can be stored into
	 * {@link #textureMap}
	 * 
	 * @param filePath The path of the file that contains the animation sequence
	 * @return An array containing the singular frames of an animation
	 */
	private TextureRegion[] loadTexture(FileHandle file) {
		Texture texture = new Texture(file);

		TextureRegion region = new TextureRegion(texture);

		int amountOfAnimationFrames = getAmountOfFrames(file);

		if (amountOfAnimationFrames == 0) {
			amountOfAnimationFrames = 1;
		}

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
	private int getAmountOfFrames(FileHandle handle) {
		int frameAmount = 0;

		Pixmap map = new Pixmap(handle);

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
	 * Gets a texture frame of the animation specified by textureName. The exact
	 * animation frame is selected automatically
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

	/**
	 * Gets a map specified by mapName
	 * 
	 * @param mapName The name of the desired map
	 * @return The map specified by mapName
	 */
	public TiledMap getMap(String mapName) {
		return mapMap.get(mapName);
	}


}
