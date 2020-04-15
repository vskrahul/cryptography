package com.github.vskrahul.cryptography;

import static com.github.vskrahul.cryptography.utils.NumberUtils.bitsToBytes;

import com.github.vskrahul.cryptography.des.core.DES;

public class Application {

	public static void main(String[] args) {

		DES des = new DES();
		String encryption = des.encrypt("I am a Java Programmer learning Cryptography, How are you doing today", "password");
		String plainBits = des.decrypt(encryption, "password");
		System.out.println(new String(bitsToBytes(plainBits)).trim());
	}
}