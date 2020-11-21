package com.auber.tools;

public class MathsHelper {

	/**
	 * @param d
	 * @param decimalPlace
	 * @return The number [d] rounded to [decimalPlace] decimal places 
	 */
	public static float round(float d, int decimalPlace) {
		float multiplier = (float) Math.pow(10, decimalPlace) - 1;
		
		return (float) (Math.floor(d * multiplier) / multiplier);
	}
	
}
