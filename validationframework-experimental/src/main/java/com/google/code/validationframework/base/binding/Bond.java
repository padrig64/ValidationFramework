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

import com.google.code.validationframework.api.binding.ChangeListener;
import com.google.code.validationframework.api.binding.ReadableProperty;
import com.google.code.validationframework.api.binding.WritableProperty;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bond<MO, SI> implements Disposable {

    private class MasterAdapter implements ChangeListener<MO> {

        private final Transformer<Object, SI> lastTransformer = new CastTransformer<Object, SI>();

        @Override
        public void propertyChanged(ReadableProperty<MO> property, MO oldValue, MO newValue) {
            // Get value from all masters
            List<MO> values = new ArrayList<MO>();
            for (ReadableProperty<MO> master : masters) {
                values.add(master.getValue());
            }

            // Transform value
            Object transformedValue = values;
            for (Transformer transformer : transformers) {
                transformedValue = transformer.transform(transformedValue);
            }
            SI slaveInputValue = lastTransformer.transform(transformedValue);

            // Notify slaves
            for (WritableProperty<SI> slave : slaves) {
                slave.setValue(slaveInputValue);
            }
        }
    }

    private final MasterAdapter masterAdapter = new MasterAdapter();

    private final Collection<ReadableProperty<MO>> masters;

    private final Collection<Transformer> transformers;

    private final Collection<WritableProperty<SI>> slaves;

    public Bond(Collection<ReadableProperty<MO>> masters, Collection<Transformer> transformers,
                Collection<WritableProperty<SI>> slaves) {
        this.masters = masters;
        this.transformers = transformers;
        this.slaves = slaves;

        for (ReadableProperty<MO> master : masters) {
            master.addChangeListener(masterAdapter);
        }

        // Slave initial values
        masterAdapter.propertyChanged(null, null, null);
    }

    public Collection<ReadableProperty<MO>> getMasters() {
        return masters;
    }

    public Collection<Transformer> getTransformers() {
        return transformers;
    }

    public Collection<WritableProperty<SI>> getSlaves() {
        return slaves;
    }

    @Override
    public void dispose() {
        for (ReadableProperty<MO> master : masters) {
            master.removeChangeListener(masterAdapter);
        }
    }
}
