/*
 * Copyright (c) 2012, Patrick Moawad
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

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.base.trigger.AbstractTrigger;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Trigger that initiates the validation whenever the model of a list changes.
 */
public class JListModelChangedTrigger extends AbstractTrigger implements Disposable {

    /**
     * Listener to changes in the model of the list, triggering the validation.<br>Note that there is no need to track
     * replacement of the model in the list as this is already done in {@link javax.swing.JList}.
     */
    private class ModelChangeAdapter implements ListDataListener {

        /**
         * @see ListDataListener#intervalAdded(ListDataEvent)
         */
        @Override
        public void intervalAdded(final ListDataEvent e) {
            fireTriggerEvent(new TriggerEvent(list));
        }

        /**
         * @see ListDataListener#intervalRemoved(ListDataEvent)
         */
        @Override
        public void intervalRemoved(final ListDataEvent e) {
            fireTriggerEvent(new TriggerEvent(list));
        }

        /**
         * @see ListDataListener#contentsChanged(ListDataEvent)
         */
        @Override
        public void contentsChanged(final ListDataEvent e) {
            fireTriggerEvent(new TriggerEvent(list));
        }
    }

    /**
     * List to track model changes.
     */
    private final JList list;

    /**
     * Listener to model changes.
     */
    private final ModelChangeAdapter modelChangeAdapter = new ModelChangeAdapter();

    /**
     * Constructor specifying the list whose model changes are meant to trigger validation.
     *
     * @param list List whose model changes are meant to trigger validation.
     */
    public JListModelChangedTrigger(final JList list) {
        super();
        this.list = list;
        this.list.getModel().addListDataListener(modelChangeAdapter);
        // TODO Listen to model replacement
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        list.getModel().removeListDataListener((modelChangeAdapter));
    }
}
