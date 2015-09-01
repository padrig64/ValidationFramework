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

package com.google.code.validationframework.javafx.resulthandler.bool;

import com.google.code.validationframework.api.resulthandler.ResultHandler;
import javafx.css.PseudoClass;
import javafx.scene.Node;

/**
 * Result handler activating a pseudo-class on a {@link Node} depending on a boolean result.
 */
public class PseudoClassResultHandler implements ResultHandler<Boolean> {

    /**
     * Node to be controlled.
     */
    private final Node node;

    /**
     * Pseudo-class to be activated or de-activated.
     */
    private final PseudoClass pseudoClass;

    /**
     * Constructor.
     *
     * @param node        Node to be controlled.
     * @param pseudoClass Pseudo-class to be activated or de-activated.
     */
    public PseudoClassResultHandler(Node node, String pseudoClass) {
        this(node, PseudoClass.getPseudoClass(pseudoClass));
    }

    /**
     * Constructor.
     *
     * @param node        Node to be controlled.
     * @param pseudoClass Pseudo-class to be activated or de-activated.
     */
    public PseudoClassResultHandler(Node node, PseudoClass pseudoClass) {
        this.node = node;
        this.pseudoClass = pseudoClass;
    }

    /**
     * @see ResultHandler#handleResult(Object)
     */
    @Override
    public void handleResult(Boolean result) {
        node.pseudoClassStateChanged(pseudoClass, result);
    }
}
