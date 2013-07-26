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

package com.google.code.validationframework.base.validator.generalvalidator.dsl;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.transform.Transformer;
import com.google.code.validationframework.base.validator.generalvalidator.GeneralValidator;

import java.util.Collection;

/**
 * DSL-related context of the {@link GeneralValidatorBuilder} after adding one or more result handlers.
 *
 * @param <DPO> Type of data provider output.
 * @param <RI>  Type of rule input.
 * @param <RO>  Type of rule output.
 * @param <RHI> Type of result handler input.
 */
public class ResultHandlerContext<DPO, RI, RO, RHI> {

    /**
     * Triggers to be added to the validator under construction.
     */
    private final Collection<Trigger> addedTriggers;

    /**
     * Data providers to be added to the validator under construction.
     */
    private final Collection<DataProvider<DPO>> addedDataProviders;

    /**
     * Data provider to rule mapping to be set to the validator under construction.
     */
    private final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping;

    /**
     * Rule input transformers to be added to the validator under construction.
     */
    private final Collection<Transformer> addedRuleInputTransformers;

    /**
     * Rules to be added to the validator under construction.
     */
    private final Collection<Rule<RI, RO>> addedRules;

    /**
     * Rule to result handler mapping to be set to the validator under construction.
     */
    private final GeneralValidator.RuleToResultHandlerMapping ruleToResultHandlerMapping;

    /**
     * Result handler input transformers to be added to the validator under construction.
     */
    private final Collection<Transformer> addedResultHandlerInputTransformers;

    /**
     * Result handler to be added to the validator under construction.
     */
    private final Collection<ResultHandler<RHI>> addedResultHandlers;

    /**
     * Validator under construction.
     */
    private final GeneralValidator<DPO, RI, RO, RHI> builtValidator;

    /**
     * Constructor specifying the already known elements of the validator under construction.
     *
     * @param addedTriggers              Triggers to be added.
     * @param addedDataProviders         Data providers to be added.
     * @param dataProviderToRuleMapping  Data provider to rule mapping to be set.
     * @param addedRuleInputTransformers Rule input transformers to be added.
     * @param addedRules                 Rules to be added.
     * @param ruleToResultHandlerMapping Rule to result handler mapping to be set.
     * @param addedResultHandlerInputTransformers
     *                                   Result handler input transformers to be added.
     * @param addedResultHandlers        Result handlers to be added.
     */
    public ResultHandlerContext(Collection<Trigger> addedTriggers, //
                                Collection<DataProvider<DPO>> addedDataProviders, //
                                GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping, //
                                Collection<Transformer> addedRuleInputTransformers, //
                                Collection<Rule<RI, RO>> addedRules, //
                                GeneralValidator.RuleToResultHandlerMapping ruleToResultHandlerMapping, //
                                Collection<Transformer> addedResultHandlerInputTransformers, //
                                Collection<ResultHandler<RHI>> addedResultHandlers) {
        this.addedTriggers = addedTriggers;
        this.addedDataProviders = addedDataProviders;
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.addedRuleInputTransformers = addedRuleInputTransformers;
        this.addedRules = addedRules;
        this.ruleToResultHandlerMapping = ruleToResultHandlerMapping;
        this.addedResultHandlerInputTransformers = addedResultHandlerInputTransformers;
        this.addedResultHandlers = addedResultHandlers;

        // Build validator now so that there is no need for the programmer to call build()
        builtValidator = build();
    }

    /**
     * Adds the specified result handler to the validator under construction.
     *
     * @param resultHandler Result handler to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public ResultHandlerContext<DPO, RI, RO, RHI> handleWith(ResultHandler<RHI> resultHandler) {
        if (resultHandler != null) {
            addedResultHandlers.add(resultHandler);
            builtValidator.addResultHandler(resultHandler);
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    /**
     * Adds the specified result handlers to the validator under construction.
     *
     * @param resultHandlers Result handlers to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public ResultHandlerContext<DPO, RI, RO, RHI> handleWith(Collection<ResultHandler<RHI>> resultHandlers) {
        if (resultHandlers != null) {
            this.addedResultHandlers.addAll(resultHandlers);

            for (ResultHandler<RHI> resultHandler : resultHandlers) {
                builtValidator.addResultHandler(resultHandler);
            }
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    /**
     * Builds the validator.
     *
     * @return Fully constructed validator.
     */
    private GeneralValidator<DPO, RI, RO, RHI> build() {
        // Create validator
        GeneralValidator<DPO, RI, RO, RHI> validator = new GeneralValidator<DPO, RI, RO, RHI>();

        // Add triggers
        for (Trigger trigger : addedTriggers) {
            validator.addTrigger(trigger);
        }

        // Add data providers
        for (DataProvider<DPO> dataProvider : addedDataProviders) {
            validator.addDataProvider(dataProvider);
        }

        // Map data providers output to rules input
        validator.setDataProviderToRuleMapping(dataProviderToRuleMapping);
        validator.setRuleInputTransformers(addedRuleInputTransformers);

        // Add rules
        for (Rule<RI, RO> rule : addedRules) {
            validator.addRule(rule);
        }

        // Map rules output to result handlers input
        validator.setRuleToResultHandlerMapping(ruleToResultHandlerMapping);
        validator.setResultHandlerInputTransformers(addedResultHandlerInputTransformers);

        // Add result handlers
        for (ResultHandler<RHI> resultHandler : addedResultHandlers) {
            validator.addResultHandler(resultHandler);
        }

        return validator;
    }

    /**
     * Gets the fully constructed validator.
     * <p/>
     * Note that this method does not build the validator. At this point, the validator is already fully constructed
     * and effective since the addition of the first result handler.
     *
     * @return Fully constructed validator.
     */
    public GeneralValidator<DPO, RI, RO, RHI> getValidator() {
        return builtValidator;
    }
}
