package org.twuni.zen.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.twuni.zen.ZenMessage;

/**
 * A Zen channel has its own exclusive, persistent access to an I/O device dedicated to handling Zen messages.
 */
public class ZenChannel {

	protected final ZenInputStream in;
	protected final ZenOutputStream out;

	public ZenChannel( InputStream in ) {
		this( in, null );
	}

	public ZenChannel( OutputStream out ) {
		this( null, out );
	}

	public ZenChannel( InputStream in, OutputStream out ) {
		this.in = new ZenInputStream( in );
		this.out = new ZenOutputStream( out );
	}

	/**
	 * Reads the next message fragment from the input stream.
	 * 
	 * @return A message containing exactly one fragment.
	 * @throws IOException whenever any error occurs with the input stream, including any data corruption or breach of
	 *             protocol.
	 */
	public ZenMessage read() throws IOException {
		in.readProtocol();
		ZenMessage message = new ZenMessage( in.readEndpoint(), in.readEndpoint(), in.readInt() );
		message.setTimestamp( in.readLong() );
		message.putFragment( in.readInt(), in.readBody() );
		return message;
	}

	/**
	 * Writes the message fragment at the given position.
	 * 
	 * @throws IOException only if some catastrophic event occurs with the output stream.
	 * @throws IndexOutOfBoundsException if the given position is illegal for this message.
	 * @throws NullPointerException if there is no fragment at the given position.
	 */
	public void write( ZenMessage message, int position ) throws IOException {

		byte [] body = message.getFragmentBody( position );

		out.writeProtocol();
		out.writeEndpoint( message.getDestination() );
		out.writeEndpoint( message.getSource() );
		out.writeInt( message.getNumberOfFragments() );
		out.writeLong( System.currentTimeMillis() );
		out.writeInt( position );
		out.writeBody( body );

		out.flush();

	}

	/**
	 * Writes the most recently written fragment of the given message to the output stream. Same as calling write(
	 * message, message.getPosition() )
	 */
	public void write( ZenMessage message ) throws IOException {
		write( message, message.getPosition() );
	}

}
