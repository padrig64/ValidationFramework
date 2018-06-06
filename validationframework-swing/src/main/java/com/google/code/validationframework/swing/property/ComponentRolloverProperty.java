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
import com.google.code.validationframework.base.utils.ValueUtils;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Read-only property representing the rollover state of a component.
 * <p/>
 * If the mouse is over the component, the property value will be true.
 */
@Deprecated
public class ComponentRolloverProperty extends AbstractReadableProperty<Boolean> {

    /**
     * Rollover state tracker.
     */
    private final MouseListener rolloverAdapter = new RolloverAdapter();

    /**
     * Component to track the rollover state for.
     */
    private Component component;

    /**
     * Current property value.
     */
    private boolean rollover = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param component Component whose rollover state is to be tracked.
     * @see #dispose()
     */
    public ComponentRolloverProperty(Component component) {
        super();
        this.component = component;
        this.component.addMouseListener(rolloverAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (component != null) {
            component.removeMouseListener(rolloverAdapter);
            component = null;
        }
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return rollover;
    }

    /**
     * Applies the specified value and notifies the value change listeners if needed.
     *
     * @param rollover New value.
     */
    private void setValue(boolean rollover) {
        if (!ValueUtils.areEqual(this.rollover, rollover)) {
            boolean oldValue = this.rollover;
            this.rollover = rollover;
            maybeNotifyListeners(oldValue, rollover);
        }
    }

    /**
     * Rollover state tracker.
     */
    private class RolloverAdapter extends MouseAdapter {

        /**
         * @see MouseAdapter#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            setValue(true);
        }

        /**
         * @see MouseAdapter#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(MouseEvent e) {
            setValue(false);
        }
    }
}
