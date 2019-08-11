package br.com.rezende.shortener.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import br.com.rezende.shortener.app.common.URLChecker;
import br.com.rezende.shortener.app.exception.SmalURLExpiredException;
import br.com.rezende.shortener.app.model.UrlTransformedRequest;
import br.com.rezende.shortener.app.service.SmallURLService;
import br.com.rezende.shortener.app.util.SmallURLConstants;
import br.com.rezende.shortener.app.util.http.URLFormatterUtil;

/**
 * Shrinks and Redirect an URL Requested.
 * 
 * @author Andre.Rezende
 * @since 10/08/2019
 */
@RestController
public class SmallURLController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmallURLController.class);
	private final SmallURLService urlConverterService;

	@LocalServerPort
	private int port;

	/**
	 * 
	 * @param urlConverterService
	 */
	public SmallURLController(SmallURLService urlConverterService) {
		this.urlConverterService = urlConverterService;
	}

	/**
	 * Convert a long URL to small URL.
	 * 
	 * @param shrikenRequest Long URL.
	 * @param request        Client HTTP Request.
	 * @return String of shortned URL.
	 * @throws Exception Invald URL.
	 */
	@RequestMapping(value = "/tinier", method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<String> tinierUrl(@RequestBody @Valid final UrlTransformedRequest shrikenRequest,
			HttpServletRequest request) throws Exception {
		LOGGER.info("Received url to shriken: " + shrikenRequest.getUrl());

		String longUrl = shrikenRequest.getUrl();
		String shrinkedUrl = longUrl;

		if (longUrl.contains(SmallURLConstants.REDIS_HOST)) {
			try {
				shrinkedUrl = urlConverterService
						.getOriginalURLFromID(URLFormatterUtil.removeHostAndPort(longUrl, port));
			} catch (Exception e) {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} else {
			if (URLChecker.INSTANCE.validateURL(longUrl)) {
				String localURL = request.getRequestURL().toString();
				shrinkedUrl = urlConverterService.shrinkURL(localURL, longUrl);

				LOGGER.info("Shrinked url to: " + shrinkedUrl);
			} else {
				throw new Exception("Please enter a valid URL");
			}
		}
		return new ResponseEntity<>(shrinkedUrl, HttpStatus.OK);
	}

	/**
	 * Redirect Shortned URL to Original URL Shortned.
	 * 
	 * @param uniqueID Identification of URL Shortned.
	 * @param request  Client HTTP Request.
	 * @param response Response to redirect Long URL.
	 * @return Original URL Shortned.
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws Exception          URL not found.
	 */
	@RequestMapping(value = "/{uniqueID}", method = RequestMethod.GET)
	public RedirectView redirectUrl(@PathVariable String uniqueID, HttpServletRequest request,
			HttpServletResponse response) throws IOException, URISyntaxException, Exception {
		LOGGER.info("Received shrikned url to redirect: " + uniqueID);

		String redirectUrlString = null;
		RedirectView redirectView = new RedirectView("");
		try {
			redirectUrlString = urlConverterService.getOriginalURLFromID(uniqueID);
			if (redirectUrlString == null || "".equals(redirectUrlString.trim())) {
				redirectUrlString = String.format("%s:%s/%s", SmallURLConstants.REDIS_HOST, "8080", "error/404.html");
			}
			LOGGER.info("Original URL: " + redirectUrlString);

			redirectView.setUrl(redirectUrlString);
		} catch (SmalURLExpiredException e) {
			redirectView.setStatusCode(HttpStatus.GONE);
		} catch (Exception e) {
			// PageNotFound
			redirectView.setStatusCode(HttpStatus.NOT_FOUND);
		}
		return redirectView;
	}

}
