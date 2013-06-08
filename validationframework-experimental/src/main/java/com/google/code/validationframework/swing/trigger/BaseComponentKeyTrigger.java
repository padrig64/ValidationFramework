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

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Trigger that initiates the validation whenever a key is pressed, released or typed on the component.
 *
 * @see AbstractTrigger
 * @see Disposable
 */
public class BaseComponentKeyTrigger<C extends Component> extends AbstractTrigger implements Disposable {

    /**
     * Listener to key strokes on source text component.
     */
    private class SourceAdapter implements KeyListener {

        /**
         * @see KeyListener#keyPressed(KeyEvent)
         */
        @Override
        public void keyPressed(final KeyEvent keyEvent) {
            processEvent(keyEvent);
        }

        /**
         * @see KeyListener#keyReleased(KeyEvent)
         */
        @Override
        public void keyReleased(final KeyEvent keyEvent) {
            processEvent(keyEvent);
        }

        /**
         * @see KeyListener#keyTyped(KeyEvent)
         */
        @Override
        public void keyTyped(final KeyEvent keyEvent) {
            processEvent(keyEvent);
        }

        /**
         * Checks whether the specified key event matches and triggers the validation if so.
         *
         * @param keyEvent Key event to be triggered.
         */
        private void processEvent(final KeyEvent keyEvent) {
            // Check event ID and key code
            if ((keyEvent.getID() == keyEventId) && (keyCodes.contains(keyEvent.getKeyCode()))) {
                // Check modifiers
                if (strictModifiersMatch) {
                    if ((keyEvent.getModifiers() == modifiersMask)) {
                        fireTriggerEvent(new TriggerEvent(source));
                    }
                } else if ((keyEvent.getModifiers() & modifiersMask) == modifiersMask) {
                    fireTriggerEvent(new TriggerEvent(source));
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

    private int keyEventId = KeyEvent.KEY_TYPED;

    private int modifiersMask = 0;

    private boolean strictModifiersMatch = false;

    private final Set<Integer> keyCodes = new HashSet<Integer>();

    /**
     * Constructor specifying the text component to listen to.
     *
     * @param source   Text component to listen to.
     * @param keyCodes Virtual key codes ({@link KeyEvent}#VK_*).
     */
    public BaseComponentKeyTrigger(final C source, final int... keyCodes) {
        super();
        this.source = source;
        if (keyCodes != null) {
            for (final int keyCode : keyCodes) {
                addKey(keyCode);
            }
        }
        source.addKeyListener(sourceAdapter);
    }

    public int getKeyEventId() {
        return keyEventId;
    }

    public void setKeyEventId() {
        this.keyEventId = keyEventId;
    }

    public int getModifiers() {
        return modifiersMask;
    }

    public void setModifiers(final int modifiersMask) {
        setModifiers(modifiersMask, false);
    }

    public void setModifiers(final int modifiersMask, final boolean strictModifiersMatch) {
        this.modifiersMask = modifiersMask;
        this.strictModifiersMatch = strictModifiersMatch;
    }

    /**
     * Gets the key codes for which the validation will be triggered.<br>Key codes correspond to {@link KeyEvent}#VK_*.
     *
     * @return Key codes for which the validation will be triggered.
     */
    public Collection<Integer> getKeys() {
        return keyCodes;
    }

    /**
     * Adds a key code for which the validation will be triggered.<br>Key codes correspond to {@link KeyEvent}#VK_*.
     *
     * @param keyCode Key code to be added to trigger the validation.
     */
    public void addKey(final int keyCode) {
        keyCodes.add(keyCode);
    }

    /**
     * Removes a key code for which the validation should no longer be triggered.<br>Key codes correspond to {@link
     * KeyEvent}#VK_*.
     *
     * @param keyCode Key code to be removed.
     */
    public void removeKey(final int keyCode) {
        keyCodes.remove(keyCode);
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
        source.removeKeyListener(sourceAdapter);
        source = null;
    }
}
