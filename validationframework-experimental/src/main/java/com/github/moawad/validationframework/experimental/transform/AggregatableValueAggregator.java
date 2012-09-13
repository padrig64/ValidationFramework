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

package com.github.moawad.validationframework.experimental.transform;

import java.util.ArrayList;
import java.util.Collection;

import com.github.moawad.validationframework.base.transform.Transformer;

public class AggregatableValueAggregator<I, O> implements Transformer<Collection<Aggregatable<I>>, O> {

	private final Transformer<Collection<I>, O> delegate;

	public AggregatableValueAggregator(final Transformer<Collection<I>, O> delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see Transformer#transform(Object)
	 */
	@Override
	public O transform(final Collection<Aggregatable<I>> elements) {
		Collection<I> aggregatableValues = null;

		if (elements != null) {
			aggregatableValues = new ArrayList<I>();
			for (final Aggregatable<I> element : elements) {
				aggregatableValues.add(element.getAggregatableValue());
			}
		}

		return delegate.transform(aggregatableValues);
	}
}
