package com.github.moawad.validationframework.base.rule;

import java.util.Arrays;
import java.util.List;

import com.github.moawad.validationframework.api.rule.Rule;
import org.junit.runners.Parameterized;

/**
 * @author: arnoud
 */
public class AndCompositeBooleanRuleTest extends AbstractCompositeBooleanRuleTest {

	@Parameterized.Parameters
	public static List<Object[]> getTestData() {
		Object[][] data = new Object[][]{
				//             Rules (Object data, Boolean result)                                     Result
				new Object[]{new Rule[]{createRule(true),
										createRule(true)},
							 true},
				new Object[]{new Rule[]{createRule(true)},
							 true},
				new Object[]{new Rule[]{},
							 true},
				new Object[]{new Rule[]{createRule(true),
										createRule(false)},
							 false},
				new Object[]{new Rule[]{createRule(false),
										createRule(true)},
							 false},
				new Object[]{new Rule[]{createRule(false),
										createRule(false)},
							 false}};
		return Arrays.asList(data);
	}

	public AndCompositeBooleanRuleTest(Rule<Object, Boolean>[] rules, boolean result) {
		super(rules, result);
	}

	@Override
	protected AbstractCompositeRule<Object, Boolean> createCompositeBooleanRule() {
		return new AndCompositeBooleanRule<Object>(rules);
	}

}
