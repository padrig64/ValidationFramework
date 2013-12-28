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

package com.google.code.validationframework.base.transform;

import java.text.Format;

/**
 * Transformer using a {@link Format} to format the input object into a string.
 * <p/>
 * If the specified format object or the input object to be formatted are null, then the output will be null.
 *
 * @see ParseTransformer
 */
public class FormatTransformer implements Transformer<Object, String> {

    /**
     * Format object to be used for formatting.
     */
    private Format format = null;

    /**
     * Constructor specifying the format object to be used for formatting.
     *
     * @param format Format object to be used for formatting.
     */
    public FormatTransformer(Format format) {
        this.format = format;
    }

    /**
     * Gets the format object used for formatting.
     *
     * @return Format object used for formatting.
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Sets the format object to be used for formatting.
     *
     * @param format Format object to be used for formatting.
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public String transform(Object input) {
        String output = null;

        if (format != null && input != null) {
            output = format.format(input);
        }

        return output;
    }
}
