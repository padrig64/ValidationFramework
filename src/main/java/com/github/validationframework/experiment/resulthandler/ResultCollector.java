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

package com.github.validationframework.experiment.resulthandler;

import com.github.validationframework.api.dataprovider.TypedDataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.trigger.AbstractTrigger;
import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.experiment.transform.CastTransformer;
import com.github.validationframework.experiment.transform.Transformer;

public class ResultCollector<O, D> extends AbstractTrigger implements ResultHandler<O>, Trigger, TypedDataProvider<D> {

	private O lastResult = null;

	private final Transformer<O, D> transformer;

	public ResultCollector() {
		this(new CastTransformer<O, D>());
	}

	public ResultCollector(final Transformer<O, D> transformer) {
		this.transformer = transformer;
	}

	/**
	 * @see ResultHandler#handleResult(Object)
	 */
	@Override
	public void handleResult(final O result) {
		lastResult = result;
		fireTriggerEvent(new TriggerEvent(this));
	}

	/**
	 * @see TypedDataProvider#getData()
	 */
	@Override
	public D getData() {
		return transformer.transform(lastResult);
	}
}
