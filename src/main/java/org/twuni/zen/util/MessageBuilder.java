package org.twuni.zen.util;

public class MessageBuilder {

	private static final String DELIMITER = "\n";
	private static final String EQUALS = ": ";

	private static final String [] ESCAPE_VALUES = {
		DELIMITER, EQUALS
	};

	private final StringBuilder builder = new StringBuilder();
	private boolean bodyExists = false;

	public MessageBuilder( String identifier, String version ) {
		builder.append( identifier ).append( "/" ).append( version ).append( DELIMITER );
	}

	public MessageBuilder header( String key, String value ) {
		builder.append( escape( key ) ).append( EQUALS ).append( escape( value ) ).append( DELIMITER );
		return this;
	}

	public MessageBuilder body( String body ) {
		if( !bodyExists ) {
			builder.append( DELIMITER ).append( body );
			bodyExists = true;
		} else {
			throw new UnsupportedOperationException( "The envelope body has already been written." );
		}
		return this;
	}

	public String build() {
		if( !bodyExists ) {
			builder.append( DELIMITER );
			bodyExists = true;
		}
		return builder.toString();
	}

	private String escape( String value ) {
		String result = value;
		for( String escape : ESCAPE_VALUES ) {
			result = result.replaceAll( "\\" + escape, "\\" + escape );
		}
		return result;
	}

}
