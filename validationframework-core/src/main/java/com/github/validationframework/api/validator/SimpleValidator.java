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

package com.github.validationframework.api.validator;

/**
 * Interface to be implemented by simple validators.<br>The validator is the central point of the validation framework.
 * It implements the whole chain of validation, from the triggers, till the display of the feedback.<br>Upon validation
 * trigger (for example, whenever the user enters some data in a field, presses an Apply button, etc.), data is
 * retrieved (for example, from the input field, a database, etc.) and passed to the validation algorithm (for example,
 * expecting data to be entered in a specific format), which produces validation results (for example, invalid input,
 * valid input, input too long, misspelled, etc.), which are then used to give appropriate feedback to the user (for
 * example, a popup dialog, an error icon, etc.).<br>A simple validator allows to add and remove triggers, data
 * providers, rules and result handlers.
 *
 * @param <T> Type of trigger initiating the validation.
 * @param <P> Type of data provider providing the input data to be validated.
 * @param <R> Type of validation rules to be used on the input data.
 * @param <H> Type of result handlers to be used on validation output.
 */
public interface SimpleValidator<T, P, R, H> {

	/**
	 * Adds the specified validation trigger.
	 *
	 * @param trigger Validation trigger to be added.
	 */
	public void addTrigger(final T trigger);

	/**
	 * Removes the specified validation trigger.
	 *
	 * @param trigger Validation trigger to be removed.
	 */
	public void removeTrigger(final T trigger);

	/**
	 * Adds the specified validation trigger.
	 *
	 * @param dataProvider Validation data provider to be added.
	 */
	public void addDataProvider(final P dataProvider);

	/**
	 * Removes the specified validation trigger.
	 *
	 * @param dataProvider Validation data provider to be removed.
	 */
	public void removeDataProvider(final P dataProvider);

	/**
	 * Adds the specified validation rule.
	 *
	 * @param rule Validation rule to be added.
	 */
	public void addRule(final R rule);

	/**
	 * Removes the specified validation rule.
	 *
	 * @param rule Validation rule to be removed.
	 */
	public void removeRule(final R rule);

	/**
	 * Adds the specified validation result handler.
	 *
	 * @param resultHandler Validation result handler to be added.
	 */
	public void addResultHandler(final H resultHandler);

	/**
	 * Removes the specified validation result handler.
	 *
	 * @param resultHandler Validation result handler to be removed.
	 */
	public void removeResultHandler(final H resultHandler);
}
