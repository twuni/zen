package org.twuni.zen.util;

import junit.framework.Assert;


import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.twuni.zen.util.MessageBuilder;

public class MessageBuilderTest {

	@Test
	public void testSpecification() {

		String expectedDAP = "DAP/1.0\nAddress: www.google.com\nPort: 80\n\n";
		String expectedRAP = "RAP/1.0\nResource: default\nAction: get\n\n";
		String expectedSTP = "STP/1.0\nSequence: 1/1\nSHA1: " + DigestUtils.shaHex( expectedDAP + expectedRAP ) + "\n\n";

		String expected = expectedSTP + expectedDAP + expectedRAP;
		String actual = buildSTPMessage();

		Assert.assertEquals( expected, actual );
		
		System.out.println( actual );

	}
	
	/**
	 * STP is a transmission protocol replacement for TCP.
	 * 
	 * @return
	 */
	private String buildSTPMessage() {

		MessageBuilder stp = new MessageBuilder( "STP", "1.0" );

		stp.header( "Sequence", "1/1" );

		String body = buildDAPMessage();
		stp.header( "SHA1", DigestUtils.shaHex( body ) );
		stp.body( body );
		
		return stp.build();
		
	}
	
	/**
	 * DAP is an addressing system replacement for IP.
	 * 
	 * @return
	 */
	private String buildDAPMessage() {
		
		MessageBuilder dap = new MessageBuilder( "DAP", "1.0" );

		dap.header( "Address", "www.google.com" );
		dap.header( "Port", "80" );
		
		// TODO: Return Address
		
		dap.body( buildRAPMessage() );
		
		return dap.build();

	}
	
	/**
	 * RAP is an application-level protocol replacement for HTTP.
	 * 
	 * @return
	 */
	private String buildRAPMessage() {
		
		MessageBuilder rap = new MessageBuilder( "RAP", "1.0" );

		rap.header( "Resource", "default" );
		rap.header( "Action", "get" );
		
		return rap.build();

	 }

}
