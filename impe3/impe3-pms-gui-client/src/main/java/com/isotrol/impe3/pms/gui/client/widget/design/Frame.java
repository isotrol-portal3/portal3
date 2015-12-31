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

package com.isotrol.impe3.pms.gui.client.widget.design;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.CssClassNameValidator;
import com.isotrol.impe3.pms.gui.client.widget.design.dnd.DesignDNDController;


/**
 * GUI for Frames
 * @author Manuel Ruiz
 * 
 */
public abstract class Frame<D extends FrameDTO> extends VerticalPanel {

	private static final String CSS_BOX = "box";

	/** Default title for css selector button. This string will be showed when frame has not css class */
	protected static final String DEFAULT_CSS_TITLE = "Añadir clases css al frame";

	private FocusPanel vertPanel;
	/** panel con el botón de cierre y la zona draggable */
	private final HorizontalPanel menuPanel;
	/** indica si tiene remove button */
	private boolean closable;
	/** indica si tiene que ser clonado al hacerle remove */
	private boolean clonable = true;
	/** flag to indicate if the frame has a css selector button */
	private boolean cssSelector;
	/** field to store the value of frame's css class */
	private String cssClass;
	/** indicates if the frame is inherited froma page parent */
	private boolean inherited;
	/** the css selcetor button */
	private IconButton cssButton = null;

	/** listener to open css window */
	private SelectionListener<IconButtonEvent> cssListener = null;
	
	/** wrapped dto */
	private FrameDTO frame = null;

	/*
	 * Injected deps
	 */
	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Pms styles bundle
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Common styles bundle
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Add a div that disables the box
	 */
	public abstract void block();

	/**
	 * Removes the frame from the main column
	 */
	public abstract void removeFrameFromMainColumn();

	/**
	 * Constructor
	 */
	public Frame() {
		menuPanel = new HorizontalPanel();
		vertPanel = new FocusPanel();
	}

	protected Frame<D> init(D frame) {
		this.frame = frame;
		this.inherited = frame.isInherited();

		addStyleName(CSS_BOX);

		if (!inherited) {
			Element domDraggable = DOM.createDiv();
			domDraggable.setInnerText("a");
			domDraggable.setClassName(getDraggableStyle());
			HTML draggableZone = new HTML(domDraggable.getString());
			menuPanel.add(draggableZone);
			add(menuPanel);
			// make the frame draggable
			DesignDNDController.getInstance().makeDraggable(this, draggableZone);
		}

		add(vertPanel);
		
		return this;
	}

	protected abstract String getDraggableStyle();

	private void changeCssSelectorStyle(List<String> cssClasses) {

		cssButton.changeStyle(pmsStyles.iconCssSelectorRed());

		for (String css : cssClasses) {
			if (!Util.emptyString(css)) {
				cssButton.changeStyle(pmsStyles.iconCssSelectorGreen());
				return;
			}
		}
	}

	/**
	 * @return the vertPanel
	 */
	public FocusPanel getVertPanel() {
		return vertPanel;
	}

