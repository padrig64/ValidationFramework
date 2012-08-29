package com.github.validationframework.base.rule;

import com.github.validationframework.api.rule.TypedDataRule;
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
public abstract class AbstractCompositeTypedDataBooleanRuleTest {

    protected final TypedDataRule<Object, Boolean>[] rules;
    protected final boolean result;

    protected AbstractCompositeTypedDataBooleanRuleTest(TypedDataRule<Object, Boolean>[] rules, boolean result) {
        this.rules = rules;
        this.result = result;
    }

    protected abstract AbstractCompositeTypedDataRule<Object, Boolean> createCompositeTypedDataBooleanRule();

    public static TypedDataRule<Object, Boolean> createTypedDataRule(boolean result) {
        TypedDataRule<Object, Boolean> typedDataRule = mock(TypedDataRule.class);
        when(typedDataRule.validate(any(Object.class))).thenReturn(result);
        return typedDataRule;
    }

    @Test
    public void testResult() {
        AbstractCompositeTypedDataRule<Object, Boolean> abstractCompositeTypedDataRule = createCompositeTypedDataBooleanRule();
        assertEquals(result, abstractCompositeTypedDataRule.validate(new Object()));
    }
}
