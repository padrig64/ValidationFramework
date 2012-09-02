/*
 * Copyright (c) 2012, Patrick Moawad
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

package com.github.validationframework.base.rule.bool;

import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.base.transform.OrBooleanAggregator;
import com.github.validationframework.base.transform.Transformer;
import java.util.Collection;

public class OrBooleanRule implements Rule<Collection<Boolean>, Boolean> {

	public static final boolean DEFAULT_EMPTY_COLLECTION_VALID = OrBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALID;

	public static final boolean DEFAULT_NULL_ELEMENT_VALID = OrBooleanAggregator.DEFAULT_NULL_ELEMENT_VALID;

	private final Transformer<Collection<Boolean>, Boolean> aggregator;

	public OrBooleanRule() {
		this(DEFAULT_EMPTY_COLLECTION_VALID, DEFAULT_NULL_ELEMENT_VALID);
	}

	public OrBooleanRule(final boolean emptyCollectionValid, final boolean nullElementValid) {
		aggregator = new OrBooleanAggregator(emptyCollectionValid, nullElementValid);
	}

	@Override
	public Boolean validate(final Collection<Boolean> elements) {
		return aggregator.transform(elements);
	}
}
