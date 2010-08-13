package org.twuni.zen.io;

import java.util.ArrayList;
import java.util.List;

/**
 * A Zen channel group may forward a message from one channel to any or all of its other channels.
 */
public class ZenChannelGroup {

	protected List<ZenChannel> channels = new ArrayList<ZenChannel>();

}
