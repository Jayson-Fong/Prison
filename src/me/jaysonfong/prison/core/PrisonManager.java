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
package me.jaysonfong.prison.core;

import javafx.application.Application;
import javafx.stage.Stage;
import me.jaysonfong.prison.core.utils.SceneManager;
import me.jaysonfong.prison.core.utils.StageManager;
import me.jaysonfong.prison.core.utils.SystemScene;
import me.jaysonfong.prison.scenes.IManager;
import me.jaysonfong.prison.scenes.ManagerScene;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class PrisonManager extends Application {
    
    public static final String SERVICE_NAME = "Gwinnett County Sheriff's Department | Fong Prison Manager";
    
    private StageManager stageManager;
    
    @Override
    public void start(Stage primaryStage) { 
        this.stageManager = StageManager.getInstance(primaryStage);
                
        IManager manager = SceneManager.getManager(SystemScene.WELCOME);
        ManagerScene scene = manager.getScene();
                
        this.stageManager.setScene(scene);
        this.stageManager.showScene();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
