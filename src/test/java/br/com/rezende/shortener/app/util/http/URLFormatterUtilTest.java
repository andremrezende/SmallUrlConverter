package br.com.rezende.shortener.app.util.http;

import static org.junit.Assert.*;

import org.junit.Test;

public class URLFormatterUtilTest {

	@Test
	public void testFormatLocalURLFromShortener() {
		String hostAndPort = URLFormatterUtil.splitHostAndPort("localhost:8080/XPTO");
		assertEquals("localhost:8080/", hostAndPort);
		
		hostAndPort = URLFormatterUtil.splitHostAndPort("localhost:8080/tinier");
		assertEquals("localhost:8080/", hostAndPort);
		
		hostAndPort = URLFormatterUtil.splitHostAndPort("localhost:8080/tinier/test");
		assertEquals("localhost:8080/", hostAndPort);
	}
	
	@Test
	public void testremoveHttp() {
		String withoutHttp = URLFormatterUtil.removeHttp("http://www.abc.com.br");
		assertEquals("www.abc.com.br", withoutHttp);
		
		withoutHttp = URLFormatterUtil.removeHttp("HTTP://www.abc.com.br");
		assertEquals("www.abc.com.br", withoutHttp);
		
		withoutHttp = URLFormatterUtil.removeHttp("https://www.abc.com.br");
		assertEquals("www.abc.com.br", withoutHttp);
		
		withoutHttp = URLFormatterUtil.removeHttp("HTTPS://www.abc.com.br");
		assertEquals("www.abc.com.br", withoutHttp);
	}
	
	
	@Test
	public void testremoveHostAndPort() {
		String withoutHttp = URLFormatterUtil.removeHostAndPort("http://localhost/a", 8080);
		assertEquals("/a", withoutHttp);
		
		withoutHttp = URLFormatterUtil.removeHostAndPort("http://:/a", 8080);
		assertEquals("/a", withoutHttp);
		
		withoutHttp = URLFormatterUtil.removeHostAndPort("http://8080/a", 8080);
		assertEquals("/a", withoutHttp);
		
		withoutHttp =URLFormatterUtil.removeHostAndPort("http://localhost:8080/a", 8080);
		assertEquals("/a", withoutHttp);
	}

}
