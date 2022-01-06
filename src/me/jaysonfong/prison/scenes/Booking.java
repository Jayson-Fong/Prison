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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import me.jaysonfong.prison.core.utils.BlockManager;
import me.jaysonfong.prison.core.utils.NodeSet;
import me.jaysonfong.prison.core.utils.PrisonerManager;
import me.jaysonfong.prison.core.utils.StageManager;
import me.jaysonfong.prison.core.utils.SystemScene;
import me.jaysonfong.prison.entities.CellBlock;
import me.jaysonfong.prison.entities.Inmate;
import me.jaysonfong.prison.scenes.templates.BaseTemplate;
import me.jaysonfong.prison.scenes.templates.Template;
import me.jaysonfong.prison.scenes.templates.entity.IContainer;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class Booking extends IManager {
    
    // Scenes
    private final ManagerScene scene;
    
    // Primary Nodes
    private final VBox outerBox;
    
    private boolean built = false;
    
    private final String dynamicTitle = "Booking";
    
    private final String[] fieldLabelList = {
        "first_name", "last_name", "number", "age", "offense", "image_path", "block", "submit"
    };
    
    private NodeSet nodeSet;
        
    public Booking() {
        this.outerBox = new VBox();
        Rectangle2D visualBounds = this.getVisualBounds();
        this.scene = new ManagerScene(this.outerBox, visualBounds.getMaxX(), visualBounds.getMaxY(), this);
    }
    
    public Booking(int length, int width) {
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
        
        this.nodeSet = new NodeSet();
        this.generateNodes();
        
        int position = 0b0;
        for (String label : this.fieldLabelList)
            this.addNodesToGrid(grid, label, position++);
                
        // Register Content
        baseContainer.addBody(grid);
        this.outerBox.getChildren().add(baseContainer.getPane());
        this.built = true;
    }
    
    private void addNodesToGrid(GridPane grid, String label, int y) {
        grid.add(this.nodeSet.get(label + "_label"), 0, y);
        grid.add(this.nodeSet.get(label + "_input"), 1, y);
    }
    
    private void generateNodes() {
        this.setDuo("first_name", "First Name");
        this.setDuo("last_name", "Last Name");
        this.setDuo("number", "Phone Number");
        this.setDuoInt("age", "Age");
        this.setDuo("offense", "Offense");
        this.setDuo("image_path", "Image Path");
        // Possible improvement: Using CellBlock.values() instead...but that's a bit of midnight recoding.
        this.setDuoComboBlockList("block", "Block", 
                CellBlock.BLUE.toString(), 
                CellBlock.RED.toString(), 
                CellBlock.SOLITARY.toString(), 
                CellBlock.MAX.toString()
        );
        this.setDuoSubmit("submit", "Submit");
    }
    
    private void setDuo(String label, String name) {
        TextField field = new TextField();
        field.promptTextProperty().setValue(name);
        this.nodeSet.set(label + "_label", (Node) new Label(name));
        this.nodeSet.set(label + "_input", (Node) field);
    }
    
    private void setDuoSubmit(String label, String name) {
        Button submit = new Button(name);
        this.nodeSet.set(label + "_label", (Node) new Label(name));
        this.nodeSet.set(label + "_input", (Node) submit);
        
        submit.setOnMouseClicked(e -> {
        
            if (this.isValidInput()) {
                Inmate prisoner;
                
                prisoner = new Inmate(
                        this.getTextFromNode("first_name"),
                        this.getTextFromNode("last_name"),
                        this.getTextFromNode("number"),
                        this.getIntFromNode("age"),
                        this.getTextFromNode("offense"),
                        this.getTextFromNode("image_path"),
                        BlockManager.getBlockByText(this.getTextFromBox("block"))
                );
                
                PrisonerManager pManager;
                pManager = PrisonerManager.getInstance();
                Integer id = pManager.registerPrisoner(prisoner);
                
                StageManager sManager = StageManager.getInstance();
                PrisonerView pViewManager = (PrisonerView) SystemScene.PRISONERVIEW.getManager();
                pViewManager.showPrisoner(id);
                Scene pScene = pViewManager.getScene();
                sManager.setScene(pScene);
                sManager.showScene();
                SystemScene.BOOKING.reset();
            }
        
        });
    }
    
    private String getTextFromNode(String label) {
        return ((TextField) this.nodeSet.get(label + "_input")).getText();
    }
    
    private String getTextFromBox(String label) {
        return (String) ((ComboBox) this.nodeSet.get(label + "_input")).getValue();
    }
    
    private int getIntFromNode(String label) throws NumberFormatException {
        try {
            return Integer.valueOf(((TextField) this.nodeSet.get(label + "_input")).getText());
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
    
    private void setDuoInt(String label, String name) {
        TextField field = new TextField();
        field.promptTextProperty().setValue(name);
        this.nodeSet.set(label + "_label", (Node) new Label(name));
        this.nodeSet.set(label + "_input", (Node) field);
        
        field.setOnKeyTyped(e -> {
        
            TextField inputField = (TextField) e.getSource();
            String text = inputField.getText();
            if (text.isEmpty())
                return;
            
            String newText = "";
            char charCheck;
            for (int charNum = 0b0; charNum < text.length(); charNum++) {
                charCheck = text.charAt(charNum);
                if (Character.isDigit(charCheck)) {
                    newText += charCheck;
                }
            }
            inputField.setText(newText);
            inputField.positionCaret(newText.length());
            
        });
    }
    
    private void setDuoComboBlockList(String label, String name, String... set) {
        ComboBox box = new ComboBox();
        this.nodeSet.set(label + "_label", (Node) new Label(name));
        this.nodeSet.set(label + "_input", (Node) box);
        box.getItems().setAll((Object[]) set);
    }
    
    private boolean isValidInput() {
        for (String label : this.fieldLabelList) {
            if (!this.nodeSet.contains(label + "_input"))
                return false;
            Node node = this.nodeSet.get(label + "_input");
            if (node instanceof Button)
                continue;
            if (node instanceof TextField && ((TextField) node).getText().isEmpty())
                return false;
            try {
                if (node instanceof ComboBox && ((String) ((ComboBox) node).getValue()).isEmpty())
                    return false;
            } catch (NullPointerException e) {
                return false;
            }
        }
        return true;
    }
    
}
