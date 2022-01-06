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

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.jaysonfong.prison.core.utils.SystemScene;
import me.jaysonfong.prison.scenes.templates.BaseTemplate;
import me.jaysonfong.prison.scenes.templates.Template;
import me.jaysonfong.prison.scenes.templates.entity.IContainer;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class Welcome extends IManager {
    
    // Scenes
    private final ManagerScene scene;
    
    // Primary Nodes
    private final VBox outerBox;
    
    private boolean built = false;
    
    private final String dynamicTitle = "Welcome";
        
    public Welcome() {
        this.outerBox = new VBox();
        Rectangle2D visualBounds = this.getVisualBounds();
        this.scene = new ManagerScene(this.outerBox, visualBounds.getMaxX(), visualBounds.getMaxY(), this);
    }
    
    public Welcome(int length, int width) {
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
        VBox vBox = new VBox(15);
        Text welcomeText = new Text("Gwinnett County Sheriff's Department");
        welcomeText.setFont(new Font(20));
        vBox.getChildren().add(welcomeText);
        welcomeText = new Text("Authorized users only. Misuse of this system may lead to prosecution.");
        welcomeText.setFont(new Font(10));
        vBox.getChildren().add(welcomeText);
        Button enterButton = this.getSceneButton("Enter Prison Manager", SystemScene.MAINMENU);
        enterButton.setMinWidth(156);
        enterButton.setMinHeight(40);
        vBox.getChildren().add(enterButton);
        vBox.setAlignment(Pos.CENTER);
                
        // Register Content
        baseContainer.addBody(vBox);
        this.outerBox.getChildren().add(baseContainer.getPane());
        this.built = true;
    }
    
}
