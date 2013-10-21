package com.google.code.validationframework.javafx.resulthandler;

import com.google.code.validationframework.api.resulthandler.ResultHandler;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;

public class NodeDisablingResultHandler implements ResultHandler<Boolean> {

    private final BooleanProperty disableProperty;

    public NodeDisablingResultHandler(Node node) {
        if (node == null) {
            disableProperty = null;
        } else {
            disableProperty = node.disableProperty();
        }
    }

    @Override
    public void handleResult(Boolean result) {
        if (disableProperty != null) {
            disableProperty.set(result);
        }
    }
}
