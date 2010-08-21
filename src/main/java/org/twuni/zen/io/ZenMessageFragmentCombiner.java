package org.twuni.zen.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.twuni.zen.ZenMessage;

public class ZenMessageFragmentCombiner implements ZenMessageConsumer {

	private Map<ZenMessage, ZenMessage> messages = new HashMap<ZenMessage, ZenMessage>();
	private ZenMessageConsumer consumer;

	public ZenMessageFragmentCombiner( ZenMessageConsumer consumer ) {
		this.consumer = consumer;
	}

	@Override
	public void consume( ZenMessage message ) throws IOException {
		ZenMessage existing = messages.get( message );
		if( existing == null ) {
			existing = message;
			messages.put( message, existing );
		} else {
			existing.mergeWith( message );
		}
		if( existing.isComplete() ) {
			messages.remove( message );
			consumer.consume( message );
		}
	}

}
