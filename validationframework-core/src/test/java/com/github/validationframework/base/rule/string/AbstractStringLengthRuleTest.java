package com.github.validationframework.base.rule.string;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

/**
 * @author arnoud
 */
@RunWith(Parameterized.class)
public abstract class AbstractStringLengthRuleTest {

    protected final String input;
    protected final int length;
    protected final boolean trimData;
    protected final boolean result;

    protected AbstractStringLengthRuleTest(final String input, final int length, final boolean trimData,
                                           final boolean result) {
        this.input = input;
        this.length = length;
        this.trimData = trimData;
        this.result = result;
    }

    protected abstract AbstractStringBooleanRule createStringLengthRule();

    @Test
    public void testResult() {
        final AbstractStringBooleanRule abstractStringBooleanRule = createStringLengthRule();
        assertEquals(result, abstractStringBooleanRule.validate(input));
    }
}
