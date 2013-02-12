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

package com.isotrol.impe3.pms.gui.client.widget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProviderDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleDependencyDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ProviderModelData;

/**
 * FieldSet that manages a {@link DependencyTemplateDTO}.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 *
 */
public class DependencesPanel extends LayoutContainer implements IDetailPanel {
	
	/**
	 * Combo width<br/>
	 */
	private static final String PROVIDERS_WIDTH = "310";
	
	private Map<DependencyTemplateDTO,ComboBox<ProviderModelData>> depMap 
		= new HashMap<DependencyTemplateDTO, ComboBox<ProviderModelData>>();
	
	/**
	 * Forwards caught "Change" event to parent containers.<br/>
	 */
	private Listener<BaseEvent> forwardChangeEventListener = null;

	/**
	 * Bound data<br/>
	 */
	private List<DependencyTemplateDTO> dependencies = null;
	
	/**
	 * Messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * Forms helper service.<br/>
	 */
	private FormSupport formSupport = null;
	
	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Fields validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;
	
	/**
	 * Generic styles bundle
	 */
	private GuiCommonStyles guiCommonStyles = null;
	
	/**
	 * Constructor provided with injectable dependences.
	 * 
	 * @param pmsm
	 * @param m
	 * @param fs
	 * @param bs
	 * @param vl
	 */
	@Inject
	public DependencesPanel(GuiCommonMessages m, FormSupport fs, 
							Buttons bs, ValidatorListener vl) {
		this.messages = m;
		this.formSupport = fs;
		this.buttonsSupport = bs;
		this.validatorListener = vl;
	}
	
	/**
	 * Inits the widget. Must be called after dependencies injection.
	 * @param dtDto
	 */
	public void init(List<DependencyTemplateDTO> dtDto) {
		this.dependencies = dtDto;
		
		initListeners();
		initComponents();
	}
	
