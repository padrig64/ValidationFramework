package com.github.validationframework.base.rule;

import com.github.validationframework.api.rule.TypedDataRule;
import com.github.validationframework.api.rule.UntypedDataRule;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * @author: arnoud
 */
public class OrCompositeUntypedDataBooleanRuleTest extends AbstractCompositeUntypedDataBooleanRuleTest {

    @Parameterized.Parameters
    public static List<Object[]> getTestData() {
        Object[][] data = new Object[][] {
                //             UntypedDataRules (Object data, Boolean result)                                       Result
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(true), createUntypedDataRule(true) },  true },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(true) },                               true },
                new Object[] { new UntypedDataRule[] { },                                                           false },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(true), createUntypedDataRule(false) }, true },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(false), createUntypedDataRule(true) }, true },
                new Object[] { new UntypedDataRule[] { createUntypedDataRule(false), createUntypedDataRule(false) },false }
        };
        return Arrays.asList(data);
    }

    public OrCompositeUntypedDataBooleanRuleTest(UntypedDataRule<Boolean>[] rules, boolean result) {
        super(rules, result);
    }

    @Override
    protected AbstractCompositeUntypedDataRule<Boolean> createCompositeUntypedDataBooleanRule() {
        return new OrCompositeUntypedDataBooleanRule(rules);
    }
}
