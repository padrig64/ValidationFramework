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

package com.google.code.validationframework.base.rule.number;

import com.google.code.validationframework.api.rule.Rule;

/**
 * Rule checking whether the data, being a number, is equal to a specific value.
 * <p>
 * Note that if the data and the specified value are both null or both NaN, they will be considered equal.
 *
 * @param <RI> Type of number handled by this rule.<br>
 *             It is not strictly required for the internal logic of the rule, but it helps in reducing compilation
 *             warnings and/or errors.
 *
 * @see Rule
 */
public class NumberEqualToRule<RI extends Number> implements Rule<RI, Boolean> {

    /**
     * Value to which the data is to be compared.
     */
    private RI exactValue = null;

    /**
     * Default constructor.
     */
    public NumberEqualToRule() {
        // Nothing to be done
    }

    /**
     * Constructor specifying the value to which the data is to be compared.
     *
     * @param exactValue Value to which the data is to be compared.
     */
    public NumberEqualToRule(RI exactValue) {
        setExactValue(exactValue);
    }

    /**
     * Gets the value to which the data is compared.
     *
     * @return Value to which the data is compared.
     */
    public RI getExactValue() {
        return exactValue;
    }

    /**
     * Sets the value to which the data is to be compared.
     *
     * @param exactValue Value to which the data is to be compared.
     */
    public void setExactValue(RI exactValue) {
        this.exactValue = exactValue;
    }

    /**
     * @see Rule#validate(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Boolean validate(RI data) {
        boolean valid;

        if ((data == null) && (exactValue == null)) {
            valid = true;
        } else if (data == null) {
            // exactValue is not null
            valid = false;
        } else if (exactValue == null) {
            // data is not null
            valid = false;
        } else if (data instanceof Comparable<?>) {
            // Both are not null
            valid = (((Comparable) data).compareTo(exactValue) == 0);
        } else if (exactValue instanceof Comparable<?>) {
            // Both are not null
            valid = (((Comparable) exactValue).compareTo(data) == 0);
        } else {
            // Both are not null
            valid = data.equals(exactValue);
        }

        return valid;
    }
}
