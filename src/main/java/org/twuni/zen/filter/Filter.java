package org.twuni.zen.filter;

import java.io.IOException;

public interface Filter<T> {

	public abstract void handle( T message ) throws IOException;

}
