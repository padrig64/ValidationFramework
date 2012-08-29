package com.github.validationframework.base.rule;

import com.github.validationframework.api.rule.UntypedDataRule;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * @author: arnoud
 */
public class AndCompositeUntypedDataBooleanRuleTest extends AbstractCompositeUntypedDataBooleanRuleTest {

    @Parameterized.Parameters
    public static List<Object[]> getTestData() {
        Object[][] data = new Object[][] {
                //             UntypedDataRule (Object data, Boolean result)                                        Result
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(true), createUntypedDataRule(true) },  true },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(true) },                               true },
                new Object[] { new UntypedDataRule[] { },                                                           true },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(true), createUntypedDataRule(false) }, false },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(false), createUntypedDataRule(true) }, false },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(false), createUntypedDataRule(false) },false }
        };
        return Arrays.asList(data);
    }

    public AndCompositeUntypedDataBooleanRuleTest(UntypedDataRule<Boolean>[] rules, boolean result) {
        super(rules, result);
    }

    @Override
    protected AbstractCompositeUntypedDataRule<Boolean> createCompositeUntypedDataBooleanRule() {
        return new AndCompositeUntypedDataBooleanRule(rules);
    }
}
