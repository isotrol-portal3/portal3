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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;

/**
 * Window that allows to specify a new name for the passed DTO.
 * 
 * <dl> <dt><b>Events:</b></dt>
 * 
 * <dd><b>Change</b> : BaseEvent(eventType, ComponentInPageNameEditor)<br> <div>
 * Fired after component name update.</div> 
 * <ul>
 * <li>eventType : {@link Events#Change}.</li> 
 * <li>ComponentInPageNameEditor : <code>this</code></li> </ul> 
 * </dd>
 * 
 * </dl>
 * 
 * @author Andrei Cojocaru
 *
 */
public class ComponentInPageNameEditor extends TypicalWindow implements IDetailPanel, IInitializableWidget {

	/**
	 * Will change to <code>true</code> when initialized.<br/>
	 */
	private boolean initialized = false;
	
	/**
	 * Bound Component-in-Page DTO.<br/>
	 */
	private ComponentInPageTemplateDTO dto = null;
	
	/**
	 * <b>Name</b> text field.<br/>
	 */
	private TextField<String> tfName = null;
	
	/*
	 * Injected deps.
	 */
	private Util util = null;
	/**
	 * Events listening strategy.<br/>
	 */
	private IComponentListeningStrategy eventsListeningStrategy = null;
	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttonsSupport = null;
	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * Form layout helper.<br/>
	 */
	private FormSupport formSupport = null;
	
	/**
	 * Generic styles bundle
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * Provided with necessary services.
	 * @param els
	 * @param bs
	 * @param pmsm
	 * @param fs
	 */
	@Inject
	public ComponentInPageNameEditor(PmsListeningStrategy els, Buttons bs, PmsMessages pmsm,
			FormSupport fs, Util u) {
		super();
		this.eventsListeningStrategy = (IComponentListeningStrategy) els;
		this.buttonsSupport = bs;
		this.pmsMessages = pmsm;
		this.formSupport = fs;
		this.util = u;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init()
	 */
	/**
	 * <br/>
	 */
	public ComponentInPageNameEditor init() {
		assert dto != null : getClass().getName() + ": bound DTO must not be null";
		
		this.initialized = true;
		
		initThis();
		initComponents();
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setClosable(false);
		setWidth(FormSupport.RECOMMENDED_WIDTH);
		setHeadingText(pmsMessages.headerRenamePanel());
		setAutoHeight(true);
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		LayoutContainer container = new LayoutContainer(formSupport.getStandardLayout());
		container.addStyleName(guiCommonStyles.margin10px());
		container.setAutoHeight(true);
		add(container);

		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.focus();
		String name = dto.getName();
		tfName.setValue(name);
		tfName.setOriginalValue(name);
		container.add(tfName);
		
		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				dto.setName(tfName.getValue());
				
				BaseEvent be = new BaseEvent(ComponentInPageNameEditor.this);
				ComponentInPageNameEditor.this.fireEvent(Events.Change, be);
				
				hide();
				util.info(pmsMessages.msgOkRenameComponentInPage());
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(
				this, 
				lAccept, 
				Arrays.asList(new Component [] {tfName}), 
				eventsListeningStrategy);
		addButton(bAccept);
		
		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * @param dto the dto to set
	 */
	public void setDto(ComponentInPageTemplateDTO dto) {
		this.dto = dto;
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	/**
	 * <br/>
	 */
	public boolean isDirty() {
		return tfName.isDirty();
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	/**
	 * <br/>
	 */
	public boolean isEdition() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	/**
	 * <br/>
	 */
	public boolean isValid() {
		return tfName.isValid();
	}

	/**
	 * @param guiCommonStyles the guiCommonStyles to set
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

}
