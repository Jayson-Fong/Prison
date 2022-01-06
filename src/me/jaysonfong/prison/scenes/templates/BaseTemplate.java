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
package me.jaysonfong.prison.scenes.templates;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.jaysonfong.prison.core.PrisonManager;
import me.jaysonfong.prison.core.utils.StageManager;
import me.jaysonfong.prison.core.utils.SystemScene;
import me.jaysonfong.prison.scenes.templates.entity.BaseContainer;
import me.jaysonfong.prison.scenes.templates.entity.IContainer;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class BaseTemplate extends Template {
    
    //private final ObservableList<Scene> lastScenes;
    private Button backButton;
    
    /**public BaseTemplate() {
        this.lastScenes = FXCollections.observableArrayList();
    }**/
    
    @Override
    public IContainer build() {
        IContainer container = new BaseContainer(
                this.getHeader(), this.getFooter()
        );
        return container;
    }
    
    @Override
    protected Node getHeader() {
        Text headerText = new Text("Gwinnett County Sheriff's Department");
        headerText.setFill(Color.WHITE);
        headerText.setFont(new Font(15));
        
        Text titleText = new Text("Welcome");
        titleText.setFill(Color.WHITE);
        titleText.setFont(new Font(15));
        /**StageManager.getInstance().getTitleProperty().setOnChange(e -> {
        
            titleText.setText(String.format("Current Window: %s", (String) e.getNewValue()));
        
        });**/
        StageManager.getInstance().getStringTitleProperty().addListener((observable, oldValue, newValue) -> {
        
            String displayText = newValue.substring(0b0, newValue.indexOf(" | " + PrisonManager.SERVICE_NAME));
            titleText.setText(String.format("Current Window: %s", displayText));
            
        }); 

        /**
        StageManager.getInstance().setOnSceneChange(e -> {
        
            if (e.getOldValue() == null) return;
            this.lastScenes.set(this.lastScenes.size(), (Scene) e.getOldValue());
            this.backButton.setVisible(true);
         
        });
         
         
        this.backButton = new Button("Back");
        this.backButton.setVisible(false);
        this.backButton.setOnMouseClicked(e -> {
         
            if (!this.lastScenes.isEmpty()) {
                StageManager.getInstance().setScene((Scene) this.lastScenes.get(0b0));
                this.lastScenes.remove(0b0);
            }
         
        });**/
        
        ToolBar headerPane = new ToolBar(
                headerText,
                this.refactorButton(this.getSceneButton("Main Menu", SystemScene.MAINMENU)),
                this.refactorButton(this.getSceneButton("Lookup", SystemScene.LOOKUP)),
                this.refactorButton(this.getSceneButton("Booking", SystemScene.BOOKING)),
                //this.backButton,
                titleText
        );
        headerPane.setMinHeight(20);
        headerPane.setBackground(new Background(new BackgroundFill(
                Color.GREY, CornerRadii.EMPTY, Insets.EMPTY
        )));
        
        return headerPane;
    }
    
    @Override
    protected Node getFooter() {
        HBox footerPane = new HBox();
        
        return footerPane;
    }
    
    private Button refactorButton(Button button) {
        button.prefHeightProperty().set(35);
        button.prefWidthProperty().set(100);
        button.setFont(new Font(15));
        return button;
    }
    
}
