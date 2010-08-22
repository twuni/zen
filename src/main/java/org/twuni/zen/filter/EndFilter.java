package org.twuni.zen.filter;

import java.io.IOException;

import org.twuni.zen.ZenMessage;

/**
 * This filter is the default next item for other filters whenever none is specified. It simply discards any messages
 * passed to it.
 */
public final class EndFilter implements Filter {

	/**
	 * This method does nothing.
	 */
	@Override
	public void handle( ZenMessage message ) throws IOException {
	}

}
