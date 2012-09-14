package com.github.validationframework.base.rule.string;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

/**
 * @author: arnoud
 */
@RunWith(Parameterized.class)
public class StringRegexRuleTest {
	@Parameterized.Parameters
	public static List<Object[]> getTestData() {
		Object[][] data = new Object[][] {
				//             Data             Pattern             Trim   Expected result
				new Object[] { "123456", "^[0-9]*$", false, true }, new Object[] { "ABC", "^[ABC]*$", false, true },
				new Object[] { " ABC ", "^[ABC]*$", false, false }, new Object[] { " ABC ", "^[ABC]*$", true, true },
				new Object[] { "ONE", "^(ONE|TWO)$", true, true },
				new Object[] { "   TWO  ", "^(ONE|TWO)$", true, true },
				new Object[] { "THREE", "^(ONE|TWO)$", true, false } };
		return Arrays.asList(data);
	}

	private final String input;
	private final String pattern;
	private final boolean trim;
	private final boolean result;

	public StringRegexRuleTest(String input, String pattern, boolean trim, boolean result) {
		this.input = input;
		this.pattern = pattern;
		this.trim = trim;
		this.result = result;
	}

	@Test
	public void testResult() {
		StringRegexRule stringRegexRule = new StringRegexRule(pattern);
		stringRegexRule.setTrimDataBeforeValidation(trim);
		assertEquals(result, stringRegexRule.validate(input));
	}
}
