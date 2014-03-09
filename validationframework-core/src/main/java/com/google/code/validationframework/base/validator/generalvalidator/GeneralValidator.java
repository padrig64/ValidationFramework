/*
 * Copyright (c) 2014, Patrick Moawad
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

package com.google.code.validationframework.base.validator.generalvalidator;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.resulthandler.ResultCollector;
import com.google.code.validationframework.base.validator.AbstractSimpleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Simple validator allowing to have different mapping between data providers, rules and result handlers.
 * <p/>
 * For type safety, it is highly advised to use the {@link com.google.code.validationframework.base.validator
 * .generalvalidator.dsl.GeneralValidatorBuilder}.
 * <p/>
 * The validator allows to transform the data providers' output before mapping, the rules' input after mapping, the
 * rules' output before mapping and the result handlers' input after mapping.
 * <p/>
 * This means that the output of each data provider will be transformed using a series of data provider output {@link
 * Transformer}s. Then, if the data provider to rule mapping is {@link MappingStrategy#SPLIT}, the output of each data
 * provider output transformation will transformed again using a series of rule input {@link Transformer}s, before being
 * passed to each rule. If the data provider to rule mapping is {@link MappingStrategy#JOIN}, the output of all data
 * provider output transformations will be put in a collection and transformed again using the series of rule input
 * {@link Transformer}s, before being passed to each rule.
 * <p/>
 * The output of each rule will be transformed using a series of rule output {@link Transformer}s. Then, if the rule to
 * result handler mapping is {@link MappingStrategy#SPLIT}, the output of each rule output transformation will be
 * transformed again using a series of result handler input {@link Transformer}s, before being passed to each result
 * handler.
 * <p/>
 * If the rule to result handler mapping is {@link MappingStrategy#JOIN}, the output of all rule output transformations
 * will be put in a collection and transformed as a whole using the series of result handler input {@link Transformer}s,
 * before being passed to each result handler.
 * <p/>
 * The general validation flow can be represented by the following pattern:<br>
 * triggers -> data providers -> data provider output transformers -> data provider to rule mapping -> rule input
 * transformers -> rules -> rule output transformers -> rule output to result handler input transformers -> result
 * handler input transformers -> result handlers
 * <p/>
 * Note that the use of transformers is optional. By default, the data provider to rule mapping is set to {@link
 * MappingStrategy#SPLIT} and the rule to result handler mapping is set to {@link MappingStrategy#SPLIT}.
 *
 * @param <DPO> Type of data provider output.<br>
 *              This may or may not be the same type as the rule input.
 * @param <RI>  Type of rule input.<br>
 *              This may or may not be the same type as the data provider output.
 * @param <RO>  Type of rule output.<br>
 *              This may or may not be the same type as the result handler input.
 * @param <RHI> Type of result handler input.<br>
 *              This may or may not be the same type as the rule output.
 *
 * @see com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder
 * @see com.google.code.validationframework.base.validator.AbstractSimpleValidator
 */
