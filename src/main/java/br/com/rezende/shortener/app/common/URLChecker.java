package br.com.rezende.shortener.app.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checkes if an URL is valid.
 * @author Andre.Rezende
 * @since 10/08/2019
 */
public class URLChecker {
	public static final URLChecker INSTANCE = new URLChecker();
	private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

	private URLChecker() {
	}

	/**
	 * Validates URL.
	 * @param url String of URL.
	 * @return True if URL is valid, false some else.
	 */
	public boolean validateURL(String url) {
		Matcher m = URL_PATTERN.matcher(url);
		return m.matches();
	}
}
