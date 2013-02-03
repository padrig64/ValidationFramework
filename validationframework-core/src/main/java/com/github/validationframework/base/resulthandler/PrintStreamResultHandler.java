/*
 * Copyright (c) 2012, Patrick Moawad
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

package com.github.validationframework.base.resulthandler;

import com.github.validationframework.api.resulthandler.ResultHandler;

import java.io.PrintStream;

/**
 * Result handler writing the result on a printing stream.<br>This can be useful for logging and/or debugging.
 *
 * @param <O> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 *
 * @see ResultHandler
 */
public class PrintStreamResultHandler<O> implements ResultHandler<O> {

    /**
     * Default output print stream.
     */
    private static final PrintStream DEFAULT_STREAM = System.out;

    /**
     * Default prefix to be prepended to the result.
     */
    private static final String DEFAULT_PREFIX = "";

    /**
     * Default suffix to be appended to the result.
     */
    private static final String DEFAULT_SUFFIX = "";

    /**
     * Output print stream.
     */
    private final PrintStream stream;

    /**
     * Prefix to be prepended to the result.
     */
    private final String prefix;

    /**
     * Suffix to be appended to the result.
     */
    private final String suffix;

    /**
     * Default constructor making the result handler print the results to the standard output, with no prefix and no
     * suffix.
     */
    public PrintStreamResultHandler() {
        this(DEFAULT_STREAM, DEFAULT_PREFIX, DEFAULT_SUFFIX);
    }

    /**
     * Constructor making the result handler write the results to the specified print stream, with no prefix and no
     * suffix.
     *
     * @param stream Print stream to write the results to.
     */
    public PrintStreamResultHandler(final PrintStream stream) {
        this(stream, DEFAULT_PREFIX, DEFAULT_SUFFIX);
    }

    /**
     * Default constructor making the result handler print the results to the standard output, with the specified prefix
     * but no suffix.
     *
     * @param prefix Prefix to be prepended to the result.
     */
    public PrintStreamResultHandler(final String prefix) {
        this(DEFAULT_STREAM, prefix, DEFAULT_SUFFIX);
    }

    /**
     * Constructor making the result handler write the results to the specified print stream,
     * with the specified prefix and
     * suffix.
     *
     * @param stream Print stream to write the results to.
     * @param prefix Prefix to be prepended to the result.
     * @param suffix Suffix to be appened to the result.
     */
    public PrintStreamResultHandler(final PrintStream stream, final String prefix, final String suffix) {
        this.stream = stream;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * @see ResultHandler#handleResult(Object)
     */
    @Override
    public void handleResult(final O result) {
        final StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        builder.append(result);
        builder.append(suffix);

        stream.println(builder);
    }
}
