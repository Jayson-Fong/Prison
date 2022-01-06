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
package me.jaysonfong.prison.core.utils.entities;

import javafx.beans.property.StringProperty;
import me.jaysonfong.prison.core.PrisonManager;
import me.jaysonfong.prison.core.utils.events.ChangeEvent;
import me.jaysonfong.prison.core.utils.events.EventHandler;

/**
 * Title Property
 * Depreciated and should not actually be used...but this is for backwards compatibility
 * because ultimately tracking down every reference is tiring.
 * @author Jayson Fong <contact@jaysonfong.me>
 */
public class TitleProperty implements IProperty {
    
    private final StringProperty property;
    private EventHandler<? super ChangeEvent> changeEventHandler;
    
    public TitleProperty(StringProperty stringProperty) {
        this.property = stringProperty;
    }
    
    public synchronized void set(String title) {
        ChangeEvent eventInfo = new ChangeEvent(
            this.get(),
            title,
            this
        );
        this.property.setValue(this.process(title));
        if (this.changeEventHandler != null)
            this.changeEventHandler.handle(eventInfo);
    }
    
    @Override
    public synchronized void set(Object title) {
        if (!(title instanceof String))
            throw new IllegalArgumentException();
        this.set((String) title);
    }
    
    @Override
    public String get() {
        return this.property.getValue();
    }

    public final void setOnChange(EventHandler<? super ChangeEvent> value) {
        this.changeEventHandler = value;
    }
    
    protected String process(String str) {
        String processed;
        processed = String.format("%s | %s", str, PrisonManager.SERVICE_NAME);
        return processed;
    }
    
}
