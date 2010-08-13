package org.twuni.zen.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.twuni.zen.ZenEndpoint;
import org.twuni.zen.ZenProtocol;
import org.twuni.zen.io.exception.InvalidChecksumException;

/**
 * Convenience wrapper that supports atomically reading protocol-specific constructs.
 */
public class ZenInputStream extends DataInputStream {

	public ZenInputStream( InputStream in ) {
		super( in );
	}

	public void readProtocol() throws IOException, ProtocolException {
		ZenProtocol.validate( readUTF(), readByte() );
	}

	public ZenEndpoint readEndpoint() throws IOException {
		String address = readUTF();
		int messageId = readInt();
		return new ZenEndpoint( address, messageId );
	}

	public byte [] readChecksum() throws IOException {
		byte [] checksum = new byte[20];
		read( checksum );
		return checksum;
	}

	public byte [] readBody() throws IOException {
		byte [] expectedChecksum = readChecksum();
		byte [] body = new byte[readInt()];
		read( body );
		byte [] actualChecksum = DigestUtils.sha( body );
		if( !Arrays.equals( expectedChecksum, actualChecksum ) ) { throw new InvalidChecksumException( expectedChecksum, actualChecksum ); }
		return body;
	}
}
