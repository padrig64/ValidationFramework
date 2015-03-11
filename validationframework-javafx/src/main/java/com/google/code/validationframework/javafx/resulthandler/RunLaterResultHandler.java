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

package com.google.code.validationframework.javafx.resulthandler;

import com.google.code.validationframework.api.resulthandler.ResultHandler;
import javafx.application.Platform;

/**
 * Result handler wrapper to re-schedule the handling of the result by the wrapped result handler on the FX Application
 * Thread.
 * <p/>
 * This can be useful when validation is performed outside the application thread but the handling needs to be done on
 * the application thread.
 */
public class RunLaterResultHandler<RHI> implements ResultHandler<RHI> {

    /**
     * Runnable to be scheduled on the application thread to handle the result with the wrapped result handler.
     */
    private class RescheduledResultHandler implements Runnable {

        /**
         * Result to be handled by the wrapped result handler.
         */
        private final RHI result;

        /**
         * Constructor specifying the result to be handled by the wrapped result handler.
         *
         * @param result Result to be processed.
         */
        public RescheduledResultHandler(RHI result) {
            this.result = result;
        }

        /**
         * @see Runnable#run()
         */
        @Override
        public void run() {
            wrappedResultHandler.handleResult(result);
        }
    }

    /**
     * Default behavior of running later if already on the application thread.
     */
    private static final boolean DEFAULT_EVEN_IF_ALREADY_ON_APPLICATION_THREAD = true;

    /**
     * Flag indicating whether re-scheduling the result handler on the application thread should always occur, or only
     * if not already on the application thread.
     */
    private final boolean eventIfAlreadyOnApplicationThread;

    /**
     * Wrapped result handler to be re-scheduled on the application thread.
     */
    private final ResultHandler<RHI> wrappedResultHandler;

    /**
     * Constructor specifying the wrapped result handler to be re-scheduled.
     * <p/>
     * By default, the handling of the result by the wrapped result handler will always be re-scheduled later, even if
     * already on the application thread.
     *
     * @param wrappedResultHandler Wrapped result handler.
     */
    public RunLaterResultHandler(ResultHandler<RHI> wrappedResultHandler) {
        this(wrappedResultHandler, DEFAULT_EVEN_IF_ALREADY_ON_APPLICATION_THREAD);
    }

    /**
     * Constructor specifying the wrapped result handler to be re-scheduled and whether re-scheduling should occur even
     * if already on the application thread.
     *
     * @param wrappedResultHandler Wrapped result handler.
     * @param evenIfAlreadyOnApplicationThread
     *                             Flag indicating whether re-scheduling the result handler on the application thread
     *                             should always occur, or only if not already on the application thread.
     */
    public RunLaterResultHandler(ResultHandler<RHI> wrappedResultHandler, boolean evenIfAlreadyOnApplicationThread) {
        this.wrappedResultHandler = wrappedResultHandler;
        this.eventIfAlreadyOnApplicationThread = evenIfAlreadyOnApplicationThread;
    }

    /**
     * @see ResultHandler#handleResult(Object)
     */
    @Override
    public void handleResult(RHI result) {
        if (eventIfAlreadyOnApplicationThread || !Platform.isFxApplicationThread()) {
            // Either forced or not yet on application thread
            Platform.runLater(new RescheduledResultHandler(result));
        } else {
            // Already on application thread
            wrappedResultHandler.handleResult(result);
        }
    }
}
