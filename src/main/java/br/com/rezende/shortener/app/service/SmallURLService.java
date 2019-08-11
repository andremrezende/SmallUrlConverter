package br.com.rezende.shortener.app.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rezende.shortener.app.common.UniqueSmallURLGenerator;
import br.com.rezende.shortener.app.repository.SmallURLRepository;
import br.com.rezende.shortener.app.util.SmallURLConstants;
import br.com.rezende.shortener.app.util.http.URLFormatterUtil;

/**
 * Small URL Services, such as shinken URL or retrieves an original URL
 * convertted.
 * 
 * @author Andre.Rezende
 * @since 10/08/2019
 */
@Service
public class SmallURLService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmallURLService.class);
	private final SmallURLRepository urlRepository;

	@Autowired
	public SmallURLService(SmallURLRepository urlRepository) {
		this.urlRepository = urlRepository;
	}

	/**
	 * Shortening URL.
	 * 
	 * @param localURL URL Host.
	 * @param longUrl  URL received to shortening.
	 * @return Shrinken URL.
	 */
	public String shrinkURL(String localURL, String longUrl) {
		LOGGER.info("Shortening {}", longUrl);

		String originalURL = searchOriginalURL(longUrl);
		String uniqueID = "";
		if ("".equals(originalURL)) {
			uniqueID = UniqueSmallURLGenerator.createUniqueURLID();
			urlRepository.saveUrl(SmallURLConstants.URL + uniqueID, longUrl);
		} else {
			uniqueID = originalURL.replaceAll(SmallURLConstants.URL, "");
		}

		localURL = URLFormatterUtil.removeHttp(localURL);

		String baseString = URLFormatterUtil.splitHostAndPort(localURL);
		String shrinkenedURL = baseString + uniqueID;
		return shrinkenedURL;

	}

	/**
	 * Retrived Original URL from uniqueID requested.
	 * 
	 * @param uniqueID Shortened URL received.
	 * @return Original URL.
	 * @throws Exception
	 */
	public String getOriginalURLFromID(String uniqueID) throws Exception {
		String longUrl = "";
		longUrl = urlRepository.getUrl(uniqueID);
		LOGGER.info("Converting shortened URL back to {}", longUrl);

		if (longUrl == null) {
			
		}

		return longUrl;
	}

	/**
	 * Searches Long URL received in list already had shortned.
	 * 
	 * @param longUrl Original URL.
	 * @return An URL shortned if it exists, else empty.
	 */
	private String searchOriginalURL(String longUrl) {
		String urlFound = urlRepository.getAllValues().entrySet().stream().filter(map -> longUrl.equals(map.getValue()))
				.map(map -> map.getKey()).collect(Collectors.joining());

		return urlFound;
	}

}
