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
import com.google.code.validationframework.base.property.AbstractReadableProperty;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Read-only property representing the pressed state of a button.
 * <p/>
 * Note that this property is not writable because the pressed state is normally controller by user action. But it might
 * become writable in the future.
 *
 * @see JButtonPressedProperty
 * @see JMenuItemPressedProperty
 * @see JToggleButtonPressedProperty
 */
public class ButtonPressedProperty extends AbstractReadableProperty<Boolean> {

    /**
     * Pressed state tracker.
     */
    private final ChangeListener pressedStateAdapter = new EventAdapter();

    /**
     * Button whose pressed state is to be tracked.
     */
    private AbstractButton button;

    /**
     * Flag stating whether the button is pressed or not.
     */
    private boolean pressed = false;

    /**
     * Constructor specifying the button whose pressed state is to be tracked.
     *
     * @param button Button whose pressed state is to be tracked.
     */
    public ButtonPressedProperty(AbstractButton button) {
        super();
        this.button = button;
        this.button.addChangeListener(pressedStateAdapter);

        // Set initial value
        this.pressed = button.getModel().isPressed();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (button != null) {
            button.removeChangeListener(pressedStateAdapter);
            button = null;
        }
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return pressed;
    }

    /**
     * Sets the value of this property.
     *
     * @param pressed True if the button is pressed, false otherwise.
     */
    private void setValue(boolean pressed) {
        boolean oldValue = this.pressed;
        this.pressed = pressed;
        maybeNotifyListeners(oldValue, pressed);
    }

    /**
     * Pressed state tracker.
     */
    private class EventAdapter implements ChangeListener {

        /**
         * @see ChangeListener#stateChanged(ChangeEvent)
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            if (button != null) {
                setValue(button.getModel().isPressed());
            }
        }
    }
}
