package org.twuni.zen.filter;

import java.io.IOException;

import org.twuni.zen.ZenMessage;
import org.twuni.zen.io.exception.RoutingException;

public class DestinationFilter implements Filter {

	private Filter filter;

	public DestinationFilter( Filter filter ) {
		this.filter = filter;
	}

	/**
	 * Delegates the given message to the next filter in the chain only if the destination address is a known local
	 * address. Otherwise, throws a RoutingException.
	 * 
	 * @throws RoutingException if the message's destination address is not a known local address.
	 */
	@Override
	public void delegate( ZenMessage message ) throws IOException {
		if( isLocalAddress( message.getDestination().getAddress() ) ) {
			filter.delegate( message );
		} else {
			throw new RoutingException();
		}
	}

	/**
	 * TODO: This method is stubbed for now.
	 * 
	 * @param address
	 * @return For now, always returns true. Should only return true if the given address is the designated local
	 *         address.
	 */
	private boolean isLocalAddress( String address ) {
		return true;
	}

}
