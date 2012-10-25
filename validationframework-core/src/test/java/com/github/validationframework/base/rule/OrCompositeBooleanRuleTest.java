package com.github.validationframework.base.rule;

import com.github.validationframework.api.rule.Rule;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized;

/**
 * @author arnoud
 */
public class OrCompositeBooleanRuleTest extends AbstractCompositeBooleanRuleTest {

	@Parameterized.Parameters
	public static List<Object[]> getTestData() {
		final Object[][] data = new Object[][] {
				//             Rules (Object data, Boolean result)                                     Result
				new Object[] { new Rule[] { createRule(true), createRule(true) }, true },
				new Object[] { new Rule[] { createRule(true) }, true }, new Object[] { new Rule[] { }, true },
				new Object[] { new Rule[] { createRule(true), createRule(false) }, true },
				new Object[] { new Rule[] { createRule(false), createRule(true) }, true },
				new Object[] { new Rule[] { createRule(false), createRule(false) }, false } };
		return Arrays.asList(data);
	}

	public OrCompositeBooleanRuleTest(final Rule<Object, Boolean>[] rules, final boolean result) {
		super(rules, result);
	}

	@Override
	protected AbstractCompositeRule<Object, Boolean> createCompositeBooleanRule() {
		return new OrCompositeBooleanRule<Object>(rules);
	}
}
