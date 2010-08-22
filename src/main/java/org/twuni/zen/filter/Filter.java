package org.twuni.zen.filter;

import java.io.IOException;

import org.twuni.zen.ZenMessage;

public interface Filter {

	public abstract void delegate( ZenMessage message ) throws IOException;

}
