package com.github.validationframework.base.rule.string;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: arnoud
 */
public class StringLengthEqualToRuleTest extends AbstractStringLengthRuleTest {

    @Parameterized.Parameters
    public static List<Object[]> getTestData() {
        Object[][] data = new Object[][] {
                //             Data             Length  Trim   Expected result
                new Object[] { "",              10,     false, false},
                new Object[] { "0123456789",    10,     false, true},
                new Object[] { "012345",        5,      false, false},
                new Object[] { "01234 ",        5,      true,  true},
                new Object[] { " 01234",        5,      true,  true},
                new Object[] { " 01234 ",       5,      true,  true},
                new Object[] { " 01234 ",       5,      false, false},
        };
        return Arrays.asList(data);
    }

    public StringLengthEqualToRuleTest(String input, int length, boolean trimData, boolean result) {
        super(input, length, trimData, result);
    }

    @Override
    protected AbstractStringBooleanRule createStringLengthRule() {
        StringLengthEqualToRule stringLengthEqualToRule = new StringLengthEqualToRule(length);
        stringLengthEqualToRule.setTrimDataBeforeValidation(trimData);
        return stringLengthEqualToRule;
    }
}
