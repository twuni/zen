package org.twuni.zen;

import java.net.ProtocolException;

public class ZenProtocol {

	private static final String NAME = "Zen";
	private static final byte VERSION = 1;
	
	public static String getName() {
		return NAME;
	}
	
	public static byte getVersion() {
		return VERSION;
	}

	public static void validate( String name, byte version ) throws ProtocolException {
		if( !NAME.equals( name ) || version != VERSION ) { throw new ProtocolException(); }
	}

}
