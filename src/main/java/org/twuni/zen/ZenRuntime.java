package org.twuni.zen;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.twuni.zen.filter.ChannelDelegatorFilter;
import org.twuni.zen.filter.Filter;
import org.twuni.zen.filter.ReconstitutionFilter;
import org.twuni.zen.filter.RoutingFilter;
import org.twuni.zen.io.ZenChannel;
import org.twuni.zen.io.ZenMessageListener;
import org.twuni.zen.io.exception.RoutingException;

public class ZenRuntime {

	public static void main( String [] args ) {

		final Set<ZenChannel> channels = new HashSet<ZenChannel>();

		final ZenChannel channel = new ZenChannel( System.in, System.out );

		final Filter delegator = new ChannelDelegatorFilter( channels );
		final Filter combiner = new ReconstitutionFilter();
		final Filter router = new RoutingFilter( combiner );

		final Filter director = new Filter() {

			@Override
			public void handle( ZenMessage message ) throws IOException {
				try {
					router.handle( message );
				} catch( RoutingException exception ) {
					delegator.handle( message );
				}
			}

		};

		final ZenMessageListener listener = new ZenMessageListener( channel, director );

		channels.add( channel );
		listener.start();

	}

}
