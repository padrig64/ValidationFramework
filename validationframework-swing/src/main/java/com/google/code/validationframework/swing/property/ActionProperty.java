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
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.AbstractReadableWritableProperty;
import com.google.code.validationframework.base.transform.CastTransformer;

import javax.swing.Action;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Implementation of a readable/writable property representing a bean property of an {@link Action} that can be tracked
 * using a {@link PropertyChangeListener}.
 * <p/>
 * It is possible to control the bean property of the action by setting the value of this property or by calling the
 * bean property setter method of that action.
 * <p/>
 * Refer to {@link Action} for a list of supported property names, as well as the associated types and values.
 *
 * @param <P> Type of bean property.
 * @see Action
 */
@Deprecated
public class ActionProperty<P> extends AbstractReadableWritableProperty<P> {

    /**
     * Name of the property to be tracked.
     */
    private final String propertyName;

    /**
     * Bean property tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Transformer used to cast the property value to the required type.
     */
    private final Transformer<Object, P> castTransformer = new CastTransformer<Object, P>();

    /**
     * Action to track the bean property of.
     */
    protected Action action;

    /**
     * Current property value.
     */
    private P value = null;

    /**
     * Flag indicating whether the {@link #setValue(Object)} call is due to a property change event.
     */
    private boolean updatingFromAction = false;

    /**
     * Constructor specifying the action for which the property applies.
     *
     * @param action       Action whose property is to be tracked.
     * @param propertyName Name of the property to be tracked.
     */
    public ActionProperty(Action action, String propertyName) {
        super();

        this.action = action;
        this.propertyName = propertyName;

        // Hook to action
        this.action.addPropertyChangeListener(eventAdapter);

        // Set initial value
        value = getPropertyValueFromAction();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (action != null) {
            action.removePropertyChangeListener(eventAdapter);
            action = null;
        }
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public P getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(P value) {
        if (!isNotifyingListeners()) {
            if (updatingFromAction) {
                P oldValue = this.value;
                this.value = value;
                maybeNotifyListeners(oldValue, this.value);
            } else {
                setPropertyValueToAction(value);
            }
        }
    }

    /**
     * Effectively gets the value from the property of the action.
     *
     * @return Action property value.
     */
    protected P getPropertyValueFromAction() {
        Object actionValue;

        if (action == null) {
            actionValue = null;
        } else {
            if ("enabled".equals(propertyName)) {
                actionValue = action.isEnabled();
            } else {
                actionValue = action.getValue(propertyName);
            }
        }

        return castTransformer.transform(actionValue);
    }

    /**
     * Effectively sets the value to the property of the action.
     *
     * @param value New action property value.
     */
    protected void setPropertyValueToAction(P value) {
        if (action != null) {
            if ("enabled".equals(propertyName)) {
                action.setEnabled(Boolean.TRUE.equals(value));
            } else {
                action.putValue(propertyName, value);
            }
        }
    }

    /**
     * Bean property tracker.
     */
    private class EventAdapter implements PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updatingFromAction = true;
            setValue(getPropertyValueFromAction());
            updatingFromAction = false;
        }
    }
}
