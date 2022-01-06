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

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.jaysonfong.prison.core.utils.BlockManager;
import me.jaysonfong.prison.core.utils.PrisonerManager;
import me.jaysonfong.prison.core.utils.StageManager;
import me.jaysonfong.prison.entities.CellBlock;
import me.jaysonfong.prison.entities.Inmate;
import me.jaysonfong.prison.entities.Status;
import me.jaysonfong.prison.scenes.templates.BaseTemplate;
import me.jaysonfong.prison.scenes.templates.Template;
import me.jaysonfong.prison.scenes.templates.entity.IContainer;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class PrisonerView extends IManager {
    
    // Scenes
    private final ManagerScene scene;
    
    // Primary Nodes
    private final VBox outerBox;
    
    private boolean built = false;
    
    private String dynamicTitle = "View Prisoner";
    
    private Inmate prisoner;
    
    private final ObservableMap<String, Node> gridContent;
    
    private GridPane gridSet;
    
    private final ImageView imageView;
    
    private Text releaseText;
    
    private boolean isReleased;
        
    public PrisonerView() {
        this.outerBox = new VBox();
        Rectangle2D visualBounds = this.getVisualBounds();
        this.scene = new ManagerScene(this.outerBox, visualBounds.getMaxX(), visualBounds.getMaxY(), this);
        this.gridContent = FXCollections.observableHashMap();
        this.imageView = new ImageView();
        this.imageView.fitHeightProperty().set(250);
        this.imageView.fitWidthProperty().set(250);
        this.initReleaseText();
    }
    
    public PrisonerView(int length, int width) {
        this.outerBox = new VBox();
        this.scene = new ManagerScene(this.outerBox, length, width, this);
        this.gridContent = FXCollections.observableHashMap();
        this.imageView = new ImageView();
        this.imageView.fitHeightProperty().set(250);
        this.imageView.fitWidthProperty().set(250);
        this.initReleaseText();
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
    
    public void showPrisoner(int id) {
        this.prisoner = PrisonerManager.getInstance().getPrisonerById(id);
        if (this.prisoner.getId() == 0)
            throw new RuntimeException("Unknown Prisoner Lookup");
        this.update();
    }
    
    public void update() {
        if (this.gridSet == null)
            this.buildScene();
        
        this.gridContent("id", "ID", this.prisoner.getId(), 0);
        this.gridContent("firstname", "First Name", this.prisoner.getFirstName(), 1);
        this.gridContent("lastname", "Last Name", this.prisoner.getLastName(), 2);
        this.gridContent("age", "Age", this.prisoner.getAge(), 3);
        this.gridContent("offense", "Offense", this.prisoner.getOffense(), 4);
        this.gridContent("number", "Phone Number", this.prisoner.getPhone(), 5);
        this.gridContent("status", "Status", this.prisoner.getStatus(), 6);
        this.gridContent("block", "Cell Block", this.prisoner.getCellBlock(), 7);
        this.imageView.setImage(this.prisoner.getImage());
        
        this.isReleased = !this.prisoner.getStatus().equals(Status.ACTIVE);
        
        this.releaseText.setVisible(this.isReleased);
        if (this.isReleased) {
            this.lockComboBox("status", "block");
        }
        
        this.dynamicTitle = "Viewing " + this.prisoner;
        StageManager.getInstance().getTitleProperty().set(this.dynamicTitle);
    }
    
    private void buildScene() {
        // Build Template
        Template baseTemplate = new BaseTemplate();
        IContainer baseContainer = baseTemplate.build();
        
        // Create Body Content
        HBox box = new HBox(20);
        this.gridSet = new GridPane();
        box.getChildren().add(this.gridSet);
        this.gridSet.setHgap(25);
        this.gridSet.setVgap(25);
        this.gridSet.setAlignment(Pos.CENTER_LEFT);
        box.setAlignment(Pos.CENTER);
        VBox innerProfileBox = new VBox();
        innerProfileBox.getChildren().addAll(this.imageView, this.releaseText);
        box.getChildren().add(innerProfileBox);
        
        // Grid Content
        this.gridContent("id", "ID", 0, 0);
        this.lockTextField("id");
        this.gridContent("firstname", "First Name", "", 1);
        this.gridContent("lastname", "Last Name", "", 2);
        this.gridContent("age", "Age", 0, 3);
        this.gridContent("offense", "Offense", "", 4);
        this.gridContent("number", "Phone Number", "", 5);
        this.gridContent("status", "Status", Status.ACTIVE, 6);
        this.gridContent("block", "Cell Block", CellBlock.BLUE, 7);
        this.gridContent("submit", "Save Changes", 8);
        this.lockTextField("id");
        this.lockDigit("age");
                
        // Register Content
        baseContainer.addBody(box);
        this.outerBox.getChildren().add(baseContainer.getPane());
        this.built = true;
    }
    
    private void gridContent(String label, String viewLabel, Status content, int level) {
        this.gridContent(label, viewLabel, level, content.getTitle(), 
                Status.ACTIVE.getTitle(), Status.RELEASED.getTitle()); 
    }
    
    private void gridContent(String label, String viewLabel, CellBlock content, int level) {
        this.gridContent(label, viewLabel, level, content.toString(), 
                CellBlock.BLUE.toString(), CellBlock.MAX.toString(), CellBlock.RED.toString(), CellBlock.SOLITARY.toString());
    }
    
    private void gridContent(String label, String viewLabel, int level, String value, String... set) {
        if (this.gridContent.containsKey(label + "_field")) {
            ComboBox node = (ComboBox) this.gridContent.get(label + "_field");
            node.setValue(value);
        } else {
            Label mapLabel = new Label(viewLabel);
            ComboBox mapNode = new ComboBox();
            mapNode.getItems().setAll((Object[]) set);
            this.gridSet.add(mapLabel, 0, level);
            this.gridSet.add(mapNode, 1, level);
            this.gridContent.put(label + "_label", mapLabel);
            this.gridContent.put(label + "_field", mapNode);
        }
    }
    
    // Assumes Field is a TextField
    private void gridContent(String label, String viewLabel, int content, int level) {
        this.gridContent(label, viewLabel, String.valueOf(content), level);
    }
    
    // Assumes Field is a TextField
    private void gridContent(String label, String viewLabel, String content, int level) {
        if (this.gridContent.containsKey(label + "_field")) {
            TextField node = (TextField) this.gridContent.get(label + "_field");
            node.setText(content);
        } else {
            Label mapLabel = new Label(viewLabel);
            TextField mapNode = new TextField();
            this.gridSet.add(mapLabel, 0, level);
            this.gridSet.add(mapNode, 1, level);
            this.gridContent.put(label + "_label", mapLabel);
            this.gridContent.put(label + "_field", mapNode);
        }
    }
    
    // Assumes Field is Submit
    private void gridContent(String label, String viewLabel, int level) {
        if (!this.gridContent.containsKey(label + "_field")) {
            Label mapLabel = new Label(viewLabel);
            Button mapNode = new Button(viewLabel);
            mapNode.setOnMouseClicked(e -> {
            
                this.processSave();
            
            });
            this.gridSet.add(mapLabel, 0, level);
            this.gridSet.add(mapNode, 1, level);
            this.gridContent.put(label + "_label", mapLabel);
            this.gridContent.put(label + "_field", mapNode);
        }
    }
    
    private void processSave() {
        int id;
        try {
            id = Integer.valueOf(((TextField) this.gridContent.get(("id_field"))).getText());
        } catch (NumberFormatException | NullPointerException nfe) {
            return;
        }
        if (id == 0 || this.prisoner == null || id != this.prisoner.getId()) return;
        // Process Age Change
        try {
            this.prisoner.setAge(
                    Integer.valueOf(((TextField) this.gridContent.get("age_field")).getText())
            );
        } catch (NumberFormatException e) {}
        // Process Name Changes
        this.prisoner.setFirstName(
            this.getTextFieldValue("firstname")
        );
        this.prisoner.setLastName(
            this.getTextFieldValue("lastname")
        );
        this.prisoner.setOffense(
            this.getTextFieldValue("offense")
        );
        this.prisoner.getPhoneProperty().set(
            this.getTextFieldValue("number")
        );
        // Process Block Change
        this.prisoner.setBlock(BlockManager.getBlockByText(
                ((ComboBox) this.gridContent.get("block_field")).getValue().toString()
        ));
        // Process Status Change
        this.prisoner.setStatus(BlockManager.getStatusByText(
                ((ComboBox) this.gridContent.get("status_field")).getValue().toString()
        ));
        this.update();
    }
    
    private String getTextFieldValue(String label) {
        return ((TextField) this.gridContent.get(label + "_field")).getText();
    }
    
    private void lockDigit(String label) {
            this.gridContent.get(label + "_field").setOnKeyTyped(e -> {
        
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
    
    private void lockTextField(String label) {
        if (this.gridContent.containsKey(label + "_field"))
            ((TextField) this.gridContent.get(label + "_field")).setEditable(false);
    }
    
    private void lockTextField(String... labels) {
        for (String label : labels)
            this.lockTextField(label);
    }
    
    private void lockComboBox(String label) {
        if (this.gridContent.containsKey(label + "_field")) {
            ComboBox comboBox = ((ComboBox) this.gridContent.get(label + "_field"));
            comboBox.getEditor().setEditable(false);
            comboBox.setOnShown(e -> comboBox.hide());
        }
    }
    
    private void lockComboBox(String... labels) {
        for (String label : labels)
            this.lockComboBox(label);
    }
    
    private void initReleaseText() {
        this.releaseText = new Text("Released");
        this.releaseText.setFill(Color.RED);
        this.releaseText.setFont(Font.font("Arial", FontWeight.BOLD, 25));
    }

}
