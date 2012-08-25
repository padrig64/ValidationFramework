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

package com.github.validationframework.base.validator;

import com.github.validationframework.api.dataprovider.TypedDataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.TypedDataRule;
import com.github.validationframework.api.trigger.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete implementation of a typed data simple validator.<br>A typed data validator is a validator whose data
 * providers and rules are bound to a known specific type of data, and whose result handlers are bound to a known
 * specific type of result.<br>When any of its triggers is initiated, the simple typed data validator will read all the
 * data from all of its data providers, check them all against all of its rules, and handles all the results using all
 * of its result handlers.
 *
 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * type of the component itself.
 * @param <R> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 *
 * @see AbstractSimpleValidator
 * @see Trigger
 * @see TypedDataProvider
 * @see TypedDataRule
 * @see ResultHandler
 */
public class TypedDataSimpleValidator<D, R>
		extends AbstractSimpleValidator<Trigger, TypedDataProvider<D>, TypedDataRule<D, R>, R, ResultHandler<R>> {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TypedDataSimpleValidator.class);

	/**
	 * @see AbstractSimpleValidator#processTrigger(Trigger)
	 */
	@Override
	protected void processTrigger(final Trigger trigger) {
		if (dataProviders.isEmpty()) {
			LOGGER.warn("No data providers in validator: " + this);
		} else {
			// Process data from all providers
			for (final TypedDataProvider<D> dataProvider : dataProviders) {
				processData(dataProvider.getData());
			}
		}
	}

	/**
	 * Validates the specified data all rules.
	 *
	 * @param data Data to be validated against all rules.
	 */
	protected void processData(final D data) {
		// Check data against all rules
		for (final TypedDataRule<D, R> rule : rules) {
			processResult(rule.validate(data));
		}
	}

	/**
	 * Handles the specified result using all result handlers.
	 *
	 * @param result Result to be processed by all result handlers.
	 */
	protected void processResult(final R result) {
		for (final ResultHandler<R> resultHandler : resultHandlers) {
			resultHandler.handleResult(result);
		}
	}
}
