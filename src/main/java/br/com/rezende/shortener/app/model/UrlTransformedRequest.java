package br.com.rezende.shortener.app.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO Requested.
 * 
 * @author Andre.Rezende
 * @since 10/08/2019
 */
public class UrlTransformedRequest {
	private String url;

	@JsonCreator
	public UrlTransformedRequest() {

	}

	@JsonCreator
	public UrlTransformedRequest(@JsonProperty("url") String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}