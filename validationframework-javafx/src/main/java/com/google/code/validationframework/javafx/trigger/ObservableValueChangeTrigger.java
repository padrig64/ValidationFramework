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

package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Trigger initiating the validation whenever an observable value is changed.
 *
 * @see ChangeListenerTrigger
 * @see ObservableInvalidationTrigger
 * @see ObservableValue
 */
public class ObservableValueChangeTrigger<T> extends AbstractTrigger implements Disposable {

    /**
     * Listener to changes of the observable value.
     */
    private class ChangeAdapter implements ChangeListener<T> {

        /**
         * @see ChangeListener#changed(ObservableValue, Object, Object)
         */
        @Override
        public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
            fireTriggerEvent(new TriggerEvent(observableValue));
        }
    }

    /**
     * Observable value to listen to.
     */
    private final ObservableValue<T> observableValue;

    /**
     * Listener to changes of the observable value.
     */
    private final ChangeAdapter changeAdapter = new ChangeAdapter();

    /**
     * Constructor specifying the observable object to listen to.
     *
     * @param observableValue Observable value to listen to.
     */
    public ObservableValueChangeTrigger(ObservableValue<T> observableValue) {
        super();
        this.observableValue = observableValue;
        this.observableValue.addListener(changeAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        observableValue.removeListener(changeAdapter);
    }
}
