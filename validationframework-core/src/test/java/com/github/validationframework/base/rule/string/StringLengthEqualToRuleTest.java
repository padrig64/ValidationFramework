package com.github.validationframework.base.rule.string;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * @author arnoud
 */
public class StringLengthEqualToRuleTest extends AbstractStringLengthRuleTest {

    @Parameterized.Parameters
    public static List<Object[]> getTestData() {
        final Object[][] data = new Object[][]{
                //             Data             Length  Trim   Expected result
                new Object[]{"", 10, false, false}, new Object[]{"0123456789", 10, false, true},
                new Object[]{"012345", 5, false, false}, new Object[]{"01234 ", 5, true, true},
                new Object[]{" 01234", 5, true, true}, new Object[]{" 01234 ", 5, true, true},
                new Object[]{" 01234 ", 5, false, false},};
        return Arrays.asList(data);
    }

    public StringLengthEqualToRuleTest(final String input, final int length, final boolean trimData,
                                       final boolean result) {
        super(input, length, trimData, result);
    }

    @Override
    protected AbstractStringBooleanRule createStringLengthRule() {
        final StringLengthEqualToRule stringLengthEqualToRule = new StringLengthEqualToRule(length);
        stringLengthEqualToRule.setTrimDataBeforeValidation(trimData);
        return stringLengthEqualToRule;
    }
}
