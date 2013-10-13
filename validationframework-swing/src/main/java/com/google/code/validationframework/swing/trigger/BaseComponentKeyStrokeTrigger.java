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

package com.google.code.validationframework.swing.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;

import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Trigger that initiates the validation whenever registered key strokes happen on a component.
 *
 * @see AbstractTrigger
 * @see Disposable
 */
public class BaseComponentKeyStrokeTrigger<C extends Component> extends AbstractTrigger implements Disposable {

    /**
     * Listener to key strokes on source text component.
     */
    private class SourceAdapter implements KeyListener {

        /**
         * @see KeyListener#keyPressed(KeyEvent)
         */
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            processEvent(keyEvent);
        }

        /**
         * @see KeyListener#keyReleased(KeyEvent)
         */
        @Override
        public void keyReleased(KeyEvent keyEvent) {
            processEvent(keyEvent);
        }

        /**
         * @see KeyListener#keyTyped(KeyEvent)
         */
        @Override
        public void keyTyped(KeyEvent keyEvent) {
            processEvent(keyEvent);
        }

        /**
         * Checks whether the specified key event matches and triggers the validation if so.
         *
         * @param keyEvent Key event to be triggered.
         */
        private void processEvent(KeyEvent keyEvent) {
            if (keyStrokes.isEmpty()) {
                // No key stroke define, so react on any key event
                fireTriggerEvent(new TriggerEvent(source));
            } else {
                for (KeyStroke keyStroke : keyStrokes) {
                    if (KeyStroke.getKeyStrokeForEvent(keyEvent).equals(keyStroke)) {
                        fireTriggerEvent(new TriggerEvent(source));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Text component that is the source of the trigger.
     */
    protected C source = null;

    /**
     * Listener to key strokes on the text component.
     */
    private final KeyListener sourceAdapter = new SourceAdapter();

    /**
     * Key strokes for which the validation will be triggered.
     */
    private final Set<KeyStroke> keyStrokes = new HashSet<KeyStroke>();

    /**
     * Constructor specifying the text component to listen to and the key stroke to trigger the validation.
     * <p/>
     * If no key stroke is provided, the trigger will initiate the validation on any key stroke.
     * <p/>
     * You may use {@link KeyStroke} utility methods to build a {@link KeyStroke}.
     *
     * @param source     Text component to listen to.
     * @param keyStrokes Key strokes to trigger the validation.
     *
     * @see KeyStroke
     */
    public BaseComponentKeyStrokeTrigger(C source, KeyStroke... keyStrokes) {
        super();

        this.source = source;
        if (keyStrokes != null) {
            for (KeyStroke keyStroke : keyStrokes) {
                addKeyStroke(keyStroke);
            }
        }
        source.addKeyListener(sourceAdapter);
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
     * Gets the key strokes for which the validation will be triggered.
     *
     * @return Key strokes for which the validation will be triggered.
     */
    public Collection<KeyStroke> getKeyStokes() {
        return new ArrayList<KeyStroke>(keyStrokes);
    }

    /**
     * Adds a key stroke for which the validation will be triggered.
     * <p/>
     * You may use {@link KeyStroke} utility methods to build a {@link KeyStroke}.
     *
     * @param keyStroke Key stroke to be added to trigger the validation.
     *
     * @see KeyStroke
     */
    public void addKeyStroke(KeyStroke keyStroke) {
        keyStrokes.add(keyStroke);
    }

    /**
     * Removes a key stroke for which the validation should no longer be triggered.
     * <p/>
     * If all key strokes are removed, the trigger will initiate the validation on any key stroke.
     *
     * @param keyStroke Key stroke to be removed.
     */
    public void removeKey(KeyStroke keyStroke) {
        keyStrokes.remove(keyStroke);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (source != null) {
            source.removeKeyListener(sourceAdapter);
            source = null;
        }
    }
}
