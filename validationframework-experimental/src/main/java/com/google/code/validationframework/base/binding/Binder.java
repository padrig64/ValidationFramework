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

import com.google.code.validationframework.api.binding.ReadableProperty;
import com.google.code.validationframework.api.binding.WritableProperty;
import com.google.code.validationframework.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
 * @see SingleMasterBond
 * @see MultipleMasterBond
 */
public final class Binder {

    public static class SingleMasterBinding<MO, SI> {

        private final ReadableProperty<MO> master;

        private final List<Transformer<?, ?>> transformers = new ArrayList<Transformer<?, ?>>();

        public SingleMasterBinding(ReadableProperty<MO> master, Collection<Transformer<?, ?>> transformers) {
            this.master = master;
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public <TSI> SingleMasterBinding<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            transformers.add(transformer);
            return new SingleMasterBinding<MO, TSI>(master, transformers);
        }

        public SingleMasterBond<MO, SI> to(WritableProperty<SI> slave) {
            return new SingleMasterBond<MO, SI>(master, transformers, Collections.singleton(slave));
        }

        public SingleMasterBond<MO, SI> to(Collection<WritableProperty<SI>> slaves) {
            return new SingleMasterBond<MO, SI>(master, transformers, slaves);
        }

        public SingleMasterBond<MO, SI> to(WritableProperty<SI>... slaves) {
            return to(Arrays.asList(slaves));
        }
    }

    public static class MultipleMasterBinding<MO, SI> {

        private final Collection<ReadableProperty<MO>> masters;

        private final List<Transformer<?, ?>> transformers = new ArrayList<Transformer<?, ?>>();

        public MultipleMasterBinding(Collection<ReadableProperty<MO>> masters, Collection<Transformer<?,
                ?>> transformers) {
            this.masters = masters;
            if (transformers != null) {
                this.transformers.addAll(transformers);
            }
        }

        public <TSI> MultipleMasterBinding<MO, TSI> transform(Transformer<SI, TSI> transformer) {
            transformers.add(transformer);
            return new MultipleMasterBinding<MO, TSI>(masters, transformers);
        }

        public MultipleMasterBond<MO, SI> to(WritableProperty<SI> slave) {
            return new MultipleMasterBond<MO, SI>(masters, transformers, Collections.singleton(slave));
        }

        public MultipleMasterBond<MO, SI> to(Collection<WritableProperty<SI>> slaves) {
            return new MultipleMasterBond<MO, SI>(masters, transformers, slaves);
        }

        public MultipleMasterBond<MO, SI> to(WritableProperty<SI>... slaves) {
            return to(Arrays.asList(slaves));
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
    public static <MO> MultipleMasterBinding<MO, Collection<MO>> from(Collection<ReadableProperty<MO>> masters) {
        return new MultipleMasterBinding<MO, Collection<MO>>(masters, null);
    }

    /**
     * Specifies the master properties that are part of the binding.
     *
     * @param masters Master properties.
     * @param <MO>    Type of value to be read from the master properties.
     *
     * @return DSL object.
     */
    public static <MO> MultipleMasterBinding<MO, Collection<MO>> from(ReadableProperty<MO>... masters) {
        return new MultipleMasterBinding<MO, Collection<MO>>(Arrays.asList(masters), null);
    }
}
