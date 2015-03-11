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

package com.google.code.validationframework.swing.dataprovider;

import com.google.code.validationframework.api.dataprovider.DataProvider;

import javax.swing.JToggleButton;

/**
 * Data provider retrieving the selection state from a toggle button.
 *
 * @param <C> Type of toggle button to retrieve the selection state from.
 *
 * @see DataProvider
 * @see JToggleButton
 */
public class BaseJToggleButtonValueProvider<C extends JToggleButton> implements DataProvider<Boolean> {

    /**
     * Toggle button to retrieve the selection state from.
     */
    private final C toggleComponent;

    /**
     * Constructor specifying the toggle button to retrieve the selection state from.
     *
     * @param toggleComponent Toggle button to retrieve the selection state from.
     */
    public BaseJToggleButtonValueProvider(final C toggleComponent) {
        this.toggleComponent = toggleComponent;
    }

    /**
     * Gets the component providing the data to be validated.
     *
     * @return Component providing the data to be validated.
     */
    public C getComponent() {
        return toggleComponent;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public Boolean getData() {
        return toggleComponent.isSelected();
    }
}
