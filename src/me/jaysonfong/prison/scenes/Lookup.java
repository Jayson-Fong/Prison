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
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.jaysonfong.prison.core.utils.BlockManager;
import me.jaysonfong.prison.core.utils.PrisonerManager;
import me.jaysonfong.prison.core.utils.StageManager;
import me.jaysonfong.prison.core.utils.SystemScene;
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
public class Lookup extends IManager {
    
    // Scenes
    private final ManagerScene scene;
    
    // Primary Nodes
    private final VBox outerBox;
    
    private boolean built = false;
    
    private final String dynamicTitle = "Lookup";
    
    private ListView<Inmate> listView;
    
    private Button viewButton;
    
    private final ImageView imageView;
    
    private ComboBox options;
    
    private Text prisonerName;
        
    public Lookup() {
        this.outerBox = new VBox();
        Rectangle2D visualBounds = this.getVisualBounds();
        this.scene = new ManagerScene(this.outerBox, visualBounds.getMaxX(), visualBounds.getMaxY(), this);
        this.imageView = new ImageView();
        this.imageView.fitHeightProperty().set(250);
        this.imageView.fitWidthProperty().set(250);
    }
    
    public Lookup(int length, int width) {
        this.outerBox = new VBox();
        this.scene = new ManagerScene(this.outerBox, length, width, this);
        this.imageView = new ImageView();
        this.imageView.maxHeight(250);
        this.imageView.maxWidth(250);
        this.imageView.minHeight(250);
        this.imageView.minWidth(250);
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
        
        Label headerLabel = new Label("Lookup Prisoner");
        headerLabel.setFont(new Font(20));
        vBox.getChildren().add(headerLabel);
        
        HBox filterSettings = new HBox(15);
        this.fillFilterSettings(filterSettings);
        vBox.getChildren().add(filterSettings);
        filterSettings.setAlignment(Pos.CENTER);
        
        HBox hBox = new HBox(15);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);
        this.listView = new ListView();
        this.configureDefaultFiltered();
        this.configureListView();
        hBox.getChildren().add(this.listView);
        
        VBox displayBox = new VBox(15);
        hBox.getChildren().add(displayBox);
        this.imageView.setImage(new Image((new File(Inmate.DEFAULT_AVATAR)).toURI().toString()));
        this.configureViewButton();
        this.configurePrisonerName();
        displayBox.getChildren().addAll(this.imageView, this.prisonerName, this.viewButton);
                
        // Register Content
        baseContainer.addBody(vBox);
        this.outerBox.getChildren().add(baseContainer.getPane());
        this.built = true;
    }
    
    @Override
    public void initView() {
        // Update ListView
        this.updateList();
    }
    
    private void updateList() {
        // Verify Selected
        if (this.options.getValue() == null) {
            // If nothing selected, show all active inmates
            this.configureDefaultFiltered();
            return;
        }
        // Update
        String value = this.options.getValue().toString();
        switch (value) {
            case "All Booked Inmates":
                // Show all inmates, including released
                this.configureDefault();
                break;
            case "All Active Inmates":
                this.configureDefaultFiltered();
                break;
            default:
                CellBlock block = BlockManager.getBlockByText(value);
                ObservableList<Inmate> inmates = BlockManager.getInstance().getInmates(block);
                //if (!inmates.isEmpty())
                this.listView.setItems(inmates);
                //else
                // Show all active inmates only
                //this.configureDefaultFiltered();
                break;
        }
    }
    
    private void configureDefault() {
        PrisonerManager.getInstance().configureNode(this.listView);
    }
    
    private void configureDefaultFiltered() {
        PrisonerManager.getInstance().configureNode(this.listView);
        ObservableList<Inmate> filteredList = FXCollections.observableArrayList(
                this.listView.getItems()
        );
        // Remove Non-Active Inmates
        Inmate cInmate;
        for (Iterator<Inmate> inmateIterator = filteredList.iterator(); inmateIterator.hasNext();) {
            cInmate = inmateIterator.next();
            if (cInmate.getStatus().equals(Status.RELEASED))
                inmateIterator.remove();
        }
        this.listView.setItems(filteredList);
    }
    
    private void configureListView() {
        this.listView.tooltipProperty().set(new Tooltip("Select Inmate"));
        this.listView.minWidthProperty().bind(this.scene.widthProperty().divide(3));
        this.listView.minHeightProperty().bind(this.scene.heightProperty().divide(5).multiply(3));
        this.listView.setOnKeyPressed(e -> this.updateListService());
        this.listView.setOnMouseClicked(e -> this.updateListService());
    }
    
    private void updateListService() {
        // Check if a prisoner is selected
        if (this.listView.getSelectionModel().getSelectedItems().isEmpty()) {
            this.viewButton.setVisible(false);
            this.prisonerName.setVisible(false);
            return;
        }
            
        Inmate selected = this.listView.getSelectionModel().getSelectedItem();
        this.imageView.setImage(selected.getImage());
        this.viewButton.setVisible(true);
        this.updatePrisonerName();
        this.prisonerName.setVisible(true);
    }
    
    private void fillFilterSettings(HBox settings) {
        Label label = new Label("Cell Block");
        label.setFont(new Font(25));
        this.options = new ComboBox();
        this.options.getItems().addAll("All Booked Inmates", "All Active Inmates");
        this.options.setValue("All Booked Inmates");
        for (CellBlock block : CellBlock.values()) {
            this.options.getItems().add(block.getBlockName());
        }
        this.options.valueProperty().addListener(e -> {
        
           this.updateList();
            
        });
        
        settings.getChildren().addAll(label, options);
    }
    
    private void configureViewButton() {
        this.viewButton = new Button("View Selected Prisoner");
        this.viewButton.setVisible(false);
        
        this.viewButton.setOnMouseClicked(e -> {
        
            // Check if a prisoner is selected
            if (this.listView.getSelectionModel().getSelectedItems().isEmpty())
                return;
            
            Inmate selected = this.listView.getSelectionModel().getSelectedItem();
            // Load PrisonerView
            StageManager sManager = StageManager.getInstance();
            PrisonerView pViewManager = (PrisonerView) SystemScene.PRISONERVIEW.getManager();
            pViewManager.showPrisoner(selected.getIdRaw());
            Scene pScene = pViewManager.getScene();
            sManager.setScene(pScene);
            sManager.showScene();
            
        });
    }
    
    private void configurePrisonerName() {
        this.prisonerName = new Text("");
        this.prisonerName.setFont(new Font(20));
        this.prisonerName.setVisible(false);
    }
    
    private void updatePrisonerName() {
        // Check if a prisoner is selected
        if (this.listView.getSelectionModel().getSelectedItems().isEmpty())
            return;
        
        Inmate selected = this.listView.getSelectionModel().getSelectedItem();
        this.prisonerName.setText(selected.getLastName() + ", " + selected.getFirstName());
        //this.prisonerName.textProperty().bind(selected.getLastNameProperty().concat(", ").concat(selected.getFirstNameProperty()));
    }
    
}
