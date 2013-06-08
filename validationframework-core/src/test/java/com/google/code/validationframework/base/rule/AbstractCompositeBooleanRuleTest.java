package com.google.code.validationframework.base.rule;

import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.base.rule.AbstractCompositeRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author arnoud
 */
@RunWith(Parameterized.class)
public abstract class AbstractCompositeBooleanRuleTest {

    protected final Rule<Object, Boolean>[] rules;
    protected final boolean result;

    protected AbstractCompositeBooleanRuleTest(final Rule<Object, Boolean>[] rules, final boolean result) {
        this.rules = rules;
        this.result = result;
    }

    protected abstract AbstractCompositeRule<Object, Boolean> createCompositeBooleanRule();

    @SuppressWarnings("unchecked")
    public static Rule<Object, Boolean> createRule(final boolean result) {
        final Rule<Object, Boolean> rule = mock(Rule.class);
        when(rule.validate(any(Object.class))).thenReturn(result);
        return rule;
    }

    @Test
    public void testResult() {
        final AbstractCompositeRule<Object, Boolean> abstractCompositeRule = createCompositeBooleanRule();
        assertEquals(result, abstractCompositeRule.validate(new Object()));
    }
}
