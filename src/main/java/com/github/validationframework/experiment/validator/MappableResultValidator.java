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

package com.github.validationframework.experiment.validator;

/**
 * Interface to be implemented by mappable validators.<br>The validator is the central point of the validation
 * framework. It implements the whole chain of validation, from the triggers, till the display of the feedback.<br>Upon
 * validation trigger (for example, whenever the user enters some data in a field, presses an Apply button, etc.), data
 * is retrieved (for example, from the input field, a database, etc.) and passed to the validation algorithm (for
 * example, expecting data to be entered in a specific format), which produces validation results (for example, invalid
 * input, valid input, input too long, misspelled, etc.), which are then used to give appropriate feedback to the user
 * (for example, a popup dialog, an error icon, etc.).<br>A mappable validator allows to map triggers to data providers,
 * data providers to rules, and results to result handlers.<br>It can be used, for example, to build validation of a
 * group of components.
 *
 * @param <T> Type of trigger initiating the validation.
 * @param <P> Type of data provider providing the input data to be validated.
 * @param <R> Type of validation rules to be used on the input data.
 * @param <O> Type of validation results produced by the validation rules.
 * @param <H> Type of result handlers to be used on validation output.
 */
public interface MappableResultValidator<T, P, R, O, H> {

	/**
	 * Maps the specified trigger to the specified data provider.<br>This means that whenever the specified trigger is
	 * initiated, the specified data provider will be use to read the data, which will be passed to the rules that are
	 * mapped to the this data provider.<br>Specifying null for the trigger will unmap the specified data provider from all
	 * triggers. This means that the specified data provider will no longer be used upon any trigger.<br>Specifying null
	 * for the data provider will unmap the specified trigger from all data providers. This means that the trigger will no
	 * longer have effect on the validation.
	 *
	 * @param trigger Trigger to be mapped to the data provider.
	 * @param dataProvider Data provider to be mapped to the trigger.
	 */
	public void mapTriggerToDataProvider(final T trigger, final P dataProvider);

	/**
	 * Maps the specified data provider to the specified rule.<br>This means that whenever the specified data provider is
	 * used, the specified rule will be used to validate the data, and the validation result will be passed to the result
	 * handlers that are mapped to the this result.<br>Specifying null for the data provider will unmap the specified rule
	 * from all data providers. This means that the rule will no longer be part of the validation.<br>Specifying null for
	 * the rule will unmap the specified data provider from all rules. This means that the data from this data provider
	 * will no longer be validated.
	 *
	 * @param dataProvider Data provider to be mapped to the rule.
	 * @param rule Rule to be mapped to the data provider.
	 */
	public void mapDataProviderToRule(final P dataProvider, final R rule);

	/**
	 * Maps the specified result to the specified result handler.<br>This means that whenever the specified result is
	 * issued, the specified result handler will be used to process it.<br>Specifying null for the result will unmap the
	 * specified result handlers from all results. This means that the result handler will no longer be used to process any
	 * result.<br>Specifying null for the result handler will unmap the specified rule from all result handlers. This means
	 * that the result will no longer be processed.
	 *
	 * @param result Result to be mapped to the result handler.
	 * @param resultHandler Result handler to be mapped to the result.
	 */
	public void mapResultToResultHandler(final O result, final H resultHandler);
}
