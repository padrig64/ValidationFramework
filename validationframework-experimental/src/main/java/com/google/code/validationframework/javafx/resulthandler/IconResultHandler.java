/*
 * Copyright (c) 2013, Patrick Moawad
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class IconResultHandler implements ResultHandler<Boolean> {

    private static class DecorationPane extends Pane {

        private Node wrappedNode = null;

        public DecorationPane() {
            this(null);
        }

        public DecorationPane(Node wrappedNode) {
            setWrappedNode(wrappedNode);
        }

        public Node getWrappedNode() {
            return wrappedNode;
        }

        public void setWrappedNode(Node wrappedNode) {
            this.wrappedNode = wrappedNode;
            if (wrappedNode != null) {
                getChildren().add(wrappedNode);
            }

            requestLayout();
        }

        @Override
        protected double computeMinWidth(double v) {
            double width;
            if (wrappedNode == null) {
                width = super.computeMinWidth(v);
            } else {
                width = wrappedNode.minWidth(v);
            }
            return width;
        }

        @Override
        protected double computeMinHeight(double v) {
            double height;
            if (wrappedNode == null) {
                height = super.computeMinHeight(v);
            } else {
                height = wrappedNode.minHeight(v);
            }
            return height;
        }

        @Override
        protected double computePrefWidth(double v) {
            double width;
            if (wrappedNode == null) {
                width = super.computePrefWidth(v);
            } else {
                width = wrappedNode.prefWidth(v);
            }
            return width;
        }

        @Override
        protected double computePrefHeight(double v) {
            double height;
            if (wrappedNode == null) {
                height = super.computePrefHeight(v);
            } else {
                height = wrappedNode.prefHeight(v);
            }
            return height;
        }

        @Override
        protected double computeMaxWidth(double v) {
            double width;
            if (wrappedNode == null) {
                width = super.computeMaxWidth(v);
            } else {
                width = wrappedNode.maxWidth(v);
            }
            return width;
        }

        @Override
        protected double computeMaxHeight(double v) {
            double height;
            if (wrappedNode == null) {
                height = super.computeMaxHeight(v);
            } else {
                height = wrappedNode.maxHeight(v);
            }
            return height;
        }

        @Override
        protected void layoutChildren() {
            super.layoutChildren();

            System.out.println("IconResultHandler$DecorationPane.layoutChildren");
            if (wrappedNode != null) {
                Bounds localBounds = getBoundsInLocal();
                System.out.println(" |_ " + localBounds.getWidth());
                System.out.println(" |_ " + localBounds.getHeight());
                wrappedNode.resizeRelocate(0, 0, localBounds.getWidth(), localBounds.getHeight());
            }

            for (Node decorationNode : getChildren()) {
                if (decorationNode instanceof Decoration) {
                    Bounds bounds = ((Decoration) decorationNode).getDecorated().getBoundsInLocal();
                    bounds = ((Decoration) decorationNode).getDecorated().localToScene(bounds);
                    bounds = decorationNode.getParent().sceneToLocal(bounds);

                    decorationNode.relocate(bounds.getMinX() - decorationNode.getBoundsInLocal().getWidth() / 2 + 2,
                            bounds.getMaxY() - decorationNode.getBoundsInLocal().getWidth() / 2 - 2);
                }
            }
        }
    }

    private static class DecorationPaneInstaller implements ChangeListener<Scene>, Runnable {

        private final Decoration decoration;

        private final DecorationPane decorationPane = new DecorationPane();

        public DecorationPaneInstaller(Decoration decoration) {
            this.decoration = decoration;
        }

        @Override
        public void changed(ObservableValue<? extends Scene> observableValue, Scene scene, Scene scene2) {
            Platform.runLater(this);
        }

        @Override
        public void run() {
            // Uninstall decoration pane if needed
            Scene oldScene = decorationPane.getScene();
            if (oldScene != null) {
//                oldScene.setRoot(decorationPane.getWrappedNode());
                // TODO
            }

            if (decoration.getDecorated() != null) {
                Scene scene = decoration.getDecorated().getScene();
                if (scene != null) {
                    Parent root = scene.getRoot();
                    if (!(root instanceof DecorationPane)) {
                        DecorationPane decorationPane = new DecorationPane(root);
                        scene.setRoot(decorationPane);
                        decorationPane.getChildren().add(decoration);
                    }
                }
            }
        }
    }

    private static class Decoration extends ImageView {

        private final Node decorated;

        public Decoration(Node decorated) {
            super();
            this.decorated = decorated;
        }

        public Node getDecorated() {
            return decorated;
        }
    }

    private final Image validImage = new Image("/images/defaults/valid.png");
    private final Image invalidImage = new Image("/images/defaults/invalid.png");

    private final Decoration decoration;

    private final Node decoratedNode;

    public IconResultHandler(Node decoratedNode) {
        this.decoratedNode = decoratedNode;

        decoration = new Decoration(decoratedNode);
        decoration.setManaged(false);

        decoratedNode.sceneProperty().addListener(new DecorationPaneInstaller(decoration));
    }

    public Node getDecoratedNode() {
        return decoration;
    }

    @Override
    public void handleResult(Boolean result) {
        if ((result == null) || !result) {
            decoration.setImage(invalidImage);
        } else {
            decoration.setImage(validImage);
        }
    }
}
