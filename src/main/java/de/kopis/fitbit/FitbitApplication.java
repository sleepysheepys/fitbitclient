package de.kopis.fitbit;

import java.io.Console;


public class FitbitApplication {

	public static void main(String[] args) throws FitbitException {
		FitbitService service = new FitbitService();

		Console console = System.console();
		System.out.println("Open " + service.getAuthorizationUrl());
		String pin = console.readLine("Enter PIN:");
		service.authorize(pin);
		
		System.out.println("Profile: " + service.getProfile());
	}

}
