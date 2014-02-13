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

package com.google.code.validationframework.base.utils;

import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Wrapper for {@link Format} objects that adds additional convenient features.
 * <p/>
 * The wrapper allows stricter parsing of input strings. This means that if an object has been successfully parsed from
 * an input string, but if there are still characters left, the input string will be considered invalid. This differs
 * from the default best-effort parsing from the super class. For example, it may be useful to wrap the {@link java
 * .text.DecimalFormat} to consider the string "0.7dfg" invalid.
 * <p/>
 * Also, the wrapper allows null and empty strings to be parsed without throwing an exception. For example, it may be
 * useful to wrap a {@link java.text.DecimalFormat} to consider an input field valid when the operator leaves it empty.
 * <p/>
 * Additionally, the wrapper allows null objects to be formatted. The behavior is that it will just be ignore and not
 * fire any exception. Formatting a null object will result in an empty string. It may be useful to wrap a {@link
 * Format} object that does not support null values.
 * <p/>
 * Finally, the wrapper allows to transform the parsed object before returning it. This can be useful when wrapping
 * a number format, for example, that does not return always the same type of parsed object (sometimes a Long, sometimes
 * a Double, sometimes an Integer, etc.). So this wrapper makes sure that the type of output is always the same, what is
 * more useful to applications.
 *
 * @param <T> Type of parsed objects.
 *
 * @see #strictParsing
 * @see #nullValueFormatting
 * @see #nullOrEmptyTextParsing
 */
public class FormatWrapper<T> extends Format {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -1976245584938560560L;

    private final Format delegate;

    private final Transformer<Object, T> parsedObjectTransformer;

    private boolean strictParsing = true;

    private boolean nullOrEmptyTextParsing = true;

    private boolean nullValueFormatting = true;

    public FormatWrapper(Format delegate) {
        this(delegate, null);
    }

    public FormatWrapper(Format delegate, Transformer<Object, T> parsedObjectTransformer) {
        super();
        this.delegate = delegate;

        if (parsedObjectTransformer == null) {
            this.parsedObjectTransformer = new CastTransformer<Object, T>(CastTransformer.CastErrorBehavior.LOG_ERROR);
        } else {
            this.parsedObjectTransformer = parsedObjectTransformer;
        }
    }

    public boolean getStrictParsing() {
        return strictParsing;
    }

    public void setStrictParsing(boolean strictParsing) {
        this.strictParsing = strictParsing;
    }

    public boolean getNullOrEmptyTextParsing() {
        return nullOrEmptyTextParsing;
    }

    public void setNullOrEmptyTextParsing(boolean nullOrEmptyTextParsing) {
        this.nullOrEmptyTextParsing = nullOrEmptyTextParsing;
    }

    public boolean getNullValueFormatting() {
        return nullValueFormatting;
    }

    public void setNullValueFormatting(boolean nullValueFormatting) {
        this.nullValueFormatting = nullValueFormatting;
    }

    /**
     * @see Format#parseObject(String)
     */
    @Override
    public T parseObject(String source) throws ParseException {
        Object rawResult;
        if (nullOrEmptyTextParsing && ((source == null) || source.isEmpty())) {
            // Allow null and empty text, so do not use the delegate format
            rawResult = null;
        } else {
            // Delegate the parsing
            ParsePosition pos = new ParsePosition(0);
            rawResult = parseObject(source, pos);

            if (pos.getIndex() == 0) {
                // Default behavior of super class
                throw new ParseException("Failed parsing '" + source + "'", pos.getErrorIndex());
            } else if (strictParsing && (pos.getIndex() != source.length())) {
                // Stricter parsing than in super class
                throw new ParseException("Failed parsing '" + source + "'", pos.getIndex());
            }
        }

        // Transform output
        return parsedObjectTransformer.transform(rawResult);
    }

    /**
     * @see Format#parseObject(String, ParsePosition)
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return delegate.parseObject(source, pos);
    }

    /**
     * @see Format#format(Object, StringBuffer, FieldPosition)
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        StringBuffer formatted;

        if (nullValueFormatting && (obj == null)) {
            // Allow null input object: just do nothing with it
            formatted = toAppendTo;
        } else {
            formatted = delegate.format(obj, toAppendTo, pos);
        }

        return formatted;
    }
}
