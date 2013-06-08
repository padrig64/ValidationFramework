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

package com.google.code.validationframework.base.validator;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.api.trigger.TriggerListener;
import com.google.code.validationframework.api.validator.MappableValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of a mappable validator.<br>It merely implements the methods to map triggers to data
 * providers, data providers to rules, and rules to result handlers. However, the use triggers, data providers, rules
 * and result handlers, as well as all the validation logic is left to the sub-classes.
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
 * @see Disposable
 */
public abstract class AbstractMappableValidator<T extends Trigger, DP extends DataProvider<DPO>, DPO,
        R extends Rule<RI, RO>, RI, RO, RH extends ResultHandler<RHI>, RHI> implements MappableValidator<T, DP, DPO,
        R, RI, RO, RH, RHI>, Disposable {

    /**
     * Listener to all registered triggers, initiating the validation logic.
     */
    private class TriggerAdapter implements TriggerListener {

        /**
         * Trigger that is listened to.
         */
        private final T trigger;

        /**
         * Constructor specifying the trigger that is listened to.
         *
         * @param trigger Trigger that is listened to.
         */
        public TriggerAdapter(final T trigger) {
            this.trigger = trigger;
        }

        /**
         * @see TriggerListener#triggerValidation(TriggerEvent)
         */
        @Override
        public void triggerValidation(final TriggerEvent event) {
            // Start validation logic
            processTrigger(trigger);
        }
    }

    /**
     * Listeners to all registered validation triggers.
     */
    protected final Map<T, TriggerListener> triggersToTriggerAdapters = new HashMap<T, TriggerListener>();

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMappableValidator.class);

    /**
     * Warning message displayed when both input parameters are null when call the map methods.
     */
    private static final String NULL_PARAMETERS_WARNING = "Call to method will have no effect since both parameters "
            + "are null";

    /**
     * Mapping between triggers and data providers.
     */
    protected final Map<T, List<DP>> triggersToDataProviders = new HashMap<T, List<DP>>();

    /**
     * Mapping between data providers and rules.
     */
    protected final Map<DP, List<R>> dataProvidersToRules = new HashMap<DP, List<R>>();

    /**
     * Mapping between rules and result handlers.
     */
    protected final Map<R, List<RH>> rulesToResultHandlers = new HashMap<R, List<RH>>();

    /**
     * Registers a trigger listener to start the validation flow.<br>If a trigger listener was already previously
     * registered, calling this method will have no effect.
     *
     * @param trigger Trigger to hook to.
     */
    private void hookToTrigger(final T trigger) {
        // Hook to trigger only if not already done (the same trigger adatper will be used if it was already hooked)
        if (!triggersToTriggerAdapters.containsKey(trigger)) {
            final TriggerListener triggerAdapter = new TriggerAdapter(trigger);
            triggersToTriggerAdapters.put(trigger, triggerAdapter);
            trigger.addTriggerListener(triggerAdapter);
        }
    }

    /**
     * De-registers the trigger listener.
     *
     * @param trigger Trigger to unhook from.
     */
    private void unhookFromTrigger(final T trigger) {
        // Unhook from trigger
        final TriggerListener triggerAdapter = triggersToTriggerAdapters.get(trigger);
        trigger.removeTriggerListener(triggerAdapter);

        // Check if trigger was added several times
        if (!triggersToTriggerAdapters.containsKey(trigger)) {
            // All occurrences of the same trigger have been removed
            triggersToTriggerAdapters.remove(trigger);
        }
    }

    /**
     * @see MappableValidator#mapTriggerToDataProvider(Trigger, DataProvider)
     */
    @Override
    public void mapTriggerToDataProvider(final T trigger, final DP dataProvider) {
        if ((trigger == null) && (dataProvider == null)) {
            LOGGER.warn(NULL_PARAMETERS_WARNING);
        } else if (trigger == null) {
            unmapDataProviderFromAllTriggers(dataProvider);
        } else if (dataProvider == null) {
            unmapTriggerFromAllDataProviders(trigger);
        } else {
            // Hook trigger
            hookToTrigger(trigger);

            // Do the mapping
            List<DP> mappedDataProviders = triggersToDataProviders.get(trigger);
            if (mappedDataProviders == null) {
                mappedDataProviders = new ArrayList<DP>();
                triggersToDataProviders.put(trigger, mappedDataProviders);
            }
            mappedDataProviders.add(dataProvider);
        }
    }

    /**
     * Disconnects the specified trigger from all data providers.
     *
     * @param trigger Trigger to be unmapped.
     */
    private void unmapTriggerFromAllDataProviders(final T trigger) {
        if (trigger != null) {
            unhookFromTrigger(trigger);
            triggersToDataProviders.remove(trigger);
        }
    }

    /**
     * Disconnects the specified data providers from all triggers.
     *
     * @param dataProvider Data provider to be unmapped.
     */
    private void unmapDataProviderFromAllTriggers(final DP dataProvider) {
        if (dataProvider != null) {
            for (final List<DP> mappedDataProviders : triggersToDataProviders.values()) {
                mappedDataProviders.remove(dataProvider);
            }
        }
    }

    /**
     * @see MappableValidator#mapDataProviderToRule(DataProvider, Rule)
     */
    @Override
    public void mapDataProviderToRule(final DP dataProvider, final R rule) {
        if ((dataProvider == null) && (rule == null)) {
            LOGGER.warn(NULL_PARAMETERS_WARNING);
        } else if (dataProvider == null) {
            unmapRuleFromAllDataProviders(rule);
        } else if (rule == null) {
            unmapDataProviderFromAllRules(dataProvider);
        } else {
            List<R> mappedRules = dataProvidersToRules.get(dataProvider);
            if (mappedRules == null) {
                mappedRules = new ArrayList<R>();
                dataProvidersToRules.put(dataProvider, mappedRules);
            }
            mappedRules.add(rule);
        }
    }

    /**
     * Disconnects the specified data provider from all rules.
     *
     * @param dataProvider Data provider to be unmapped.
     */
    private void unmapDataProviderFromAllRules(final DP dataProvider) {
        if (dataProvider != null) {
            dataProvidersToRules.remove(dataProvider);
        }
    }

    /**
     * Disconnects the specified rule from all data providers.
     *
     * @param rule Rule to be unmapped.
     */
    private void unmapRuleFromAllDataProviders(final R rule) {
        if (rule != null) {
            for (final List<R> mappedRules : dataProvidersToRules.values()) {
                mappedRules.remove(rule);
            }
        }
    }

    /**
     * @see MappableValidator#mapRuleToResultHandler(Rule, ResultHandler)
     */
    @Override
    public void mapRuleToResultHandler(final R rule, final RH resultHandler) {
        if ((rule == null) && (resultHandler == null)) {
            LOGGER.warn(NULL_PARAMETERS_WARNING);
        } else if (rule == null) {
            unmapResultHandlerFromAllRules(resultHandler);
        } else if (resultHandler == null) {
            unmapRuleFromAllResultHandlers(rule);
        } else {
            List<RH> mappedResultHandlers = rulesToResultHandlers.get(rule);
            if (mappedResultHandlers == null) {
                mappedResultHandlers = new ArrayList<RH>();
                rulesToResultHandlers.put(rule, mappedResultHandlers);
            }
            mappedResultHandlers.add(resultHandler);
        }
    }

    /**
     * Disconnects the specified rule from all result handlers.
     *
     * @param rule Rule to be unmapped.
     */
    private void unmapRuleFromAllResultHandlers(final R rule) {
        if (rule != null) {
            rulesToResultHandlers.remove(rule);
        }
    }

    /**
     * Disconnects the specified result handler from all rules.
     *
     * @param resultHandler Result handler to be unmapped.
     */
    private void unmapResultHandlerFromAllRules(final RH resultHandler) {
        if (resultHandler != null) {
            for (final List<RH> mappedResultHandlers : rulesToResultHandlers.values()) {
                mappedResultHandlers.remove(resultHandler);
            }
        }
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        disposeTriggersAndDataProviders();
        disposeDataProvidersAndRules();
        disposeRulesAndResultHandlers();
    }

    /**
     * Disposes all triggers and data providers that are mapped to each other.
     */
    private void disposeTriggersAndDataProviders() {
        for (final Map.Entry<T, List<DP>> entry : triggersToDataProviders.entrySet()) {
            // Disconnect from trigger
            unhookFromTrigger(entry.getKey());

            // Dispose trigger itself
            final T trigger = entry.getKey();
            if (trigger instanceof Disposable) {
                ((Disposable) trigger).dispose();
            }

            // Dispose data providers
            final List<DP> dataProviders = entry.getValue();
            if (dataProviders != null) {
                for (final DP dataProvider : dataProviders) {
                    if (dataProvider instanceof Disposable) {
                        ((Disposable) dataProvider).dispose();
                    }
                }
            }
        }

        // Clears all triggers
        triggersToDataProviders.clear();
    }

    /**
     * Disposes all data providers and rules that are mapped to each other.<br>Note that some data providers may have
     * been
     * disposed already in the other disposal methods.
     */
    private void disposeDataProvidersAndRules() {
        for (final Map.Entry<DP, List<R>> entry : dataProvidersToRules.entrySet()) {
            // Dispose data provider
            final DP dataProvider = entry.getKey();
            if (dataProvider instanceof Disposable) {
                ((Disposable) dataProvider).dispose();
            }

            // Dispose rules
            final List<R> rules = entry.getValue();
            if (rules != null) {
                for (final R rule : rules) {
                    if (rule instanceof Disposable) {
                        ((Disposable) rule).dispose();
                    }
                }
            }
        }

        // Clears all triggers
        dataProvidersToRules.clear();
    }

    /**
     * Disposes all rules and result handlers that are mapped to each other.
     */
    private void disposeRulesAndResultHandlers() {
        for (final Map.Entry<R, List<RH>> entry : rulesToResultHandlers.entrySet()) {
            // Dispose rule
            final R rule = entry.getKey();
            if (rule instanceof Disposable) {
                ((Disposable) rule).dispose();
            }

            // Dispose result handlers
            final List<RH> resultHandlers = entry.getValue();
            if (resultHandlers != null) {
                for (final RH resultHandler : resultHandlers) {
                    if (resultHandler instanceof Disposable) {
                        ((Disposable) resultHandler).dispose();
                    }
                }
            }
        }

        // Clears all triggers
        rulesToResultHandlers.clear();
    }

    /**
     * Performs the whole validation logic for the specified trigger.<br>Typically, data will be read from the data
     * providers and passed to the rules, and the rule results will be processed by the result handlers.
     *
     * @param trigger Trigger actually initiated.
     */
    protected abstract void processTrigger(final T trigger);
}
