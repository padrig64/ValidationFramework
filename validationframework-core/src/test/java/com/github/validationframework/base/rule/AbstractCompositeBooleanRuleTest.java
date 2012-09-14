package com.github.validationframework.base.rule;

import com.github.validationframework.api.rule.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author: arnoud
 */
@RunWith(Parameterized.class)
public abstract class AbstractCompositeBooleanRuleTest {

	protected final Rule<Object, Boolean>[] rules;
	protected final boolean result;

	protected AbstractCompositeBooleanRuleTest(Rule<Object, Boolean>[] rules, boolean result) {
		this.rules = rules;
		this.result = result;
	}

	protected abstract AbstractCompositeRule<Object, Boolean> createCompositeBooleanRule();

	public static Rule<Object, Boolean> createRule(boolean result) {
		Rule<Object, Boolean> rule = mock(Rule.class);
		when(rule.validate(any(Object.class))).thenReturn(result);
		return rule;
	}

	@Test
	public void testResult() {
		AbstractCompositeRule<Object, Boolean> abstractCompositeRule = createCompositeBooleanRule();
		assertEquals(result, abstractCompositeRule.validate(new Object()));
	}
}
