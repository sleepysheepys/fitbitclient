package de.kopis.fitbit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FitbitError {
	public static final String TYPE_REQUEST = "request";

	public static String getErrorMessage(JSONObject obj) {
		return ((JSONObject) ((JSONArray) obj.get("errors")).get(0)).get(
				"message").toString();
	}

	public static String getErrorType(JSONObject obj) {
		return ((JSONObject) ((JSONArray) obj.get("errors")).get(0)).get(
				"errorType").toString();
	}

	public static String getFieldname(JSONObject obj) {
		return ((JSONObject) ((JSONArray) obj.get("errors")).get(0)).get(
				"fieldName").toString();
	}
}