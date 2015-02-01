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

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.base.property.simple.SimpleProperty;

import java.text.Format;
import java.text.ParseException;

/**
 * Rule checking whether the input can be parsed using a specific format object.
 * <p/>
 * Note that if not format object is specified, the input will be considered parsable.
 */
public class IsParsableRule implements Rule<String, Boolean> {

    /**
     * Property holding the format object.
     */
    private final ReadableWritableProperty<Format, Format> formatProperty = new SimpleProperty<Format>();

    /**
     * Constructor leaving the format property unset.
     */
    public IsParsableRule() {
        // Nothing to be done
    }

    /**
     * Constructor specifying the format object to be used to check whether the input is parsable.
     * <p/>
     * The format project get be read and set using the format property.
     *
     * @param format Format to be used.
     */
    public IsParsableRule(Format format) {
        formatProperty.setValue(format);
    }

    /**
     * Gets the format property that can be used to read or set the format object to be used to check whether the input
     * is parsable.
     *
     * @return Format property.
     */
    public ReadableWritableProperty<Format, Format> getFormatProperty() {
        return formatProperty;
    }

    /**
     * @see Rule#validate(Object)
     */
    @Override
    public Boolean validate(String input) {
        boolean parsable;

        try {
            if (formatProperty.getValue() != null) {
                formatProperty.getValue().parseObject(input);
            }
            parsable = true;
        } catch (ParseException e) {
            parsable = false;
        }

        return parsable;
    }
}
