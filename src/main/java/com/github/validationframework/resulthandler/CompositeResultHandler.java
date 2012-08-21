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

package com.github.validationframework.resulthandler;

import java.util.ArrayList;
import java.util.List;

public class CompositeResultHandler<R> implements ResultHandler<R> {

	private final List<ResultHandler<R>> resultHandlers = new ArrayList<ResultHandler<R>>();

	/**
	 * Default constructor.
	 */
	public CompositeResultHandler() {
		// Nothing to be done
	}

	public CompositeResultHandler(final ResultHandler<R>... resultHandlers) {
		if (resultHandlers != null) {
			for (final ResultHandler<R> resultHandler : resultHandlers) {
				addResultHandler(resultHandler);
			}
		}
	}

	public void addResultHandler(final ResultHandler<R> resultHandler) {
		resultHandlers.add(resultHandler);
	}

	public void removeResultHandler(final ResultHandler<R> resultHandler) {
		resultHandlers.remove(resultHandler);
	}

	@Override
	public void handleResult(final R result) {
		for (final ResultHandler<R> resultHandler : resultHandlers) {
			resultHandler.handleResult(result);
		}
	}
}
