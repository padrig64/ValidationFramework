package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.api.binding.ReadableProperty;
import com.google.code.validationframework.api.binding.ReadablePropertyChangeListener;
import com.google.code.validationframework.api.binding.WritableProperty;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bond<MO, SI> implements Disposable {

    private class MasterAdapter implements ReadablePropertyChangeListener<MO> {

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
