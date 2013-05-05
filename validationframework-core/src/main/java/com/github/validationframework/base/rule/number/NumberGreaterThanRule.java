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

package com.github.validationframework.base.rule.number;

import com.github.validationframework.api.rule.Rule;

/**
 * Rule checking whether the data, being a number, is strictly greater than a specific value.<br>Note that if the data
 * and the specified value are both null, they will be considered equal. If they are by NaN, they will be considered
 * equal. And everything is considered bigger than null.
 *
 * @param <T> Type of number handled by this rule.<br>It also it is not really required for the internal logic of the
 *            rule, it helps in reducing compilation warnings and/or errors when add a rule in a validator.
 *
 * @see Rule
 */
public class NumberGreaterThanRule<T extends Number> implements Rule<T, Boolean> {

    /**
     * Value to which the data is to be compared.
     */
    private T minimumValue = null;

    /**
     * Default constructor.
     */
    public NumberGreaterThanRule() {
        // Nothing to be done
    }

    /**
     * Constructor specifying the value to which the data is to be compared.
     *
     * @param minimumValue Value to which the data is to be compared.
     */
    public NumberGreaterThanRule(final T minimumValue) {
        setMinimumValue(minimumValue);
    }

    /**
     * Gets the value to which the data is compared.
     *
     * @return Value to which the data is compared.
     */
    public Number getMinimumValue() {
        return minimumValue;
    }

    /**
     * Sets the value to which the data is to be compared.
     *
     * @param minimumValue Value to which the data is to be compared.
     */
    public void setMinimumValue(final T minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * @see Rule#validate(Object)
     */
    @Override
    public Boolean validate(final T data) {
        final boolean valid;

        if ((data == null) && (minimumValue == null)) {
            valid = false;
        } else if (data == null) {
            // exactValue is not null, everything is bigger than null
            valid = false;
        } else if (minimumValue == null) {
            // data is not null, everything is bigger than null
            valid = true;
        } else if (data instanceof Comparable) {
            // Both are not null
            valid = (((Comparable) data).compareTo(minimumValue) > 0);
        } else if (minimumValue instanceof Comparable<?>) {
            // Both are not null
            valid = (((Comparable) minimumValue).compareTo(data) < 0);
        } else {
            // Both are not null
            valid = false;
        }

        return valid;
    }
}
