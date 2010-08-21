package org.twuni.zen.io;

import java.io.IOException;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.io.exception.RoutingException;

public class ZenMessageDestinationFilter implements ZenMessageConsumer {

	private ZenMessageConsumer consumer;

	public ZenMessageDestinationFilter( ZenMessageConsumer consumer ) {
		this.consumer = consumer;
	}

	@Override
	public void consume( ZenMessage message ) throws IOException {
		if( isLocalAddress( message.getDestination().getAddress() ) ) {
			consumer.consume( message );
		} else {
			throw new RoutingException();
		}
	}

	private boolean isLocalAddress( String address ) {
		return true;
	}

}
