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

package com.google.code.validationframework.base.trigger;

import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.api.trigger.TriggerListener;

/**
 * Composite trigger.
 * <p>
 * It can be used, for instance, to easily add a same set of triggers to several validators.
 * <p>
 * Note that no reference will be kept to the added triggers. Only a trigger listener will be added to them.
 */
public class CompositeTrigger extends AbstractTrigger {

    /**
     * Entity forwarding the trigger events from the individual triggers to the listeners of the composite trigger.
     */
    private class TriggerForwarder implements TriggerListener {

        /**
         * @see TriggerListener#triggerValidation(TriggerEvent)
         */
        @Override
        public void triggerValidation(TriggerEvent event) {
            // Forward event
            fireTriggerEvent(event);
        }
    }

    /**
     * Trigger listener to be added to all individual triggers, and forwarding the trigger events to the listeners of
     * the composite trigger.
     */
    private final TriggerListener triggerForwarder = new TriggerForwarder();

    /**
     * Default constructor.
     */
    public CompositeTrigger() {
        super();
    }

    /**
     * Constructor specifying the triggers to be added.
     *
     * @param triggers Triggers to be added.
     *
     * @see #addTrigger(Trigger)
     */
    public CompositeTrigger(Trigger... triggers) {
        super();
        for (Trigger trigger : triggers) {
            addTrigger(trigger);
        }
    }

    /**
     * Adds the specified trigger to the composition.
     * <p>
     * Note that no reference to the given trigger will be kept. Only a trigger listener will be added to it.
     *
     * @param trigger Trigger to be added.
     */
    public void addTrigger(Trigger trigger) {
        if (trigger != null) {
            trigger.addTriggerListener(triggerForwarder);
        }
    }

    /**
     * Removes the specified trigger to the composition.
     * <p>
     * The trigger listener added
     *
     * @param trigger Trigger to be removed.
     */
    public void removeTrigger(Trigger trigger) {
        if (trigger != null) {
            trigger.removeTriggerListener(triggerForwarder);
        }
    }
}
