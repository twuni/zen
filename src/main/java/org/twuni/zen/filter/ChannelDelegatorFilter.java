package org.twuni.zen.filter;

import java.io.IOException;
import java.util.Set;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.io.ZenChannel;

/**
 * The message delegator forwards a message along each of its known channels.
 */
public class ChannelDelegatorFilter implements Filter<ZenMessage> {

	protected final Set<ZenChannel> channels;

	public ChannelDelegatorFilter( Set<ZenChannel> channels ) {
		this.channels = channels;
	}

	/**
	 * Simply forwards the given message to all of its known channels.
	 */
	@Override
	public void handle( ZenMessage message ) throws IOException {
		for( ZenChannel channel : channels ) {
			channel.write( message );
		}
	}

}
