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

package com.google.code.validationframework.swing.property;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.property.AbstractReadableWritableProperty;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Read/writable property representing the text in the document of a {@link JTextComponent} (for instance, a textfield,
 * a text area, etc.).
 * <p>
 * This property will always be synchronized with the contents of the document. Also, it is possible to modify the
 * component text by calling its {@link JTextComponent#setText(String)} method, or by modifying the contents of its
 * {@link Document}, or by call the {@link #setValue(String)} method. In all cases, this property will notify its
 * listeners of any change.
 * <p>
 * However, please note that calling {@link JTextComponent#setText(String)} is very likely to make this property fire
 * two value change events, because replacing is usually first done by removing the old text and inserting the new one.
 *
 * @see JTextComponent#getText()
 * @see JTextComponent#setText(String)
 * @see javax.swing.text.AbstractDocument#replace(int, int, String, javax.swing.text.AttributeSet)
 * @see Document
 */
public class JTextComponentTextProperty extends AbstractReadableWritableProperty<String, String> implements Disposable {

    /**
     * Entity tracking changes of/in the document.
     */
    private class ContentAdapter implements DocumentListener, PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("document".equals(propertyChangeEvent.getPropertyName())) {
                // Unhook from previous document
                if (propertyChangeEvent.getOldValue() instanceof Document) {
                    ((Document) propertyChangeEvent.getOldValue()).removeDocumentListener(this);
                }

                // Hook to new document
                if (propertyChangeEvent.getNewValue() instanceof Document) {
                    ((Document) propertyChangeEvent.getNewValue()).addDocumentListener(this);

                    // Update value from new document
                    updateValue();
                }
            }
        }

        /**
         * @see DocumentListener#insertUpdate(DocumentEvent)
         */
        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            updateValue();
        }

        /**
         * @see DocumentListener#removeUpdate(DocumentEvent)
         */
        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            updateValue();
        }

        /**
         * @see DocumentListener#changedUpdate(DocumentEvent)
         */
        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            updateValue();
        }

        /**
         * Updates the value of the property, possibly notifying the value change listeners.
         */
        private void updateValue() {
            if (!settingPropertyValue) {
                updatingFromComponent = true;
                try {
                    setValue(textComponent.getText());
                } finally {
                    updatingFromComponent = false;
                }
            }
        }
    }

    /**
     * Text component this property applies to.
     */
    private final JTextComponent textComponent;

    /**
     * Document changes tracker.
     */
    private final ContentAdapter documentTracker = new ContentAdapter();

    /**
     * Property value.
     */
    private String value = null;

    /**
     * Flag indicating whether the call to {@link #setValue(String)} comes from the document change.
     * <p>
     * This is used to distinguish whether the call to {@link #setValue(String)} is direct (call from the outside) or
     * indirect (change of the document itself from the outside or by the user).
     */
    private boolean settingPropertyValue = false;

    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the text component to which the property applies.
     *
     * @param textComponent Text component to which the property applies.
     */
    public JTextComponentTextProperty(JTextComponent textComponent) {
        this.textComponent = textComponent;

        // Hook to component
        textComponent.addPropertyChangeListener("document", documentTracker);
        textComponent.getDocument().addDocumentListener(documentTracker);

        // Set initial value
        this.value = textComponent.getText();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        textComponent.removePropertyChangeListener("document", documentTracker);
        textComponent.getDocument().removeDocumentListener(documentTracker);
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(String value) {
        if (!isNotifyingListeners()) {
            settingPropertyValue = true;
            try {
                String oldValue = this.value;
                this.value = value;
                if (!updatingFromComponent) {
                    // Use setText() because it already does all what is needed to update the document
                    textComponent.setText(value);
                }
                maybeNotifyListeners(oldValue, value);
            } finally {
                settingPropertyValue = false;
            }
        }
    }
}
