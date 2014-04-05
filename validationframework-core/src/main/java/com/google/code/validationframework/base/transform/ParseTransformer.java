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

package com.google.code.validationframework.base.transform;

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.simple.SimpleFormatProperty;

import java.text.Format;
import java.text.ParsePosition;

/**
 * Transformer using a {@link Format} to parse the input string into an object.
 * <p/>
 * It is also possible to specify whether strict parsing should be enabled or not. Strict parsing considers the input
 * string invalid if there is still some characters left to be parsed. For example, if the given {@link Format} object
 * is a {@link java.text.DecimalFormat}, parsing the string "1.4sdf" strictly will not be consider the parsed value to
 * be 1.4.
 * <p/>
 * If the format object or the input string to be parsed are null, then the output will be null.
 *
 * @param <O> Type of parsed object.
 *
 * @see ParseTransformer
 */
public class ParseTransformer<O> implements Transformer<String, O> {

    /**
     * Format object to be used for parsing;
     */
    private final ReadableWritableProperty<Format, Format> parserProperty;

    /**
     * Flag indicating whether strict parsing is enabled or not.
     */
    private boolean strictParsing;

    /**
     * Transformer used to cast the parsed object to the wanted type.
     * <p/>
     * If types are incompatible, the output value will be null.
     */
    private final Transformer<Object, O> typeTransformer = new CastTransformer<Object,
            O>(CastTransformer.CastErrorBehavior.IGNORE);

    /**
     * Constructor specifying the {@link Format} object to be used for parsing.
     * <p/>
     * Note that by default, strict parsing is enabled.
     *
     * @param parser Format object to be used for parsing.
     */
    public ParseTransformer(Format parser) {
        this(parser, true);
    }

    /**
     * Constructor specifying the {@link Format} object to be used for parsing and whether or not strict parsing should
     * be enabled or not.
     *
     * @param parser        Format object to be used for parsing.
     * @param strictParsing True to enable strict parsing, false otherwise.
     */
    public ParseTransformer(Format parser, boolean strictParsing) {
        this(new SimpleFormatProperty(parser), strictParsing);
    }

    /**
     * Constructor specifying the {@link Format} property to be used for parsing.
     * <p/>
     * Note that by default, strict parsing is enabled.
     *
     * @param parserProperty Format property to be used for parsing.
     */
    public ParseTransformer(ReadableWritableProperty<Format, Format> parserProperty) {
        this(parserProperty, true);
    }

    /**
     * Constructor specifying the {@link Format} object to be used for parsing and whether or not strict parsing should
     * be enabled or not.
     *
     * @param parserProperty Format property to be used for parsing.
     * @param strictParsing  True to enable strict parsing, false otherwise.
     */
    public ParseTransformer(ReadableWritableProperty<Format, Format> parserProperty, boolean strictParsing) {
        this.parserProperty = parserProperty;
        this.strictParsing = strictParsing;
    }

    /**
     * Gets the property holding the format object used for parsing.
     *
     * @return Format property.
     */
    public ReadableWritableProperty<Format, Format> getParserProperty() {
        return parserProperty;
    }

    /**
     * Gets the {@link Format} object used for parsing.
     *
     * @return Format object used for parsing.
     */
    public final Format getParser() {
        return parserProperty.getValue();
    }

    /**
     * Sets the {@link Format} object to be used for parsing.
     *
     * @param parser Format object to be used for parsing.
     */
    public final void setParser(Format parser) {
        parserProperty.setValue(parser);
    }

    /**
     * States whether strict parsing is enabled or not.
     *
     * @return True if strict parsing is enabled, false otherwise.
     */
    public boolean getStrictParsing() {
        return strictParsing;
    }

    /**
     * States whether strict parsing should be enabled or not.
     *
     * @param strictParsing True to enabled strict parsing, false otherwise.
     */
    public void setStrictParsing(boolean strictParsing) {
        this.strictParsing = strictParsing;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public O transform(String input) {
        O value = null;

        if ((input != null) && (parserProperty.getValue() != null)) {
            // Parse
            ParsePosition pos = new ParsePosition(0);
            Object object = parserProperty.getValue().parseObject(input, pos);

            // Cast if valid
            if ((pos.getIndex() != 0) && (!strictParsing || (pos.getIndex() == input.length()))) {
                value = typeTransformer.transform(object);
            }
        }

        return value;
    }
}
