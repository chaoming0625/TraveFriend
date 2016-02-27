/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gotraveling.insthub.gps.utils;

/**
 *
 * @author ChaoMing
 */
public class GpsMath {
	public static double NMEA_PI = (3.141592653589793);
	/** < PI value */
	public static double NMEA_PI180 = (NMEA_PI / 180);
	/** < PI division by 180 */
	public static int NMEA_EARTHRADIUS_KM = (6378);
	/** < Earth's mean radius in km */
	public static int NMEA_EARTHRADIUS_M = (NMEA_EARTHRADIUS_KM * 1000);
	/** < Earth's mean radius in m */
	public static double NMEA_EARTH_SEMIMAJORAXIS_M = (6378137.0);
	/** < Earth's semi-major axis in m according WGS84 */
	public static double NMEA_EARTH_FLATTENING = (1 / 298.257223563);
	/** < Earth's flattening according WGS 84 */
	public static int NMEA_DOP_FACTOR = (5);

	/** < Factor for translating DOP to meters */

	/**
	 * Convert NDEG (NMEA degree) to fractional degree,+/-[degree].[min+sec]/60]
	 */
	public static double ndeg2degree(double val) {
		double deg = ((int) (val / 100));
		val = deg + (val - deg * 100) / 60;
		return val;
	}

	/**
	 * Convert NMEA (NMEA degree) to fractional degree,+/-[degree].[min+sec]/60]
	 */
	public static double degree2ndeg(double val) {
		double int_part;
		double fra_part;
		int_part = Math.floor(val);
		fra_part = val - int_part;
		val = int_part * 100 + fra_part * 60;
		return val;
	}

	public static double degree2radian(double val) {
		return val * NMEA_PI180;
	}

	public static double ndeg2radian(double val) {
		return ndeg2degree(val) * NMEA_PI180;
	}

}
