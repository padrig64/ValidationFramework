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
import java.util.ArrayList;
import java.util.List;

/**
 * Composite result handler that will process results using all delegate result handlers.
 *
 * @param <R> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 *
 * @see ResultHandler
 */
public class CompositeResultHandler<R> implements ResultHandler<R> {

	/**
	 * Delegate result handlers.
	 */
	private final List<ResultHandler<R>> resultHandlers = new ArrayList<ResultHandler<R>>();

	/**
	 * Default constructor.
	 */
	public CompositeResultHandler() {
		// Nothing to be done
	}

	/**
	 * Constructor specifying the delegate result handler(s).
	 *
	 * @param resultHandlers Delegate result handler(s).
	 *
	 * @see #addResultHandler(ResultHandler)
	 */
	public CompositeResultHandler(final ResultHandler<R>... resultHandlers) {
		if (resultHandlers != null) {
			for (final ResultHandler<R> resultHandler : resultHandlers) {
				addResultHandler(resultHandler);
			}
		}
	}

	/**
	 * Adds the specified delegate result handler.
	 *
	 * @param resultHandler Delegate result handler to be added.
	 */
	public void addResultHandler(final ResultHandler<R> resultHandler) {
		resultHandlers.add(resultHandler);
	}

	/**
	 * Removes the specified delegate result handler.
	 *
	 * @param resultHandler Delegate result handler to be removed.
	 */
	public void removeResultHandler(final ResultHandler<R> resultHandler) {
		resultHandlers.remove(resultHandler);
	}

	/**
	 * Processes the specified result using all delegate result handlers.
	 *
	 * @param result Validation result to be handled.
	 *
	 * @see ResultHandler#handleResult(Object)
	 */
	@Override
	public void handleResult(final R result) {
		for (final ResultHandler<R> resultHandler : resultHandlers) {
			resultHandler.handleResult(result);
		}
	}
}
