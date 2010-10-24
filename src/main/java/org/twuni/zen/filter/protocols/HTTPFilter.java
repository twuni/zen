package org.twuni.zen.filter.protocols;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.twuni.zen.filter.BodyFilter;

public class HTTPFilter implements BodyFilter {

	@Override
	public boolean test( InputStream input ) {
		BufferedReader reader = new BufferedReader( new InputStreamReader( input ) );
		try {
			String firstLine = reader.readLine();
			return firstLine.matches( "HTTP/1.1" );
		} catch( IOException exception ) {
		}
		return false;
	}

	@Override
	public void handle( InputStream message ) throws IOException {
	}

}
