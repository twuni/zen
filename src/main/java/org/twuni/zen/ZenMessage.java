package org.twuni.zen;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.twuni.zen.io.ByteArraysInputStream;
import org.twuni.zen.io.ZenMessageOutputStream;
import org.twuni.zen.io.exception.FragmentAlreadyExistsException;

public class ZenMessage {

	public static final String PROTOCOL = "Zen";
	public static final byte VERSION = 1;

	private final ZenMessageEndpoint source;
	private final ZenMessageEndpoint destination;
	private final byte [][] bodyFragments;

	private int fragmentsRemaining;

	/**
	 * Prepares a message between the given source and destination that will contain a single fragment.
	 * 
	 * @param source The origin endpoint of this message.
	 * @param destination The destination endpoint of this message.
	 */
	public ZenMessage( ZenMessageEndpoint source, ZenMessageEndpoint destination ) {
		this( source, destination, 1 );
	}

	/**
	 * Prepares a message between the given source and destination that will contain the given number of fragments.
	 * 
	 * @param source The origin endpoint of this message.
	 * @param destination The destination endpoint of this message.
	 * @param numberOfFragments The total number of fragments contained within this message.
	 */
	public ZenMessage( ZenMessageEndpoint source, ZenMessageEndpoint destination, int numberOfFragments ) {
		this.source = source;
		this.destination = destination;
		this.bodyFragments = new byte[numberOfFragments][];
		fragmentsRemaining = numberOfFragments;
	}

	/**
	 * Creates a fragment at the given position within this message.
	 * 
	 * @return The Zen protocol message fragment, with headers.
	 * @throws IndexOutOfBoundsException if the position is not between 1 and the total number of expected fragments,
	 *             inclusive.
	 * @throws FragmentAlreadyExistsException if a fragment already exists at the given position.
	 */
	public byte [] putFragment( int position, byte [] body ) throws FragmentAlreadyExistsException, IOException {

		if( position < 1 || position > bodyFragments.length ) { throw new IndexOutOfBoundsException(); }
		if( bodyFragments[position - 1] != null ) { throw new FragmentAlreadyExistsException(); }

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream( out );

		data.writeUTF( PROTOCOL );
		data.writeByte( VERSION );
		// TODO: Header checksum.
		data.writeUTF( destination.getAddress() );
		data.writeInt( destination.getMessageId() );
		data.writeUTF( source.getAddress() );
		data.writeInt( source.getMessageId() );
		data.writeInt( bodyFragments.length );
		data.writeInt( position );
		data.writeLong( System.currentTimeMillis() );
		data.write( DigestUtils.sha( body ) );
		data.writeInt( body.length );
		data.write( body );

		bodyFragments[position - 1] = body;
		fragmentsRemaining--;

		return out.toByteArray();

	}

	/**
	 * @return The entire message body as an InputStream.
	 */
	public InputStream getBodyAsInputStream() {
		return new ByteArraysInputStream( bodyFragments );
	}

	/**
	 * Convenience method for getting an instance of ZenMessageOutputStream for this message.
	 * 
	 * @param out The underlying output stream to which fragments will be written.
	 * @return an instance of ZenMessageOutputStream.
	 */
	public OutputStream getOutputStream( OutputStream out ) {
		return new ZenMessageOutputStream( this, out );
	}

	/**
	 * @return The body of the fragment contained at the given position.
	 * @throws IndexOutOfBoundsException if no fragment is found at the given position.
	 */
	public byte [] getFragmentBody( int position ) {
		return bodyFragments[position - 1];
	}

	/**
	 * @return true if all fragments for this message have been filled, false otherwise.
	 */
	public boolean isComplete() {
		return fragmentsRemaining <= 0;
	}

}
