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

package com.google.code.validationframework.base.trigger;

import com.google.code.validationframework.api.trigger.TriggerEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Trigger that can be added as a {@link PropertyChangeListener} and triggers the validation when a property change
 * event is received.
 * <p/>
 * This is particularly handy if the entity you register to is based on a {@link java.beans.PropertyChangeSupport} or
 * equivalent.
 * <p/>
 * However, it is still possible in this trigger to initiate the validation only on some properties, instead of all
 * properties.
 *
 * @see AbstractTrigger
 * @see PropertyChangeListener
 */
public class PropertyChangeTrigger extends AbstractTrigger implements PropertyChangeListener {

    private final Collection<String> triggerProperties;

    /**
     * Default constructor.
     * <p/>
     * The trigger will initiate the validation whenever any property change is trigger.
     */
    public PropertyChangeTrigger() {
        this.triggerProperties = null;
    }

    /**
     * Constructor specifying the properties to initiate the validation.
     * <p/>
     * Any other property change will not initiate the validation.
     *
     * @param triggerProperties Property names to initiate the validation.
     */
    public PropertyChangeTrigger(String... triggerProperties) {
        this.triggerProperties = Arrays.asList(triggerProperties);
    }

    /**
     * Constructor specifying the properties to initiate the validation.
     * <p/>
     * Any other property change will not initiate the validation.
     *
     * @param triggerProperties Property names to initiate the validation.
     */
    public PropertyChangeTrigger(Collection<String> triggerProperties) {
        this.triggerProperties = new ArrayList<String>(triggerProperties);
    }

    /**
     * Triggers the validation when the property change event is received.
     *
     * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if ((triggerProperties == null) || triggerProperties.contains(propertyChangeEvent.getPropertyName())) {
            fireTriggerEvent(new TriggerEvent(propertyChangeEvent.getSource()));
        }
    }
}
