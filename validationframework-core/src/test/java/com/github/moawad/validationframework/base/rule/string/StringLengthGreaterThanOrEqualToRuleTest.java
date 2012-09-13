package com.github.moawad.validationframework.base.rule.string;

import java.util.Arrays;
import java.util.List;

import org.junit.runners.Parameterized;

/**
 * @author: arnoud
 */
public class StringLengthGreaterThanOrEqualToRuleTest extends AbstractStringLengthRuleTest {
	@Parameterized.Parameters
	public static List<Object[]> getTestData() {
		Object[][] data = new Object[][]{
				//             Data             Length  Trim   Expected result
				new Object[]{"",
							 10,
							 false,
							 false},
				new Object[]{"0123456789",
							 10,
							 false,
							 true},
				new Object[]{"012345",
							 5,
							 false,
							 true},
				new Object[]{"01234 ",
							 5,
							 true,
							 true},
				new Object[]{" 01234",
							 5,
							 true,
							 true},
				new Object[]{" 01234 ",
							 5,
							 true,
							 true},
				new Object[]{" 01234 ",
							 5,
							 false,
							 true},
				new Object[]{" 012   ",
							 5,
							 true,
							 false},};
		return Arrays.asList(data);
	}

	public StringLengthGreaterThanOrEqualToRuleTest(String input, int length, boolean trimData, boolean result) {
		super(input, length, trimData, result);
	}

	@Override
	protected AbstractStringBooleanRule createStringLengthRule() {
		StringLengthGreaterThanOrEqualToRule stringLengthGreaterThanOrEqualToRule
				= new StringLengthGreaterThanOrEqualToRule(length);
		stringLengthGreaterThanOrEqualToRule.setTrimDataBeforeValidation(trimData);
		return stringLengthGreaterThanOrEqualToRule;
	}

}
