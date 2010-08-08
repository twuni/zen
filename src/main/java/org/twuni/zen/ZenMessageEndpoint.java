package org.twuni.zen;

public class ZenMessageEndpoint {

	/**
	 * This value serves as a placeholder whenever the message ID for an endpoint is not known.
	 */
	public static final int UNKNOWN_ID = 0;

	private final int messageId;
	private final String address;

	public ZenMessageEndpoint( String address, int messageId ) {
		this.address = address;
		this.messageId = messageId;
	}

	public ZenMessageEndpoint( String address ) {
		this( address, UNKNOWN_ID );
	}

	public int getMessageId() {
		return messageId;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		return ( address + "|" + messageId ).hashCode();
	}

	@Override
	public boolean equals( Object object ) {
		if( object instanceof ZenMessageEndpoint ) {
			ZenMessageEndpoint other = (ZenMessageEndpoint) object;
			return messageId == other.messageId && address.equals( other.address );
		}
		return false;
	}

}
