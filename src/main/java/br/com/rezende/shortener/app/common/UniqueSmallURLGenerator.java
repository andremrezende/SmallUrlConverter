package br.com.rezende.shortener.app.common;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Create Unique Array Character.
 * @author Rezende
 * @since 10/08/2019
 */
public class UniqueSmallURLGenerator {
	public static final UniqueSmallURLGenerator INSTANCE = new UniqueSmallURLGenerator();
	private static final int MAX_CHARACTERS = 36;
	private static final int MIN_CHARACTERS = 5;

	private UniqueSmallURLGenerator() {
	}

	public static String createUniqueURLID() {
		String uniqueURLID = RandomStringUtils.randomAlphanumeric(MIN_CHARACTERS, MAX_CHARACTERS);
		return uniqueURLID;
	}
}
