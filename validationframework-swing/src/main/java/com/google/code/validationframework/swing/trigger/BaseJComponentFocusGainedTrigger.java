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

package com.google.code.validationframework.swing.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Base class for triggers on focus gain.
 * <p/>
 * When this trigger is not longer required, do not forget to call {@link #dispose()}.
 *
 * @param <C> Type of component whose focus is to be tracked.
 *
 * @see AbstractTrigger
 * @see #dispose()
 */
public class BaseJComponentFocusGainedTrigger<C extends Component> extends AbstractTrigger implements Disposable {

    /**
     * Focus listener firing a trigger event to the trigger listeners.
     */
    private class SourceAdapter extends FocusAdapter {

        /**
         * @see FocusListener#focusGained(FocusEvent)
         */
        @Override
        public void focusGained(FocusEvent e) {
            fireTriggerEvent(new TriggerEvent(source));
        }
    }

    /**
     * Component whose focus is to be tracked.
     */
    private C source = null;

    /**
     * Focus listener installed on the component.
     */
    private final FocusListener sourceAdapter = new SourceAdapter();

    /**
     * Constructor specified the component whose focus is to be tracked.
     * <p/>
     * A focus listener will be installed. So you may need to call {@link #dispose()} when trigger is no longer needed.
     *
     * @param source Component whose focus is to be tracked.
     *
     * @see #dispose()
     */
    public BaseJComponentFocusGainedTrigger(C source) {
        super();
        this.source = source;
        source.addFocusListener(sourceAdapter);
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
            source.removeFocusListener(sourceAdapter);
            source = null;
        }
    }
}
