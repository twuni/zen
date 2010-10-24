package org.twuni.zen.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtils {

	@SuppressWarnings( "unchecked" )
	public static <T> Set<T> getComplement( Set<T> all, T except ) {
		T [] array = (T []) all.toArray();
		Set<T> complement = new HashSet<T>( Arrays.asList( array ) );
		complement.remove( except );
		return complement;
	}

}
