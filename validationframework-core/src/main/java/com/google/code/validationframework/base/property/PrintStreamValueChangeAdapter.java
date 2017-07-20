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

package com.google.code.validationframework.base.property;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;

import java.io.PrintStream;

/**
 * Value change listener printing the change information in a {@link PrintStream}.
 *
 * @param <T> Type of property value.
 */
public class PrintStreamValueChangeAdapter<T> implements ValueChangeListener<T> {

    /**
     * Print stream to be use to print the change information.
     */
    private final PrintStream stream;

    /**
     * Name of the property to be displayed in the property.
     */
    private final String propertyName;

    /**
     * Default constructor.
     * <p>
     * By default, the {@link System#out} print stream will be used, and the simple class name of the property will be
     * printed out.
     */
    public PrintStreamValueChangeAdapter() {
        this(null, null);
    }

    /**
     * Constructor specifying the print stream to be used to print the change information.
     * <p>
     * By default, the simple class name of the property will be printed with the change information.
     *
     * @param stream Print stream to be used to print the change information.
     */
    public PrintStreamValueChangeAdapter(PrintStream stream) {
        this(stream, null);
    }

    /**
     * Constructor specifying the property name to be printed with the change information.
     * <p>
     * By default, the {@link System#out} print stream will be used.
     *
     * @param propertyName Property name to be printed with the change information.
     */
    public PrintStreamValueChangeAdapter(String propertyName) {
        this(null, propertyName);
    }

    /**
     * Constructor specifying the print stream to be used to print the change information, and the property name to be
     * printed with the change information.
     *
     * @param stream       Print stream to be used to print the change information.
     * @param propertyName Property name to be printed with the change information.
     */
    public PrintStreamValueChangeAdapter(PrintStream stream, String propertyName) {
        if (stream == null) {
            this.stream = System.out;
        } else {
            this.stream = stream;
        }
        this.propertyName = propertyName;
    }

    /**
     * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
     */
    @Override
    public void valueChanged(ReadableProperty<T> property, T oldValue, T newValue) {
        String name = propertyName;
        if (name == null) {
            name = property.getClass().getSimpleName();
        }
        stream.println(name + ": " + oldValue + " => " + newValue);
    }
}
