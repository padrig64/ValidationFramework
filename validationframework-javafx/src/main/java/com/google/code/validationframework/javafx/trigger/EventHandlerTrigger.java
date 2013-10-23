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

package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Trigger initiating the validation whenever an event of the specified class and type(s) is fired.
 *
 * @param <E> Class of event triggering the validation.
 *
 * @see EventHandler
 */
public class EventHandlerTrigger<E extends Event> extends AbstractTrigger implements EventHandler<E> {

    /**
     * Event types triggering the validation.
     */
    private final Set<EventType<? extends Event>> eventTypes = new HashSet<EventType<? extends Event>>();

    /**
     * Default constructor accepting any type of event to trigger the validation.
     */
    public EventHandlerTrigger() {
        super();
        eventTypes.add(E.ANY);
    }

    /**
     * Constructor specifying the types of event to trigger the validation.
     *
     * @param eventTypes Event types triggering the validation.
     */
    @SafeVarargs
    public EventHandlerTrigger(EventType<? extends Event>... eventTypes) {
        super();
        if (eventTypes != null) {
            Collections.addAll(this.eventTypes, eventTypes);
        }
    }

    /**
     * @see EventHandler#handle(Event)
     */
    @Override
    public void handle(E e) {
        if (eventTypes.isEmpty() || eventTypes.contains(E.ANY) || eventTypes.contains(e.getEventType()))
            fireTriggerEvent(new TriggerEvent(e.getSource()));
    }
}
