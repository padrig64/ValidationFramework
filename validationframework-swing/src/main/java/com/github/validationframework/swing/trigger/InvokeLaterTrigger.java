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

package com.github.validationframework.swing.trigger;

import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.api.trigger.TriggerListener;
import com.github.validationframework.base.trigger.AbstractTrigger;

import javax.swing.SwingUtilities;

/**
 * Trigger wrapper to re-schedule the wrapped trigger on the Event Dispatch Thread.<br>This can be useful when triggers
 * are initially trigger outside the EDT, but also to schedule the trigger later. The latter case is useful if you have
 * triggers initiated on keystrokes on an input field and data providers reading the text of the same input field: this
 * trigger wrapper can be used to make sure that the input field treats the keystrokes before the trigger is initiated.
 */
public class InvokeLaterTrigger extends AbstractTrigger {

    private class TriggerRescheduler implements TriggerListener, Runnable {

        private final boolean onlyIfNotOnEDT;

        public TriggerRescheduler(final boolean onlyIfNotOnEDT) {
            this.onlyIfNotOnEDT = onlyIfNotOnEDT;
        }

        /**
         * @see TriggerListener#triggerValidation(TriggerEvent)
         */
        @Override
        public void triggerValidation(final TriggerEvent event) {
            if (!onlyIfNotOnEDT || !SwingUtilities.isEventDispatchThread()) {
                // Either forced or not yet on the EDT
                SwingUtilities.invokeLater(this);
            } else {
                // Already on the EDT
                run();
            }
        }

        /**
         * @see Runnable#run()
         */
        @Override
        public void run() {
            fireTriggerEvent(new TriggerEvent(InvokeLaterTrigger.this));
        }
    }

    public InvokeLaterTrigger(final Trigger wrappedTrigger) {
        this(wrappedTrigger, false);
    }

    public InvokeLaterTrigger(final Trigger wrappedTrigger, final boolean onlyIfNotOnEDT) {
        wrappedTrigger.addTriggerListener(new TriggerRescheduler(onlyIfNotOnEDT));
    }
}
