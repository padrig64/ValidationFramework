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

package com.google.code.validationframework.swing.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.api.trigger.TriggerListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Trigger initiating the validation when an action is performed.
 * <p/>
 * This trigger can be used wherever actions and {@link java.awt.event.ActionListener}s are used (for instance, on
 * buttons).
 */
public class ActionTrigger extends AbstractAction implements Trigger, Disposable {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -3496046784335243792L;

    /**
     * Trigger listeners to be notified when the action is performed.
     */
    private final List<TriggerListener> triggerListeners = new ArrayList<TriggerListener>();

    /**
     * @see AbstractAction#AbstractAction()
     */
    public ActionTrigger() {
        super();
    }

    /**
     * @see AbstractAction#AbstractAction(String)
     */
    public ActionTrigger(String s) {
        super(s);
    }

    /**
     * @see AbstractAction#AbstractAction(String, Icon)
     */
    public ActionTrigger(String s, Icon icon) {
        super(s, icon);
    }

    /**
     * @see AbstractAction#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        TriggerEvent event = new TriggerEvent(actionEvent.getSource());
        for (TriggerListener listener : triggerListeners) {
            listener.triggerValidation(event);
        }
    }

    /**
     * @see Trigger#addTriggerListener(TriggerListener)
     */
    @Override
    public void addTriggerListener(TriggerListener listener) {
        triggerListeners.add(listener);
    }

    /**
     * @see Trigger#removeTriggerListener(TriggerListener)
     */
    @Override
    public void removeTriggerListener(TriggerListener listener) {
        triggerListeners.remove(listener);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        for (TriggerListener listener : triggerListeners) {
            if (listener instanceof Disposable) {
                ((Disposable) listener).dispose();
            }
        }
        triggerListeners.clear();
    }
}
