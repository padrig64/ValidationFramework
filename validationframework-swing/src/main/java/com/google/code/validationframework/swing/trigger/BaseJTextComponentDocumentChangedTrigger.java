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

package com.google.code.validationframework.swing.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * Trigger that initiates the validation whenever the document of a text component is modified.
 *
 * @see AbstractTrigger
 * @see Disposable
 */
public class BaseJTextComponentDocumentChangedTrigger<C extends JTextComponent> extends AbstractTrigger implements
        Disposable {

    /**
     * Listener to changes in the document of the source text component.
     */
    private class SourceAdapter implements DocumentListener {

        /**
         * Flag indicating whether a pending remove update should be ignored.
         *
         * @see #removeUpdate(DocumentEvent)
         * @see #insertUpdate(DocumentEvent)
         */
        private boolean skipPendingRemove = false;

        /**
         * @see DocumentListener#insertUpdate(DocumentEvent)
         * @see #skipPendingRemove
         * @see #removeUpdate(DocumentEvent)
         */
        @Override
        public void insertUpdate(DocumentEvent e) {
            // Inhibit the pending remove update if any
            skipPendingRemove = true;

            // Fire trigger event (once for both remove and insert updates)
            fireTriggerEvent(new TriggerEvent(source));
        }

        /**
         * @see DocumentListener#removeUpdate(DocumentEvent)
         * @see #skipPendingRemove
         * @see #insertUpdate(DocumentEvent)
         */
        @Override
        public void removeUpdate(DocumentEvent e) {
            /*
             * Reschedule the remove update later and fire the associated event only if there is no subsequent insert
             * update.<br>This is done because, very often, when the text is replace by another text (or if the focus
             * leaves a formatted textfield that has a valid value with the COMMIT focus lost behavior), the original
             * text is first removed (leaving the text component empty), and then the new text is added. So two document
             * change events are fired what would trigger the validation twice. Here is an example where it becomes
             * annoying: a formatted textfield validating numbers should consider an empty field to be invalid; an Apply
             * button gets disabled when the field is invalid: if the user enters a valid number and then clicks the
             * Apply button, the focus lost behavior set on the formatted textfield would make it reformat the value,
             * what would first fire a remove update, what would make the validation disable the Apply button, and then
             * would fire an insert update, what would make the validation re-enable the Apply button; but disabling the
             * button would actually disarm it while it is being pressed.
             */
            skipPendingRemove = false;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // Check if it is has not been inhibited by a sub-sequent insert update
                    if (!skipPendingRemove) {
                        fireTriggerEvent(new TriggerEvent(source));
                    }
                }
            });
        }

        /**
         * @see DocumentListener#changedUpdate(DocumentEvent)
         */
        @Override
        public void changedUpdate(DocumentEvent e) {
            fireTriggerEvent(new TriggerEvent(source));
        }
    }

    /**
     * Text component that is the source of the trigger.
     */
    protected C source = null;

    /**
     * Listener to changes in the document of the text component.
     */
    private final DocumentListener sourceAdapter = new SourceAdapter();

    /**
     * Constructor specifying the text component to listen to.
     *
     * @param source Text component to listen to.
     */
    public BaseJTextComponentDocumentChangedTrigger(C source) {
        super();
        this.source = source;
        source.getDocument().addDocumentListener(sourceAdapter);
        // TODO Track document replacement
    }

    /**
     * Gets the source component.
     *
     * @return Source component.
     */
    public C getComponent() {
        return source;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (source != null) {
            source.getDocument().removeDocumentListener(sourceAdapter);
            source = null;
        }
    }
}
