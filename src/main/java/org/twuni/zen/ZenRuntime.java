package org.twuni.zen;

import java.io.IOException;

import org.twuni.zen.filter.DestinationFilter;
import org.twuni.zen.filter.ReconstitutionFilter;
import org.twuni.zen.filter.Filter;
import org.twuni.zen.io.ZenChannel;
import org.twuni.zen.io.ZenMessageListener;

public class ZenRuntime {

	public static void main( String [] args ) {

		ZenChannel channel = new ZenChannel( System.in, System.out );

		Filter consumer = new Filter() {

			@Override
			public void delegate( ZenMessage message ) throws IOException {
				System.out.println( message );
			}

		};

		ReconstitutionFilter combiner = new ReconstitutionFilter( consumer );
		DestinationFilter filter = new DestinationFilter( combiner );

		ZenMessageListener listener = new ZenMessageListener( channel, filter );

		listener.start();

	}

}
