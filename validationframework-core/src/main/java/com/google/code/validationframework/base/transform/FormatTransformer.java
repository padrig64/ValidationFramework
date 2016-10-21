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

package com.google.code.validationframework.base.transform;

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.simple.SimpleFormatProperty;

import java.text.Format;

/**
 * Transformer using a {@link Format} to format the input object into a string.
 * <p/>
 * If the specified format object or the input object to be formatted are null, then the output will be null. If the
 * input object cannot be formatted (the {@link Format} object throwing an {@link IllegalArgumentException}), then the
 * output will be null.
 *
 * @see ParseTransformer
 */
public class FormatTransformer<T> implements Transformer<T, String> {

    /**
     * Format property to be used for formatting.
     */
    private final ReadableWritableProperty<Format> formatProperty;

    /**
     * Constructor specifying the format object to be used for formatting.
     *
     * @param format Format object to be used for formatting.
     */
    public FormatTransformer(Format format) {
        this(new SimpleFormatProperty(format));
    }

    /**
     * Constructor specifying the format object to be used for formatting.
     *
     * @param formatProperty Format property to be used for formatting.
     */
    public FormatTransformer(ReadableWritableProperty<Format> formatProperty) {
        this.formatProperty = formatProperty;
    }

    /**
     * Gets the property holding the format object used for formatting.
     *
     * @return Format property.
     */
    public ReadableWritableProperty<Format> getFormatProperty() {
        return formatProperty;
    }

    /**
     * Gets the format object used for formatting.
     *
     * @return Format object used for formatting.
     */
    public final Format getFormat() {
        return formatProperty.getValue();
    }

    /**
     * Sets the format object to be used for formatting.
     *
     * @param format Format object to be used for formatting.
     */
    public final void setFormat(Format format) {
        formatProperty.setValue(format);
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public String transform(T input) {
        String output;

        if (input == null) {
            output = null;
        } else if (formatProperty.getValue() == null) {
            output = input.toString();
        } else {
            try {
                output = formatProperty.getValue().format(input);
            } catch (IllegalArgumentException e) {
                // Ignore: null will be returned
                output = null;
            }
        }

        return output;
    }
}
