package com.auber.tools;

import java.math.BigDecimal;

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
	
}
