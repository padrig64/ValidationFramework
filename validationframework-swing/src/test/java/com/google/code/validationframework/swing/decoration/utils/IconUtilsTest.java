package com.google.code.validationframework.swing.decoration.utils;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class IconUtilsTest {

    @Test
    public void testInfoIcon() throws Exception {
        assertNotNull(IconUtils.INFO_ICON);
    }

    @Test
    public void testInvalidIcon() throws Exception {
        assertNotNull(IconUtils.INVALID_ICON);
    }

    @Test
    public void testValidIcon() throws Exception {
        assertNotNull(IconUtils.VALID_ICON);
    }

    @Test
    public void testWarningIcon() throws Exception {
        assertNotNull(IconUtils.WARNING_ICON);
    }
}