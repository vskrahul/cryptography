package com.github.vskrahul.cryptography.utils;

import static java.lang.Long.*;
import static java.lang.Long.toBinaryString;

public class NumberUtils {

	public static String XOR(String a, String b) {
		String xor = toBinaryString(parseUnsignedLong(a, 2) ^ parseUnsignedLong(b, 2));
		return addZeroes(xor, a.length() - xor.length());
	}
	
	public static String asciiTextToBinaryString(String asciiText) {
		byte[] bytes = asciiText.getBytes();
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%s%s", leadingZeroes(b), Integer.toBinaryString(b)));
		}
		return sb.toString();
	}
	
	public static String decimalToXbits(int decimal, int x) {
		if(decimal > Math.pow(2, x) - 1)
			throw new RuntimeException(String.format("%d can not be fit in %d bits", decimal, x));
		String bits = Integer.toBinaryString(decimal);
		return addZeroes(bits, x - bits.length());
	}
	
	private static String addZeroes(String s, int z) {
		for(int i = 0; i < z; i++)
			s = "0" + s;
		return s;
	}
	
	private static String leadingZeroes(byte b) {
		byte i = (byte) numberOfLeadingZeros(b);
		String s = "";
		for (byte x = 0; x < i; x++) {
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
