package com.auber.rendering;

public interface Renderable {

	/**
	 * @return The x coordinate for the texture's location on the screen
	 */
	public float getX();
	/**
	 * @return The y coordinate for the texture's location on the screen
	 */
	public float getY();
	
	/**
	 * @return The width of the texture
	 */
	public int getWidth();
	/**
	 * @return The height of the texture
	 */
	public int getHeight();
	
	/**
	 * @return The name of the texture
	 */
	public String getTextureName();
	
}
