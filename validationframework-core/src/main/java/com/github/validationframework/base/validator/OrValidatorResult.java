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

package com.github.validationframework.base.validator;

import com.github.validationframework.base.transform.OrBooleanAggregator;

/**
 * Simple validator using boolean results and aggregating all results from the rules into a single result using the OR
 * operation.
 *
 * @param <PO> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 *             type of the component itself.
 *
 * @see ResultAggregationValidator
 * @see OrBooleanAggregator
 * @see AndValidatorResult
 */
public class OrValidatorResult<PO> extends ResultAggregationValidator<PO, Boolean, Boolean> {

    /**
     * Default constructor using the default constructor of {@link OrBooleanAggregator}.
     *
     * @see OrBooleanAggregator
     */
    public OrValidatorResult() {
        super(new OrBooleanAggregator());
    }

    /**
     * Constructor specifying the boolean values for empty and null collections, and null elements, to be used when
     * aggregating the results from all the rules.
     *
     * @param emptyCollectionValue Value for empty and null collections of results.
     * @param nullElementValue     Value for null elements in the transformed collections of results.
     */
    public OrValidatorResult(final boolean emptyCollectionValue, final boolean nullElementValue) {
        super(new OrBooleanAggregator(emptyCollectionValue, nullElementValue));
    }
}
