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
import com.google.code.validationframework.base.property.simple.SimpleIntegerProperty;
import com.google.code.validationframework.swing.decoration.AbstractComponentDecoration;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Example of implementation of a component decoration showing a number of something (e.g. messages).
 */
public class CountDecoration extends AbstractComponentDecoration {

    private int width = 0;

    private int height = 0;

    private final SimpleIntegerProperty countProperty = new SimpleIntegerProperty(0);

    public CountDecoration(JComponent decoratedComponent, Anchor decoratedComponentAnchor, Anchor decorationAnchor) {
        super(decoratedComponent, decoratedComponentAnchor, decorationAnchor);
        countProperty.addValueChangeListener(new CountAdapter());
    }

    public ReadableWritableProperty<Integer, Integer> getCountProperty() {
        return countProperty;
    }

    private void updateSize() {
        String text = getText();
        Font font = getDecoratedComponent().getFont().deriveFont(Font.BOLD);
        width = getDecoratedComponent().getFontMetrics(font).stringWidth(text) + 10;
        height = getDecoratedComponent().getFontMetrics(font).getAscent() + 5;
    }

    private String getText() {
        String text;

        if (countProperty.getValue() > 100) {
            text = "100+";
        } else {
            text = Integer.toString(countProperty.getValue());
        }

        return text;
    }

    @Override
    protected int getWidth() {
        return width;
    }

    @Override
    protected int getHeight() {
        return height;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(20, 20, 20, 100));
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g.setColor(new Color(235, 235, 235));
        g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);

        g.setColor(new Color(255, 89, 71));
        g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);

        g.setColor(Color.WHITE);
        g.setFont(getDecoratedComponent().getFont().deriveFont(Font.BOLD));
        g.drawString(getText(), 5, getHeight() - 4);

        g.dispose();
    }

    private class CountAdapter implements ValueChangeListener<Integer> {

        @Override
        public void valueChanged(ReadableProperty<Integer> property, Integer oldValue, Integer newValue) {
            updateSize();
            followAndRepaint();
        }
    }
}
