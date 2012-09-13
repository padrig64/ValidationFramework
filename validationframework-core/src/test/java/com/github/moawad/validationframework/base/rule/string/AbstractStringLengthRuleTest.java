package com.github.moawad.validationframework.base.rule.string;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

/**
 * @author: arnoud
 */
@RunWith(Parameterized.class)
public abstract class AbstractStringLengthRuleTest {

	protected final String input;
	protected final int length;
	protected final boolean trimData;
	protected final boolean result;

	protected AbstractStringLengthRuleTest(String input, int length, boolean trimData, boolean result) {
		this.input = input;
		this.length = length;
		this.trimData = trimData;
		this.result = result;
	}

	protected abstract AbstractStringBooleanRule createStringLengthRule();

	@Test
	public void testResult() {
		AbstractStringBooleanRule abstractStringBooleanRule = createStringLengthRule();
		assertEquals(result, abstractStringBooleanRule.validate(input));
	}
}
