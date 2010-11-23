/**
 * Copyright 2010 Tommi S.E. Laukkanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.addons.lazyquerycontainer;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * Helper class for Vaadin tables to generate status column.
 * 
 * @author Tommi S.E. Laukkanen
 */
public final class QueryItemStatusColumnGenerator implements ColumnGenerator, ValueChangeListener {
    /** Serial version UID of this class. */
    private static final long serialVersionUID = 1L;
    /** The Vaadin application this instance is part of. */
    private Application application;
    /** Icon resource for none state. */
    private Resource noneIconResource;
    /** Icon resource for added state. */
    private Resource addedIconResource;
    /** Icon resource for modified state. */
    private Resource modifiedIconResource;
    /** Icon resource for removed state. */
    private Resource removedIconResource;
    /** The status icon Vaadin component. */
    private Embedded statusIcon;

    /**
     * Construct which sets the application instance.
     * 
     * @param application
     *            The application to be set.
     */
    public QueryItemStatusColumnGenerator(final Application application) {
        this.application = application;
    }

    /**
     * Generates cell component.
     * @param source The table this cell is generated for.
     * @param itemId ID of the item this cell is presenting property of.
     * @param columnId ID of the column this cell is located at.
     * @return Component used to render this cell.
     */
    public Component generateCell(final Table source, final Object itemId, final Object columnId) {
        Property statusProperty = source.getItem(itemId).getItemProperty(columnId);

        noneIconResource = new ClassResource(QueryItemStatusColumnGenerator.class, "images/textfield.png",
                this.application);
        addedIconResource = new ClassResource(QueryItemStatusColumnGenerator.class, "images/textfield_add.png",
                this.application);
        modifiedIconResource = new ClassResource(QueryItemStatusColumnGenerator.class, "images/textfield_rename.png",
                this.application);
        removedIconResource = new ClassResource(QueryItemStatusColumnGenerator.class, "images/textfield_delete.png",
                this.application);

        statusIcon = new Embedded(null, noneIconResource);
        statusIcon.setHeight("16px");

        if (statusProperty instanceof ValueChangeNotifier) {
            ValueChangeNotifier notifier = (ValueChangeNotifier) statusProperty;
            notifier.addListener(this);
        }

        refreshImage(statusProperty);

        return statusIcon;
    }

    /**
     * Event handler for ValueChangeEvent.
     * @param event The event to be handled.
     */
    public void valueChange(final ValueChangeEvent event) {
        refreshImage(event.getProperty());
        statusIcon.requestRepaint();
    }

    /**
     * Refreshes the status Icon according to the property value.
     * @param statusProperty The property according to which status is updated.
     */
    private void refreshImage(final Property statusProperty) {
        if (statusProperty.getValue() == null) {
            statusIcon.setSource(noneIconResource);
            return;
        }
        QueryItemStatus status = (QueryItemStatus) statusProperty.getValue();
        if (status == QueryItemStatus.None) {
            statusIcon.setSource(noneIconResource);
        }
        if (status == QueryItemStatus.Modified) {
            statusIcon.setSource(modifiedIconResource);
        }
        if (status == QueryItemStatus.Added) {
            statusIcon.setSource(addedIconResource);
        }
        if (status == QueryItemStatus.Removed) {
            statusIcon.setSource(removedIconResource);
        }
    }

}