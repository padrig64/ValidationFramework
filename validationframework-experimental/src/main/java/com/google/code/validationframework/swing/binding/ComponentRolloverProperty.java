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

package com.google.code.validationframework.swing.binding;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.binding.AbstractReadableProperty;
import com.google.code.validationframework.base.utils.ValueUtils;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ComponentRolloverProperty extends AbstractReadableProperty<Boolean> implements Disposable {

    private class RolloverAdapter implements MouseListener {

        @Override
        public void mouseEntered(MouseEvent e) {
            setValue(true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setValue(false);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // Nothing to be done
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Nothing to be done
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Nothing to be done
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -2940271817151485560L;

    private boolean rollover = false;

    private final Component component;

    private final MouseListener rolloverAdapter = new RolloverAdapter();

    public ComponentRolloverProperty(Component component) {
        this.component = component;
        this.component.addMouseListener(rolloverAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        component.removeMouseListener(rolloverAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return rollover;
    }

    private void setValue(boolean rollover) {
        if (!ValueUtils.areEqual(this.rollover, rollover)) {
            boolean oldValue = this.rollover;
            this.rollover = rollover;
            notifyListeners(oldValue, rollover);
        }
    }
}
