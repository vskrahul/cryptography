package com.github.vskrahul.cryptography.utils;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilsTest {

	@Test
	public void testDecimalToXbits() {
		Assert.assertEquals("1111", NumberUtils.decimalToXbits(15, 4));
		Assert.assertEquals("0100", NumberUtils.decimalToXbits(4, 4));
		
	}
}
