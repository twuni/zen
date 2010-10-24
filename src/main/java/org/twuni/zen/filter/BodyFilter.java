package org.twuni.zen.filter;

import java.io.InputStream;

public interface BodyFilter extends Filter<InputStream> {

	/**
	 * Determines whether the input stream should be handled by this filter.
	 * 
	 * @return true if so, false otherwise.
	 */
	public abstract boolean test( InputStream input );

}
