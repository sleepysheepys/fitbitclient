package de.kopis.fitbit;

import java.io.IOException;

public class FitbitException extends Exception {
	private static final long serialVersionUID = 1L;

	public FitbitException(IOException e) {
		super(e);
	}

	public FitbitException(String msg) {
		super(msg);
	}

}
