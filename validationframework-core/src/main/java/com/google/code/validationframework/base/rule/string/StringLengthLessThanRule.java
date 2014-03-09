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

package com.google.code.validationframework.base.rule.string;

/**
 * Rule checking whether the data, being a string, has a length less than a specific value.
 *
 * @see AbstractStringBooleanRule
 */
public class StringLengthLessThanRule extends AbstractStringBooleanRule {

    /**
     * String length to which the data string length is to be compared.
     */
    private int lengthLimit = Integer.MAX_VALUE;

    /**
     * Default constructor.
     */
    public StringLengthLessThanRule() {
        super();
    }

    /**
     * Constructor specifying the string length to which the data string length is to be compared.
     *
     * @param lengthLimit String length to which the data string length is to be compared.
     */
    public StringLengthLessThanRule(int lengthLimit) {
        super();
        setLengthLimit(lengthLimit);
    }

    /**
     * Gets the string length to which the data string length is compared.
     *
     * @return String length to which the data string length is compared.
     */
    public int getLengthLimit() {
        return lengthLimit;
    }

    /**
     * Sets the string length to which the data string length is compared.
     *
     * @param lengthLimit String length to which the data string length is compared.
     */
    public void setLengthLimit(int lengthLimit) {
        this.lengthLimit = lengthLimit;
    }

    /**
     * @see AbstractStringBooleanRule#validate(Object)
     */
    @Override
    public Boolean validate(String data) {
        int length = 0;
        if (data != null) {
            length = trimIfNeeded(data).length();
        }

        return (length < lengthLimit);
    }
}
