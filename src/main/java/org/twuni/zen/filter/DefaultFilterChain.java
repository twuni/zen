package org.twuni.zen.filter;

import java.io.IOException;
import java.util.Set;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.io.ZenChannel;
import org.twuni.zen.io.exception.RoutingException;

public class DefaultFilterChain implements Filter<ZenMessage> {

	private Filter<ZenMessage> delegation;
	private Filter<ZenMessage> reconstitution;
	private Filter<ZenMessage> routing;

	public DefaultFilterChain( Set<ZenChannel> channels ) {
		delegation = new ChannelDelegatorFilter( channels );
		reconstitution = new ReconstitutionFilter();
		routing = new RoutingFilter( reconstitution );
	}

	@Override
	public void handle( ZenMessage message ) throws IOException {
		try {
			routing.handle( message );
		} catch( RoutingException exception ) {
			delegation.handle( message );
		}
	}

}
