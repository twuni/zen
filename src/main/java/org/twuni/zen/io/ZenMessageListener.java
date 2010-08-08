package org.twuni.zen.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.codec.digest.DigestUtils;
import org.twuni.zen.ZenMessage;
import org.twuni.zen.ZenMessageEndpoint;
import org.twuni.zen.io.exception.InvalidChecksumException;
import org.twuni.zen.io.exception.MessageIncompleteException;
import org.twuni.zen.io.exception.RoutingException;
import org.twuni.zen.io.exception.TimestampValidationException;
import org.twuni.zen.io.exception.UnsupportedProtocolException;

public class ZenMessageListener {

	private Map<ZenMessageEndpoint, ZenMessage> messages = new HashMap<ZenMessageEndpoint, ZenMessage>();

	private InputStream in;

	public ZenMessageListener( InputStream in ) {
		this.in = in;
	}

	/**
	 * Reads the next Zen message fragment from the input stream.
	 */
	public ZenMessage nextFragment() throws IOException {
		in.mark( Integer.MAX_VALUE );
		DataInputStream data = new DataInputStream( in );
		validateProtocol( data );
		ZenMessage message = readMessage( data );
		int sequence = data.readInt();
		validateTimestamp( data );
		byte [] body = readBody( data );
		message.putFragment( sequence, body );
		if( !message.isComplete() ) { throw new MessageIncompleteException(); }
		return message;
	}

	private byte [] readBody( DataInputStream data ) throws IOException {

		byte [] checksum = new byte[20];
		data.readFully( checksum );

		int length = data.readInt();
		byte [] body = new byte[length];
		data.readFully( body );

		validateBody( body, checksum );

		return body;

	}

	private void validateTimestamp( DataInputStream data ) throws IOException {

		long timestamp = data.readLong();

		if( timestamp > System.currentTimeMillis() ) {
			in.reset();
			throw new TimestampValidationException();
		}

	}

	private ZenMessage readMessage( DataInputStream data ) throws IOException {

		ZenMessageEndpoint destination = readEndpoint( data );
		ZenMessageEndpoint source = readEndpoint( data );

		validateRouting( destination );

		int total = data.readInt();

		ZenMessage message = messages.get( source );

		if( message == null ) {
			message = new ZenMessage( source, destination, total );
			messages.put( source, message );
		}

		return message;

	}

	private ZenMessageEndpoint readEndpoint( DataInputStream data ) throws IOException {
		String address = data.readUTF();
		int id = data.readInt();
		return new ZenMessageEndpoint( address, id );
	}

	private void validateBody( byte [] body, byte [] expectedChecksum ) throws IOException {

		byte [] actualChecksum = DigestUtils.sha( body );

		if( !Arrays.equals( expectedChecksum, actualChecksum ) ) {
			in.reset();
			throw new InvalidChecksumException( expectedChecksum, actualChecksum );
		}

	}

	private void validateRouting( ZenMessageEndpoint destination ) throws IOException {

		if( !isOwnedAddress( destination.getAddress() ) ) {
			in.reset();
			throw new RoutingException();
		}

	}

	private void validateProtocol( DataInputStream data ) throws IOException {

		String protocol = data.readUTF();
		byte version = data.readByte();

		if( !ZenMessage.PROTOCOL.equals( protocol ) || ZenMessage.VERSION != version ) {
			in.reset();
			throw new UnsupportedProtocolException();
		}

	}

	private boolean isOwnedAddress( String address ) {
		// TODO: Compare this address against a list of known local addresses.
		return true;
	}

}
