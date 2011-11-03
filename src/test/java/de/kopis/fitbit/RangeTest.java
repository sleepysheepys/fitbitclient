package de.kopis.fitbit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

public class RangeTest {

	@Test
	public void buildRange() {
		assertThat(new Range(new Date(0), 5, "w").build(),
				is(equalTo("1970-01-01/5w")));
	}
}
