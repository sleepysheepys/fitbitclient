package de.kopis.fitbit;

import static org.hamcrest.text.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class FitbitScribeTest {

	private FitbitService service;

	@Test
	@Ignore
	public void authorize() throws FitbitException, IOException {
		final String authUrl = service.getAuthorizationUrl();
		assertThat(authUrl,
				startsWith("https://www.fitbit.com/oauth/authorize"));
		System.out.println("Open " + authUrl + " and enter PIN:");
		final String pin = read();
		service.authorize(pin);
	}

	@Test
	public void getProfile() throws IOException, FitbitException {
		final String data = service.getProfile();
		assertThat(data, startsWith("{\"user\":{"));
	}

	@Test
	public void getBodyMeasurements() throws IOException, FitbitException {
		final String data = service.getBodyMeasurements();
		assertThat(data, startsWith("{\"body\":{"));
	}

	@Test
	public void getActivities() throws IOException, FitbitException {
		final String data = service.getActivities();
		assertThat(data, startsWith("{\"activities\":["));
	}

	@Test
	public void getFoods() throws IOException, FitbitException {
		final String data = service.getFoods();
		assertThat(data, startsWith("{\"foods\":["));
	}

	@Test
	public void getWater() throws IOException, FitbitException {
		final String data = service.getWater();
		assertThat(data, startsWith("{\"summary\":{\"water\":"));
	}

	@Test
	public void getSleep() throws IOException, FitbitException {
		final String data = service.getSleep();
		assertThat(data, startsWith("{\"sleep\":["));
	}

	@Test
	public void getBloodPressure() throws IOException, FitbitException {
		final String data = service.getBloodPressure();
		assertThat(data, startsWith("{\"bp\":["));
	}

	@Before
	public void setUp() throws FitbitException {
		service = new FitbitService();
	}

	private String read() throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		final String line = br.readLine();
		br.close();
		return line;
	}

}
