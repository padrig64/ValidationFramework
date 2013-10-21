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
