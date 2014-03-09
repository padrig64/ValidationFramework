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

package com.google.code.validationframework.base.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.api.trigger.TriggerListener;
import com.google.code.validationframework.base.common.RethrowUncheckedExceptionHandler;
import com.google.code.validationframework.base.common.UncheckedExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a typical trigger.
 * <p/>
 * It merely implements the methods to add and remove trigger listeners, and provides the method {@link
 * #fireTriggerEvent(TriggerEvent)} to fire a trigger event to these listeners. However, the call of this method is left
 * to the sub-classes.
 *
 * @see Trigger
 * @see TriggerListener
 * @see TriggerEvent
 * @see Disposable
 */
public abstract class AbstractTrigger implements Trigger, Disposable {

    /**
     * Strategy for handling exceptions thrown when the trigger events are fired.
     */
    protected final UncheckedExceptionHandler uncheckedExceptionHandler;

    /**
     * Trigger listeners.
     */
    protected final List<TriggerListener> listeners = new ArrayList<TriggerListener>();

    /**
     * Default constructor.
     * <p/>
     * By default, any exception occurring when the trigger events are fired will not be caught.
     *
     * @see RethrowUncheckedExceptionHandler
     */
    public AbstractTrigger() {
        this(null);
    }

    /**
     * Constructor specifying what to do when an exception occurs when the trigger event is fired.
     *
     * @param uncheckedExceptionHandler Strategy for handling exceptions thrown when the trigger events are fired.<br>
     *                                  If null, the default {@link RethrowUncheckedExceptionHandler} will be used.
     *
     * @see UncheckedExceptionHandler
     * @see RethrowUncheckedExceptionHandler
     */
    public AbstractTrigger(UncheckedExceptionHandler uncheckedExceptionHandler) {
        if (uncheckedExceptionHandler == null) {
            this.uncheckedExceptionHandler = new RethrowUncheckedExceptionHandler();
        } else {
            this.uncheckedExceptionHandler = uncheckedExceptionHandler;
        }
    }

    /**
     * @see Trigger#addTriggerListener(TriggerListener)
     */
    @Override
    public void addTriggerListener(TriggerListener listener) {
        listeners.add(listener);
    }

    /**
     * @see Trigger#removeTriggerListener(TriggerListener)
     */
    @Override
    public void removeTriggerListener(TriggerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fires the specified trigger event.<br>Calling this method is left to the sub-classes.
     *
     * @param event Trigger event to be fired.
     */
    protected void fireTriggerEvent(TriggerEvent event) {
        try {
            for (TriggerListener listener : listeners) {
                listener.triggerValidation(event);
            }
        } catch (RuntimeException e) {
            uncheckedExceptionHandler.handleException(e);
        } catch (Error e) {
            uncheckedExceptionHandler.handleError(e);
        }
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        for (TriggerListener listener : listeners) {
            if (listener instanceof Disposable) {
                ((Disposable) listener).dispose();
            }
        }
        listeners.clear();
    }
}
