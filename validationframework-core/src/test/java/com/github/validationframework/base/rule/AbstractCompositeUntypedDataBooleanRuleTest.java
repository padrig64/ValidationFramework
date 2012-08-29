package com.github.validationframework.base.rule;

import com.github.validationframework.api.rule.TypedDataRule;
import com.github.validationframework.api.rule.UntypedDataRule;
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
public abstract class AbstractCompositeUntypedDataBooleanRuleTest {

    protected final UntypedDataRule<Boolean>[] rules;
    protected final boolean result;

    protected AbstractCompositeUntypedDataBooleanRuleTest(UntypedDataRule<Boolean>[] rules, boolean result) {
        this.rules = rules;
        this.result = result;
    }

    public static UntypedDataRule<Boolean> createUntypedDataRule(boolean result) {
        UntypedDataRule<Boolean> untypedDataRule = mock(UntypedDataRule.class);
        when(untypedDataRule.validate()).thenReturn(result);
        return untypedDataRule;
    }

    @Test
    public void testResult() {
        AbstractCompositeUntypedDataRule<Boolean> abstractCompositeUntypedDataRule = createCompositeUntypedDataBooleanRule();
        assertEquals(result, abstractCompositeUntypedDataRule.validate());
    }

    protected abstract AbstractCompositeUntypedDataRule<Boolean> createCompositeUntypedDataBooleanRule();
}
