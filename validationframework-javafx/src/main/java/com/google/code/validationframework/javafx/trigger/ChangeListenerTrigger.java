package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ChangeListenerTrigger<T> extends AbstractTrigger implements ChangeListener<T> {

    /**
     * @see ChangeListener#changed(ObservableValue, Object, Object)
     */
    @Override
    public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
        fireTriggerEvent(new TriggerEvent(observableValue));
    }
}
