package org.twuni.zen.io;

import java.io.IOException;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.filter.Filter;

public class ZenMessageListener extends Thread {

	private ZenChannel channel;
	private Filter filter;

	public ZenMessageListener( ZenChannel channel, Filter filter ) {
		super();
		this.channel = channel;
		this.filter = filter;
	}

	@Override
	public void run() {
		try {
			while( true ) {
				ZenMessage message = channel.read();
				filter.delegate( message );
			}
		} catch( IOException exception ) {
			exception.printStackTrace();
		}
	}

}
