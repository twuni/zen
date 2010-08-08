package org.twuni.zen.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.twuni.zen.ZenMessage;


public class ZenMessageOutputStream extends OutputStream {

	private final ZenMessage message;
	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private final OutputStream out;

	private int sequence = 1;

	public ZenMessageOutputStream( ZenMessage message, OutputStream out ) {
		this.message = message;
		this.out = out;
	}

	/**
	 * Appends the given byte to the buffer.
	 */
	@Override
	public void write( int b ) throws IOException {
		buffer.write( (byte) b );
	}

	/**
	 * Writes a Zen message fragment containing the buffered data to the underlying output stream, then flushes it.
	 * 
	 * @throws IndexOutOfBoundsException if the maximum number of fragments for this message has already been reached.
	 */
	@Override
	public void flush() throws IOException {
		if( buffer.size() > 0 ) {
			byte [] fragment = message.putFragment( sequence, buffer.toByteArray() );
			out.write( fragment );
			sequence++;
			buffer.reset();
			out.flush();
		}
	}

	/**
	 * This method calls flush(), then closes the underlying output stream.
	 */
	@Override
	public void close() throws IOException {
		flush();
		out.close();
	}

}
