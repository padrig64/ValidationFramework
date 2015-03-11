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

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Test;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see ActionProperty
 */
public class ActionPropertyTest {

    private static final String DESCRIPTION1 = "First long description";

    private static final String DESCRIPTION2 = "Second long description";

    @Test
    public void testInitialEnabledPropertyFromAction() {
        Action action = new TestAction();
        action.setEnabled(true);
        ReadableProperty<Boolean> enabledProperty = new ActionProperty<Boolean>(action, "enabled");
        assertTrue(enabledProperty.getValue());

        action.setEnabled(false);
        enabledProperty = new ActionProperty<Boolean>(action, "enabled");
        assertFalse(enabledProperty.getValue());
    }

    @Test
    public void testInitialOtherPropertyFromAction() {
        Action action = new TestAction();
        ReadableProperty<String> longDescriptionProperty = new ActionProperty<String>(action, Action.LONG_DESCRIPTION);
        assertEquals(null, longDescriptionProperty.getValue());

        action.putValue(Action.LONG_DESCRIPTION, DESCRIPTION1);
        longDescriptionProperty = new ActionProperty<String>(action, Action.LONG_DESCRIPTION);
        assertEquals(DESCRIPTION1, longDescriptionProperty.getValue());

        action.putValue(Action.LONG_DESCRIPTION, DESCRIPTION2);
        longDescriptionProperty = new ActionProperty<String>(action, Action.LONG_DESCRIPTION);
        assertEquals(DESCRIPTION2, longDescriptionProperty.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        Action action = new TestAction();
        ReadableWritableProperty<String, String> property = new ActionProperty<String>(action, Action.LONG_DESCRIPTION);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals(null, property.getValue());
        assertEquals(null, action.getValue(Action.LONG_DESCRIPTION));
        property.setValue(DESCRIPTION1);
        assertEquals(DESCRIPTION1, action.getValue(Action.LONG_DESCRIPTION));

        property.setValue(DESCRIPTION2);
        assertEquals(DESCRIPTION2, action.getValue(Action.LONG_DESCRIPTION));

        // Check exactly two events fired
        verify(listenerMock).valueChanged(property, null, DESCRIPTION1);
        verify(listenerMock).valueChanged(property, DESCRIPTION1, DESCRIPTION2);
        verify(listenerMock, times(2)).valueChanged(any(ActionProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromAction() {
        Action action = new TestAction();
        ReadableWritableProperty<String, String> property = new ActionProperty<String>(action, Action.LONG_DESCRIPTION);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals(null, property.getValue());
        action.putValue(Action.LONG_DESCRIPTION, DESCRIPTION1);
        assertEquals(DESCRIPTION1, property.getValue());

        action.putValue(Action.LONG_DESCRIPTION, DESCRIPTION2);
        assertEquals(DESCRIPTION2, property.getValue());

        // Check exactly two events fired
        verify(listenerMock).valueChanged(property, null, DESCRIPTION1);
        verify(listenerMock).valueChanged(property, DESCRIPTION1, DESCRIPTION2);
        verify(listenerMock, times(2)).valueChanged(any(ActionProperty.class), anyString(), anyString());
    }

    /**
     * Dummy implementation of an action for test purposes.
     */
    private static class TestAction extends AbstractAction {

        /**
         * Generated serial UID.
         */
        private static final long serialVersionUID = -7129510551813878814L;

        /**
         * @see AbstractAction#actionPerformed(ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Do nothing
        }
    }
}
