package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ObservableValueChangeTrigger<T> extends AbstractTrigger implements Disposable {

    private class ChangeAdapter implements ChangeListener<T> {

        @Override
        public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
            fireTriggerEvent(new TriggerEvent(observableValue));
        }
    }

    private final ObservableValue<T> observableValue;

    private final ChangeAdapter changeAdapter = new ChangeAdapter();

    public ObservableValueChangeTrigger(ObservableValue<T> observableValue) {
        super();
        this.observableValue = observableValue;
        this.observableValue.addListener(changeAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        observableValue.removeListener(changeAdapter);
    }
}
