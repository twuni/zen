package org.twuni.zen;

import java.io.InputStream;

import org.twuni.zen.io.ByteArraysInputStream;
import org.twuni.zen.io.exception.FragmentAlreadyExistsException;

public class ZenMessage {

	private final ZenEndpoint destination;

	private ZenEndpoint source;
	private byte [][] bodyFragments;
	private int fragmentsRemaining;
	private long timestamp = Long.MAX_VALUE;
	private int position;

	/**
	 * Prepares a message between the given source and destination that will contain a single fragment.
	 * 
	 * @param source The origin endpoint of this message.
	 * @param destination The destination endpoint of this message.
	 */
	public ZenMessage( ZenEndpoint destination, ZenEndpoint source, byte [] body ) {
		this( source, destination, 1 );
		try {
			putFragment( 1, body );
		} catch( FragmentAlreadyExistsException impossible ) {
		}
	}

	/**
	 * Prepares a message between the given source and destination that will contain the given number of fragments.
	 * 
	 * @param source The origin endpoint of this message.
	 * @param destination The destination endpoint of this message.
	 * @param numberOfFragments The total number of fragments contained within this message.
	 */
	public ZenMessage( ZenEndpoint destination, ZenEndpoint source, int numberOfFragments ) {
		this.destination = destination;
		this.source = source;
		this.bodyFragments = new byte[numberOfFragments][];
		fragmentsRemaining = numberOfFragments;
	}

	@Override
	public boolean equals( Object object ) {
		if( object instanceof ZenMessage ) {
			ZenMessage other = ZenMessage.class.cast( object );
			return source.equals( other.source ) && destination.equals( other.destination );
		}
		return false;
	}

	/**
	 * @return The entire message body as an InputStream.
	 */
	public InputStream getBodyAsInputStream() {
		return new ByteArraysInputStream( bodyFragments );
	}

	public ZenEndpoint getDestination() {
		return destination;
	}

	/**
	 * @return The body of the fragment contained at the given position.
	 * @throws IndexOutOfBoundsException if no fragment is found at the given position.
	 */
	public byte [] getFragmentBody( int position ) {
		return bodyFragments[position - 1];
	}

	public int getNumberOfFragments() {
		return bodyFragments.length;
	}

	public int getPosition() {
		return position;
	}

	public ZenEndpoint getSource() {
		return source;
	}

	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return true if all fragments for this message have been filled, false otherwise.
	 */
	public boolean isComplete() {
		return fragmentsRemaining <= 0;
	}

	/**
	 * Creates a fragment at the given position within this message.
	 * 
	 * @throws IndexOutOfBoundsException if the position is not between 1 and the total number of expected fragments,
	 *             inclusive.
	 * @throws FragmentAlreadyExistsException if a fragment already exists at the given position.
	 */
	public void putFragment( int position, byte [] body ) throws FragmentAlreadyExistsException {
		if( bodyFragments[position - 1] != null ) { throw new FragmentAlreadyExistsException(); }
		bodyFragments[position - 1] = body;
		this.position = position;
		fragmentsRemaining--;
	}

	/**
	 * The message timestamp should be the earliest of all of its fragments, therefore this method only updates the
	 * message timestamp if the given timestamp is earlier than its current one.
	 */
	public void setTimestamp( long timestamp ) {
		if( timestamp < this.timestamp ) {
			this.timestamp = timestamp;
		}
	}

	public void mergeWith( ZenMessage message ) throws FragmentAlreadyExistsException {
		mergeWith( message, message.getPosition() );
	}

	public void mergeWith( ZenMessage message, int position ) throws FragmentAlreadyExistsException {
		byte [] body = message.getFragmentBody( position );
		this.putFragment( position, body );
	}

}
