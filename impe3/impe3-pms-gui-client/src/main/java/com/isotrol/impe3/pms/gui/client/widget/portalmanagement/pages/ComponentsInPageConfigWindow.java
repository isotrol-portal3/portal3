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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages;


import java.util.Arrays;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.ConfigurationPanel;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;


/**
 * @author acojocaru
 * 
 */
public class ComponentsInPageConfigWindow extends TypicalWindow implements IDetailPanel, IInitializableWidget {

	/**
	 * Configuration template DTO.
	 */
	private ConfigurationTemplateDTO configDto = null;

	/**
	 * Suport for {@link IInitializableWidget#isInitialized()};
	 */
	private boolean initialized = false;

	/*
	 * Injected deps.
	 */
	/**
	 * Form layout helper
	 */
	private FormSupport formSupport = null;

	/**
	 * Buttons helper
	 */
	private Buttons buttonsSupport = null;

	/**
	 * The events listening strategy on the configuration field.
	 */
	private IComponentListeningStrategy eventsListeningStrategy = null;

	/**
	 * The configuration panel
	 */
	private ConfigurationPanel fsConfig = null;

	/**
	 * Default constructor.
	 */
	public ComponentsInPageConfigWindow() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init()
	 */
	/**
	 * 
	 */
	public ComponentsInPageConfigWindow init() {
		assert configDto != null : "A ConfigurationTemplateDTO must be set before initializing the widget.";
		initialized = true;

		initThis();
		initComponents();

		return this;
	}

	/**
	 * Inits this component properties
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setClosable(false);
		setButtonAlign(HorizontalAlignment.LEFT);
		setWidth(PmsConstants.DETAIL_WINDOW_WIDTH);
		setScrollMode(Scroll.AUTOY);
	}

	/**
	 * Inits this container inner components
	 */
	private void initComponents() {
		FormLayout fl = formSupport.getStandardLayout();
		LayoutContainer container = new LayoutContainer(fl);
		add(container);

		// configuration panel:
		fsConfig.init(configDto);
		container.add(fsConfig);

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				fsConfig.applyValues();
				hide();
				Util.fireChangeEvent(ComponentsInPageConfigWindow.this);
			}
		};
		Button saveConfigButton = buttonsSupport.createSaveButtonForDetailPanels(fsConfig, listener, Arrays
			.asList(new Component[] {fsConfig}), eventsListeningStrategy);
		addButton(saveConfigButton);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		return fsConfig.isDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return fsConfig.isEdition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		return fsConfig.isValid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param configDto
	 */
	public void setConfigDto(ConfigurationTemplateDTO configDto) {
		this.configDto = configDto;
	}

	/**
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param strat
	 */
	@Inject
	public void setEventsListeningStrategy(PmsListeningStrategy strat) {
		this.eventsListeningStrategy = (IComponentListeningStrategy) strat;
	}

	/**
	 * @param fsConfig
	 */
	@Inject
	public void setFsConfig(ConfigurationPanel fsConfig) {
		this.fsConfig = fsConfig;
	}

}
