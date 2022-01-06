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

import javafx.scene.Node;
import me.jaysonfong.prison.scenes.IManager;
import me.jaysonfong.prison.scenes.ManagerScene;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class SceneManager {
    
    // For backwards compatibility...because this used to be different.
    public static IManager getManager(SystemScene sScene) {
        return sScene.getManager();
    }
    
    public static ManagerScene getScene(SystemScene sScene) {
        return getManager(sScene).getScene();
    }
    
    public static void configureNode(Node node, SystemScene sScene) {
        node.setOnMouseClicked(e -> {
        
            StageManager stageManager = StageManager.getInstance();
            stageManager.setScene(getScene(sScene));
            stageManager.showScene();
            
        });
    }
    
}
