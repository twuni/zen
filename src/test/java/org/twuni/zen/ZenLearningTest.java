package org.twuni.zen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.twuni.zen.io.ZenMessageListener;
import org.twuni.zen.io.ZenMessageOutputStream;
import org.twuni.zen.io.exception.MessageIncompleteException;

public class ZenLearningTest {

	private static final int BUFFER_SIZE = 0x0FFF;

	/**
	 * In this example, we will be constructing a simple message with a single fragment.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testHelloWorld() throws IOException {

		// Step 1: Who are you?
		ZenMessageEndpoint source = new ZenMessageEndpoint( "localhost", 1 );

		// Step 2: Who are you talking to?
		ZenMessageEndpoint destination = new ZenMessageEndpoint( "localhost" );

		// Step 3: Get ready to send your message.
		ZenMessage message = new ZenMessage( source, destination );

		// Step 4: Prepare the output stream.
		OutputStream out = message.getOutputStream( System.out );

		// Step 5: Write your message.
		out.write( "Hello, world!".getBytes() );

		// Step 6: Send your message.
		out.flush();

	}

	@Test
	public void testSendFile() throws IOException {

		File input = new File( "src/test/resources/pop.mp3" );
		File output = new File( "target/pop.zen.mp3" );

		output.delete();

		copyFile( input, output );

	}

	private void copyFile( File input, File output ) throws IOException {

		File zen = new File( "target/pop.zen" );

		zen.delete();

		convertFileToZen( input, zen );
		convertZenToFile( zen, output );

		zen.delete();

	}

	private void convertFileToZen( File inputFile, File outputFile ) throws IOException {

		byte [] buffer = new byte[BUFFER_SIZE];

		ZenMessageEndpoint source = new ZenMessageEndpoint( "localhost", 1 );
		ZenMessageEndpoint destination = new ZenMessageEndpoint( "localhost" );

		InputStream input = new FileInputStream( inputFile );
		OutputStream output = new FileOutputStream( outputFile );

		ZenMessage message = new ZenMessage( source, destination, getNumberOfFragments( inputFile ) );

		ZenMessageOutputStream zen = new ZenMessageOutputStream( message, output );
		for( int length = input.read( buffer ); length > -1; length = input.read( buffer ) ) {
			zen.write( buffer, 0, length );
			zen.flush();
		}
		zen.close();

	}

	private void convertZenToFile( File inputFile, File outputFile ) throws IOException {

		ZenMessage message = getMessageFromFile( inputFile );

		byte [] buffer = new byte[BUFFER_SIZE];

		InputStream input = message.getBodyAsInputStream();
		OutputStream output = new FileOutputStream( outputFile );

		for( int length = input.read( buffer ); length > -1; length = input.read( buffer ) ) {
			output.write( buffer, 0, length );
			output.flush();
		}

		input.close();
		output.close();

	}

	private ZenMessage getMessageFromFile( File inputFile ) throws IOException {

		InputStream input = new FileInputStream( inputFile );

		ZenMessageListener listener = new ZenMessageListener( input );

		ZenMessage message = null;
		while( message == null ) {
			try {
				message = listener.nextFragment();
			} catch( MessageIncompleteException exception ) {
			}
		}
		input.close();

		return message;

	}

	private int getNumberOfFragments( File file ) {
		long fragments = file.length() / BUFFER_SIZE;
		return (int) ( 1 + fragments );
	}

}
