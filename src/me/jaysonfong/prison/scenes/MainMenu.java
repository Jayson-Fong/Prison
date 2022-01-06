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
package me.jaysonfong.prison.scenes;

import java.io.File;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.jaysonfong.prison.core.utils.SystemScene;
import me.jaysonfong.prison.scenes.templates.BaseTemplate;
import me.jaysonfong.prison.scenes.templates.Template;
import me.jaysonfong.prison.scenes.templates.entity.IContainer;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class MainMenu extends IManager {
    
    // Scenes
    private final ManagerScene scene;
    
    // Primary Nodes
    private final VBox outerBox;
    
    private boolean built = false;
    
    private final String dynamicTitle = "Main Menu";
        
    public MainMenu() {
        this.outerBox = new VBox();
        Rectangle2D visualBounds = this.getVisualBounds();
        this.scene = new ManagerScene(this.outerBox, visualBounds.getMaxX(), visualBounds.getMaxY(), this);
    }
    
    public MainMenu(int length, int width) {
        this.outerBox = new VBox();
        this.scene = new ManagerScene(this.outerBox, length, width, this);
    }
    
    @Override
    public String getDynamicTitle() {
        return this.dynamicTitle;
    }
    
    @Override
    public ManagerScene getScene() {
        if (!this.built)
            this.buildScene();
        return this.scene;
    }
    
    private void buildScene() {
        // Build Template
        Template baseTemplate = new BaseTemplate();
        IContainer baseContainer = baseTemplate.build();
        
        // Create Body Content
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);
        
        String path;
        
        SimpleStringProperty pathProperty = new SimpleStringProperty(new File("prison.jpg").toURI().toString());
        grid.add(this.refactorButton(this.getSceneButton("Booking", SystemScene.BOOKING), pathProperty), 0, 0);
        pathProperty = new SimpleStringProperty(new File("book.png").toURI().toString());
        grid.add(this.refactorButton(this.getSceneButton("Lookup", SystemScene.LOOKUP), pathProperty), 1, 0);
                
        // Register Content
        baseContainer.addBody(grid);
        this.outerBox.getChildren().add(baseContainer.getPane());
        this.built = true;
    }
    
    private Button refactorButton(Button button, SimpleStringProperty imagePath) {
        button.prefHeightProperty().bind(this.scene.heightProperty().divide(3));
        button.prefWidthProperty().bind(this.scene.widthProperty().divide(8).multiply(3));
        SimpleDoubleProperty fontSize = new SimpleDoubleProperty(25);
        fontSize.bind(button.prefHeightProperty().divide(5));
        button.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;" + 
                "-fx-background-image: url('%s');"
                + "-fx-color: #bababa; -fx-text-fill: white;"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-background-size: cover, auto;"
                + "-fx-background-radius: 30px;", fontSize, imagePath));
        return button;
    }
        
}