	private IconButton createCloseButton() {

		IconButton closeButton = new IconButton(pmsStyles.iconDesignColumnClose());
		closeButton.setTitle("Eliminar frame de la zona de maquetación");
		closeButton.addSelectionListener(new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				removeFrameFromMainColumn();
				Design.getInstance().setDirty(true);
			}
		});

		return closeButton;
	}

	/**
	 * adds a button to remove the frame from its parent
	 */
	private void addRemoveButton() {
		IconButton cb = createCloseButton();
		menuPanel.insert(cb, 0);
		menuPanel.setCellWidth(cb, "10px");
	}

	/**
	 * @return the hasRemoveButton
	 */
	public boolean isClosable() {
		return closable;
	}

	/**
	 * @param closable the closable to set
	 */
	public void setClosable(boolean closable) {
		if (closable) {
			addRemoveButton();
		}
		this.closable = closable;
	}

	/**
	 * @param clonable
	 */
	protected void removeFromParent(boolean clonar) {
		this.clonable = clonar;
		super.removeFromParent();
	}

	/**
	 * Add a button to select a css style name
	 */
	public void addCssSelectorButton() {

		// sets the css flag to true
		cssSelector = true;

		String cssStyle = pmsStyles.iconCssSelectorRed();
		cssButton = new IconButton(cssStyle);
		changeCssSelectorStyle(getInitialStyles());

		createCssToolTip();
		cssListener = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				new CSSWindow().show();
			}
		};
		cssButton.addSelectionListener(cssListener);

		menuPanel.insert(cssButton, 1);
		menuPanel.setCellWidth(cssButton, "20px");
	}

	protected List<String> getInitialStyles() {
		return Arrays.asList(cssClass);
	}

	private void createCssToolTip() {
		ToolTipConfig config = new ToolTipConfig();
		config.setText(getInitialToolTipText());
		config.setMouseOffset(new int[] {0, 0});
		config.setAnchor("left");
		cssButton.setToolTip(config);
	}

	protected String getInitialToolTipText() {
		String tooltipText = DEFAULT_CSS_TITLE;
		if (!Util.emptyString(cssClass)) {
			tooltipText = "CSS del frame: <b>" + cssClass + "</b>";
		}
		
		return tooltipText;
	}

	protected void removeCssSelector() {
		cssSelector = false;
		menuPanel.remove(cssButton);
	}
	
	protected void saveCss(List<String> cssClasses) {
		changeCssSelectorStyle(cssClasses);
		createCssToolTip();
	}

	class CSSWindow extends Dialog {

		private LayoutContainer pCssSelector;

		private TextField<String> cssField;

		private List<Field<String>> fields = null;

		/** Listener to validate css fields */
		private Listener<BaseEvent> validateListener = null;

		public CSSWindow() {

			setHeadingText(pmsMessages.headerFrameConfigurationPanel());
			setModal(true);
			setHideOnButtonClick(true);
			setResizable(false);
			setLayout(new FitLayout());
			setWidth(330);
			fields = new ArrayList<Field<String>>();
			final Button okButton = getButtonById(Dialog.OK);
			okButton.disable();
			validateListener = new Listener<BaseEvent>() {
				public void handleEvent(BaseEvent be) {
					okButton.setEnabled(isValid());
				}
			};

			final FormLayout formLayout = new FormLayout();
			formLayout.setDefaultWidth(180);
			pCssSelector = new LayoutContainer(formLayout);
			pCssSelector.setLayoutOnChange(true);
			pCssSelector.addStyleName(styles.padding5px());
			pCssSelector.setAutoHeight(true);

			cssField = new TextField<String>();
			cssField.setFieldLabel("Css frame");
			cssField.setValidator(new CssClassNameValidator(pmsMessages));
			if (cssClass != null) {
				cssField.setValue(cssClass);
			}
			cssField.addListener(Events.OnKeyUp, validateListener);
			addField(cssField);

			add(pCssSelector);

			addWindowListener(new WindowListener() {
				@Override
				public void handleEvent(WindowEvent we) {
					Button bc = we.getButtonClicked();
					if (bc != null && bc.getItemId().equals(Dialog.OK)) {
						cssClass = cssField.getValue();
						List<String> cssClasses = new ArrayList<String>();
						for (Field<String> field : fields) {
							cssClasses.add(field.getValue());
						}
						saveCss(cssClasses);
						
					}
				}
			});
		}

		protected boolean isValid() {
			boolean valid = true;
			for (Field<?> field : fields) {
				valid = valid && field.isValid();
			}

			return valid;
		}

		protected void addField(TextField<String> field) {
			fields.add(field);
			pCssSelector.add(field);
		}

		/**
		 * @return the cssField
		 */
		public TextField<String> getCssField() {
			return cssField;
		}

		/**
		 * @return the fields
		 */
		public List<Field<String>> getFields() {
			return fields;
		}

		/**
		 * @return the validateListener
		 */
		public Listener<BaseEvent> getValidateListener() {
			return validateListener;
		}
	}

	/**
	 * @return the clonable
	 */
	public boolean isClonable() {
		return clonable;
	}

	/**
	 * @return the menuPanel
	 */
	public HorizontalPanel getMenuPanel() {
		return menuPanel;
	}

	/**
	 * @return the cssSelector
	 */
	public boolean isCssSelector() {
		return cssSelector;
	}

	/**
	 * @param cssSelector the cssSelector to set
	 */
	public void setCssSelector(boolean cssSelector) {
		this.cssSelector = cssSelector;
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the inherited
	 */
	public boolean isInherited() {
		return inherited;
	}

	/**
	 * Injects the PMS specific messages.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the pmsMessages
	 */
	public PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the pmsStyles
	 */
	public PmsStyles getPmsStyles() {
		return pmsStyles;
	}
	
	/**
	 * Injects the Pms styles bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @return the cssButton
	 */
	public IconButton getCssButton() {
		return cssButton;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @return the cssListener
	 */
	public SelectionListener<IconButtonEvent> getCssListener() {
		return cssListener;
	}

	/**
	 * @return the frame
	 */
	public FrameDTO getFrame() {
		return frame;
	}
}
