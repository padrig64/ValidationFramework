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

import com.google.code.validationframework.api.binding.ReadableProperty;
import com.google.code.validationframework.api.binding.ValueChangeListener;
import com.google.code.validationframework.api.binding.WritableProperty;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.transform.Transformer;

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
 * @see CompositeReadableProperty
 * @see CompositeWritableProperty
 */
public class Bond<MO, SI> implements Disposable {

    /**
     * Listener to master property changes and updating the slave property.
     */
    private class MasterAdapter implements ValueChangeListener<MO> {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<MO> property, MO oldValue, MO newValue) {
            // Transform value
            SI slaveInputValue = transformer.transform(newValue);

            // Notify slave(s)
            slave.setValue(slaveInputValue);
        }
    }

    /**
     * Listener to master property changes and updating the slave property.
     */
    private final MasterAdapter masterAdapter = new MasterAdapter();

    /**
     * Master (possibly composite) that is part of the bond.
     */
    private final ReadableProperty<MO> master;

    /**
     * Transformer (possible composite) to be part of the bond.
     */
    private final Transformer<MO, SI> transformer;

    /**
     * Slave (possibly composite) that is part of the bond.
     */
    private final WritableProperty<SI> slave;

    /**
     * Constructor specifying the master property, the transformers and the slaves that are part of the binding.
     * <p/>
     * Note that the master property can be a composition of multiple properties, for instance, using the {@link
     * CompositeReadableProperty}.
     * <p/>
     * For type safety, it is highly advised to use the {@link Binder} to create the bond.
     *
     * @param master      Master (possibly composite) property to be part of the bond.
     * @param transformer Transformer (possibly composite) to be part of the bond.
     * @param slave       Slave (possibly composite) property to be part of the bond.
     */
    public Bond(ReadableProperty<MO> master, Transformer<MO, SI> transformer, WritableProperty<SI> slave) {
        this.master = master;
        this.transformer = transformer;
        this.slave = slave;

        master.addValueChangeListener(masterAdapter);

        // Slave initial values
        masterAdapter.valueChanged(master, null, master.getValue());
    }

    /**
     * Constructor specifying the master property, the transformers and the slaves that are part of the binding.
     * <p/>
     * Note that the master property can be a composition of multiple properties, for instance, using the {@link
     * CompositeReadableProperty}.
     * <p/>
     * For type safety, it is highly advised to use the {@link Binder} to create the bond.
     *
     * @param master      Master (possibly composite) property to be part of the bond.
     * @param transformer Transformer (possible composite) to be part of the bond.
     * @param slaves      Slave properties to be part of the bond.
     */
    public Bond(ReadableProperty<MO> master, Transformer<MO, SI> transformer, Collection<WritableProperty<SI>> slaves) {
        this(master, transformer, new CompositeWritableProperty<SI>(slaves));
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        master.removeValueChangeListener(masterAdapter);
    }
}
