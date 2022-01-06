/*
 * The MIT License
 *
 * Copyright 2020 Jayson Fong <contact@jaysonfong.me>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.jaysonfong.prison.scenes.templates.entity;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class BaseContainer implements IContainer {
 
    private final BorderPane container;
    
    private final Pane bodyPane;
    
    public BaseContainer(Node header, Node footer) {        
        this.container = new BorderPane();
        this.bodyPane = new VBox();
        
        this.bodyPane.setPadding(new Insets(10, 20, 20, 20));
        this.container.setPadding(new Insets(0, 0, 50, 0));
        
        this.container.setTop(header);
        this.container.setCenter(this.bodyPane);
        this.container.setBottom(footer);
    }
    
    @Override
    public Pane getPane() {
        return this.container;
    }
    
    @Override
    public void addBody(Pane pane) {
        this.bodyPane.getChildren().add(pane);
    }
    
    @Override
    public void setBody(Pane pane) {
        this.bodyPane.getChildren().clear();
        this.bodyPane.getChildren().add(pane);
    }
    
}
