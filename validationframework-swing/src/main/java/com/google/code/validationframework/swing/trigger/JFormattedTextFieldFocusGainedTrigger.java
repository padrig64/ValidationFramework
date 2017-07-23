/*
 * Copyright (c) 2017, ValidationFramework Authors
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

package com.google.code.validationframework.swing.trigger;

import javax.swing.JFormattedTextField;

/**
 * Convenience class for triggers on focus gain by a {@link JFormattedTextField}.
 * <p>
 * When this trigger is not longer required, do not forget to call {@link #dispose()}.
 *
 * @see BaseJComponentFocusGainedTrigger
 * @see #dispose()
 */
public class JFormattedTextFieldFocusGainedTrigger extends BaseJComponentFocusGainedTrigger<JFormattedTextField> {

    /**
     * Constructor specified the formatted textfield whose focus is to be tracked.
     * <p>
     * A focus listener will be installed. So you may need to call {@link #dispose()} when trigger is no longer needed.
     *
     * @param source Component whose focus is to be tracked.
     *
     * @see #dispose()
     */
    public JFormattedTextFieldFocusGainedTrigger(JFormattedTextField source) {
        super(source);
    }
}
