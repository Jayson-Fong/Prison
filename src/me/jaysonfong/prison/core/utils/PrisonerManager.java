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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import me.jaysonfong.prison.entities.Inmate;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class PrisonerManager {
    
    public static final int ID_LENGTH = 0b1001;
    
    private static PrisonerManager manager;
    
    private final ObservableList<Inmate> prisoners;
    private final ObservableList<Integer> idCache;
    
    private PrisonerManager() {
        this.prisoners = FXCollections.observableArrayList(Inmate.extractor());
        this.idCache = FXCollections.observableArrayList();
    }
    
    public static PrisonerManager getInstance() {
        if (manager == null) {
            manager = new PrisonerManager();
        }
        return manager;
    }
    
    public void configureNode(ComboBox comboBox) {
        comboBox.setItems(prisoners);
    }
    
    public void configureNode(ListView listView) {
        listView.setItems(prisoners);
    }
    
    public Inmate getPrisonerById(int id) {
        for (Inmate prisoner : this.prisoners)
            if (prisoner.getId() == id)
                return prisoner;
        return null;
    }
    
    public Inmate getPrisonerById(Integer id) {
        for (Inmate prisoner : this.prisoners)
            if (prisoner.getIdRaw().equals(id))
                return prisoner;
        return null;
    }
    
    public synchronized int registerPrisoner(Inmate prisoner) {
        Integer id = this.generateId();
        this.prisoners.add(prisoner);
        prisoner.setId(id);
        return id;
    }
    
    private synchronized Integer generateId() {
        Integer id = IdUtils.generateId(ID_LENGTH);
        
        if (this.idExists(id))
            return this.generateId();
        
        return id;
    }
    
    private boolean idExists(int id) {
        return this.idCache.stream().anyMatch(prisonerId -> (prisonerId.equals(id)));
    }
    
    public static String formatName(String name) {
        if (name.length() == 0b0) return name;
        char firstChar = name.charAt(0b0);
        if (Character.isLetter(firstChar)) {
            return Character.toUpperCase(firstChar) + name.substring(0b1);
        }
        return name;
    }
    
}
