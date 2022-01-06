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
import javafx.collections.ObservableMap;
import me.jaysonfong.prison.entities.CellBlock;
import me.jaysonfong.prison.entities.Inmate;
import me.jaysonfong.prison.entities.Status;

/**
 *
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class BlockManager {
    
    private static BlockManager manager;
    
    private final ObservableMap<CellBlock, ObservableList<Integer>> inmateMap;
    
    private BlockManager() {
        this.inmateMap = FXCollections.observableHashMap();
    }
    
    public boolean blockHasInmates(CellBlock block) {
        return
                this.inmateMap.containsKey(block)
                && this.inmateMap.get(block).size() > 0b0;
    }
    
    public ObservableList<Inmate> getInmates(CellBlock block) {
        ObservableList<Inmate> inmates = FXCollections.observableArrayList(Inmate.extractor());
        if (!this.blockHasInmates(block)) {
            return inmates;
        }
        ObservableList<Integer> inmateIds = this.inmateMap.get(block);
        
        Inmate inmate;
        for (Integer id : inmateIds) {
            inmate = PrisonerManager.getInstance().getPrisonerById(id);
            if (inmate != null) inmates.add(inmate);
        }
                
        return inmates;
    }
    
    public synchronized void putInmate(CellBlock block, Integer inmateId) {
        ObservableList<Integer> inmateList;
        if (!this.inmateMap.containsKey(block)) {
            inmateList = FXCollections.observableArrayList();
            inmateList.add(inmateId);
            this.inmateMap.put(block, inmateList);
        } else {
            inmateList = inmateMap.get(block);
            if (!inmateList.contains(inmateId))
                inmateList.add(inmateId);
        }
    }
    
    public synchronized void removeInmate(CellBlock block, Integer inmateId) {
        if (this.blockHasInmates(block)) {
            ObservableList<Integer> inmateList;
            inmateList = this.inmateMap.get(block);
            inmateList.remove(inmateId);
        }
    }
    
    public static BlockManager getInstance() {
        if (manager == null)
            manager = new BlockManager();
        return manager;
    }
    
    public static CellBlock getBlockByText(String text) {
        for (CellBlock block : CellBlock.values())
            if (block.toString().equals(text)) return block;
        return null;
    }
    
    public static Status getStatusByText(String text) {
        for (Status status : Status.values())
            if (status.getTitle().equals(text)) return status;
        return null;
    }
    
}
