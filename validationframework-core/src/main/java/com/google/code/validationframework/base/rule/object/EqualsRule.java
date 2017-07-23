/*
 * Copyright (c) 2017, ValidationFramework Authors
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
import com.google.code.validationframework.base.utils.ValueUtils;

/**
 * Rule making sure that the provided data is null.
 * <p>
 * It will return true if the data is null, and false otherwise.<br>Note that even though generics are not needed in the
 * logic of this class, providing a specific type makes it more convenient to reduce compilation warnings and errors.
 * <p>
 * Refer to {@link ValueUtils#areEqual(Object, Object)} to see how null, {@link Float#NaN} and {@link Float#NaN} values
 * are treated.
 *
 * @param <RI> Type of data to be validated.
 *
 * @see Rule
 * @see ValueUtils#areEqual(Object, Object)
 */
public class EqualsRule<RI> implements Rule<RI, Boolean> {

    private final RI referenceData;

    /**
     * Constructor specifying the reference object against which the input data will be checked.
     *
     * @param referenceData Reference object, or null.
     */
    public EqualsRule(RI referenceData) {
        this.referenceData = referenceData;
    }

    /**
     * @see Rule#validate(Object)
     * @see ValueUtils#areEqual(Object, Object)
     */
    @Override
    public Boolean validate(RI data) {
        return ValueUtils.areEqual(referenceData, data);
    }
}
