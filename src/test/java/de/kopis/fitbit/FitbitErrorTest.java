package de.kopis.fitbit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class FitbitErrorTest {

	private static final String QUOTA_MESSAGE = "{\"errors\":[{\"errorType\":\"request\",\"fieldName\":\"n/a\",\"message\":\"API CLIENT+VIEWER access quota reached for the authorized user: 22CQ9G.\"}]}";

	@Test
	public void parseErrorMessage() throws ParseException {
		JSONObject obj = (JSONObject) new JSONParser().parse(QUOTA_MESSAGE);
		assertThat(
				FitbitError.getErrorMessage(obj),
				is(equalTo("API CLIENT+VIEWER access quota reached for the authorized user: 22CQ9G.")));
	}

	@Test
	public void parseErrorType() throws ParseException {
		JSONObject obj = (JSONObject) new JSONParser().parse(QUOTA_MESSAGE);
		assertThat(FitbitError.getErrorType(obj),
				is(equalTo(FitbitError.TYPE_REQUEST)));
	}

	@Test
	public void parseErrorFieldname() throws ParseException {
		JSONObject obj = (JSONObject) new JSONParser().parse(QUOTA_MESSAGE);
		assertThat(FitbitError.getFieldname(obj), is(equalTo("n/a")));
	}
}
