package com.google.code.validationframework.base.property;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;

/**
 * Readable property holding a constant value.
 * <p/>
 * Note that no listener will ever be notified since the value will not be able to change.
 * <p/>
 * Finally note that if the passed value is an object whose internal state can change (e.g. a collection where items can
 * be added/modified/removed), no listeners will notified. The constance in this case is about the reference. Such a use
 * case is obviously not the purpose of this class.
 *
 * @param <R> Type of data that can be read from this property.
 */
public class ConstantProperty<R> implements ReadableProperty<R> {

    /**
     * Constant value.
     */
    private final R value;

    /**
     * Constructor.
     *
     * @param value Constant value.
     */
    public ConstantProperty(R value) {
        this.value = value;
    }

    /**
     * Does nothing because the listener will never be triggered, because this property's value will never change.
     *
     * @see ReadableProperty#addValueChangeListener(ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ValueChangeListener<R> listener) {
        // Nothing to be done
    }

    /**
     * Does nothing because no listener was actually added.
     *
     * @see ReadableProperty#removeValueChangeListener(ValueChangeListener)
     * @see #addValueChangeListener(ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ValueChangeListener<R> listener) {
        // Nothing to be done
    }

    /**
     * @see ReadableProperty#getValue()
     */
    @Override
    public R getValue() {
        return value;
    }
}
