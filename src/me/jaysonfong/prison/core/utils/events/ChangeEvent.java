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
package me.jaysonfong.prison.core.utils.events;

import me.jaysonfong.prison.core.utils.entities.IProperty;

/**
 * Change Event
 * Depreciated and should not actually be used...but this is for backwards compatibility
 * because ultimately tracking down every reference is tiring.
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class ChangeEvent {
    
    private final Object oldValue;
    private final Object newValue;
    private final Object source;
    
    public ChangeEvent(Object IOldValue, Object INewValue, Object property) {
        this.oldValue = IOldValue;
        this.newValue = INewValue;
        this.source = property;
    }
    
    public Object getOldValue() {
        return this.oldValue;
    }
    
    public Object getNewValue() {
        return this.newValue;
    }
    
    public Object getSource() {
        return this.source;
    }
    
}