package org.twuni.zen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.twuni.zen.io.ZenChannel;

public class ZenLearningTest {

	private static final int BUFFER_SIZE = 0x0FFF;

	private ZenEndpoint local;
	private ZenEndpoint remote;

	private ByteArrayOutputStream buffer;
	private ZenChannel channel;

	@Before
	public void setUp() {
		buffer = new ByteArrayOutputStream();
		channel = new ZenChannel( buffer );
		local = new ZenEndpoint( "localhost", 1 );
		remote = new ZenEndpoint( "localhost" );
	}

	@Test
	public void testWrite() throws IOException {
		String body = "Hello, world!";
		ZenMessage message = new ZenMessage( local, remote, body.getBytes() );
		channel.write( message );
	}

	@Test
	public void testWriteTwoFragments() throws IOException {

		String [] fragments = {
			"Hello", "World"
		};

		ZenMessage message = new ZenMessage( local, remote, fragments.length );

		for( int i = 0; i < fragments.length; i++ ) {
			String fragment = fragments[i];
			message.putFragment( i + 1, fragment.getBytes() );
			channel.write( message );
		}

	}

	@Test
	public void testRead() throws IOException {
		testWrite();
		channel = new ZenChannel( new ByteArrayInputStream( buffer.toByteArray() ) );
		ZenMessage message = channel.read();
		Assert.assertEquals( "Hello, world!", new String( message.getFragmentBody( 1 ) ) );
	}

	@Test
	public void testReadTwoFragments() throws IOException {

		testWriteTwoFragments();

		channel = new ZenChannel( new ByteArrayInputStream( buffer.toByteArray() ) );
		ZenMessage message = channel.read();
		message.mergeWith( channel.read() );

		String hello = new String( message.getFragmentBody( 1 ) );
		String world = new String( message.getFragmentBody( 2 ) );

		Assert.assertEquals( "Hello", hello );
		Assert.assertEquals( "World", world );

	}

	@Test
	public void testSendFile() throws IOException {

		File input = new File( "src/test/resources/pop.mp3" );
		File output = new File( "target/pop.mp3" );

		output.delete();

		copyFile( input, output );

	}

	private void copyFile( File input, File output ) throws IOException {
		ByteArrayOutputStream zen = new ByteArrayOutputStream();
		convertFileToZen( input, new ZenChannel( zen ) );
		convertZenToFile( new ZenChannel( new ByteArrayInputStream( zen.toByteArray() ) ), output );
	}

	private void convertFileToZen( File inputFile, ZenChannel output ) throws IOException {

		byte [] buffer = new byte[BUFFER_SIZE];

		InputStream input = new FileInputStream( inputFile );

		ZenMessage message = new ZenMessage( local, remote, getNumberOfFragments( inputFile ) );

		int position = 1;
		for( int length = input.read( buffer ); length > -1; length = input.read( buffer ) ) {
			message.putFragment( position, Arrays.copyOfRange( buffer, 0, length ) );
			output.write( message );
			position++;
		}

	}

	private void convertZenToFile( ZenChannel input, File outputFile ) throws IOException {

		ZenMessage message = input.read();

		while( !message.isComplete() ) {
			ZenMessage fragment = input.read();
			int position = fragment.getPosition();
			message.putFragment( position, fragment.getFragmentBody( position ) );
		}

		byte [] buffer = new byte[BUFFER_SIZE];

		InputStream body = message.getBodyAsInputStream();
		OutputStream output = new FileOutputStream( outputFile );

		for( int length = body.read( buffer ); length > -1; length = body.read( buffer ) ) {
			output.write( buffer, 0, length );
			output.flush();
		}

		body.close();
		output.close();

	}

	private int getNumberOfFragments( File file ) {
		long fragments = file.length() / BUFFER_SIZE;
		return (int) ( 1 + fragments );
	}

}
