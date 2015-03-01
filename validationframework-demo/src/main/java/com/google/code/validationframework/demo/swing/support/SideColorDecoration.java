/*
 * Copyright (c) 2015, Patrick Moawad
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

package com.google.code.validationframework.demo.swing.support;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.swing.decoration.AbstractComponentDecoration;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.property.simple.SimpleColorProperty;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;

public class SideColorDecoration extends AbstractComponentDecoration {

    private final SimpleColorProperty colorProperty = new SimpleColorProperty(new Color(255, 89, 71));

    public SideColorDecoration(JComponent decoratedComponent) {
        super(decoratedComponent, new Anchor(0.0f, -1, 0.0f, 3), Anchor.TOP_RIGHT);
        colorProperty.addValueChangeListener(new ValueChangeListener<Color>() {
            @Override
            public void valueChanged(ReadableProperty<Color> property, Color oldValue, Color newValue) {
                followAndRepaint();
            }
        });
    }

    public ReadableWritableProperty<Color, Color> getColorProperty() {
        return colorProperty;
    }

    @Override
    protected int getWidth() {
        return 6;
    }

    @Override
    protected int getHeight() {
        return getDecoratedComponent().getHeight() - 6;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(colorProperty.getValue());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
