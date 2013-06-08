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

package com.github.validationframework.api.validator;

import com.github.validationframework.api.dataprovider.DataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.api.trigger.Trigger;

/**
 * Interface to be implemented by mappable validators.<br>The validator is the central point of the validation
 * framework. It implements the whole chain of validation, from the triggers, till the display of the feedback.<br>Upon
 * validation trigger (for example, whenever the user enters some data in a field, presses an Apply button, etc.), data
 * is retrieved (for example, from the input field, a database, etc.) and passed to the validation algorithm (for
 * example, expecting data to be entered in a specific format), which produces validation results (for example, invalid
 * input, valid input, input too long, misspelled, etc.), which are then used to give appropriate feedback to the user
 * (for example, a popup dialog, an error icon, etc.).<br>A mappable validator allows to map triggers to data providers,
 * data providers to rules, and rules to result handlers.<br>It can be used, for example, to build validation of a group
 * of components.
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
 * @see SimpleValidator
 */
public interface MappableValidator<T extends Trigger, DP extends DataProvider<DPO>, DPO, R extends Rule<RI, RO>, RI,
        RO, RH extends ResultHandler<RHI>, RHI> {

    /**
     * Maps the specified trigger to the specified data provider.<br>This means that whenever the specified trigger is
     * initiated, the specified data provider will be use to read the data, which will be passed to the rules that are
     * mapped to the this data provider.<br>Specifying null for the trigger will unmap the specified data provider
     * from all triggers. This means that the specified data provider will no longer be used upon any
     * trigger.<br>Specifying null for the data provider will unmap the specified trigger from all data providers.
     * This means that the trigger will no longer have effect on the validation.
     *
     * @param trigger      Trigger to be mapped to the data provider.
     * @param dataProvider Data provider to be mapped to the trigger.
     */
    public void mapTriggerToDataProvider(final T trigger, final DP dataProvider);

    /**
     * Maps the specified data provider to the specified rule.<br>This means that whenever the specified data provider
     * is used, the specified rule will be used to validate the data, and the validation result will be passed to the
     * result handlers that are mapped to this rule.<br>Specifying null for the data provider will unmap the
     * specified rule from all data providers. This means that the rule will no longer be part of the
     * validation.<br>Specifying null for the rule will unmap the specified data provider from all rules. This means
     * that the data from this data provider will no longer be validated.
     *
     * @param dataProvider Data provider to be mapped to the rule.
     * @param rule         Rule to be mapped to the data provider.
     */
    public void mapDataProviderToRule(final DP dataProvider, final R rule);

    /**
     * Maps the specified rule to the specified result handler.<br>This means that whenever the specified rule is
     * used, the specified result handler will be used to process its result.<br>Specifying null for the rule will
     * unmap the specified result handlers from all rules, This means that the result handler will no longer be used
     * to process any result.<br>Specifying null for the result handler will unmap the specified rule from all result
     * handlers. This means that the rule will no longer be processed.
     *
     * @param rule          Rule to be mapped to the result handler.
     * @param resultHandler Result handler to be mapped to the rule.
     */
    public void mapRuleToResultHandler(final R rule, final RH resultHandler);
}
