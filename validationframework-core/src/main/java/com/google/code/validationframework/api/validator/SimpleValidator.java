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

package com.google.code.validationframework.api.validator;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;

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
 * @param <T>   Type of trigger initiating the validation.
 * @param <DP>  Type of data provider providing the input data to be validated.
 * @param <DPO> Type of data provided by the data providers.
 * @param <R>   Type of validation rules to be used on the input data.
 * @param <RI>  Type of data the rules will check.
 * @param <RO>  Type of result the rules will produce.
 * @param <RH>  Type of result handlers to be used on validation output.
 * @param <RHI> Type of result the result handlers will handler.<br>It may or may not be the same as RO depending on the
 *              implementations.<br>For instance, an implementation could aggregate/transform the results before using
 *              the result handlers.
 *
 * @see Trigger
 * @see DataProvider
 * @see Rule
 * @see ResultHandler
 * @see MappableValidator
 */
public interface SimpleValidator<T extends Trigger, DP extends DataProvider<DPO>, DPO, R extends Rule<RI, RO>, RI,
        RO, RH extends ResultHandler<RHI>, RHI> {

    /**
     * Adds the specified validation trigger.
     *
     * @param trigger Validation trigger to be added.
     */
    void addTrigger(final T trigger);

    /**
     * Removes the specified validation trigger.
     *
     * @param trigger Validation trigger to be removed.
     */
    void removeTrigger(final T trigger);

    /**
     * Adds the specified validation trigger.
     *
     * @param dataProvider Validation data provider to be added.
     */
    void addDataProvider(final DP dataProvider);

    /**
     * Removes the specified validation trigger.
     *
     * @param dataProvider Validation data provider to be removed.
     */
    void removeDataProvider(final DP dataProvider);

    /**
     * Adds the specified validation rule.
     *
     * @param rule Validation rule to be added.
     */
    void addRule(final R rule);

    /**
     * Removes the specified validation rule.
     *
     * @param rule Validation rule to be removed.
     */
    void removeRule(final R rule);

    /**
     * Adds the specified validation result handler.
     *
     * @param resultHandler Validation result handler to be added.
     */
    void addResultHandler(final RH resultHandler);

    /**
     * Removes the specified validation result handler.
     *
     * @param resultHandler Validation result handler to be removed.
     */
    void removeResultHandler(final RH resultHandler);
}
