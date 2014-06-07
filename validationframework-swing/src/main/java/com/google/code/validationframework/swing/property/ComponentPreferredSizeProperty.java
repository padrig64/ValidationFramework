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

package com.google.code.validationframework.swing.property;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Readable/writable property representing the preferred size of a {@link Component}.
 * <p/>
 * It is possible to control the preferred size of the component by setting the value of this property or by calling the
 * {@link Component#setPreferredSize(Dimension)} method of that component.
 * <p/>
 * Note that changing the width or height attribute of the {@link Dimension} object directly will have no effect on this
 * property. It is therefore not advised to do so.
 * <p/>
 * Finally, note that null values are supported by this property.
 *
 * @see Component#getPreferredSize()
 * @see Component#setPreferredSize(Dimension)
 * @see Component#isPreferredSizeSet()
 */
public class ComponentPreferredSizeProperty extends AbstractComponentProperty<Component, Dimension> {

    /**
     * @see AbstractComponentProperty#AbstractComponentProperty(Component, String)
     */
    public ComponentPreferredSizeProperty(Component component) {
        super(component, "preferredSize");
    }

    /**
     * @see AbstractComponentProperty#getPropertyValueFromComponent()
     */
    @Override
    protected Dimension getPropertyValueFromComponent() {
        return component.getPreferredSize();
    }

    /**
     * @see AbstractComponentProperty#setPropertyValueToComponent(Object)
     */
    @Override
    protected void setPropertyValueToComponent(Dimension value) {
        component.setPreferredSize(value);
    }
}
