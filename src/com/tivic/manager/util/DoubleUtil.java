package com.tivic.manager.util;

public class DoubleUtil {

	public static String formatPrint(Double value, int decimal) {
		String valueStr = String.valueOf(value);
		String[] partsValueStr = valueStr.split("\\.");
		return partsValueStr[0] + partsValueStr[1].substring(0, (partsValueStr.length - 1 <= decimal ? (partsValueStr.length - 1) : decimal));
	}
	
}
