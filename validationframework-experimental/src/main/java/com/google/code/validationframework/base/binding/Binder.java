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

package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Utility class that can be used to help binding properties and transform their values.
 * <p/>
 * If you are using JavaFX, you should better use JavaFX's property binding mechanism. The binding mechanism provided by
 * the ValidationFramework is mostly meant for Swing and other frameworks that can benefit from it. JavaFX has a much
 * more furnished API to achieve similar goals and much more.
 *
 * @see ReadableProperty
 * @see WritableProperty
 */
public final class Binder {

    public static class SingleMasterBinding<MO, SI> {

        private final ReadableProperty<MO> master;

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        public SingleMasterBinding(ReadableProperty<MO> master, Collection<Transformer> transformers) {
            this.master = master;
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public <TSI> SingleMasterBinding<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            transformers.add(transformer);
            return new SingleMasterBinding<MO, TSI>(master, transformers);
        }

        public void to(WritableProperty<SI> slave) {
            // Connect master -> proxy -> slave
            SingleMasterProxy<MO, SI> proxy = new SingleMasterProxy<MO, SI>(transformers);
            master.addChangeListener(proxy);
            proxy.addSlave(slave);

            // Set initial values in proxy and slave
            proxy.propertyChanged(null, null, master.getValue());
        }

        public void to(Collection<WritableProperty<SI>> slaves) {
            // Connect master -> proxy -> slaves
            SingleMasterProxy<MO, SI> proxy = new SingleMasterProxy<MO, SI>(transformers);
            master.addChangeListener(proxy);
            for (WritableProperty<SI> slave : slaves) {
                proxy.addSlave(slave);
            }

            // Set initial values in proxy and slave
            proxy.propertyChanged(null, null, master.getValue());
        }

        public void to(WritableProperty<SI>... slaves) {
            to(Arrays.asList(slaves));
        }
    }

    private static class SingleMasterProxy<MO, SI> implements ReadablePropertyChangeListener<MO> {

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        private final Transformer<Object, SI> lastTransformer = new CastTransformer<Object, SI>();

        private final List<WritableProperty<SI>> slaves = new ArrayList<WritableProperty<SI>>();

        public SingleMasterProxy(Collection<Transformer> transformers) {
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public void addSlave(WritableProperty<SI> slave) {
            slaves.add(slave);
        }

        public void removeSlave(WritableProperty<SI> slave) {
            slaves.remove(slave);
        }

        @Override
        public void propertyChanged(ReadableProperty<MO> property, MO oldValue, MO newValue) {
            // Transform value
            Object transformedValue = newValue;
            for (Transformer transformer : transformers) {
                transformedValue = transformer.transform(transformedValue);
            }
            SI slaveInputValue = lastTransformer.transform(transformedValue);

            // Update slaves
            for (WritableProperty<SI> slave : slaves) {
                slave.setValue(slaveInputValue);
            }
        }
    }

    public static class MultipleMastersBinding<MO, SI> {

        private final Collection<ReadableProperty<MO>> masters;

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        public MultipleMastersBinding(Collection<ReadableProperty<MO>> masters, Collection<Transformer> transformers) {
            this.masters = masters;
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public <TSI> MultipleMastersBinding<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            transformers.add(transformer);
            return new MultipleMastersBinding<MO, TSI>(masters, transformers);
        }

        public void to(WritableProperty<SI> slave) {
            // Connect masters -> proxy -> slave
            MultipleMastersProxy<MO, SI> proxy = new MultipleMastersProxy<MO, SI>(masters, transformers);
            for (ReadableProperty<MO> master : masters) {
                master.addChangeListener(proxy);
            }
            proxy.addSlave(slave);

            // Slave initial values in proxy and slave
            proxy.propertyChanged(null, null, null);
        }

        public void to(Collection<WritableProperty<SI>> slaves) {
            // Connect masters -> proxy -> slaves
            MultipleMastersProxy<MO, SI> proxy = new MultipleMastersProxy<MO, SI>(masters, transformers);
            for (ReadableProperty<MO> master : masters) {
                master.addChangeListener(proxy);
            }
            for (WritableProperty<SI> slave : slaves) {
                proxy.addSlave(slave);
            }

            // Slave initial values in proxy and slave
            proxy.propertyChanged(null, null, null);
        }

        public void to(WritableProperty<SI>... slaves) {
            to(Arrays.asList(slaves));
        }
    }

    private static class MultipleMastersProxy<MO, SI> implements ReadablePropertyChangeListener<MO> {

        private final List<ReadableProperty<MO>> masters = new ArrayList<ReadableProperty<MO>>();

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        private final Transformer<Object, SI> lastTransformer = new CastTransformer<Object, SI>();

        private final List<WritableProperty<SI>> slaves = new ArrayList<WritableProperty<SI>>();

        public MultipleMastersProxy(Collection<ReadableProperty<MO>> masters, Collection<Transformer> transformers) {
            if (masters != null) {
                this.masters.addAll(masters);
            }
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public void addSlave(WritableProperty<SI> slave) {
            slaves.add(slave);
        }

        public void removeSlave(WritableProperty<SI> slave) {
            slaves.remove(slave);
        }

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

    /**
     * Private constructor for utility class.
     */
    private Binder() {
        // Nothing to be done
    }

    /**
     * Specifies the master property that is part of the binding.
     *
     * @param master Master property.
     * @param <MO>   Type of value to be read from the master property.
     *
     * @return DSL object.
     */
    public static <MO> SingleMasterBinding<MO, MO> from(ReadableProperty<MO> master) {
        return new SingleMasterBinding<MO, MO>(master, null);
    }

    /**
     * Specifies the master properties that are part of the binding.
     *
     * @param masters Master properties.
     * @param <MO>    Type of value to be read from the master properties.
     *
     * @return DSL object.
     */
    public static <MO> MultipleMastersBinding<MO, Collection<MO>> from(Collection<ReadableProperty<MO>> masters) {
        return new MultipleMastersBinding<MO, Collection<MO>>(masters, null);
    }

    /**
     * Specifies the master properties that are part of the binding.
     *
     * @param masters Master properties.
     * @param <MO>    Type of value to be read from the master properties.
     *
     * @return DSL object.
     */
    public static <MO> MultipleMastersBinding<MO, Collection<MO>> from(ReadableProperty<MO>... masters) {
        return new MultipleMastersBinding<MO, Collection<MO>>(Arrays.asList(masters), null);
    }
}
