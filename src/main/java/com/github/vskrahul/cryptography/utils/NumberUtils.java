package com.github.vskrahul.cryptography.utils;

import static java.lang.Long.parseLong;
import static java.lang.Long.toBinaryString;

public class NumberUtils {

	public static String XOR(String a, String b) {
		return toBinaryString(parseLong(a, 2) ^ parseLong(b, 2));
	}
	
	public static String asciiTextToBinaryString(String asciiText) {
		byte[] bytes = asciiText.getBytes();
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%s%s", leadingZeroes(b), Integer.toBinaryString(b)));
		}
		return sb.toString();
	}
	
	public static String decimalToBinary(int decimal) {
		return leadingZeroes((byte)decimal) + Integer.toBinaryString(decimal);
	}
	
	private static String leadingZeroes(byte b) {
		byte i = (byte) numberOfLeadingZeros(b);
		String s = "";
		for (byte x = 0; x < i; i++) {
			s += "0";
		}
		return s;
	}
	
	public static byte numberOfLeadingZeros(byte i) {
		if(i >>> 1 == 0) return 7;
		if(i >>> 2 == 0) return 6;
		if(i >>> 3 == 0) return 5;
		if(i >>> 4 == 0) return 4;
		if(i >>> 5 == 0) return 3;
		if(i >>> 6 == 0) return 2;
		if(i >>> 7 == 0) return 1;
		return 1;
	}
}