public class GeneralValidator<DPO, RI, RO, RHI> extends AbstractSimpleValidator<Trigger, DataProvider<DPO>, DPO,
        Rule<RI, RO>, RI, RO, ResultHandler<RHI>, RHI> {

    /**
     * Mapping strategy between data providers and rules, and between rules and result handlers.
     */
    public enum MappingStrategy {

        SPLIT,

        JOIN
    }

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralValidator.class);

    /**
     * List of {@link Transformer}s transforming the output of each data provider.
     * <p/>
     * The type of input of the first transformer shall match the type of output of the data providers. The type of the
     * input of a subsequent transformer shall match the type of output of the previous transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> dataProviderOutputTransformers = new ArrayList<Transformer>();

    /**
     * Mapping between the (possibly transformed) data provider output to the rule input.
     */
    private MappingStrategy dataProviderToRuleMapping = GeneralValidator.MappingStrategy.SPLIT;

    /**
     * List of {@link Transformer}s transforming the input of each rule.
     * <p/>
     * The type of input of the first transformer shall match the type of output of the data provider to rule mapping:
     * so either the type of data provider output or a {@link Collection} of objects of the same type as the data
     * provider output. The type of the input of a subsequent transformer shall match the type of output of the previous
     * transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> ruleInputTransformers = new ArrayList<Transformer>();

    /**
     * List of {@link Transformer}s transforming the output of each rule.
     * <p/>
     * The type of input of the first transformer shall match the type of output of the rules. The type of the input of
     * a subsequent transformer shall match the type of output of the previous transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> ruleOutputTransformers = new ArrayList<Transformer>();

    /**
     * Mapping between the (possibly transformed) rule output to the result handler input.
     */
    private MappingStrategy ruleToResultHandlerMapping = MappingStrategy.SPLIT;

    /**
     * List of {@link Transformer}s transforming the input of each result handler.
     * <p/>
     * The type of input of the first transformer shall match the type of rule to result handler mapping: so either the
     * type of rule output or a {@link Collection} of objects of the same type as the rule output. The type of the input
     * of a subsequent transformer shall match the type of output of the previous transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> resultHandlerInputTransformers = new ArrayList<Transformer>();

    /**
     * Adds the specified result collector to the triggers and data providers.
     *
     * @param resultCollector Result collector to be added.
     */
    public void addResultCollector(ResultCollector<?, DPO> resultCollector) {
        if (resultCollector != null) {
            addTrigger(resultCollector);
            addDataProvider(resultCollector);
        }
    }

    /**
     * Removes the specified result collector from the triggers and data providers.
     *
     * @param resultCollector Result collector to be removed.
     */
    public void removeResultCollector(ResultCollector<?, DPO> resultCollector) {
        if (resultCollector != null) {
            removeTrigger(resultCollector);
            removeDataProvider(resultCollector);
        }
    }

    /**
     * Gets the transformers transforming the output of each data provider before they are mapped to the rules.
     *
     * @return Data provider output transformers.
     */
    public Transformer[] getDataProviderOutputTransformers() {
        Transformer[] transformers;

        if (dataProviderOutputTransformers == null) {
            transformers = null;
        } else {
            transformers = dataProviderOutputTransformers.toArray(new Transformer[dataProviderOutputTransformers.size
                    ()]);
        }

        return transformers;
    }

    /**
     * Sets teh transformers transforming the output of each data provider before they are mapped to the rules.
     *
     * @param dataProviderOutputTransformers Data provider output transformers.
     */
    public void setDataProviderOutputTransformers(Transformer... dataProviderOutputTransformers) {
        if (dataProviderOutputTransformers == null) {
            this.dataProviderOutputTransformers = null;
        } else {
            this.dataProviderOutputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.dataProviderOutputTransformers, dataProviderOutputTransformers);
        }
    }

    /**
     * Sets teh transformers transforming the output of each data provider before they are mapped to the rules.
     *
     * @param dataProviderOutputTransformers Data provider output transformers.
     */
    public void setDataProviderOutputTransformers(Collection<Transformer> dataProviderOutputTransformers) {
        if (dataProviderOutputTransformers == null) {
            this.dataProviderOutputTransformers = null;
        } else {
            this.dataProviderOutputTransformers = new ArrayList<Transformer>();
            this.dataProviderOutputTransformers.addAll(dataProviderOutputTransformers);
        }
    }

    public MappingStrategy getDataProviderToRuleMappingStrategy() {
        return dataProviderToRuleMapping;
    }

    public void setDataProviderToRuleMappingStrategy(MappingStrategy dataProviderToRuleMapping) {
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
    }

    public Transformer[] getRuleInputTransformers() {
        Transformer[] transformers;

        if (ruleInputTransformers == null) {
            transformers = null;
        } else {
            transformers = ruleInputTransformers.toArray(new Transformer[ruleInputTransformers.size()]);
        }

        return transformers;
    }

    public void setRuleInputTransformers(Transformer... ruleInputTransformers) {
        if (ruleInputTransformers == null) {
            this.ruleInputTransformers = null;
        } else {
            this.ruleInputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.ruleInputTransformers, ruleInputTransformers);
        }
    }

    public void setRuleInputTransformers(Collection<Transformer> ruleInputTransformers) {
        if (ruleInputTransformers == null) {
            this.ruleInputTransformers = null;
        } else {
            this.ruleInputTransformers = new ArrayList<Transformer>();
            this.ruleInputTransformers.addAll(ruleInputTransformers);
        }
    }

    public Transformer[] getRuleOutputTransformers() {
        Transformer[] transformers;

        if (ruleOutputTransformers == null) {
            transformers = null;
        } else {
            transformers = ruleOutputTransformers.toArray(new Transformer[ruleOutputTransformers.size()]);
        }

        return transformers;
    }

    public void setRuleOutputTransformers(Transformer... ruleOutputTransformers) {
        if (ruleOutputTransformers == null) {
            this.ruleOutputTransformers = null;
        } else {
            this.ruleOutputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.ruleOutputTransformers, ruleOutputTransformers);
        }
    }

    public void setRuleOutputTransformers(Collection<Transformer> ruleOutputTransformers) {
        if (ruleOutputTransformers == null) {
            this.ruleOutputTransformers = null;
        } else {
            this.ruleOutputTransformers = new ArrayList<Transformer>();
            this.ruleOutputTransformers.addAll(ruleOutputTransformers);
        }
    }

    public MappingStrategy getRuleToResultHandlerMappingStrategy() {
        return ruleToResultHandlerMapping;
    }

    public void setRuleToResultHandlerMappingStrategy(MappingStrategy ruleToResultHandlerMapping) {
        this.ruleToResultHandlerMapping = ruleToResultHandlerMapping;
    }

    public Transformer[] getResultHandlerInputTransformers() {
        Transformer[] transformers;

        if (resultHandlerInputTransformers == null) {
            transformers = null;
        } else {
            transformers = resultHandlerInputTransformers.toArray(new Transformer[resultHandlerInputTransformers.size
                    ()]);
        }

        return transformers;
    }

    public void setResultHandlerInputTransformers(Transformer... resultHandlerInputTransformers) {
        if (resultHandlerInputTransformers == null) {
            this.resultHandlerInputTransformers = null;
        } else {
            this.resultHandlerInputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.resultHandlerInputTransformers, resultHandlerInputTransformers);
        }
    }

    public void setResultHandlerInputTransformers(Collection<Transformer> resultHandlerInputTransformers) {
        if (resultHandlerInputTransformers == null) {
            this.resultHandlerInputTransformers = null;
        } else {
            this.resultHandlerInputTransformers = new ArrayList<Transformer>();
            this.resultHandlerInputTransformers.addAll(resultHandlerInputTransformers);
        }
    }

    /**
     * Convenience method to trigger the validation without adding a
     * {@link com.google.code.validationframework.base.trigger.ManualTrigger}.
     */
    public void trigger() {
        processTrigger(null);
    }

    /**
     * @see AbstractSimpleValidator#processTrigger(Trigger)
     */
    @Override
    protected void processTrigger(Trigger trigger) {
        switch (dataProviderToRuleMapping) {
            case SPLIT:
                processEachDataProviderWithEachRule();
                break;
            case JOIN:
                processAllDataProvidersWithEachRule();
                break;
            default:
                LOGGER.error("Unsupported " + com.google.code.validationframework.base.validator.generalvalidator
                        .GeneralValidator.MappingStrategy.class.getSimpleName() + ": " +
                        dataProviderToRuleMapping);
        }
    }

    /**
     * Processes the output of each data provider one by one with each rule.
     */
    @SuppressWarnings("unchecked") // NOSONAR (Avoid Duplicate Literals)
    private void processEachDataProviderWithEachRule() {
        // For each data provider
        for (DataProvider<DPO> dataProvider : dataProviders) {
            // Get the data provider output
            Object transformedOutput = dataProvider.getData();

            // Transform the data provider output
            if (dataProviderOutputTransformers != null) {
                for (Transformer transformer : dataProviderOutputTransformers) {
                    transformedOutput = transformer.transform(transformedOutput);
                }
            }

            // Transform the transformed data provider output to rule input
            if (ruleInputTransformers != null) {
                for (Transformer transformer : ruleInputTransformers) {
                    transformedOutput = transformer.transform(transformedOutput);
                }
            }
            RI ruleInput = (RI) transformedOutput;

            // Process the rule input with the rules
            processRules(ruleInput);
        }
    }

    /**
     * Processes the output of all data providers, all at once, with each rule.
     */
    @SuppressWarnings("unchecked") // NOSONAR (Avoid Duplicate Literals)
    private void processAllDataProvidersWithEachRule() {
        // For each data provider
        List<Object> transformedDataProvidersOutput = new ArrayList<Object>(dataProviders.size());
        for (DataProvider<DPO> dataProvider : dataProviders) {
            // Get the data provider output
            Object transformedOutput = dataProvider.getData();

            // Transform the data provider output
            if (dataProviderOutputTransformers != null) {
                for (Transformer transformer : dataProviderOutputTransformers) {
                    transformedOutput = transformer.transform(transformedOutput);
                }
            }

            // Put the transformed data provider output in a list
            transformedDataProvidersOutput.add(transformedOutput);
        }

        // Transform the list of transformed data provider output to rule input
        Object transformedRulesInput = transformedDataProvidersOutput;
        if (ruleInputTransformers != null) {
            for (Transformer transformer : ruleInputTransformers) {
                transformedRulesInput = transformer.transform(transformedRulesInput);
            }
        }
        RI ruleInput = (RI) transformedRulesInput;

        // Process the rule input with the rules
        processRules(ruleInput);
    }

    /**
     * Processes the specified rule input.
     *
     * @param ruleInput Rule input to be validated.
     */
    private void processRules(RI ruleInput) {
        switch (ruleToResultHandlerMapping) {
            case SPLIT:
                processEachRuleWithEachResultHandler(ruleInput);
                break;
            case JOIN:
                processAllRulesWithEachResultHandler(ruleInput);
                break;
            default:
                LOGGER.error("Unsupported " + MappingStrategy.class.getSimpleName() + ": " +
                        ruleToResultHandlerMapping);
        }
    }

    /**
     * Processes the specified rule input with each rule, and processes the results of each rule one by one with each
     * result handler.
     *
     * @param ruleInput Rule input to be validated.
     */
    @SuppressWarnings("unchecked") // NOSONAR (Avoid Duplicate Literals)
    private void processEachRuleWithEachResultHandler(RI ruleInput) {
        // For each rule
        for (Rule<RI, RO> rule : rules) {
            // Validate the data and get the rule output
            Object ruleOutput = rule.validate(ruleInput);

            // Transform the rule output
            if (ruleOutputTransformers != null) {
                for (Transformer transformer : ruleOutputTransformers) {
                    ruleOutput = transformer.transform(ruleOutput);
                }
            }

            // Transform the transformed rule output to result handler input
            if (resultHandlerInputTransformers != null) {
                for (Transformer transformer : resultHandlerInputTransformers) {
                    ruleOutput = transformer.transform(ruleOutput);
                }
            }
            RHI resultHandlerInput = (RHI) ruleOutput;

            // Process the result handler input with the result handlers
            processResultHandlers(resultHandlerInput);
        }
    }

    /**
     * Processes the specified rule input with each rule, and processes the result of all rules, all at once, with each
     * result handler.
     *
     * @param ruleInput Rule input to be validated.
     */
    @SuppressWarnings("unchecked") // NOSONAR (Avoid Duplicate Literals)
    private void processAllRulesWithEachResultHandler(RI ruleInput) {
        // For each rule
        List<Object> combinedRulesOutput = new ArrayList<Object>(rules.size());
        for (Rule<RI, RO> rule : rules) {
            // Validate the data and get the rule output
            Object data = rule.validate(ruleInput);

            // Transform the rule output
            if (ruleOutputTransformers != null) {
                for (Transformer transformer : ruleOutputTransformers) {
                    data = transformer.transform(data);
                }
            }

            // Put the transformed rule output in a list
            combinedRulesOutput.add(data);
        }

        // Transform the list of transformed rule output to result handler input
        Object ruleOutput = combinedRulesOutput;
        if (resultHandlerInputTransformers != null) {
            for (Transformer transformer : resultHandlerInputTransformers) {
                ruleOutput = transformer.transform(ruleOutput);
            }
        }
        RHI resultHandlerInput = (RHI) ruleOutput;

        // Process the result handler input with the result handlers
        processResultHandlers(resultHandlerInput);
    }

    /**
     * Processes the specified result handler input with each result handler.
     *
     * @param resultHandlerInput Result handler input to be handled.
     */
    private void processResultHandlers(RHI resultHandlerInput) {
        for (ResultHandler<RHI> resultHandler : resultHandlers) {
            resultHandler.handleResult(resultHandlerInput);
        }
    }

    /**
     * @see AbstractSimpleValidator#dispose()
     */
    @Override
    public void dispose() {
        // Dispose triggers, data providers, rules and result handlers
        super.dispose();

        // Dispose transformers
        dispose(dataProviderOutputTransformers);
        dispose(ruleInputTransformers);
        dispose(ruleOutputTransformers);
        dispose(resultHandlerInputTransformers);
    }

    /**
     * Disposes the elements of the specified collection.
     *
     * @param elements Elements to be disposed.
     */
    private void dispose(Collection<Transformer> elements) {
        if (elements != null) {
            for (Transformer<?, ?> element : elements) {
                if (element instanceof Disposable) {
                    ((Disposable) element).dispose();
                }
            }
            elements.clear();
        }
    }
}
