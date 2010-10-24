package org.twuni.zen.filter;

import java.io.IOException;
import java.util.Set;

import org.twuni.zen.ZenMessage;

public class BodyDelegatorFilter implements Filter<ZenMessage> {

	protected final Set<BodyFilter> filters;

	public BodyDelegatorFilter( Set<BodyFilter> filters ) {
		this.filters = filters;
	}

	/**
	 * Detects the protocol within a message body and passes the body to known implementors of that protocol on this
	 * system.
	 */
	@Override
	public void handle( ZenMessage message ) throws IOException {
		for( BodyFilter filter : filters ) {
			if( filter.test( message.getBodyAsInputStream() ) ) {
				filter.handle( message.getBodyAsInputStream() );
			}
		}

	}
}
