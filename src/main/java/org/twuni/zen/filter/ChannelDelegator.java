package org.twuni.zen.filter;

import java.io.IOException;
import java.util.Set;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.io.ZenChannel;

/**
 * The message delegator forwards a message along each of its known channels.
 */
public class ChannelDelegator implements Filter {

	protected final Set<ZenChannel> channels;

	public ChannelDelegator( Set<ZenChannel> channels ) {
		this.channels = channels;
	}

	/**
	 * Simply forwards the given message to all of its known channels.
	 */
	@Override
	public void delegate( ZenMessage message ) throws IOException {
		for( ZenChannel channel : channels ) {
			channel.write( message );
		}
	}

}
