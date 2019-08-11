package br.com.rezende.shortener.app.util.http;

import br.com.rezende.shortener.app.util.SmallURLConstants;

/**
 * Utils to format URLs.
 * 
 * @author Rezende
 * @since 10/08/2019
 */

public abstract class URLFormatterUtil {

	private static String host = "localhost";

	/**
	 * Splits Hosts and PORT.
	 * 
	 * @param localURL URL to format.
	 * @return new URL.
	 */
	public static String splitHostAndPort(String localURL) {
		String[] addressComponents = localURL.split("/");

		StringBuilder sb = new StringBuilder();
		for (String string : addressComponents) {
			sb.append(string);
			break;
		}
		sb.append('/');
		return sb.toString();
	}

	/**
	 * Removes HTTP or HTTPS from URL.
	 * 
	 * @param localURL any url to remove HTTP(S).
	 * @return URL received without HTTP.
	 */
	public static String removeHttp(String localURL) {
		localURL = localURL.replaceAll(SmallURLConstants.HTTP_LOWER, "");
		localURL = localURL.replaceAll(SmallURLConstants.HTTP_UPPER, "");
		localURL = localURL.replaceAll(SmallURLConstants.HTTPS_LOWER, "");
		localURL = localURL.replaceAll(SmallURLConstants.HTTPS_UPPER, "");
		return localURL;
	}

	/**
	 * Removes HOST and PORT.
	 * 
	 * @param longUrl any url to remove HTTP(S).
	 * @return URL received without HOST and PORT.
	 */
	public static String removeHostAndPort(String longUrl, int port) {
		String retURL = removeHttp(longUrl);
		retURL = retURL.replaceAll(host, "");
		retURL = retURL.replaceFirst(":", "");
		retURL = retURL.replaceFirst(String.valueOf(port), "");

		return retURL;
	}
}
