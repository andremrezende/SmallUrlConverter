package br.com.rezende.shortener.app.repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.rezende.shortener.app.exception.SmalURLExpiredException;
import br.com.rezende.shortener.app.util.SmallURLConstants;
import redis.clients.jedis.Jedis;

/**
 * Repository of URLs shortned.
 * 
 * @author Andre.Rezende
 * @since 10/08/2019
 */
@Repository
public class SmallURLRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmallURLRepository.class);

	private static Set<String> includedList = new HashSet<>();

	@Autowired
	private Jedis jedis;
	private String urlKey;

	@Value("${local.redis.server.ttl}")
	private int ttls;

	public SmallURLRepository() {
		this.urlKey = SmallURLConstants.URL;
	}

	/**
	 * Custom constructor to initialize class properties.
	 * 
	 * @param jedis  Redis configuration.
	 * @param idKey  Identification Key.
	 * @param urlKey URL Identification Key.
	 */
	public SmallURLRepository(Jedis jedis, String idKey, String urlKey) {
		this.jedis = jedis;
		this.urlKey = urlKey;
	}

	/**
	 * Saves original URL into Redis.
	 * 
	 * @param key     unique identification key to save.
	 * @param longUrl Original URL.
	 */
	public void saveUrl(String key, String longUrl) {
		LOGGER.info("Saving: {} at {}", longUrl, key);

		jedis.hset(this.urlKey, key, longUrl);

		jedis.expire(this.urlKey, ttls);

		includedList.add(key);
	}

	/**
	 * Retrives Original URL given identification.
	 * 
	 * @param id Shortned URL Identification.
	 * @return Long URL received.
	 * @throws Exception
	 */
	public String getUrl(String id) throws Exception {
		LOGGER.info("Retrieving at {}", id);

		String url = jedis.hget(this.urlKey, SmallURLConstants.URL + id);
		LOGGER.info("Retrieved {} at {}", url, id);

		if (url == null) {
			if (includedList.contains(SmallURLConstants.URL + id)) {
				throw new SmalURLExpiredException();
			} else {
				throw new Exception("URL at key" + id + " does not exist");
			}
		}
		return url;
	}

	/**
	 * Returns list of all URL have already shortned.
	 * 
	 * @return List of URL shortned.
	 */
	public Map<String, String> getAllValues() {
		Map<String, String> values = jedis.hgetAll(this.urlKey);

		return values;
	}
}
