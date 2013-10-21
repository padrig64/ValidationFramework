package com.google.code.validationframework.javafx.dataprovider;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import javafx.beans.value.ObservableValue;

public class ObservableValueProvider<DPO> implements DataProvider<DPO> {

    private final ObservableValue<DPO> observableValue;

    public ObservableValueProvider(ObservableValue<DPO> observableValue) {
        this.observableValue = observableValue;
    }

    /**
     * @see DataProvider#getData()
     * @see ObservableValue#getValue()
     */
    @Override
    public DPO getData() {
        DPO value = null;

        if (observableValue != null) {
            value = observableValue.getValue();
        }

        return value;
    }
}
