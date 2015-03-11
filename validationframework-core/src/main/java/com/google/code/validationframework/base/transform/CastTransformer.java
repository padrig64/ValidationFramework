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

package com.google.code.validationframework.base.transform;

import com.google.code.validationframework.api.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Transformer transforming input by casting.
 *
 * @param <I> Type of the input object to be transformed.
 * @param <O> Type of the output object after transformation.
 *
 * @see Transformer
 */
public class CastTransformer<I, O> implements Transformer<I, O>, Serializable {

    /**
     * Type of behavior in case of an error while casting.
     */
    public enum CastErrorBehavior {

        /**
         * Just return null.
         */
        IGNORE,

        /**
         * Log a warning and return null.
         */
        LOG_WARNING,

        /**
         * Log an error and return null.
         */
        LOG_ERROR,

        /**
         * Throw a ClassCastException.
         */
        TRHOW_EXCEPTION
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -1055074777411651306L;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CastTransformer.class);

    /**
     * Behavior in case of an error while casting.
     */
    private final CastErrorBehavior castErrorBehavior;

    /**
     * Default constructor
     */
    public CastTransformer() {
        this(CastErrorBehavior.IGNORE);
    }

    /**
     * Constructor specifying the behavior in case of an error while casting.
     *
     * @param castErrorBehavior Behavior in case of an error while casting.
     */
    public CastTransformer(CastErrorBehavior castErrorBehavior) {
        super();
        this.castErrorBehavior = castErrorBehavior;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public O transform(I input) {
        O output;

        try {
            // Cast
            output = (O) input;
        } catch (ClassCastException e) {
            switch (castErrorBehavior) {
                case LOG_WARNING:
                    LOGGER.warn("Failed casting input: " + input, e);
                    break;
                case LOG_ERROR:
                    LOGGER.error("Failed casting input: " + input, e);
                    break;
                case TRHOW_EXCEPTION:
                    throw e;
                case IGNORE:
                default:
            }
            output = null;
        }

        return output;
    }
}
