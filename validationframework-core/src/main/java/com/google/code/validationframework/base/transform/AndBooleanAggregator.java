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

import java.util.Collection;

/**
 * Transforms a collection of boolean values into a single boolean value by aggregating with the boolean AND operator.
 *
 * @see Aggregator
 */
public class AndBooleanAggregator implements Aggregator<Boolean, Boolean> {

    /**
     * Default boolean value to be used when transforming an empty or null collection.<br>This value can be null.
     */
    public static final Boolean DEFAULT_EMPTY_COLLECTION_VALUE = true;

    /**
     * Default boolean value to be used when transforming a null value from the collection.<br>This value can be null.
     */
    public static final Boolean DEFAULT_NULL_ELEMENT_VALUE = false;

    /**
     * Boolean value to be used when transforming an empty collection.<br>This value can be null.
     */
    private final Boolean emptyCollectionValid;

    /**
     * Boolean value to be used when transforming a null value from the collection.<br>If this value is set to null,
     * then the element will be ignored from the aggregation.
     */
    private final Boolean nullElementValid;

    /**
     * Default constructor using default values for empty and null collections, and null elements.
     *
     * @see #DEFAULT_EMPTY_COLLECTION_VALUE
     * @see #DEFAULT_NULL_ELEMENT_VALUE
     */
    public AndBooleanAggregator() {
        this(DEFAULT_EMPTY_COLLECTION_VALUE, DEFAULT_NULL_ELEMENT_VALUE);
    }

    /**
     * Constructor specifying the boolean values for empty and null collections, and null elements.
     *
     * @param emptyCollectionValue Value for empty and null collections.
     * @param nullElementValue     Value for null elements in the transformed collection.
     */
    public AndBooleanAggregator(final boolean emptyCollectionValue, final boolean nullElementValue) {
        this.emptyCollectionValid = emptyCollectionValue;
        this.nullElementValid = nullElementValue;
    }

    /**
     * Constructor specifying the boolean values for empty and null collections, and null elements.
     *
     * @param emptyCollectionValue Value for empty and null collections.
     * @param nullElementValue     Value for null elements in the transformed collection.<br>It can be null.
     */
    public AndBooleanAggregator(final Boolean emptyCollectionValue, final Boolean nullElementValue) {
        this.emptyCollectionValid = emptyCollectionValue;
        this.nullElementValid = nullElementValue;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public Boolean transform(final Collection<Boolean> elements) {
        Boolean aggregation = true;

        if ((elements == null) || elements.isEmpty()) {
            aggregation = emptyCollectionValid;
        } else {
            for (final Boolean element : elements) {
                Boolean result = element;
                if (result == null) {
                    result = nullElementValid;
                }

                // Ignore if null
                if (result != null) {
                    aggregation &= result;
                    if (!aggregation) {
                        break;
                    }
                }
            }
        }

        return aggregation;
    }
}
