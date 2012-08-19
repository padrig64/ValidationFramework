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

package com.github.validationframework.validator;

import com.github.validationframework.dataprovider.DataProvider;
import com.github.validationframework.resulthandler.ResultHandler;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.Trigger;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a validator.<br>It merely implements the methods to add and remove triggers, data
 * providers, rules and result handlers. However, note that the connection between triggers, data providers, rules and
 * result handlers, as well as all the validation logic is left to the sub-classes.
 *
 * @param <T> Type of trigger initiating the validation.
 * @param <P> Type of data provider providing the input data to be validated.
 * @param <U> Type of validation rules to be used on the input data.
 * @param <H> Type of result handlers to be used on validation output.
 * @see Validator
 * @see Trigger
 * @see DataProvider
 * @see Rule
 * @see ResultHandler
 */
public abstract class AbstractValidator<T extends Trigger, P extends DataProvider, U extends Rule, H extends ResultHandler>
		implements Validator<T, P, U, H> {

	/**
	 * Validation triggers.
	 */
	protected final List<T> triggers = new ArrayList<T>();

	/**
	 * Validation data providers.
	 */
	protected final List<P> dataProviders = new ArrayList<P>();

	/**
	 * Validation rules.
	 */
	protected List<U> rules = new ArrayList<U>();

	/**
	 * Validation result handlers.
	 */
	protected final List<H> resultHandlers = new ArrayList<H>();

	/**
	 * @see Validator#addTrigger(Object)
	 */
	@Override
	public void addTrigger(final T trigger) {
		triggers.add(trigger);
	}

	/**
	 * @see Validator#removeTrigger(Object)
	 */
	@Override
	public void removeTrigger(final T trigger) {
		triggers.remove(trigger);
	}

	/**
	 * @see Validator#addDataProvider(Object)
	 */
	@Override
	public void addDataProvider(final P dataProvider) {
		dataProviders.add(dataProvider);
	}

	/**
	 * @see Validator#removeDataProvider(Object)
	 */
	@Override
	public void removeDataProvider(final P dataProvider) {
		dataProviders.remove(dataProvider);
	}

	/**
	 * @see Validator#addRule(Object)
	 */
	@Override
	public void addRule(final U rule) {
		rules.add(rule);
	}

	/**
	 * @see Validator#removeRule(Object)
	 */
	public void removeRule(final U rule) {
		rules.remove(rule);
	}

	/**
	 * @see Validator#addResultHandler(Object)
	 */
	@Override
	public void addResultHandler(final H resultHandler) {
		resultHandlers.add(resultHandler);
	}

	/**
	 * @see Validator#removeResultHandler(Object)
	 */
	@Override
	public void removeResultHandler(final H resultHandler) {
		resultHandlers.remove(resultHandler);
	}
}
