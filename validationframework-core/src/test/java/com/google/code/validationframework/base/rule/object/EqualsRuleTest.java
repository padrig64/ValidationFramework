/*
 * Copyright (c) 2014, Patrick Moawad
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

package com.google.code.validationframework.base.rule.object;

import com.google.code.validationframework.api.rule.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @see EqualsRule
 */
public class EqualsRuleTest {

    @Test
    public void testNull() {
        Rule<Object, Boolean> rule = new EqualsRule<Object>(null);

        assertTrue(rule.validate(null));
        assertFalse(rule.validate(new Object()));
    }

    @Test
    public void testNaN() {
        Rule<Object, Boolean> rule = new EqualsRule<Object>(Double.NaN);

        assertTrue(rule.validate(Double.NaN));
        assertTrue(rule.validate(Float.NaN));
        assertFalse(rule.validate(null));
        assertFalse(rule.validate(0));
        assertFalse(rule.validate(-54.32));

        rule = new EqualsRule<Object>(Float.NaN);
        assertTrue(rule.validate(Double.NaN));
        assertTrue(rule.validate(Float.NaN));
        assertFalse(rule.validate(null));
        assertFalse(rule.validate(0));
        assertFalse(rule.validate(-54.32));
    }

    @Test
    public void testRegular() {
        Object ref = new Object();
        Rule<Object, Boolean> rule = new EqualsRule<Object>(ref);

        assertTrue(rule.validate(ref));
        assertFalse(rule.validate(new Object()));
        assertFalse(rule.validate(null));
        assertFalse(rule.validate(Double.NaN));
    }
}
