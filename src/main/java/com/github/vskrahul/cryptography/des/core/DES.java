/**
 * 
 */
package com.github.vskrahul.cryptography.des.core;

import static com.github.vskrahul.cryptography.des.core.PermutationArrays.EXPANSION;
import static com.github.vskrahul.cryptography.des.core.PermutationArrays.FINAL_PERMUTATION;
import static com.github.vskrahul.cryptography.des.core.PermutationArrays.INITIAL_PERMUATIONS;
import static com.github.vskrahul.cryptography.des.core.PermutationArrays.PERMUTATION;
import static com.github.vskrahul.cryptography.des.core.PermutationArrays.PERMUTED_CHOICE_1;
import static com.github.vskrahul.cryptography.des.core.PermutationArrays.PERMUTED_CHOICE_2;
import static com.github.vskrahul.cryptography.des.core.PermutationArrays.SBOX;
import static com.github.vskrahul.cryptography.des.core.PermutationArrays.SHIFT_BITS;
import static com.github.vskrahul.cryptography.utils.NumberUtils.XOR;
import static com.github.vskrahul.cryptography.utils.NumberUtils.asciiTextToBinaryString;
import static com.github.vskrahul.cryptography.utils.NumberUtils.decimalToXbits;

/**
 * @author Rahul Vishvakarma
 *
 */
public class DES {

	public String permutation(int[] TABLE, String input) {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < TABLE.length; i++) {
			sb.append(input.charAt(TABLE[i] - 1));
		}
		return sb.toString();
	}
	
	public String f(String right32Bits, String keyBits) {
		
		//Expansion
		String expansion = permutation(EXPANSION, right32Bits);
		
		//XOR
		String xor = XOR(expansion, keyBits);
		
		//S-Box Confusion
		String sBox = sbox(xor);
		
		//Permutation
		return permutation(PERMUTATION, sBox);
	}
	
	public String sbox(String _48Bits) {
		StringBuilder _32Bits = new StringBuilder();
		for(int i = 0; i < 48; i = i + 6) {
			/*
			 * Converting 6-bit number to 4-bit
			 * SBOX has all number less than 15.
			 */
			String Si = _48Bits.substring(i, i + 6);
			int row = Integer.parseInt(Si.charAt(0) + "" + Si.charAt(5), 2);
			int col = Integer.parseInt(Si.substring(1, 5), 2);
			String s32 = decimalToXbits(SBOX[i/6][row][col], 4);
			_32Bits.append(s32);
		}
		return _32Bits.toString();
	}
	
	
	public String[] keySchedule(String privateKey) {
		
		String[] keySchedules = new String[16];
		
		//Permuted-Choice 1
		String PC_1 = permutation(PERMUTED_CHOICE_1, privateKey);
		
		String L_i = PC_1.substring(0, 28);
		String R_i = PC_1.substring(28);
		
		for(int i = 0; i < 16; i++) {
			
			//Left Shift
			L_i = leftShift(L_i, SHIFT_BITS[i]);
			R_i = leftShift(R_i, SHIFT_BITS[i]);
			
			//Permuted-Choice 2
			keySchedules[i] = permutation(PERMUTED_CHOICE_2, L_i + R_i);
		}
		
		return keySchedules;
	}
	
	public String leftShift(String input, int shiftBits) {
		return input.substring(shiftBits) + input.substring(0, shiftBits);
	}
	
	public String round(String input, String[] keySchedules) {
		
		String left = input.substring(0, 32);
		String right = input.substring(32);
		String temp;
		
		System.out.println(String.format("Input : %s %s", left, right));
		
		for(int i = 0; i < 16; i++) {
			String f = f(right, keySchedules[i]);
			temp = right;
			right = XOR(left, f);
			left = temp;
			System.out.println(String.format("Round %d : %s %s", i + 1, left, right));
		}
		return right + left;
	}
	
	public String encrypt64Bit(String input, String[] keySchedules) {
		System.out.println("Plain Text : " + input);
		//Initial Permutation
		String IP = permutation(INITIAL_PERMUATIONS, input);
		System.out.println("Initial Permutation : " + IP);
		String cipher = round(IP, keySchedules);
		
		//Inverse Permutation
		String IP_1 = permutation(FINAL_PERMUTATION, cipher);
		return IP_1;
	}
	
	public String encrypt(String message, String privateKey) {
		
		StringBuilder cipher = new StringBuilder();
		String[] keySchedules = keySchedule(asciiTextToBinaryString(privateKey));
		
		final int size = message.length()%8 == 0 
						? message.length()/8 
								: message.length()/8 + 1;
		
		for(int i = 0; i < size; i++) {
			cipher.append(encrypt64Bit(asciiTextToBinaryString(message.substring(i, 8)), keySchedules));
		}
		return cipher.toString();
	}
}