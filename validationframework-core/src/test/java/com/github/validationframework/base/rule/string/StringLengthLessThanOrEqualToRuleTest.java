package com.github.validationframework.base.rule.string;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * @author arnoud
 */
public class StringLengthLessThanOrEqualToRuleTest extends AbstractStringLengthRuleTest {

    @Parameterized.Parameters
    public static List<Object[]> getTestData() {
        final Object[][] data = new Object[][]{
                //             Data             Length  Trim   Expected result
                new Object[]{"", 10, false, true}, new Object[]{"0123456789", 10, false, true},
                new Object[]{"012345", 5, false, false}, new Object[]{"01234 ", 5, true, true},
                new Object[]{" 01234", 5, true, true}, new Object[]{" 01234 ", 5, true, true},
                new Object[]{" 01234 ", 5, false, false}, new Object[]{" 012   ", 5, true, true},};
        return Arrays.asList(data);
    }

    public StringLengthLessThanOrEqualToRuleTest(final String input, final int length, final boolean trimData,
                                                 final boolean result) {
        super(input, length, trimData, result);
    }

    @Override
    protected AbstractStringBooleanRule createStringLengthRule() {
        final StringLengthLessThanOrEqualToRule stringLengthLessThanOrEqualToRule = new
                StringLengthLessThanOrEqualToRule(length);
        stringLengthLessThanOrEqualToRule.setTrimDataBeforeValidation(trimData);
        return stringLengthLessThanOrEqualToRule;
    }
}
