/*
 * Copyright (c) 2017, ValidationFramework Authors
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
import com.google.code.validationframework.api.validator.SimpleValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of a simple validator.
 * <p>
 * It merely implements the methods to add and remove triggers, data providers, rules and result handlers. However, the
 * use the connection between triggers, data providers, rules and result handlers, as well as all the validation logic
 * is left to the sub-classes.
 *
 * @param <T>   Type of trigger initiating the validation.
 * @param <DP>  Type of data provider providing the input data to be validated.
 * @param <DPO> Type of data provided by the data providers.
 * @param <R>   Type of validation rules to be used on the input data.
 * @param <RI>  Type of data the rules will check.
 * @param <RO>  Type of result the rules will produce.
 * @param <RH>  Type of result handlers to be used on validation output.
 * @param <RHI> Type of result the result handlers will handle.<br>
 *              It may or may not be the same as RO depending on the implementations.<br>
 *              For instance, an implementation could aggregate/transform the results before using the result handlers.
 *
 * @see Trigger
 * @see DataProvider
 * @see Rule
 * @see ResultHandler
 * @see Disposable
 */
public abstract class AbstractSimpleValidator<T extends Trigger, DP extends DataProvider<DPO>, DPO,
        R extends Rule<RI, RO>, RI, RO, RH extends ResultHandler<RHI>, RHI> implements SimpleValidator<T, DP, DPO, R,
        RI, RO, RH, RHI>, Disposable {

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
        public TriggerAdapter(T trigger) {
            this.trigger = trigger;
        }

        /**
         * @see TriggerListener#triggerValidation(TriggerEvent)
         */
        @Override
        public void triggerValidation(TriggerEvent event) {
            // Start validation logic
            processTrigger(trigger);
        }
    }

    /**
     * Listeners to all registered validation triggers.
     */
    private final Map<T, TriggerListener> triggersToTriggerAdapters = new HashMap<T, TriggerListener>();

    /**
     * Registered validation triggers.
     */
    protected final List<T> triggers = new ArrayList<T>();

    /**
     * Registered validation data providers.
     */
    protected final List<DP> dataProviders = new ArrayList<DP>();

    /**
     * Registered validation rules.
     */
    protected List<R> rules = new ArrayList<R>();

    /**
     * Registered validation result handlers.
     */
    protected final List<RH> resultHandlers = new ArrayList<RH>();

    /**
     * Gets the triggers known to this validator.
     *
     * @return Triggers.
     */
    public Collection<T> getTriggers() {
        return new ArrayList<T>(triggers);
    }

    /**
     * @see SimpleValidator#addTrigger(Trigger)
     */
    @Override
    public void addTrigger(T trigger) {
        triggers.add(trigger);

        // Hook to trigger only if not already done (the same trigger listener will be used if it was already hooked)
        if (!triggersToTriggerAdapters.containsKey(trigger)) {
            TriggerListener triggerAdapter = new TriggerAdapter(trigger);
            triggersToTriggerAdapters.put(trigger, triggerAdapter);
            trigger.addTriggerListener(triggerAdapter);
        }
    }

    /**
     * @see SimpleValidator#removeTrigger(Trigger)
     */
    @Override
    public void removeTrigger(T trigger) {
        triggers.remove(trigger);

        // Unhook from trigger
        TriggerListener triggerAdapter = triggersToTriggerAdapters.get(trigger);
        trigger.removeTriggerListener(triggerAdapter);

        // Check if trigger was added several times
        if (!triggers.contains(trigger)) {
            // All occurrences of the same trigger have been removed
            triggersToTriggerAdapters.remove(trigger);
        }
    }

    /**
     * Gets the data providers known to this validator.
     *
     * @return Data providers.
     */
    public Collection<DP> getDataProviders() {
        return new ArrayList<DP>(dataProviders);
    }

    /**
     * @see SimpleValidator#addDataProvider(DataProvider)
     */
    @Override
    public void addDataProvider(DP dataProvider) {
        dataProviders.add(dataProvider);
    }

    /**
     * @see SimpleValidator#removeDataProvider(DataProvider)
     */
    @Override
    public void removeDataProvider(DP dataProvider) {
        dataProviders.remove(dataProvider);
    }

    /**
     * Gets the rules known to this validator.
     *
     * @return Rules.
     */
    public Collection<R> getRules() {
        return new ArrayList<R>(rules);
    }

    /**
     * @see SimpleValidator#addRule(Rule)
     */
    @Override
    public void addRule(R rule) {
        rules.add(rule);
    }

    /**
     * @see SimpleValidator#removeRule(Rule)
     */
    @Override
    public void removeRule(R rule) {
        rules.remove(rule);
    }

    /**
     * Gets the result handlers known to this validator.
     *
     * @return result handlers.
     */
    public Collection<RH> getResultHandlers() {
        return new ArrayList<RH>(resultHandlers);
    }

    /**
     * @see SimpleValidator#addResultHandler(ResultHandler)
     */
    @Override
    public void addResultHandler(RH resultHandler) {
        resultHandlers.add(resultHandler);
    }

    /**
     * @see SimpleValidator#removeResultHandler(ResultHandler)
     */
    @Override
    public void removeResultHandler(RH resultHandler) {
        resultHandlers.remove(resultHandler);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Disconnect all installed triggers
        for (T trigger : triggers) {
            // Disconnect trigger adapter and forget about the trigger
            TriggerListener triggerAdapter = triggersToTriggerAdapters.remove(trigger);
            if (triggerAdapter != null) {
                trigger.removeTriggerListener(triggerAdapter);
            }
        }

        // Dispose everything
        dispose(triggers);
        dispose(dataProviders);
        dispose(rules);
        dispose(resultHandlers);
    }

    /**
     * Clears all elements from the specified collection.
     *
     * @param elements Collection of elements to be disposed (for instance, data providers, rules, result handlers).
     */
    private void dispose(Collection<?> elements) {
        // Dispose all disposable elements
        for (Object element : elements) {
            if (element instanceof Disposable) {
                ((Disposable) element).dispose();
            }
        }

        // Clear collection
        dataProviders.clear();
    }

    /**
     * Performs the whole validation logic for the specified trigger.
     * <p>
     * Typically, data will be read from the data providers and passed to the rules, and the rule results will be
     * processed by the result handlers.
     *
     * @param trigger Trigger actually initiated.
     */
    protected abstract void processTrigger(T trigger);
}
