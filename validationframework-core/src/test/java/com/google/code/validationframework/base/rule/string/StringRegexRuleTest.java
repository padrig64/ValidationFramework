package com.google.code.validationframework.base.rule.string;

import com.google.code.validationframework.base.rule.string.StringRegexRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author arnoud
 */
@RunWith(Parameterized.class)
public class StringRegexRuleTest {
    @Parameterized.Parameters
    public static List<Object[]> getTestData() {
        final Object[][] data = new Object[][]{
                //             Data             Pattern             Trim   Expected result
                new Object[]{"123456", "^[0-9]*$", false, true}, new Object[]{"ABC", "^[ABC]*$", false, true},
                new Object[]{" ABC ", "^[ABC]*$", false, false}, new Object[]{" ABC ", "^[ABC]*$", true, true},
                new Object[]{"ONE", "^(ONE|TWO)$", true, true}, new Object[]{"   TWO  ", "^(ONE|TWO)$", true, true},
                new Object[]{"THREE", "^(ONE|TWO)$", true, false}};
        return Arrays.asList(data);
    }

    private final String input;
    private final String pattern;
    private final boolean trim;
    private final boolean result;

    public StringRegexRuleTest(final String input, final String pattern, final boolean trim, final boolean result) {
        this.input = input;
        this.pattern = pattern;
        this.trim = trim;
        this.result = result;
    }

    @Test
    public void testResult() {
        final StringRegexRule stringRegexRule = new StringRegexRule(pattern);
        stringRegexRule.setTrimDataBeforeValidation(trim);
        assertEquals(result, stringRegexRule.validate(input));

        assertEquals(false, stringRegexRule.validate(null));
    }
}
