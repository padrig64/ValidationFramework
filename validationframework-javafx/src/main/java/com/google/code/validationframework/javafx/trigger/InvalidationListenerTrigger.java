package com.google.code.validationframework.javafx.trigger;

import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.trigger.AbstractTrigger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class InvalidationListenerTrigger extends AbstractTrigger implements InvalidationListener {

    /**
     * @see InvalidationListener#invalidated(Observable)
     */
    @Override
    public void invalidated(Observable observable) {
        fireTriggerEvent(new TriggerEvent(observable));
    }
}
