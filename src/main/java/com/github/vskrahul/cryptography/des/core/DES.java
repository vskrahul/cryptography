/**
 * 
 */
package com.github.vskrahul.cryptography.des.core;

import static com.github.vskrahul.cryptography.utils.NumberUtils.XOR;

import com.github.vskrahul.cryptography.utils.NumberUtils;

/**
 * @author Rahul Vishvakarma
 *
 */
public class DES {

	public String permutation(int[] TABLE, String input) {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < input.length(); i++) {
			sb.append(input.charAt(TABLE[i] - 1));
		}
		return sb.toString();
	}
	
	public String f(String right32Bits, String keyBits) {
		
		//Expansion
		String expansion = permutation(PermutationArrays.EXPANSION, right32Bits);
		
		//XOR
		String xor = XOR(expansion, keyBits);
		
		//S-Box Confusion
		String sBox = sbox(xor);
		
		//Permutation
		return permutation(PermutationArrays.PERMUTATION, sBox);
	}
	
	public String sbox(String _48Bits) {
		StringBuilder _32Bits = new StringBuilder();
		for(int i = 0; i < 48; i = i + 6) {
			String Si = _48Bits.substring(i, i + 8);
			int row = Integer.parseInt(Si.charAt(i) + "" + Si.charAt(i + 7), 2);
			int col = Integer.parseInt(Si.substring(i + 1, i + 8), 2);
			String s32 = NumberUtils.decimalToBinary(PermutationArrays.SBOX[i/6][row][col]);
			System.out.println(String.format("%s -> %s", Si, s32));
			_32Bits.append(s32);
		}
		return _32Bits.toString();
	}
	
	
	public String[] keySchedule(String privateKey) {
		
		String[] keySchedules = new String[16];
		
		//Permuted-Choice 1
		String PC_1 = permutation(PermutationArrays.PERMUTED_CHOICE_1, privateKey);
		
		String L_i = "";
		String R_i = "";
		
		for(int i = 0; i < 16; i++) {
			
			//Left Shift
			L_i = leftShift(PC_1.substring(0, 14), PermutationArrays.SHIFT_BITS[i]);
			R_i = leftShift(PC_1.substring(14), PermutationArrays.SHIFT_BITS[i]);
			
			//Permuted-Choice 2
			keySchedules[i] = permutation(PermutationArrays.PERMUTED_CHOICE_2, L_i + R_i);
		}
		
		return keySchedules;
	}
	
	public String leftShift(String input, int shiftBits) {
		return input.substring(shiftBits) + input.substring(0, shiftBits);
	}
	
	public String round(String input, String[] keySchedules) {
		
		for(int i = 0; i < 16; i++) {
			String left = input.substring(0, 16);
			String right = input.substring(16);
			
			String fOut = f(right, keySchedules[i]);
			
			input = right + XOR(left, fOut);
		}
		return input;
	}
	
	public String encrypt64Bit(String input, String[] keySchedules) {
		//Initial Permutation
		String IP = permutation(PermutationArrays.INITIAL_PERMUATIONS, input);
		
		String cipher = round(IP, keySchedules);
		
		//Inverse Permutation
		String IP_1 = permutation(PermutationArrays.FINAL_PERMUTATION, cipher);
		return IP_1;
	}
	
	public String encrypt(String message, String privateKey) {
		
		StringBuilder cipher = new StringBuilder();
		String[] keySchedules = keySchedule(NumberUtils.asciiTextToBinaryString(privateKey));
		
		final int size = message.length()%8 == 0 
						? message.length()/8 
								: message.length()/8 + 1;
		
		for(int i = 0; i < size; i++) {
			cipher.append(encrypt64Bit(message.substring(i, 8), keySchedules));
		}
		return cipher.toString();
	}
}