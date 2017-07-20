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

package com.google.code.validationframework.base.rule.string;

/**
 * Simple rule making sure that the input string under validation is not empty.
 * <p>
 * The result in case of null input string can be specified. By default, null values are considered to be invalid.
 */
public class StringNotEmptyRule extends AbstractStringBooleanRule {

    /**
     * Result for null input data.
     * <p>
     * Note that it can be null.
     */
    private final Boolean nullResult;

    /**
     * Default constructor considering null input data to be invalid.
     */
    public StringNotEmptyRule() {
        this(false);
    }

    /**
     * Constructor specifying the result to be returned in case of null input data.
     *
     * @param nullResult Result for null input data, or null.
     */
    public StringNotEmptyRule(Boolean nullResult) {
        super();
        this.nullResult = nullResult;
    }

    /**
     * @see AbstractStringBooleanRule#validate(Object)
     */
    @Override
    public Boolean validate(String data) {
        Boolean result;

        if (data == null) {
            result = nullResult;
        } else {
            String trimmedData = trimIfNeeded(data);
            result = !trimmedData.isEmpty();
        }

        return result;
    }
}
