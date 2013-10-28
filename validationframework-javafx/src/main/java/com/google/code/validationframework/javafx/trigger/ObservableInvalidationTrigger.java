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

package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 * Trigger initiating the validation whenever an observable value is invalidated.
 *
 * @see InvalidationListenerTrigger
 * @see ObservableValueChangeTrigger
 * @see Observable
 */
public class ObservableInvalidationTrigger extends AbstractTrigger implements Disposable {

    /**
     * Listener to invalidation of the observable.
     */
    private class InvalidationAdapter implements InvalidationListener {

        /**
         * @see InvalidationListener#invalidated(Observable)
         */
        @Override
        public void invalidated(Observable observable) {
            fireTriggerEvent(new TriggerEvent(observable));
        }
    }

    /**
     * Observable object to listen to.
     */
    private final Observable observable;

    /**
     * Listener to invalidation of the observable.
     */
    private final InvalidationAdapter invalidationAdapter = new InvalidationAdapter();

    /**
     * Constructor specifying the observable object to listen to.
     *
     * @param observable Observable object to listen to.
     */
    public ObservableInvalidationTrigger(Observable observable) {
        super();
        this.observable = observable;
        this.observable.addListener(invalidationAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        observable.removeListener(invalidationAdapter);
    }
}
