package org.twuni.zen.io;

import java.io.EOFException;
import java.io.IOException;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.filter.EndFilter;
import org.twuni.zen.filter.Filter;

public class ZenMessageListener extends Thread {

	private final ZenChannel channel;
	private final Filter filter;

	public ZenMessageListener( final ZenChannel channel, Filter filter ) {
		super();
		if( filter == null ) {
			filter = new EndFilter();
		}
		this.channel = channel;
		this.filter = filter;
	}

	@Override
	public void run() {
		try {
			while( true ) {
				ZenMessage message = channel.read();
				filter.handle( message );
			}
		} catch( EOFException exception ) {
		} catch( IOException exception ) {
			exception.printStackTrace();
		}
	}

}
