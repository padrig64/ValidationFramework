/*
 * Copyright (c) 2015, Patrick Moawad
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

import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.CastTransformer;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Wrapper for {@link Format} objects that adds convenient features.
 * <p/>
 * The wrapper allows stricter parsing of input strings. This means that if an object has been successfully parsed from
 * an input string, but if there are still characters left, the input string will be considered invalid. This differs
 * from the default best-effort parsing from the super class. For example, it may be useful to wrap the {@link java
 * .text.DecimalFormat} to consider the string "0.7dfg" invalid.
 * <p/>
 * Also, the wrapper allows null and empty strings to be parsed without throwing an exception. For example, it may be
 * useful to wrap a {@link java.text.DecimalFormat} to consider an input field valid when the user leaves the field
 * empty.
 * <p/>
 * Additionally, the wrapper allows null objects to be formatted. The behavior is that it will just be ignore and not
 * fire any exception. Formatting a null object will result in an empty string. It may be useful to wrap a {@link
 * Format} object that does not support null values.
 * <p/>
 * Finally, the wrapper allows to transform the parsed object before returning it. This can be useful when wrapping
 * a number format, for example, that does not return always the same type of parsed {@link java.lang.Number} (sometimes
 * a Long, sometimes a Double, sometimes an Integer, etc.). So this wrapper makes sure that the type of output is always
 * the same, what can be more convenient to applications.
 *
 * @param <T> Type of parsed objects.
 *
 * @see #strictParsing
 * @see #delegateNullValueFormatting
 * @see #delegateNullOrEmptyTextParsing
 */
public class FormatWrapper<T> extends Format {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -1976245584938560560L;

    /**
     * Delegate format used to format and parse.
     */
    private final Format delegate;

    /**
     * Transformer used to convert successfully parsed objects to a specific type.
     * <p/>
     * By default, the transformation will just be a cast.
     */
    private final Transformer<Object, T> parsedObjectTransformer;

    /**
     * Flag indicating whether strict parsing should be enabled.
     */
    private boolean strictParsing = true;

    /**
     * Flag indicating whether the delegate format should try parse of null or empty text by itself.
     */
    private boolean delegateNullOrEmptyTextParsing = false;

    /**
     * Flag indicating whether the delegate format object should try to format null values by itself.
     */
    private boolean delegateNullValueFormatting = false;

    /**
     * Constructor specifying the delegate format.
     * <p/>
     * By default, strict parsing will be enabled, parsing of null or empty text will not be delegated, and formatting
     * of null values will not be delegated either.
     *
     * @param delegate Delegate format to be used for formatting and parsing.
     */
    public FormatWrapper(Format delegate) {
        this(delegate, null);
    }

    /**
     * Constructor specifying the delegate format and whether the parsing of null or empty text should be delegated.
     * <p/>
     * By default, strict parsing will be enabled, and formatting of null values will not be delegated either.
     *
     * @param delegate                       Delegate format to be used for formatting and parsing.
     * @param delegateNullOrEmptyTextParsing True to delegate the parsing of null or empty texts, false not to delegate.
     */
    public FormatWrapper(Format delegate, boolean delegateNullOrEmptyTextParsing) {
        this(delegate, null);
        setDelegateNullOrEmptyTextParsing(delegateNullOrEmptyTextParsing);
    }

    /**
     * Constructor specifying the delegate format and the transformer to be used to convert the parsed objects.
     * <p/>
     * By default, strict parsing will be enabled, parsing of null or empty text will not be delegated, and formatting
     * of null values will not be delegated either.
     *
     * @param delegate                Delegate format to be used for formatting and parsing.
     * @param parsedObjectTransformer Transformer to convert the parsed objects
     */
    public FormatWrapper(Format delegate, Transformer<Object, T> parsedObjectTransformer) {
        super();
        this.delegate = delegate;

        if (parsedObjectTransformer == null) {
            this.parsedObjectTransformer = new CastTransformer<Object, T>(CastTransformer.CastErrorBehavior.LOG_ERROR);
        } else {
            this.parsedObjectTransformer = parsedObjectTransformer;
        }
    }

    /**
     * Gets the delegate format used to format and parse.
     *
     * @return Delegate format.
     */
    public Format getDelegateFormat() {
        return delegate;
    }

    /**
     * States whether strict parsing is enabled.
     *
     * @return True if strict parsing is enabled, false otherwise.
     */
    public boolean getStrictParsing() {
        return strictParsing;
    }

    /**
     * States whether strict parsing should be enabled.
     *
     * @param strictParsing True to enabled strict parsing, false to disable it.
     */
    public void setStrictParsing(boolean strictParsing) {
        this.strictParsing = strictParsing;
    }

    /**
     * States whether the parsing of null or empty texts is delegated.
     *
     * @return True if the parsing of null or empty texts is delegated, false otherwise.
     */
    public boolean getDelegateNullOrEmptyTextParsing() {
        return delegateNullOrEmptyTextParsing;
    }

    /**
     * States whether the parsing of null or empty texts should be delegated.
     * <p/>
     * If it is delegated, a {@link ParseException} might be thrown depending on the behavior of the delegate format. If
     * it is not delegated, no exception will be thrown as if the input is valid.
     *
     * @param delegateNullOrEmptyTextParsing True to delegate the parsing of null or empty texts, false not to delegate.
     */
    public void setDelegateNullOrEmptyTextParsing(boolean delegateNullOrEmptyTextParsing) {
        this.delegateNullOrEmptyTextParsing = delegateNullOrEmptyTextParsing;
    }

    /**
     * States whether the formatting of null values is delegated.
     *
     * @return True if the formatting of null values is delegated, false otherwise.
     */
    public boolean getDelegateNullValueFormatting() {
        return delegateNullValueFormatting;
    }

    /**
     * States whether the formatting of null values should be delegated.
     *
     * @param delegateNullValueFormatting True to delegate the formatting of null values, false not to delegate.
     */
    public void setDelegateNullValueFormatting(boolean delegateNullValueFormatting) {
        this.delegateNullValueFormatting = delegateNullValueFormatting;
    }

    /**
     * @see Format#parseObject(String)
     */
    @Override
    public T parseObject(String source) throws ParseException {
        Object rawResult;
        if (delegateNullOrEmptyTextParsing || ((source != null) && !source.isEmpty())) {
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
        } else {
            // Allow null and empty text, so do not use the delegate format
            rawResult = null;
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

        if (delegateNullValueFormatting || (obj != null)) {
            formatted = delegate.format(obj, toAppendTo, pos);
        } else {
            // Allow null input object: just do nothing with it
            formatted = toAppendTo;
        }

        return formatted;
    }
}
