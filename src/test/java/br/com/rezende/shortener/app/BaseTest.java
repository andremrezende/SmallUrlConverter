package br.com.rezende.shortener.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import br.com.rezende.shortener.app.util.SmallURLConstants;
import redis.embedded.RedisServer;

public abstract class BaseTest {
	protected static RedisServer server;
	
	@BeforeClass
	public static void setupServer() throws IOException {
		server = RedisServer.builder().port(SmallURLConstants.REDIS_PORT).setting("maxmemory 128M") // maxheap 128M
				.build();
		server.start();
		
		  Properties p = new Properties();
		    p.load(new FileInputStream(new File("src/test/resources/jub.properties")));
		    for(String k:p.stringPropertyNames()){
		        System.setProperty(k,p.getProperty(k));
		    }
	}

	@AfterClass
	public static void shutdownServer() throws IOException {
		server.stop();
	}

}
