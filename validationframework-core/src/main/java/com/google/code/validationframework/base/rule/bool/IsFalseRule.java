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

package com.google.code.validationframework.base.rule.bool;

import com.google.code.validationframework.api.rule.Rule;

/**
 * Rule checking that the data is boolean whose value is false.
 *
 * @see Rule
 * @see IsTrueRule
 */
public class IsFalseRule implements Rule<Boolean, Boolean> {

    /**
     * Default result to be returned when a null value is checked.
     */
    private static final Boolean DEFAULT_RESULT_FOR_NULL_DATA = true;

    /**
     * Result to be returned when null value is checked.
     */
    private final Boolean resultForNullData;

    /**
     * Default constructor considering null data to be true.
     */
    public IsFalseRule() {
        this(DEFAULT_RESULT_FOR_NULL_DATA);
    }

    /**
     * Constructor specifying the result to be returned when a null value is checked.
     *
     * @param resultForNullData Result to be returned when null value is checked.
     */
    public IsFalseRule(boolean resultForNullData) {
        this.resultForNullData = resultForNullData;
    }

    /**
     * @see Rule#validate(Object)
     */
    @Override
    public Boolean validate(Boolean data) {
        Boolean result;

        if (data == null) {
            result = resultForNullData;
        } else {
            result = !data;
        }

        return result;
    }
}
