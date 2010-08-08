package org.twuni.zen.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads from an array of byte arrays.
 */
public class ByteArraysInputStream extends InputStream {

	private byte [][] arrays;
	private int array = 0;
	private int index = 0;

	public ByteArraysInputStream( byte [][] arrays ) {
		this.arrays = arrays;
	}

	@Override
	public int read() throws IOException {

		int value = -1;

		if( array < arrays.length ) {

			if( index < arrays[array].length ) {

				value = 0x000000FF & arrays[array][index];
				index++;

			} else {

				array++;
				index = 0;
				value = read();

			}

		}

		return value;

	}

}
