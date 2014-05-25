/*
 * Copyright (c) 2014, Patrick Moawad
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

package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.CompositeWritableProperty;

import java.util.Collection;

/**
 * Simple implementation of a bond between master properties and slave properties.
 * <p/>
 * It is typically created using the {@link Binder}.
 *
 * @param <MO> Type of data that can be read from master properties.
 * @param <SI> Type of data that can be written to master properties.
 *
 * @see Binder
 * @see com.google.code.validationframework.base.property.CompositeReadableProperty
 * @see CompositeWritableProperty
 * @see com.google.code.validationframework.base.transform.ChainedTransformer
 */
public class SimpleBond<MO, SI> implements Disposable {

    /**
     * Listener to master property changes and updating the slave property.
     */
    private class MasterAdapter implements ValueChangeListener<MO> {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<MO> property, MO oldValue, MO newValue) {
            updateSlaves(newValue);
        }
    }

    /**
     * Listener to master property changes and updating the slave property.
     */
    private final MasterAdapter masterAdapter = new MasterAdapter();

    /**
     * Master (possibly composite) that is part of the bond.
     */
    private ReadableProperty<MO> master;

    /**
     * Transformer (possible composite) to be part of the bond.
     */
    private Transformer<MO, SI> transformer;

    /**
     * Slave (possibly composite) that is part of the bond.
     */
    private WritableProperty<SI> slave;

    /**
     * Constructor specifying the master property, the transformers and the slaves that are part of the binding.
     * <p/>
     * Note that the master property can be a composition of multiple properties, for instance, using the {@link
     * com.google.code.validationframework.base.property.CompositeReadableProperty}.
     * <p/>
     * For type safety, it is highly advised to use the {@link Binder} to create the bond.
     *
     * @param master      Master (possibly composite) property to be part of the bond.
     * @param transformer Transformer (possibly composite) to be part of the bond.
     * @param slave       Slave (possibly composite) property to be part of the bond.
     */
    public SimpleBond(ReadableProperty<MO> master, Transformer<MO, SI> transformer, WritableProperty<SI> slave) {
        init(master, transformer, slave);
    }

    /**
     * Constructor specifying the master property, the transformers and the slaves that are part of the binding.
     * <p/>
     * Note that the master property can be a composition of multiple properties, for instance, using the {@link
     * com.google.code.validationframework.base.property.CompositeReadableProperty}.
     * <p/>
     * For type safety, it is highly advised to use the {@link Binder} to create the bond.
     *
     * @param master      Master (possibly composite) property to be part of the bond.
     * @param transformer Transformer (possible composite) to be part of the bond.
     * @param slaves      Slave properties to be part of the bond.
     */
    public SimpleBond(ReadableProperty<MO> master, Transformer<MO, SI> transformer,
                      Collection<WritableProperty<SI>> slaves) {
        // Initialize bond
        CompositeWritableProperty<SI> compositeSlave = new CompositeWritableProperty<SI>();
        init(master, transformer, compositeSlave);

        // Add slave properties only after initialization, otherwise they will first be set to null
        for (WritableProperty<SI> slave : slaves) {
            compositeSlave.addProperty(slave);
        }
    }

    /**
     * Initializes the bond.
     *
     * @param master      Master (possibly composite) property.
     * @param transformer Value transformer.
     * @param slave       Slave (possibly composite) property.
     */
    private void init(ReadableProperty<MO> master, Transformer<MO, SI> transformer, WritableProperty<SI> slave) {
        this.master = master;
        this.transformer = transformer;
        this.slave = slave;

        master.addValueChangeListener(masterAdapter);

        // Slave initial values
        updateSlaves(master.getValue());
    }

    /**
     * Sets the value of the slaves according the value of the master.
     *
     * @param masterOutputValue Master value.
     */
    private void updateSlaves(MO masterOutputValue) {
        // Transform value
        SI slaveInputValue = transformer.transform(masterOutputValue);

        // Notify slave(s)
        slave.setValue(slaveInputValue);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        master.removeValueChangeListener(masterAdapter);
    }
}
