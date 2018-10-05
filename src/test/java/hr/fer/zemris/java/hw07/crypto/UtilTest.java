package hr.fer.zemris.java.hw07.crypto;

import org.junit.Test;

import hr.fer.zemris.java.hw07.crypto.Util;

import org.junit.Assert;

public class UtilTest {

	@Test
	public void hexToByteTest() {
		String string = "01aE22";
		byte[] actual = Util.hextobyte(string);
		byte[] expected = {1, -82, 34};
		
		byte actual0 = actual[0];
		byte expected0 = expected[0];
		
		byte actual1 = actual[1];
		byte expected1 = expected[1];
		
		byte actual2 = actual[2];
		byte expected2 = expected[2];
		
		Assert.assertEquals(actual0, expected0);
		Assert.assertEquals(actual1, expected1);
		Assert.assertEquals(actual2, expected2);		
		
	}
	
	@Test
	public void byteToHexTest() {
		String expected = "01ae22";
		byte[] bytes = {1, -82, 34};
		
		String actual = Util.bytetohex(bytes);
		
		Assert.assertEquals(expected, actual);
	}
	
}
