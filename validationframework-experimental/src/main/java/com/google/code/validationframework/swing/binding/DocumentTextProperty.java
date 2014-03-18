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

package com.google.code.validationframework.swing.binding;

import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.property.AbstractReadableProperty;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DocumentTextProperty extends AbstractReadableProperty<String> implements WritableProperty<String>,
        Disposable {

    private class ContentAdapter implements DocumentListener, PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("document".equals(propertyChangeEvent.getPropertyName())) {
                if (propertyChangeEvent.getOldValue() instanceof Document) {
                    ((Document) propertyChangeEvent.getOldValue()).removeDocumentListener(this);
                }
                if (propertyChangeEvent.getNewValue() instanceof Document) {
                    ((Document) propertyChangeEvent.getNewValue()).addDocumentListener(this);
                }
            }
        }

        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            updateValue();
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            updateValue();
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            updateValue();
        }

        private void updateValue() {
            updatingFromDocument = true;
            setValue(textComponent.getText());
            updatingFromDocument = false;
        }
    }

    private final JTextComponent textComponent;

    private final ContentAdapter contentAdapter = new ContentAdapter();

    private String value = null;

    private boolean updatingFromDocument = false;

    public DocumentTextProperty(JTextComponent textComponent) {
        this.textComponent = textComponent;

        // Hook to component
        textComponent.addPropertyChangeListener("document", contentAdapter);
        textComponent.getDocument().addDocumentListener(contentAdapter);

        // Set initial value
        setValue(textComponent.getText());
    }

    @Override
    public void dispose() {
        // Unhook from components
        textComponent.removePropertyChangeListener("document", contentAdapter);
        textComponent.getDocument().removeDocumentListener(contentAdapter);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        if (updatingFromDocument) {
            String oldValue = this.value;
            this.value = value;
            notifyListeners(oldValue, this.value);
        } else {
            textComponent.setText(value);
        }
    }
}
