/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isotrol.impe3.gui.common.widget.multivalue;


import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;

/**
 * Base class for multi-valued fields.
 * <b>Events:</b>
 * <ul>
 * <li><b>Change:</b> fired when an element is added to/removed from the list store.</li>
 * </ul>
 * 
 * @author Manuel Ruiz
 * 
 */
public abstract class AMultiValueField extends LayoutContainer {

	/**
	 * Only Add and Remove will be shown
	 */
	public static final int ADD_REMOVE = 0;
	
	/**
	 * Add, Remove and Edit will be shown
	 */
	public static final int ADD_REMOVE_EDIT = 1;

	/**
	 * Management buttons configuration value
	 */
	private int buttons = ADD_REMOVE_EDIT;

	/**
	 * Vertical margin between buttons.<br/>
	 */
	private static final int MARGIN_BOTTOM = 4;

	/**
	 * "padding-left" value in px for the panel that contains the button "Add"
	 * (top-right in the component)
	 */
	private static final int PADDING_LEFT_TOP_RIGHT_PANEL = 3;

	/**
	 * Default width for inner fields.<br/>
	 */
	private static final int FIELDS_DEFAULT_WIDTH = 170;

	/**
	 * List of values th<br/>
	 */
	private List<ModelData> originalValues = null;

	/**
	 * "margin-left" property that can be set using {@link Component#setStyleAttribute(String, String)}<br/>
	 */
	private static final String MARGIN_LEFT = "marginLeft";

	/**
	 * "padding-left" property that can be set using {@link Component#setStyleAttribute(String, String)}<br/>
	 */
	private static final String PADDING_LEFT = "paddingLeft";

	/**
	 * Display property in the listview.<br/>
	 */
	private String displayProperty = "name";
	
	/**
	 * vertical separation between the text field and the list view
	 */
	private String fieldLabel = null;

	/**
	 * field margin bottom
	 */
	private String marginBottom = "20px";

	/**
	 * button text
	 */
	private String buttonAddText = "Añadir";
	
	/**
	 * button text
	 */
	private String buttonRemoveText = "Eliminar";
	
	/**
	 * "Remove All" button label 
	 */
	private String buttonRemoveAllText = "Eliminar todos";
	
	/**
	 * button text
	 */
	private String buttonEditText = "Editar";
	
	/**
	 * List View Store
	 */
	private ListStore<ModelData> store = null;

	/**
	 * List View
	 */
	private ListView<ModelData> listView = null;

	/**
	 * list view height
	 */
	private int listViewHeight = 175;

	/**
	 * label width
	 */
	private int labelWidth = 155;
	
	/**
	 * Current operation descriptor.
	 * 
	 * @author Andrei Cojocaru
	 */
	protected enum EOperation {
		/**
		 * Represents a new tag creation
		 */
		OP_CREATE,
		
		/**
		 * Represents tag edition.
		 */
		OP_EDIT
	};

	/**
	 * Current operation type
	 */
	private EOperation op = EOperation.OP_CREATE;
	
	/**
	 * The item being currently edited.
	 */
	private ModelData editingItem = null;
	
	/**
	 * "Edit" button
	 */
	private Button bEdit = null;

	/**
	 * "Remove" button
	 */
	private Button bRemove = null;

	/**
	 * "Add" button
	 */
	private Button bAdd = null;

	/**
	 * "Remove all" button
	 */
	private Button bRemoveAll = null;
	
	/**
	 * <code>dirty</code> state, initially <code>false</code>. Changes to <code>true</code>
	 * when some element is added/removed/edited.
	 */
	private boolean dirty = false;

	/**
	 * Sets the passed model as the source value, in order to be edited.<br/>
	 * @param model
	 */
	protected abstract void setSourceValue(ModelData model);
	
	/**
	 * <br/>
	 * @param label label set to the multivalue field.
	 */
	public void setFieldLabel(String label) {
		this.fieldLabel = label;
	}
	
	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Component#beforeRender()
	 */
	/**
	 * Inits this component.<br/>
	 */
	@Override
	protected void beforeRender() {
		configureThis();
		initComponents();
		displayValues();
	}
	
