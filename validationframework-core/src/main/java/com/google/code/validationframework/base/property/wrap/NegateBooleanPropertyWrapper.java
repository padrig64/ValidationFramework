package com.google.code.validationframework.base.property.wrap;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.AbstractReadableProperty;
import com.google.code.validationframework.base.transform.NegateBooleanTransformer;

public class NegateBooleanPropertyWrapper extends AbstractReadableProperty<Boolean> {

    private class ValueChangeAdapter implements ValueChangeListener<Boolean> {

        @Override
        public void valueChanged(ReadableProperty<Boolean> property, Boolean oldValue, Boolean newValue) {
            Boolean transformedOldValue = transformer.transform(oldValue);
            Boolean transformedNewValue = transformer.transform(newValue);
            notifyListeners(transformedOldValue, transformedNewValue);
        }
    }

    private final ReadableProperty<Boolean> wrappedProperty;

    private final Transformer<Boolean, Boolean> transformer;

    public NegateBooleanPropertyWrapper(ReadableProperty<Boolean> wrappedProperty) {
        this.wrappedProperty = wrappedProperty;
        this.wrappedProperty.addValueChangeListener(new ValueChangeAdapter());
        this.transformer = new NegateBooleanTransformer();
    }

    public NegateBooleanPropertyWrapper(ReadableProperty<Boolean> wrappedProperty, Boolean nullNegation) {
        this.wrappedProperty = wrappedProperty;
        this.wrappedProperty.addValueChangeListener(new ValueChangeAdapter());
        this.transformer = new NegateBooleanTransformer(nullNegation);
    }

    @Override
    public Boolean getValue() {
        return transformer.transform(wrappedProperty.getValue());
    }
}
