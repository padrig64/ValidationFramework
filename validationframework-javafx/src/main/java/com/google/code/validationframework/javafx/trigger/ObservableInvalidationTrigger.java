package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class ObservableInvalidationTrigger extends AbstractTrigger implements Disposable {

    private class InvalidationAdapter implements InvalidationListener {

        @Override
        public void invalidated(Observable observable) {
            fireTriggerEvent(new TriggerEvent(observable));
        }
    }

    private final Observable observable;

    private final InvalidationAdapter invalidationAdapter = new InvalidationAdapter();

    public ObservableInvalidationTrigger(Observable observable) {
        super();
        this.observable = observable;
        this.observable.addListener(invalidationAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        observable.removeListener(invalidationAdapter);
    }
}
