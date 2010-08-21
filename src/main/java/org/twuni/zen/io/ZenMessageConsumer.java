package org.twuni.zen.io;

import java.io.IOException;

import org.twuni.zen.ZenMessage;

public interface ZenMessageConsumer {

	public abstract void consume( ZenMessage message ) throws IOException;

}
