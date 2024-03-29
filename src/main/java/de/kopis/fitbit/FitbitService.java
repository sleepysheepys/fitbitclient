package de.kopis.fitbit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class FitbitService {

	public FitbitService() throws FitbitException {
		this(load());
	}

	public FitbitService(final Token accessToken) {
		this.accessToken = accessToken;
	}

	public String getProfile() throws FitbitException {
		return get("profile");
	}

	public String getActivities() throws FitbitException {
		return get("activities/date", today());
	}

	public String getFoods() throws FitbitException {
		return get("foods/log/date", today());
	}

	public String getWater() throws FitbitException {
		return get("foods/log/water/date", today());
	}

	public String getSleep() throws FitbitException {
		return get("sleep/date", today());
	}

	public String getBloodPressure() throws FitbitException {
		return get("bp/date", today());
	}

	public String getBodyMeasurements() throws FitbitException {
		return get("body/date", today());
	}

	public String getAuthorizationUrl() {
		requestToken = service.getRequestToken();
		return service.getAuthorizationUrl(requestToken);
	}

	public void authorize(final String pin) throws FitbitException {
		if (requestToken == null) {
			throw new FitbitException(
					"No request token available! Restart authorization.");
		}
		save(service.getAccessToken(requestToken, new Verifier(pin)));
	}

	private String getUser() {
		return user;
	}

	private static String getFormat() {
		return "." + FORMAT;
	}

	private String today() {
		return DATEFORMAT.format(new Date());
	}

	private String get(final String category) throws FitbitException {
		return get(category, "", "");
	}

	private String get(final String category, final String information)
			throws FitbitException {
		return get(category, information, "");
	}

	private String get(final String category, final String information,
			final String range) throws FitbitException {
		final OAuthRequest request = new OAuthRequest(Verb.GET, buildUrl(
				category, information, range));
		service.signRequest(accessToken, request);
		final Response response = request.send();
		final String data = response.getBody();
		return validate(data);
	}

	private String buildUrl(final String category, final String information,
			final String range) {
		final String url = getBaseUrl() + add(category) + add(information)
				+ add(range) + getFormat();
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("buildUrl=" + url);
		}
		return url;
	}

	private String validate(String json) throws FitbitException {
		if (json.startsWith("{\"errors\"")) {
			throw new FitbitException("Error while talking to Fitbit! " + json);
		}
		return json;
	}

	private String getBaseUrl() {
		return FitbitApi.BASE_URL + "/1/user/" + getUser();
	}

	private String add(String element) {
		String s = "";
		if (element != null && !"".equals(element)) {
			s += "/" + element;
		}
		return s;
	}

	private void save(final Token accessToken) throws FitbitException {
		this.accessToken = accessToken;

		final Properties props = new Properties();
		props.setProperty("access_token", accessToken.getToken());
		props.setProperty("access_token_secret", accessToken.getSecret());
		try {
			props.store(new FileWriter("fitbit.properties"),
					"DO NOT EDIT THIS FILE");
		} catch (final IOException e) {
			throw new FitbitException(e);
		}
	}

	private static Token load() throws FitbitException {
		final Properties props = new Properties();
		File file = new File("fitbit.properties");
		if (file.exists()) {
			if (LOG.isLoggable(Level.CONFIG)) {
				LOG.config("File fitbit.properties found.");
			}
			try {
				props.load(new FileReader(file));
			} catch (final FileNotFoundException e) {
				throw new FitbitException(e);
			} catch (final IOException e) {
				throw new FitbitException(e);
			}
		} else {
			LOG.warning("File fitbit.properties not found. Getting access token from environment...");
			props.setProperty("access_token", "");
			props.setProperty("access_token_secret", "");
		}

		updateFromSystemProperties(props);

		return new Token(props.getProperty("access_token", ""),
				props.getProperty("access_token_secret", ""));
	}

	private static void updateFromSystemProperties(final Properties props) {
		for (Object key : props.keySet()) {
			String value = System.getProperty((String) key);
			if (value != null) {
				props.setProperty((String) key, value);
			}
		}
	}

	private static final String YOUR_API_KEY = "d5c41a13861742009238d0f97a33a5e7";
	private static final String YOUR_API_SECRET = "a3aa0e3c42af42638692613eeae26447";
	private static final OAuthService service = new ServiceBuilder()
			.provider(FitbitApi.class).apiKey(YOUR_API_KEY)
			.apiSecret(YOUR_API_SECRET).build();
	private static final String FORMAT = "json";
	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private Token accessToken;
	private Token requestToken;
	private final String user = "-";
	private static final Logger LOG = Logger.getLogger(FitbitService.class
			.getName());
}