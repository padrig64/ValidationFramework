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
import java.util.Arrays;
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
            // Connect master -> proxy -> slave
            SingleMasterProxy<MO, SI> proxy = new SingleMasterProxy<MO, SI>(transformers);
            master.addSlave(proxy);
            proxy.addSlave(slave);

            // Set initial values in proxy and slave
            proxy.setValue(master.getValue());
        }

        public void to(Collection<Slave<SI>> slaves) {
            // Connect master -> proxy -> slaves
            SingleMasterProxy<MO, SI> proxy = new SingleMasterProxy<MO, SI>(transformers);
            master.addSlave(proxy);
            for (Slave<SI> slave : slaves) {
                proxy.addSlave(slave);
            }

            // Set initial values in proxy and slave
            proxy.setValue(master.getValue());
        }

        public void to(Slave<SI>... slaves) {
            to(Arrays.asList(slaves));
        }
    }

    private static class SingleMasterProxy<MO, SI> implements Slave<MO>, Master<SI> {

        /**
         * Generated serial UID.
         */
        private static final long serialVersionUID = 5765153977289533185L;

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        private final Transformer<Object, SI> lastTransformer = new CastTransformer<Object, SI>();

        private final List<Slave<SI>> slaves = new ArrayList<Slave<SI>>();

        private SI value = null;

        public SingleMasterProxy(Collection<Transformer> transformers) {
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        @Override
        public void addSlave(Slave<SI> slave) {
            slaves.add(slave);
        }

        @Override
        public void removeSlave(Slave<SI> slave) {
            slaves.remove(slave);
        }

        @Override
        public SI getValue() {
            return value;
        }

        @Override
        public void setValue(MO value) {
            // Transform value
            Object transformedValue = value;
            for (Transformer transformer : transformers) {
                transformedValue = transformer.transform(transformedValue);
            }
            this.value = lastTransformer.transform(transformedValue);

            // Notify slaves
            notifySlaves();
        }

        private void notifySlaves() {
            for (Slave<SI> slave : slaves) {
                slave.setValue(value);
            }
        }
    }

    public static class MultipleMasterContext<MO, SI> {

        private final Collection<Master<MO>> masters;

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        public MultipleMasterContext(Collection<Master<MO>> masters, Collection<Transformer> transformers) {
            this.masters = masters;
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public <TSI> MultipleMasterContext<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            transformers.add(transformer);
            return new MultipleMasterContext<MO, TSI>(masters, transformers);
        }

        public void to(Slave<SI> slave) {
            // Connect masters -> proxy -> slave
            MultipleMasterProxy<MO, SI> proxy = new MultipleMasterProxy<MO, SI>(masters, transformers);
            for (Master<MO> master : masters) {
                master.addSlave(proxy);
            }
            proxy.addSlave(slave);

            // Slave initial values in proxy and slave
            proxy.setValue(null);
        }

        public void to(Collection<Slave<SI>> slaves) {
            // Connect masters -> proxy -> slaves
            MultipleMasterProxy<MO, SI> proxy = new MultipleMasterProxy<MO, SI>(masters, transformers);
            for (Master<MO> master : masters) {
                master.addSlave(proxy);
            }
            for (Slave<SI> slave : slaves) {
                proxy.addSlave(slave);
            }

            // Slave initial values in proxy and slave
            proxy.setValue(null);
        }

        public void to(Slave<SI>... slaves) {
            to(Arrays.asList(slaves));
        }
    }

    private static class MultipleMasterProxy<MO, SI> implements Slave<MO>, Master<SI> {

        /**
         * Generated serial UID.
         */
        private static final long serialVersionUID = 5765153977289533185L;

        private final List<Master<MO>> masters = new ArrayList<Master<MO>>();

        private final List<Transformer> transformers = new ArrayList<Transformer>();

        private final Transformer<Object, SI> lastTransformer = new CastTransformer<Object, SI>();

        private final List<Slave<SI>> slaves = new ArrayList<Slave<SI>>();

        private SI value = null;

        public MultipleMasterProxy(Collection<Master<MO>> masters, Collection<Transformer> transformers) {
            if (masters != null) {
                this.masters.addAll(masters);
            }
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        @Override
        public void addSlave(Slave<SI> slave) {
            slaves.add(slave);
        }

        @Override
        public void removeSlave(Slave<SI> slave) {
            slaves.remove(slave);
        }

        @Override
        public SI getValue() {
            return value;
        }

        @Override
        public void setValue(MO value) {
            // Get value from all masters
            List<MO> values = new ArrayList<MO>();
            for (Master<MO> master : masters) {
                values.add(master.getValue());
            }

            // Transform value
            Object transformedValue = values;
            for (Transformer transformer : transformers) {
                transformedValue = transformer.transform(transformedValue);
            }
            this.value = lastTransformer.transform(transformedValue);

            // Notify slaves
            notifySlaves();
        }

        private void notifySlaves() {
            for (Slave<SI> slave : slaves) {
                slave.setValue(value);
            }
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

    public static <MO> MultipleMasterContext<MO, Collection<MO>> bind(Collection<Master<MO>> masters) {
        return new MultipleMasterContext<MO, Collection<MO>>(masters, null);
    }

    public static <MO> MultipleMasterContext<MO, Collection<MO>> bind(Master<MO>... masters) {
        return new MultipleMasterContext<MO, Collection<MO>>(Arrays.asList(masters), null);
    }
}
