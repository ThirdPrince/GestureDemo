package com.android.gesture.app.util;



public class MathUtil {
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return distance(x1, y1, x2, y2, 0);
	}
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param dotRadius 
	 * @return
	 */
	public static double distance(double x1, double y1, double x2, double y2, float dotRadius) {
		return Math.sqrt(Math.abs(x1 - x2 - dotRadius*2) * Math.abs(x1 - x2 - dotRadius*2)
				+ Math.abs(y1 - y2 - dotRadius*2) * Math.abs(y1 - y2) - dotRadius*2);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double pointTotoDegrees(double x, double y) {
		return Math.toDegrees(Math.atan2(x, y));
	}
	
	public static boolean checkInRound(float sx, float sy, float r, float x,
			float y) {
		return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
	}
}