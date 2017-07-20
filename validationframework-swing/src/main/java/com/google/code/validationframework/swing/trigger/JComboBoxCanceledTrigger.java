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
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.common.LogErrorUncheckedExceptionHandler;
import com.google.code.validationframework.base.common.UncheckedExceptionHandler;
import com.google.code.validationframework.base.trigger.AbstractTrigger;

import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Trigger initiating the validation when the combobox popup menu is canceled.
 *
 * @see AbstractTrigger
 * @see Disposable
 * @see PopupMenuListener#popupMenuCanceled(PopupMenuEvent)
 */
public class JComboBoxCanceledTrigger extends AbstractTrigger implements Disposable {

    /**
     * Listener to popup menu events to trigger the validation.
     */
    private class SourceAdapter implements PopupMenuListener {

        /**
         * @see PopupMenuListener#popupMenuWillBecomeVisible(PopupMenuEvent)
         */
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            // Nothing to be done
        }

        /**
         * @see PopupMenuListener#popupMenuWillBecomeInvisible(PopupMenuEvent)
         */
        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            // Nothing to be done
        }

        /**
         * @see PopupMenuListener#popupMenuCanceled(PopupMenuEvent)
         */
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            fireTriggerEvent(new TriggerEvent(source));
        }
    }

    /**
     * Source combobox that will trigger the validation.
     */
    private JComboBox source = null;

    /**
     * Combobox listener actually triggering the validation.
     */
    private final PopupMenuListener sourceAdapter = new SourceAdapter();

    /**
     * Constructor specifying the source combobox that will trigger the validation.
     * <p>
     * Note that a default handler for unchecked exceptions occurring when firing trigger events will be used.
     *
     * @param source Source combobox that will trigger the validation.
     *
     * @see LogErrorUncheckedExceptionHandler
     */
    public JComboBoxCanceledTrigger(JComboBox source) {
        this(source, new LogErrorUncheckedExceptionHandler());
    }

    /**
     * Constructor specifying the source combobox that will trigger the validation.
     *
     * @param source                    Source combobox that will trigger the validation.
     * @param uncheckedExceptionHandler Handler for exceptions occurring when firing the trigger events.<br>
     *                                  If null, the default handler will be used.
     *
     * @see AbstractTrigger#AbstractTrigger(UncheckedExceptionHandler)
     */
    public JComboBoxCanceledTrigger(JComboBox source, UncheckedExceptionHandler uncheckedExceptionHandler) {
        super(uncheckedExceptionHandler);
        this.source = source;
        source.addPopupMenuListener(sourceAdapter);
    }

    /**
     * Gets the source component.
     *
     * @return Source component.
     */
    public JComboBox getComponent() {
        return source;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (source != null) {
            source.removePopupMenuListener(sourceAdapter);
            source = null;
        }
    }
}