	/**
	 * Inits this component properties.<br/>
	 */
	private void configureThis() {

		setLayout(new RowLayout());
		setLayoutOnChange(true);
		
		addStyleName("x-form-label-12px");

	}
	
	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {

		HorizontalPanel hpTop = new HorizontalPanel();

		// the field to the left:
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(labelWidth);
		layout.setDefaultWidth(getFieldsDefaultWidth());
		LayoutContainer lc = new LayoutContainer(layout);
		lc.setLayoutOnChange(true);
		
		hpTop.add(lc);

		// a vertical panel for buttons to the right:
		LayoutContainer rc = new LayoutContainer(new RowLayout());
		rc.setIntStyleAttribute(PADDING_LEFT, PADDING_LEFT_TOP_RIGHT_PANEL);
		hpTop.add(rc);
		
		// the "Add" button to the right:
		bAdd  = new Button(buttonAddText);
		bAdd.setIcon(IconHelper.create("new-element"));
		bAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				maybeAddSourceValues();
			}
		});
		RowData layoutData = new RowData();
		layoutData.setMargins(new Margins(0, 0, MARGIN_BOTTOM, 0));
		rc.add(bAdd, layoutData);

		add(hpTop);


		store = new ListStore<ModelData>();
		store.addStoreListener(new StoreListener<ModelData>() {
			@Override
			public void storeAdd(StoreEvent<ModelData> se) {
				bRemoveAll.enable();
				setDirty(true);
				fireEvent(Events.Change);
			}
			@Override
			public void storeRemove(StoreEvent<ModelData> se) {
				if (store.getCount() == 0) {
					bRemoveAll.disable();
				}
				setDirty(true);
				fireEvent(Events.Change);
			}
		});

		listView = new ListView<ModelData>();
		listView.setStore(store);
		listView.setSize(FIELDS_DEFAULT_WIDTH, listViewHeight);
		listView.addStyleName("x-form-text");
		listView.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<ModelData> se) {
				boolean enabled = true;
				if (se.getSelectedItem() == null) {
					enabled = false;
				}
				if (bEdit != null) {
					bEdit.setEnabled(enabled);
				}
				bRemove.setEnabled(enabled);
			}
		});
		listView.setStyleAttribute(MARGIN_LEFT, Integer.toString(labelWidth + 5));
		listView.setDisplayProperty(displayProperty);

		HorizontalPanel hpBottom = new HorizontalPanel();
		hpBottom.add(listView);

		// buttons to remove items from the list view
		LayoutContainer buttonsPanel = createButtonsPanel();
		hpBottom.add(buttonsPanel);
		add(hpBottom);

		addSourceField(lc, rc);
	}

	/**
	 * Displays the values bound to this widget.
	 */
	protected void displayValues() {
		// display available source values, if necessary
		displaySourceData();
		// the values
		displayValuesData();
	}
	
	/**
	 * Displays the data relating to source, if any.
	 * Default implementation does nothing.
	 */
	protected void displaySourceData() {}

	/**
	 * Displays the bound values into the listView.<br/>
	 */
	private void displayValuesData() {
		store.removeAll();
		if (originalValues != null) {
			store.setFiresEvents(false);
			store.add(originalValues);
			store.setFiresEvents(true);
			listView.refresh();
		}
	}
	
	/**
	 * Adds the source field to the container.<br/>
	 * @param lc left container. Should contain fields.
	 * @param rc right container. May be used to insert new buttons.
	 */
	protected abstract void addSourceField(LayoutContainer lc, LayoutContainer rc);

	/**
	 * Fired when "Add" button pressed.<br/>
	 * Validates the source value. If everything is OK, adds the source value to the listview.
	 */
	protected abstract void maybeAddSourceValues();
	
	/**
	 * Resets the source, leaving the inputs in blank.
	 */
	protected abstract void resetSource();
	
	/**
	 * Adds the passed value.<br/>
	 * @param value
	 */
	protected void addValue(ModelData value) {
		store.add(value);
		op = EOperation.OP_CREATE;
		
		resetSource();
	}
	
	/**
	 * Creates a panel with the buttons "Edit", "Remove" and "Remove all"
	 * @return 
	 */
	private LayoutContainer createButtonsPanel() {
		
		LayoutContainer container = new LayoutContainer(new FitLayout());
		container.setWidth(120);
		container.setHeight(listViewHeight);
		LayoutContainer c = new LayoutContainer();
		
		VBoxLayout layout = new VBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		VBoxLayoutData layoutData = new VBoxLayoutData(new Margins(0, 0, 5, 0));
		if (buttons == ADD_REMOVE_EDIT) {
			bEdit = new Button(buttonEditText);
			bEdit.disable();
			bEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (op == EOperation.OP_EDIT) {
						addValue(editingItem);
					}
					
					editingItem = listView.getSelectionModel().getSelectedItem();
					op = EOperation.OP_EDIT;
					setSourceValue(editingItem);
					store.remove(editingItem);
				}
			});
			c.add(bEdit, layoutData);
		}
		
		bRemove = new Button(buttonRemoveText);
		bRemove.disable();
		bRemove.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<ModelData> itemsToRemove = listView.getSelectionModel().getSelectedItems();
				if (itemsToRemove != null && !itemsToRemove.isEmpty()) {
					for (int i = 0; i < itemsToRemove.size(); i++) {
						ModelData itemToRemove = itemsToRemove.get(i);
						store.remove(itemToRemove);
					}
				}

			}
		});
		c.add(bRemove, layoutData);

		bRemoveAll = new Button(buttonRemoveAllText);
		bRemoveAll.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.removeAll();
			}
		});
		bRemoveAll.disable();
		c.add(bRemoveAll, layoutData);
		
		container.add(c);
		
		return container;
	}

	/**
	 * @param listViewHeight the listViewHeight to set
	 */
	public void setListViewHeight(int listViewHeight) {
		this.listViewHeight = listViewHeight;
	}

	/**
	 * @return the marginBottom
	 */
	protected final String getMarginBottom() {
		return marginBottom;
	}

	/**
	 * @param marginBottom the marginBottom to set
	 */
	public void setMarginBottom(String marginBottom) {
		this.marginBottom = marginBottom;
	}
	
	/**
	 * Sets the display property for the list view.<br/>
	 * @param property
	 */
	public void setDisplayProperty(String property) {
		this.displayProperty = property;
		if (listView != null) {
			listView.setDisplayProperty(property);
		}
	}

	/**
	 * @return the labelWidth
	 */
	protected int getLabelWidth() {
		return labelWidth;
	}
	
	/**
	 * get the values of the assigment list
	 * @return the list
	 */
	public abstract List<?> getValues();
	
	/**
	 * Sets the component values displayed in the listview.<br/>
	 * @param values
	 */
	public void setValues(List<ModelData> models) {
		this.originalValues = models;
		if (isRendered()) {
			displayValuesData();
		}
	}

	/**
	 * @return the listView
	 */
	protected final ListView<ModelData> getListView() {
		return listView;
	}
	
	/**
	 * @return the displayProperty
	 */
	protected final String getDisplayProperty() {
		return displayProperty;
	}

	/**
	 * <br/>
	 * @return the label of this multi value field.
	 */
	protected final String getFieldLabel() {
		return fieldLabel;
	}
	
	/**
	 * Returns the inner fields default width.<br/>
	 * @return the inner fields default width.
	 */
	protected static final int getFieldsDefaultWidth() {
		return FIELDS_DEFAULT_WIDTH;
	}
	
	/**
	 * @param buttons = {@link #ADD_REMOVE_EDIT} | {@link #ADD_REMOVE}
	 */
	public void setButtons(int buttons) {
		this.buttons = buttons;
	}
	
	/**
	 * @return <code>true</code>, if some value has been added, removed, or edited.
	 */
	public boolean isDirty() {
		return dirty;
	}
	
	/**
	 * Sets the dirty value.
	 */
	protected final void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	/**
	 * @return the button "Add"
	 */
	public Button getAddButton() {
		return bAdd;
	}
	
	/*
	 * Setters for buttons labels
	 */
	
	/**
	 * @param buttonText the buttonText to set
	 */
	public void setAddButtonText(String buttonText) {
		this.buttonAddText = buttonText;
	}
	
	/**
	 * @param buttonEditText
	 */
	public void setEditButtonLabel(String bEditText) {
		this.buttonEditText = bEditText;
	}
	
	/**
	 * @param buttonRemoveText
	 */
	public void setRemoveButtonLabel(String bRemoveText) {
		this.buttonRemoveText = bRemoveText;
	}
	
	/**
	 * @param buttonRemoveAllText
	 */
	public void setRemoveAllButtonLabel(String bRemoveAllText) {
		this.buttonRemoveAllText = bRemoveAllText;
	}
	
}
