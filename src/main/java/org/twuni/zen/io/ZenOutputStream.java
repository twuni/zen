package org.twuni.zen.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.twuni.zen.ZenEndpoint;
import org.twuni.zen.ZenProtocol;

/**
 * Convenience wrapper that supports atomically writing protocol-specific constructs.
 */
public class ZenOutputStream extends DataOutputStream {

	public ZenOutputStream( OutputStream out ) {
		super( out );
	}

	public void writeProtocol() throws IOException {
		writeUTF( ZenProtocol.getName() );
		writeByte( ZenProtocol.getVersion() );
	}

	public void writeEndpoint( ZenEndpoint endpoint ) throws IOException {
		writeUTF( endpoint.getAddress() );
		writeInt( endpoint.getMessageId() );
	}

	public void writeBody( byte [] body ) throws IOException {
		write( DigestUtils.sha( body ) );
		writeInt( body.length );
		write( body );
	}

}
