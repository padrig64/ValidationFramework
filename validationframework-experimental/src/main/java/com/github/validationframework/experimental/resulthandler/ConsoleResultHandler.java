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

package com.github.validationframework.experimental.resulthandler;

import com.github.validationframework.api.resulthandler.ResultHandler;
import java.io.PrintStream;

public class ConsoleResultHandler<O> implements ResultHandler<O> {

	private static final PrintStream DEFAULT_STREAM = System.out;

	private static final String DEFAULT_PREFIX = "";

	private final PrintStream stream;

	private final String prefix;

	public ConsoleResultHandler() {
		this(DEFAULT_STREAM, DEFAULT_PREFIX);
	}

	public ConsoleResultHandler(final PrintStream stream) {
		this(stream, DEFAULT_PREFIX);
	}

	public ConsoleResultHandler(final String prefix) {
		this(DEFAULT_STREAM, prefix);
	}

	public ConsoleResultHandler(final PrintStream stream, final String prefix) {
		this.stream = stream;
		this.prefix = prefix;
	}

	@Override
	public void handleResult(final O result) {
		stream.println(prefix + result);
	}
}
