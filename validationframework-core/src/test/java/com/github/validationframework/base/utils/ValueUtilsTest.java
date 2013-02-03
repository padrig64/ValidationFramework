package com.github.validationframework.base.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author arnoud
 */
public class ValueUtilsTest {

    @Test
    public void nullReferencesAreEqualTo() {
        assertTrue(ValueUtils.areEqual(null, null));
    }

    @Test
    public void floatNanAreEqualTo() {
        assertTrue(ValueUtils.areEqual(Float.NaN, Float.NaN));
    }

    @Test
    public void doubleNanAreEqualTo() {
        assertTrue(ValueUtils.areEqual(Double.NaN, Double.NaN));
    }

    @Test
    public void floatNanEqualToDoubleNaN() {
        assertTrue(ValueUtils.areEqual(Double.NaN, Float.NaN));
    }

    @Test
    public void someStringAreEqualTo() {
        final String value1 = "Test";
        final String value2 = "Test";
        final String value3 = "test";

        assertEquals(value1.equals(value2), ValueUtils.areEqual(value1, value2));
        assertEquals(value1.equals(value2), ValueUtils.areEqual(value2, value1));
        assertEquals(value3.equals(value2), ValueUtils.areEqual(value3, value2));
    }
}
