/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.experimental.builder;

import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.experimental.builder.context.simplevalidator.DataProviderContext;
import com.google.code.validationframework.experimental.builder.context.simplevalidator.TriggerContext;

import java.util.Collection;

/**
 * Use {@link com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder} instead.
 */
public final class SimpleValidatorBuilder {

    /**
     * Private constructor for utility class.
     */
    private SimpleValidatorBuilder() {
        // Nothing to be done
    }

    /**
     * Use {@link com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder}
     * instead.
     */
    public static DataProviderContext on(Trigger trigger) {
        return new TriggerContext().on(trigger);
    }

    /**
     * Use {@link com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder}
     * instead.
     */
    public static DataProviderContext on(Trigger... triggers) {
        return new TriggerContext().on(triggers);
    }

    /**
     * Use {@link com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder}
     * instead.
     */
    public static DataProviderContext on(Collection<Trigger> triggers) {
        return new TriggerContext().on(triggers);
    }

    /**
     * Use {@link com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder}
     * instead.
     */
    public static DataProviderContext on(Class<? extends Trigger> triggerClass) {
        return new TriggerContext().on(triggerClass);
    }
}
