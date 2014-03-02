/*
 * Copyright (c) 2013, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.google.code.validationframework.base.utils;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;

import static org.junit.Assert.assertTrue;

/**
 * @see FormatWrapper
 */
public class FormatWrapperTest {

    @Test
    public void testStrictParsing() {
        Format delegate = new DecimalFormat();
        FormatWrapper<Object> wrapper = new FormatWrapper<Object>(delegate);

        // Make sure the rest of the test is relevant: parsing should not fail using the delegate
        String text = "5.77dfgdf";
        try {
            delegate.parseObject(text);
        } catch (ParseException e) {
            // Not expected
            assertTrue(false);
        }

        // Disable strict parsing and parse the same text using the wrapper: same behavior as above
        wrapper.setStrictParsing(false);
        try {
            wrapper.parseObject(text);
        } catch (ParseException e) {
            // Test failed
            assertTrue(false);
        }

        // Enable strict parsing and parse the same text: parsing should now fail
        wrapper.setStrictParsing(true);
        try {
            wrapper.parseObject(text);
            // Test failed
            assertTrue(false);
        } catch (ParseException e) {
            // Test passed
        }
    }

    @Test
    public void testNullAndEmptyTextParsing() {
        Format delegate = new DecimalFormat();
        FormatWrapper<Object> wrapper = new FormatWrapper<Object>(delegate);

        // Make sure the rest of the test is relevant: parsing should fail using the delegate
        try {
            delegate.parseObject(null);
            // Not expected
            assertTrue(false);
        } catch (NullPointerException e) {
            // Expected
        } catch (ParseException e) {
            // Not expected
            assertTrue(false);
        }
        try {
            delegate.parseObject("");
            // Not expected
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }

        // Enable delegation of null/empty text parsing: same behavior as above
        wrapper.setDelegateNullOrEmptyTextParsing(true);
        try {
            wrapper.parseObject(null);
            // Not expected
            assertTrue(false);
        } catch (NullPointerException e) {
            // Expected
        } catch (ParseException e) {
            // Not expected
            assertTrue(false);
        }
        try {
            wrapper.parseObject("");
            // Not expected
            assertTrue(false);
        } catch (ParseException e) {
            assertTrue(true);
        }

        // Disable delegation of null/empty text parsing: parsing should succeed
        wrapper.setDelegateNullOrEmptyTextParsing(false);
        try {
            wrapper.parseObject(null);
        } catch (NullPointerException e) {
            // Not expected
            assertTrue(false);
        } catch (ParseException e) {
            // Not expected
            assertTrue(false);
        }
        try {
            wrapper.parseObject("");
        } catch (ParseException e) {
            // Not expected
            assertTrue(false);
        }
    }

    @Test
    public void testNullValueFormatting() {
        Format delegate = new DecimalFormat();
        FormatWrapper<Object> wrapper = new FormatWrapper<Object>(delegate);

        // Make sure the rest of the test is relevant: formatting should fail using the delegate
        try {
            delegate.format(null);
            // Not expected
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Enable delegation of null value formatting: same behavior as above
        wrapper.setDelegateNullValueFormatting(true);
        try {
            wrapper.format(null);
            // Not expected
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Disable delegation of null value formatting: same behavior as above
        wrapper.setDelegateNullValueFormatting(false);
        try {
            wrapper.format(null);
        } catch (IllegalArgumentException e) {
            // Not expected
            assertTrue(false);
        }
    }
}
