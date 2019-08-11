package br.com.rezende.shortener.app;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;

import br.com.rezende.shortener.app.model.UrlTransformedRequest;

@AxisRange(min = 0, max =1)
@BenchmarkMethodChart(filePrefix = "benchmark-integrationTests")
@BenchmarkHistoryChart(labelWith = LabelType.RUN_ID, maxRuns = 1)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = URLTransformApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class IntegrationTests extends BaseTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	@LocalServerPort
	int serverPort;

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void testPageNotFound() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + serverPort + "/tinier";
		URI uri = new URI(baseUrl);
		UrlTransformedRequest urlRequest = new UrlTransformedRequest();
		urlRequest.setUrl("http://localhost:8080/xpto1");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<UrlTransformedRequest> request = new HttpEntity<>(urlRequest, headers);

		ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertTrue(404 == result.getStatusCodeValue());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void testInvalidURL() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + serverPort + "/tinier";
		URI uri = new URI(baseUrl);
		UrlTransformedRequest urlRequest = new UrlTransformedRequest();
		urlRequest.setUrl("localhost/xpto1");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<UrlTransformedRequest> request = new HttpEntity<>(urlRequest, headers);

		ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertTrue(404 == result.getStatusCodeValue());
	}
	
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void testGETPageNotFound() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + serverPort + "/abc12";
		URI uri = new URI(baseUrl);

		ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

		assertTrue(404 == result.getStatusCodeValue());
		String body = result.getBody();
		assertNull(body);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void testGETRedirectPage() throws Exception {
		StringBuilder baseUrl = new StringBuilder("http://");
		baseUrl.append(this.saveValidURL());
		
		URI uri = new URI(baseUrl.toString());
		ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

		assertTrue(301 == result.getStatusCodeValue());
		String body = result.getBody();
		assertNotNull(body);
	}
	
	@Test
	public void testExpiredPage() throws URISyntaxException, InterruptedException {
		StringBuilder baseUrl = new StringBuilder("http://");
		baseUrl.append(this.saveValidURL());
		
		//Check Time seconds in property local.redis.server.ttl in test/resource/application.test
		Thread.sleep(1500l);
		
		URI uri = new URI(baseUrl.toString());
		ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

		assertTrue(410 == result.getStatusCodeValue());
		String body = result.getBody();
		assertNull(body);
	}


	private String saveValidURL() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + serverPort + "/tinier";
		URI uri = new URI(baseUrl);
		UrlTransformedRequest urlRequest = new UrlTransformedRequest();
		urlRequest.setUrl("http://andremrezende.wordpress.com");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<UrlTransformedRequest> request = new HttpEntity<>(urlRequest, headers);

		ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertTrue(200 == result.getStatusCodeValue());
		String body = result.getBody();
		assertNotNull(body);
		assertTrue(!"".equals(body));
		assertTrue(body.contains("localhost:" + serverPort));

		return body.trim();
	}
}
