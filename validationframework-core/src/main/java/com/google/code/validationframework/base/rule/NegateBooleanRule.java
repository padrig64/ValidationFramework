/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.base.rule;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.rule.Rule;

/**
 * Rule wrapper to negate/invert the result of a provided rule.
 *
 * @param <RHI> Type of data to be validated by the wrapped rule.
 *
 * @see Rule
 * @see Disposable
 */
public class NegateBooleanRule<RHI> implements Rule<RHI, Boolean>, Disposable {

    /**
     * Default value returned when the result of the wrapped rule is null.
     */
    private static final Boolean DEFAULT_NULL_RESULT_NEGATION = true;

    /**
     * Wrapped rule whose boolean result is to be negated/inverted.
     */
    private final Rule<RHI, Boolean> wrappedRule;

    /**
     * Value to be returned when the result of the wrapped rule is null.
     */
    private final Boolean nullResultNegation;

    /**
     * Constructor specifying the wrapped rule whose result is to be negated.
     *
     * @param wrappedRule Rule whose result is to be negated.
     */
    public NegateBooleanRule(final Rule<RHI, Boolean> wrappedRule) {
        this(wrappedRule, DEFAULT_NULL_RESULT_NEGATION);
    }

    /**
     * Constructor specifying the wrapped rule whose result is to be negated and the default value to return in case
     * the wrapped rule's result is null.
     *
     * @param wrappedRule        Wrapped rule whose boolean result is to be negated/inverted.
     * @param nullResultNegation Value to be returned when the result of the wrapped rule is null.
     */
    public NegateBooleanRule(final Rule<RHI, Boolean> wrappedRule, final Boolean nullResultNegation) {
        this.wrappedRule = wrappedRule;
        this.nullResultNegation = nullResultNegation;
    }

    /**
     * @see Rule#validate(Object)
     */
    @Override
    public Boolean validate(final RHI data) {
        Boolean result = null;

        if (wrappedRule != null) {
            result = wrappedRule.validate(data);
        }

        if (result == null) {
            result = nullResultNegation;
        } else {
            result = !result;
        }

        return result;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (wrappedRule instanceof Disposable) {
            ((Disposable) wrappedRule).dispose();
        }
    }
}
