package org.twuni.zen.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.twuni.zen.ZenMessage;

public class ReconstitutionFilter implements Filter {

	private final Map<ZenMessage, ZenMessage> messages = new HashMap<ZenMessage, ZenMessage>();
	private final Filter next;

	public ReconstitutionFilter() {
		this( null );
	}

	public ReconstitutionFilter( Filter next ) {
		if( next == null ) {
			next = new EndFilter();
		}
		this.next = next;
	}

	/**
	 * Merges the given message with its existing message or adds it to the list of known messages. Then, if the message
	 * is complete, then delegates the message to the next filter in the chain.
	 */
	@Override
	public void handle( ZenMessage message ) throws IOException {

		ZenMessage existing = messages.get( message );

		if( existing == null ) {
			existing = message;
			messages.put( message, existing );
		} else {
			existing.mergeWith( message );
		}

		if( existing.isComplete() ) {
			messages.remove( message );
			next.handle( message );
		}

	}

}
