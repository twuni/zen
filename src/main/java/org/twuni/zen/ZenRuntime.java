package org.twuni.zen;

import java.util.HashSet;
import java.util.Set;

import org.twuni.zen.filter.DefaultFilterChain;
import org.twuni.zen.io.ZenChannel;
import org.twuni.zen.io.ZenMessageListener;
import org.twuni.zen.util.CollectionUtils;

public class ZenRuntime {

	public static void main( String [] args ) {
		Set<ZenChannel> channels = new HashSet<ZenChannel>();
		channels.add( new ZenChannel( System.in, System.out ) );
		listen( channels );
	}

	private static void listen( Set<ZenChannel> channels ) {
		for( ZenChannel channel : channels ) {
			Set<ZenChannel> complement = CollectionUtils.getComplement( channels, channel );
			DefaultFilterChain filter = new DefaultFilterChain( complement );
			ZenMessageListener listener = new ZenMessageListener( channel, filter );
			listener.start();
		}
	}

}
