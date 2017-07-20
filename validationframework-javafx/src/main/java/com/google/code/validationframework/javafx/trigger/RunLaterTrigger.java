/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.api.trigger.TriggerListener;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.application.Platform;

/**
 * Trigger wrapper to re-schedule the wrapped trigger on the FX Application Thread.
 * <p>
 * This can be useful when triggers are initially trigger outside the application thread, but also to schedule the
 * trigger later.
 */
public class RunLaterTrigger extends AbstractTrigger {

    /**
     * Listener to the wrapped trigger and that will re-schedule on the application thread as required.
     */
    private class TriggerRescheduler implements TriggerListener, Runnable {

        /**
         * Flag indicating whether to re-schedule even if the wrapped trigger is already initiated on the application
         * thread.
         */
        private final boolean evenIfAlreadyOnApplicationThread;

        /**
         * Constructor specifying whether to re-schedule only if the wrapped trigger was initiated outside the
         * application thread.
         *
         * @param evenIfAlreadyOnApplicationThread
         *         Flag indicating whether to re-schedule even if the wrapped trigger is already initiated on the
         *         application thread.
         */
        public TriggerRescheduler(boolean evenIfAlreadyOnApplicationThread) {
            this.evenIfAlreadyOnApplicationThread = evenIfAlreadyOnApplicationThread;
        }

        /**
         * @see TriggerListener#triggerValidation(TriggerEvent)
         */
        @Override
        public void triggerValidation(TriggerEvent event) {
            if (evenIfAlreadyOnApplicationThread || !Platform.isFxApplicationThread()) {
                // Either forced or not yet on the application thread
                Platform.runLater(this);
            } else {
                // Already on the application thread
                run();
            }
        }

        /**
         * @see Runnable#run()
         */
        @Override
        public void run() {
            fireTriggerEvent(new TriggerEvent(RunLaterTrigger.this));
        }
    }

    /**
     * Default behavior of running later if already on the application thread.
     */
    private static final boolean DEFAULT_EVEN_IF_ALREADY_ON_APPLICATION_THREAD = true;

    /**
     * Constructor specifying the wrapped trigger to be rescheduled.
     * <p>
     * By default, the trigger will always be re-scheduled later on the application thread, even if it is already
     * triggered on the application thread.
     *
     * @param wrappedTrigger Wrapped trigger to re-schedule later on the application thread.
     *
     * @see #RunLaterTrigger(Trigger, boolean)
     */
    public RunLaterTrigger(Trigger wrappedTrigger) {
        this(wrappedTrigger, DEFAULT_EVEN_IF_ALREADY_ON_APPLICATION_THREAD);
    }

    /**
     * Constructor specifying the wrapped trigger and whether to re-schedule on the application thread even if the
     * wrapped trigger is already initiated on the application thread.
     *
     * @param wrappedTrigger Wrapped trigger to re-schedule later on the application thread.
     * @param evenIfAlreadyInApplicationThread
     *                       Flag indicating whether to re-schedule even if the wrapped trigger is already initiated on
     *                       the application thread.
     */
    public RunLaterTrigger(Trigger wrappedTrigger, boolean evenIfAlreadyInApplicationThread) {
        wrappedTrigger.addTriggerListener(new TriggerRescheduler(evenIfAlreadyInApplicationThread));
    }
}
