package com.auber.Tools;

import java.math.BigDecimal;

import com.badlogic.gdx.math.Vector2;

public class MathsHelper {

	/**
	 * @param d
	 * @param decimalPlace
	 * @return The number [d] rounded to [decimalPlace] decimal places 
	 */
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
	
	public static float distanceBetween(Vector2 point1, Vector2 point2) {
		float distanceX = point1.x - point2.x;
		float distanceY = point1.y - point2.y;
		
		return (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}
	
}
