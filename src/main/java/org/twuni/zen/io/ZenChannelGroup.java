package org.twuni.zen.io;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.io.exception.RoutingException;

/**
 * A Zen channel group may forward a message from one channel to any or all of its other channels. TODO: Multithreaded
 * support and message lifecycle scoped consumers.
 */
public class ZenChannelGroup {

	protected final Logger log = Logger.getLogger( getClass().getName() );

	private ZenMessageConsumer consumer;
	protected final Set<ZenChannel> channels = new HashSet<ZenChannel>();

	public ZenChannelGroup( ZenMessageConsumer consumer, ZenChannel... channels ) {
		this.consumer = consumer;
		for( ZenChannel channel : channels ) {
			this.channels.add( channel );
		}
	}

	public void listen() throws IOException {
		for( ZenChannel channel : channels ) {
			ZenMessage message = channel.read();
			try {
				consumer.consume( message );
			} catch( RoutingException exception ) {
				for( ZenChannel other : channels ) {
					if( other != channel ) {
						other.write( message );
					}
				}
			}
		}
	}

}
