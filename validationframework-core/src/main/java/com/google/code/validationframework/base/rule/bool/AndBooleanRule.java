/*
 * Copyright (c) 2017, ValidationFramework Authors
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

package com.google.code.validationframework.base.rule.bool;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.AndBooleanAggregator;

import java.util.Collection;

/**
 * Rule validating a collection of boolean values using the boolean AND operator.
 *
 * @see Rule
 * @see OrBooleanRule
 * @see Disposable
 */
public class AndBooleanRule implements Rule<Collection<Boolean>, Boolean>, Disposable {

    /**
     * Default boolean result for empty or null collections.
     */
    public static final boolean DEFAULT_EMPTY_COLLECTION_VALID = AndBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE;

    /**
     * Default boolean value for null elements in the collections.
     */
    public static final boolean DEFAULT_NULL_ELEMENT_VALID = AndBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE;

    /**
     * Boolean aggregator using the AND operator.
     *
     * @see AndBooleanAggregator
     */
    private final Transformer<Collection<Boolean>, Boolean> aggregator;

    /**
     * Default constructor using default results for empty collections and null elements.
     */
    public AndBooleanRule() {
        this(DEFAULT_EMPTY_COLLECTION_VALID, DEFAULT_NULL_ELEMENT_VALID);
    }

    /**
     * Constructor specifying the results for empty and null collections, and null elements.
     *
     * @param emptyCollectionValid Result for empty and null collections.
     * @param nullElementValid     Boolean value for null elements.
     */
    public AndBooleanRule(boolean emptyCollectionValid, boolean nullElementValid) {
        aggregator = new AndBooleanAggregator(emptyCollectionValid, nullElementValid);
    }

    /**
     * @see Rule#validate(Object)
     */
    @Override
    public Boolean validate(Collection<Boolean> elements) {
        Boolean aggregated = null;

        if (aggregator != null) {
            aggregated = aggregator.transform(elements);
        }

        return aggregated;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (aggregator instanceof Disposable) {
            ((Disposable) aggregator).dispose();
        }
    }
}
