/*
 * Copyright (c) 2013, Patrick Moawad
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

package com.google.code.validationframework.binding;

import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Binder {

    public static class SingleMasterContext<MO, SI> {

        private final Master<MO> master;

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        public SingleMasterContext(Master<MO> master, Collection<Transformer> transformers) {
            this.master = master;
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public <TSI> SingleMasterContext<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            transformers.add(transformer);
            return new SingleMasterContext<MO, TSI>(master, transformers);
        }

        public void to(Slave<SI> slave) {
            SingleMasterTransformersProxy<MO, SI> proxy = new SingleMasterTransformersProxy<MO, SI>(transformers);
            master.addSlave(proxy);
            proxy.addSlave(slave);

            proxy.masterChanged(master);
        }
    }

    public static class MultipleMasterContext<T> {

        private final Collection<Master<T>> masters;

        public MultipleMasterContext(Collection<Master<T>> masters) {
            this.masters = masters;
        }

        public void to(Slave<Collection<T>> slave) {
            MultipleMastersProxy<T> proxy = new MultipleMastersProxy<T>(masters);

            for (Master<T> master : masters) {
                master.addSlave(proxy);
            }

            proxy.addSlave(slave);
        }
    }

    private static class SingleMasterTransformersProxy<MO, SI> extends SimpleMaster<SI> implements Slave<MO> {

        private final Collection<Transformer> transformers;

        private final CastTransformer<Object, SI> lastTransformer = new CastTransformer<Object, SI>();

        public SingleMasterTransformersProxy(Collection<Transformer> transformers) {
            this.transformers = transformers;
        }

        @Override
        public void masterChanged(Master<MO> changedMaster) {
            Object transformedValue = changedMaster.getValue();
            for (Transformer transformer : transformers) {
                transformedValue = transformer.transform(transformedValue);
            }

            SI slaveInput = lastTransformer.transform(transformedValue);
            setValue(slaveInput);
        }
    }

    private static class MultipleMastersProxy<MO> implements Slave<MO>, Master<Collection<MO>> {

        private final Collection<Master<MO>> masters;

        private final List<Slave<Collection<MO>>> slaves = new ArrayList<Slave<Collection<MO>>>();

        public MultipleMastersProxy(Collection<Master<MO>> masters) {
            this.masters = masters;
        }

        @Override
        public void masterChanged(Master<MO> changedMaster) {
            for (Slave<Collection<MO>> slave : slaves) {
                slave.masterChanged(this);
            }
        }

        @Override
        public void addSlave(Slave<Collection<MO>> slave) {
            slaves.add(slave);
        }

        @Override
        public void removeSlave(Slave<Collection<MO>> slave) {
            slaves.remove(slave);
        }

        @Override
        public Collection<MO> getValue() {
            List<MO> values = new ArrayList<MO>();

            for (Master<MO> master : masters) {
                values.add(master.getValue());
            }

            return values;
        }
    }

    /**
     * Private constructor for utility class.
     */
    private Binder() {
        // Nothing to be done
    }

    public static <MO> SingleMasterContext<MO, MO> bind(Master<MO> master) {
        return new SingleMasterContext<MO, MO>(master, null);
    }

    public static <MO> MultipleMasterContext<MO> bind(Collection<Master<MO>> masters) {
        return new MultipleMasterContext<MO>(masters);
    }
}
