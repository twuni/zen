package org.twuni.zen.io.exception;

import java.io.IOException;

public class InvalidChecksumException extends IOException {

	private byte [] expected;
	private byte [] actual;

	public InvalidChecksumException( byte [] expected, byte [] actual ) {
		super();
		this.expected = expected;
		this.actual = actual;
	}

	public byte [] getExpected() {
		return expected;
	}

	public byte [] getActual() {
		return actual;
	}

}
