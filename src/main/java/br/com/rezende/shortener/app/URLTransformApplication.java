package br.com.rezende.shortener.app;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.com.rezende.shortener.app.util.SmallURLConstants;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

/**
 * 
 * @author Andre.Rezende
 * @since 10/08/2019
 */
@SpringBootApplication
public class URLTransformApplication {
	private Logger log = LoggerFactory.getLogger(URLTransformApplication.class);

	private static RedisServer server;

	/**
	 * Executable application method. Starts Embbeded Redis.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		redisConfig();

		SpringApplication.run(URLTransformApplication.class, args);
	}

	/**
	 * Initializes Jedis host and port. Initalizes constant ID.
	 * 
	 * @return Instance of Jedis.
	 */
	@Bean
	public Jedis jedis() {
		Jedis jedis = new Jedis(SmallURLConstants.REDIS_HOST, SmallURLConstants.REDIS_PORT);
		jedis.set(SmallURLConstants.ID, "5073");

		return jedis;
	}

	/**
	 * Stops Redis server on application exists.
	 */
	@PreDestroy
	public void onExit() {
		log.info("###Stoping###");

		try {
			if (server != null)
				server.stop();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		log.info("###STOP FROM THE LIFECYCLE###");
	}

	/**
	 * Initializes Redis configurations.
	 */
	private static void redisConfig() {
		server = RedisServer.builder().port(SmallURLConstants.REDIS_PORT).setting("maxmemory 128M") // maxheap 128M
				.build();
		server.start();
	}

}
