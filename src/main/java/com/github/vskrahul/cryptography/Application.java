package com.github.vskrahul.cryptography;

import com.github.vskrahul.cryptography.des.core.DES;

public class Application {

	public static void main(String[] args) {

		DES des = new DES();
		String encryption = des.encrypt("password", "password");
		System.out.println(encryption);
	}
}
