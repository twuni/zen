package org.twuni.zen.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.twuni.zen.io.ByteArraysInputStream;

public class ByteArraysInputStreamTest {

	@Test
	public void testRead() throws IOException {

		byte [][] input = new byte[][] {
			new byte[] {
				1, 2, 3
			}, new byte[] {
				4, 5, 6
			}, new byte[] {
				7, 8, 9
			}
		};

		byte [] expected = new byte[] {
			1, 2, 3, 4, 5, 6, 7, 8, 9
		};

		byte [] actual = new byte[expected.length];

		InputStream stream = new ByteArraysInputStream( input );
		int length = stream.read( actual );

		Assert.assertEquals( expected.length, length );
		Assert.assertTrue( Arrays.equals( expected, actual ) );

	}

}
