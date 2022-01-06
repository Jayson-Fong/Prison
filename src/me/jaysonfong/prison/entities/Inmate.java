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
package me.jaysonfong.prison.entities;

import java.io.File;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.util.Callback;
import me.jaysonfong.prison.core.utils.BlockManager;
import me.jaysonfong.prison.core.utils.PrisonerManager;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class Inmate {
   
    public static final String DEFAULT_AVATAR = "avatar_v2.png";
    
    private Integer id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private Status status;
    private Image image;
    private CellBlock block;
    private SimpleIntegerProperty age;
    private final SimpleStringProperty offense;
    private final SimpleStringProperty number;
    private final SimpleStringProperty blockText;
    
    public Inmate() {
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.blockText = new SimpleStringProperty();
        this.offense = new SimpleStringProperty();
        this.number = new SimpleStringProperty();
    }
    
    public Inmate(String iFirstName, String iLastName) {
        this(iFirstName, iLastName, Status.ACTIVE);
    }
    
    public Inmate(String iFirstName, String iLastName, Status iStatus) {
        this.firstName = new SimpleStringProperty(PrisonerManager.formatName(iFirstName));
        this.lastName = new SimpleStringProperty(PrisonerManager.formatName(iLastName));
        this.status = iStatus;
        this.blockText = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.offense = new SimpleStringProperty();
        this.number = new SimpleStringProperty();
    }
    
    public Inmate(String iFirstName, String iLastName, String iNumber, int iAge, String iOffense, String iPath) {
        this.firstName = new SimpleStringProperty(PrisonerManager.formatName(iFirstName));
        this.lastName = new SimpleStringProperty(PrisonerManager.formatName(iLastName));
        this.number = new SimpleStringProperty(iNumber);
        this.age = new SimpleIntegerProperty(iAge);
        this.offense = new SimpleStringProperty(iOffense);
        this.setImage(new File(iPath));
        this.status = Status.ACTIVE;
        this.blockText = new SimpleStringProperty();
    }
    
    public Inmate(String iFirstName, String iLastName, String iNumber, int iAge, String iOffense, String iPath, CellBlock iBlock) {
        this.firstName = new SimpleStringProperty(PrisonerManager.formatName(iFirstName));
        this.lastName = new SimpleStringProperty(PrisonerManager.formatName(iLastName));
        this.number = new SimpleStringProperty(iNumber);
        this.age = new SimpleIntegerProperty(iAge);
        this.offense = new SimpleStringProperty(iOffense);
        this.setImage(new File(iPath));
        this.status = Status.ACTIVE;
        this.blockText = new SimpleStringProperty();
        this.setBlock(iBlock);
    }
    
    public int getId() {
        if (this.id == null) return 0b0;
        return this.id;
    }
    
    public Integer getIdRaw() {
        return this.id;
    }
    
    public String getFirstName() {
        return this.firstName.get();
    }
    
    public SimpleStringProperty getFirstNameProperty() {
        return this.firstName;
    }
    
    public String getLastName() {
        return this.lastName.get();
    }
    
    public SimpleStringProperty getLastNameProperty() {
        return this.lastName;
    }
    
    public Image getImage() {
        return this.image;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public CellBlock getCellBlock() {
        return this.block;
    }
    
    public int getAge() {
        return this.age.get();
    }
    
    public SimpleIntegerProperty getAgeProperty() {
        return this.age;
    }
    
    public String getPhone() {
        return this.number.get();
    }
    
    public SimpleStringProperty getPhoneProperty() {
        return this.number;
    }
    
    public String getOffense() {
        return this.offense.get();
    }
    
    public SimpleStringProperty getOffenseProperty() {
        return this.offense;
    }
    
    public void setId(Integer newId) {
        if (this.id != null)
            throw new RuntimeException("Prisoner ID Already Set");
        this.id = newId;
        if (this.block != null) this.setBlock(this.block); // Rerun block set
    }
    
    public void setFirstName(String newFirstName) {
        this.firstName.set(PrisonerManager.formatName(newFirstName));
    }
    
    public void setLastName(String newLastName) {
        this.lastName.set(PrisonerManager.formatName(newLastName));
    }
    
    public final void setImage(File imageFile) {
        if (imageFile.exists())
            this.image = new Image(imageFile.toURI().toString());
        else
            this.setDefaultImage();
    }
    
    public final void setDefaultImage() {
        this.image = new Image((new File(DEFAULT_AVATAR)).toURI().toString());
    }
    
    public void setAge(int newAge) {
        this.age.set(newAge);
    }
    
    public void setOffense(String newOffense) {
        this.offense.set(newOffense);
    }
    
    public void setStatus(Status newStatus) {
        this.status = newStatus;
        this.statusUpdate();
    }
    
    public final void setBlock(CellBlock newBlock) {
        BlockManager bm = BlockManager.getInstance();
        if (this.id != null) {
            if (this.block != null) bm.removeInmate(this.block, this.id);
            bm.putInmate(newBlock, this.id);
        }
        this.block = newBlock;
        this.blockText.set(newBlock.getBlockName());
    }
    
    // Forces lookup to update on property change
    public static Callback<Inmate, Observable[]> extractor() {
        return (Inmate inmate) -> new Observable[]{
            inmate.firstName, inmate.lastName, inmate.blockText, inmate.age, inmate.number, inmate.offense
        };
    }
    
    @Override
    public String toString() {
        return String.format(
                "%d [%s]: %s, %s",
                this.getId(),
                this.blockText.get(),
                this.getLastName(),
                this.getFirstName()
        );
    }
        
    protected void statusUpdate() {
        // Remove from Cell Block
        if (this.status.equals(Status.RELEASED)) {
            this.setBlock(CellBlock.NONE);
        }
    }
    
}
