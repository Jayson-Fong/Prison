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
package me.jaysonfong.prison.core.utils;

import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.jaysonfong.prison.core.utils.entities.TitleProperty;
import me.jaysonfong.prison.core.utils.events.ChangeEvent;
import me.jaysonfong.prison.core.utils.events.EventHandler;
import me.jaysonfong.prison.scenes.IManager;
import me.jaysonfong.prison.scenes.ManagerScene;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class StageManager {
    
    private static StageManager manager;
    
    private final Stage stage;
    private final TitleProperty titleProperty;
    
    private EventHandler<? super ChangeEvent> changeSceneEventHandler;
    
    private StageManager(Stage iStage) {
        this.stage = iStage;
        this.titleProperty = new TitleProperty(this.stage.titleProperty());
    }
    
    public Stage getStage() {
        return this.stage;
    }
    
    public void setTitle(String title) {
        if (this.stage == null)
            throw new RuntimeException("Uninitialized Stage Manager");
        if (this.titleProperty == null)
            throw new RuntimeException("Uninitialized Title Property");
        this.titleProperty.set(title);
    }
    
    public void setScene(Scene scene) {
        if (this.stage == null)
            throw new RuntimeException("Uninitialized Stage Manager");
        if (this.stage.getScene() == scene) return;
        ChangeEvent eventInfo = new ChangeEvent(
            this.stage.getScene(),
            scene,
            this
        );
        this.stage.setScene(scene);
        if (this.changeSceneEventHandler != null)
            this.changeSceneEventHandler.handle(eventInfo);
    }
    
    public void showScene() {
        if (this.stage == null)
            throw new RuntimeException("Uninitialized Stage Manager");
        if (this.stage.getScene() == null)
            throw new RuntimeException("Invalid Scene");
        Scene scene = this.stage.getScene();
        if (scene instanceof ManagerScene) {
            IManager viewManager = ((ManagerScene) scene).getManager();
            this.setTitle(viewManager.getDynamicTitle());
            viewManager.initView();
        }
        this.stage.show();
    }
    
    public String getTitle() {
        return this.titleProperty.get();
    }
    
    public TitleProperty getTitleProperty() {
        return this.titleProperty;
    }
    
    public StringProperty getStringTitleProperty() {
        return this.stage.titleProperty();
    }
    
    public boolean isActiveScene(Scene scene) {
        return this.stage.getScene() == scene;
    }
    
    public static StageManager getInstance() {
        if (manager == null)
            throw new RuntimeException("Uninitialized Stage Manager");
        return manager;
    }
    
    public static StageManager getInstance(Stage newStage) {
        if (manager == null)
            manager = new StageManager(newStage);
        return manager;
    }

    public final void setOnSceneChange(EventHandler<? super ChangeEvent> value) {
        this.changeSceneEventHandler = value;
    }
    
}
