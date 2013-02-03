package com.github.validationframework.base.rule;

import com.github.validationframework.api.rule.Rule;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * @author arnoud
 */
public class AndCompositeBooleanRuleTest extends AbstractCompositeBooleanRuleTest {

    @Parameterized.Parameters
    public static List<Object[]> getTestData() {
        final Object[][] data = new Object[][]{
                //             Rules (Object data, Boolean result)                                     Result
                new Object[]{new Rule[]{createRule(true), createRule(true)}, true},
                new Object[]{new Rule[]{createRule(true)}, true}, new Object[]{new Rule[]{}, true},
                new Object[]{new Rule[]{createRule(true), createRule(false)}, false},
                new Object[]{new Rule[]{createRule(false), createRule(true)}, false},
                new Object[]{new Rule[]{createRule(false), createRule(false)}, false}};
        return Arrays.asList(data);
    }

    public AndCompositeBooleanRuleTest(final Rule<Object, Boolean>[] rules, final boolean result) {
        super(rules, result);
    }

    @Override
    protected AbstractCompositeRule<Object, Boolean> createCompositeBooleanRule() {
        return new AndCompositeBooleanRule<Object>(rules);
    }

}