	/**
	 * Inits the declared listeners.<br/>
	 */
	private void initListeners() {
		forwardChangeEventListener = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent fe) {
				// forward caught event
//				fe.component = DependenciesPanel.this;
				DependencesPanel.this.fireEvent(fe.getType(), fe);
			}
		};
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		
		setLayout(new FitLayout());
		
		setLayoutOnChange(true);
		setAutoHeight(true);
		setScrollMode(Scroll.AUTOY);
		
		setLayout(formSupport.getStandardLayout());
		addStyleName(guiCommonStyles.margin10px());
		setAutoHeight(true);
		
		addDependencies();
	}

	/**
	 * Calls {@link #addDependency(DependencyTemplateDTO)} for each dependency of the bound dependences list.<br/>
	 */
	private void addDependencies() {
		if(dependencies != null) {
			for (DependencyTemplateDTO depDto : dependencies) {
				addDependency(depDto);
			}
		}
	}

	/**
	 * Adds the widgets that represent the passed dependence DTO.<br/>
	 * @param depDto
	 */
	private void addDependency(final DependencyTemplateDTO depDto) {
		
		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(this);
		LayoutContainer left = lr[0];
		LayoutContainer right = lr[1];
		
		List<ProviderDTO> providers = depDto.getProviders();
		final ModuleDependencyDTO dep = depDto.getDependency();

		final ComboBox<ProviderModelData> cbProviders = new ComboBox<ProviderModelData>();
		cbProviders.setEditable(true);
		cbProviders.setFieldLabel(dep.getName());
		cbProviders.setTriggerAction(TriggerAction.ALL);

		cbProviders.setWidth(PROVIDERS_WIDTH);
		// display "name :: bean"
		cbProviders.setDisplayField(ProviderModelData.PROPERTY_TO_DISPLAY);
		cbProviders.setEmptyText("--- dependencia ---");

		// populate combo store:
		ListStore<ProviderModelData> store = new ListStore<ProviderModelData>();
		List<ProviderModelData> lProviders = new LinkedList<ProviderModelData>();
		for (ProviderDTO dto : providers) {
			lProviders.add(new ProviderModelData(dto));
		}
		store.add(lProviders);
		cbProviders.setStore(store);
		
		// find & select current provider:
		ProviderDTO currentProvider = depDto.getCurrent();
		String currentId = null;
		if (currentProvider != null) {
			currentId = currentProvider.getCurrent().getId() + currentProvider.getBean();
		}
		ProviderModelData currentModelData = null;
		boolean found = false;
		Iterator<ProviderModelData> it = store.getModels().iterator();
		while (it.hasNext() && !found) {
			ProviderModelData providerModelData = it.next();
			found = ((String)providerModelData.get(ProviderModelData.PROPERTY_UNIQUE)).equals(currentId);
			if (found) {
				currentModelData = providerModelData;
			}
		}
		cbProviders.setValue(currentModelData);
		cbProviders.updateOriginalValue(currentModelData);

		left.add(cbProviders);
		
		/*IconButton bTrash = */buttonsSupport.addTrashIconButton(right, new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				cbProviders.setValue(null);
			}
		});
		
		depMap.put(depDto, cbProviders);
		
		/* wtf?
		cbProviders.addSelectionChangedListener(new SelectionChangedListener<ProviderModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<ProviderModelData> se) {
				List<ProviderModelData> selectedItems = se.getSelection();
				ProviderModelData selectedItem = null;
				if (!selectedItems.isEmpty()) {
					selectedItem = selectedItems.get(0);
				}
				cbProviders.disableEvents(true);
				cbProviders.setValue(selectedItem);
				cbProviders.enableEvents(true);
			}
		});
		*/

		if (dep.isRequired()) {
			cbProviders.setAllowBlank(false);
			cbProviders.getMessages().setBlankText(messages.vmRequired());
			// validate field on after render:
			cbProviders.addListener(Events.Render, validatorListener);
		}

		// forward events:
		cbProviders.addListener(Events.Select, forwardChangeEventListener);
		cbProviders.addListener(Events.SelectionChange, forwardChangeEventListener);
	}
	
	/**
	 * Applies GUI values to bound DTO, if rendered, or does nothing otherwise
	 */
	public void applyValues() {
		if(isRendered()) {
			applyGuiValues();
		}
	}
	
	/**
	 * Applies data from GUI to bound DTO<br/>
	 * 
	 * <b>Precondition:</b> widget must be rendered.
	 */
	private void applyGuiValues() {
		assert isRendered() : getClass().getName() + "#applyGuiValues(): widget must be rendered when called!";
		
		Iterator<Map.Entry<DependencyTemplateDTO, ComboBox<ProviderModelData>>> it = this.depMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<DependencyTemplateDTO, ComboBox<ProviderModelData>> entry 
				= (Map.Entry<DependencyTemplateDTO, ComboBox<ProviderModelData>>) it.next();
			DependencyTemplateDTO depDto = entry.getKey();
			ComboBox<ProviderModelData> combo = entry.getValue();
			combo.updateOriginalValue(combo.getValue());
			ProviderModelData pModelData = combo.getValue();
			ProviderDTO pDto = null;
			if(pModelData != null) {
				pDto = pModelData.getDTO();
			}
			depDto.setCurrent(pDto);
		}
	}

	/**
	 * Returns <code>true</code> if every dependence item is valid.<br/>
	 * @return <code>true</code> if every dependence item is valid;<code>false</code>, otherwise.
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<Entry<DependencyTemplateDTO, ComboBox<ProviderModelData>>> it = this.depMap.entrySet().iterator();
		while (valid && it.hasNext()) {
			Entry<DependencyTemplateDTO, ComboBox<ProviderModelData>> current = it.next();
			ComboBox<ProviderModelData> combo = current.getValue();
			if(combo.isRendered()) {	// validate GUI values
				valid = valid && combo.isValid();
			} else {	// validate bound data values
				DependencyTemplateDTO itemTemplate = current.getKey();
				boolean required = itemTemplate.getDependency().isRequired();
				valid = valid && (!required || required && itemTemplate.getCurrent() != null);
			}
		}
		return valid;
	}

	/**
	 * The <b>dirty</b> state in dependencies panel is computed "manually": every field value is
	 * compared with the <code>current</code> field value of the corresponding DTO.<br/>
	 * And that's because the {@link ComboBox#isDirty()} returns <code>true</code> for some
	 * unknown reason when the store contains values that render the same string in the combo.
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		Iterator<ComboBox<ProviderModelData>> it = this.depMap.values().iterator();
		boolean dirty = false;
		while (!dirty && it.hasNext()) {
			ComboBox<ProviderModelData> current = it.next();
			dirty = dirty || current.isDirty();
		}
		return dirty;
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return false;
	}

	/**
	 * @param guiCommonStyles the guiCommonStyles to set
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

	/**
	 * @return the dependencies
	 */
	public List<DependencyTemplateDTO> getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<DependencyTemplateDTO> dependencies) {
		this.dependencies = dependencies;
	}

}
