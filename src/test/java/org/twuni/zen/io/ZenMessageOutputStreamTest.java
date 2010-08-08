package org.twuni.zen.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;


import org.junit.Assert;
import org.junit.Test;
import org.twuni.zen.ZenMessage;
import org.twuni.zen.ZenMessageEndpoint;
import org.twuni.zen.io.ZenMessageListener;
import org.twuni.zen.io.ZenMessageOutputStream;
import org.twuni.zen.io.exception.MessageIncompleteException;

public class ZenMessageOutputStreamTest {

	private static final String [] FRAGMENTS = {
		"ABCDEFG", "1234567"
	};

	private OutputStream out;
	private ByteArrayOutputStream buffer;
	private final ZenMessageEndpoint source = new ZenMessageEndpoint( "localhost", 1 );
	private final ZenMessageEndpoint destination = new ZenMessageEndpoint( "www.google.com" );

	@Test
	public void testRoundTrip() throws IOException {
		roundTrip( FRAGMENTS[0] );
		roundTrip( FRAGMENTS );
	}

	public void roundTrip( String... fragments ) throws IOException {

		prepareMessage( fragments.length );

		byte [] expected = getBytes( fragments );

		write( fragments );

		byte [] actual = new byte[expected.length];

		read( actual );

		Assert.assertTrue( Arrays.equals( expected, actual ) );

	}

	private void prepareMessage( int totalNumberOfFragments ) {
		ZenMessage message = new ZenMessage( source, destination, totalNumberOfFragments );
		buffer = new ByteArrayOutputStream();
		out = new ZenMessageOutputStream( message, buffer );
	}

	private byte [] getBytes( String... fragments ) {
		StringBuilder builder = new StringBuilder();
		for( String fragment : fragments ) {
			builder.append( fragment );
		}
		return builder.toString().getBytes();
	}

	private ZenMessage getMessage() throws IOException {

		ByteArrayInputStream bytes = new ByteArrayInputStream( buffer.toByteArray() );
		ZenMessageListener in = new ZenMessageListener( bytes );
		ZenMessage message = null;
		
		while( message == null ) {
			try {
				message = in.nextFragment();
			} catch( MessageIncompleteException ignore ) {
			}
		}

		return message;

	}

	private void read( byte [] buffer ) throws IOException {
		getMessage().getBodyAsInputStream().read( buffer );
	}

	private void write( String [] fragments ) throws IOException {
		for( String fragment : fragments ) {
			write( fragment.getBytes() );
		}
	}

	private void write( byte [] buffer ) throws IOException {
		out.write( buffer );
		out.flush();
	}

}
