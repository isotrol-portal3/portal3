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


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.StylesheetDTO;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.design.dnd.DesignDNDController;


/**
 * Window with the Pages and Templates design panel
 * @author Manuel Ruiz
 * 
 */
public final class Design extends Window {

	private static Design instance = null;

	private static final String ATTRIBUTE_HREF = "href";
	private static final String ATTRIBUTE_TYPE = "type";
	private static final String ATTRIBUTE_REL = "rel";
	private static final String ATTRIBUTE_ID = "id";
	private static final String ATTRIBUTE_MEDIA = "media";

	private static final String TAG_LINK = "link";
	private static final String TAG_HEAD = "head";

	/**
	 * Contains dynamically added CSS files.<br/>
	 */
	private Map<String, Element> additionalCss = new HashMap<String, Element>();

	/** Contains the components from the frames */
	private List<String> frameDtoComponents = new LinkedList<String>();
	/** Contains the layout items */
	private Map<String, LayoutItemDTO> layoutItemsMap = new HashMap<String, LayoutItemDTO>();

	private static final String HEIGHT = "100%";
	private static final int UPPER_PANEL_HEIGHT = 50;
	private static final int LEFT_PANEL_WIDTH = 200;
	private static final int PANEL_MARGIN = 5;
	/** css class for html body in design panel */
	private static final String DESIGN_BODY_CSS = "body-window-design";

	private static LayoutContainer mainContainer;
	private static DesignPanel designPanel;
	private PalettesPanel palettesPanel;

	/** page model to design */
	private PageTemplateDTO page;
	/** page layout */
	private LayoutDTO pageLayoutDto = null;

	private Listener<BaseEvent> beforeCloseListener;

	/**
	 * layout changed flag
	 */
	private boolean dirty = false;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * PMS styles
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Constructor
	 */
	public Design() {
	}

	/**
	 * @return the Design instance
	 */
	public static Design getInstance() {
		if (instance == null) {
			// instantiate the design panel
			instance = PmsFactory.getInstance().getDesign();
		}
		return instance;
	}

	/**
	 * Build the list with the frame component's id and the layout items map
	 * @param layoutDto
	 */
	private void initializeFrameList() {

		// builds the list
		List<FrameDTO> frames = pageLayoutDto.getFrames();
		for (FrameDTO frameDto : frames) {
			addFrameComponents(frameDto);
		}

		// builds the map
		List<LayoutItemDTO> layoutItems = pageLayoutDto.getItems();
		for (LayoutItemDTO item : layoutItems) {
			layoutItemsMap.put(item.getId(), item);
		}
	}

	private void addFrameComponents(FrameDTO frame) {
		if (frame.isComponent()) {
			frameDtoComponents.add(frame.getComponent());
		} else if (frame.isColumns() && frame.getColumns() != null) {
			List<ColumnDTO> columns = frame.getColumns();
			for (ColumnDTO column : columns) {
				List<FrameDTO> frames = column.getFrames();
				for (FrameDTO f : frames) {
					addFrameComponents(f);
				}
			}
		} else if (frame.isFill()) {
			for (FrameDTO frameDto : frame.getFrames()) {
				addFrameComponents(frameDto);
			}
		}
	}

	private void addStyleSheets() {

		List<StylesheetDTO> listCss = pageLayoutDto.getStylesheets();
		for (StylesheetDTO css : listCss) {
			addCss(css);
		}

		if (GXT.isIE6) {
			List<StylesheetDTO> listCssIE6 = pageLayoutDto.getIE6Stylesheets();
			for (StylesheetDTO css : listCssIE6) {
				addCss(css);
			}
		} else if (GXT.isIE7) {
			List<StylesheetDTO> listCssIE7 = pageLayoutDto.getIE7Stylesheets();
			for (StylesheetDTO css : listCssIE7) {
				addCss(css);
			}
		} else if (GXT.isIE8) {
			List<StylesheetDTO> listCssIE8 = pageLayoutDto.getIE8Stylesheets();
			for (StylesheetDTO css : listCssIE8) {
				addCss(css);
			}
		}

	}

	/**
	 * Dynamically adds the passed CSS to the document, and stores in {@link #additionalCss} the resulting
	 * <code>&lt;link rel="stylesheet"&gt;</code><br/>
	 * @param css
	 */
	private void addCss(StylesheetDTO css) {

		String id = Integer.valueOf(Random.nextInt()).toString();

		Element eLink = DOM.createElement(TAG_LINK);
		eLink.setAttribute(ATTRIBUTE_ID, id);
		eLink.setAttribute(ATTRIBUTE_REL, "stylesheet");
		eLink.setAttribute(ATTRIBUTE_TYPE, "text/css");
		eLink.setAttribute(ATTRIBUTE_HREF, css.getUrl());
		if (css.getMedia() != null) {
			eLink.setAttribute(ATTRIBUTE_MEDIA, css.getMedia());
		}

		Element eHead = XDOM.getDocument().getElementsByTagName(TAG_HEAD).getItem(0);
		eHead.appendChild(eLink);

		additionalCss.put(id, eLink);
	};

	/**
	 * Removes all CSS files dynamically added with {@link #addCss(String)}.<br/>
	 */
	private void removeDynamicCss() {
		Element eHead = XDOM.getDocument().getElementsByTagName(TAG_HEAD).getItem(0);

		for (Map.Entry<String, Element> e : additionalCss.entrySet()) {
			eHead.removeChild(e.getValue());
		}
	}

	private void createDesignPanel() {

		// create main panel
		mainContainer = new LayoutContainer();
		final BorderLayout layout = new BorderLayout();
		mainContainer.setLayout(layout);
		mainContainer.setAutoWidth(true);
		mainContainer.setHeight(HEIGHT);
		mainContainer.addStyleName("design-main-panel");

		// north zone
		final BorderLayoutData upperLayoutData = new BorderLayoutData(LayoutRegion.NORTH);
		NorthPanel upperPanel = PmsFactory.getInstance().getNorthPanel();
		upperPanel.init(page, pageLayoutDto);
		upperLayoutData.setSize(UPPER_PANEL_HEIGHT);

		// west zone
		final BorderLayoutData leftLayoutData = new BorderLayoutData(LayoutRegion.WEST, 220, 150, 320);
		palettesPanel = PmsFactory.getInstance().getPalettesPanel();
		palettesPanel.init(pageLayoutDto);
		leftLayoutData.setSplit(true);
		leftLayoutData.setCollapsible(true);
		leftLayoutData.setSize(LEFT_PANEL_WIDTH);
		leftLayoutData.setMargins(new Margins(PANEL_MARGIN));

		// east zone
		final BorderLayoutData rightLayoutData = new BorderLayoutData(LayoutRegion.EAST);
		ComponentsTreePanel rightPanel = PmsFactory.getInstance().getRightPanel();
		rightPanel.init(page);
		rightLayoutData.setSplit(true);
		rightLayoutData.setCollapsible(true);
		// rightLayoutData.setSize(RIGHT_PANEL_WIDTH);
		rightLayoutData.setMargins(new Margins(PANEL_MARGIN));

		// design zone
		final BorderLayoutData centerLayoutData = new BorderLayoutData(LayoutRegion.CENTER);
		designPanel = new DesignPanel(pageLayoutDto);
		centerLayoutData.setMargins(new Margins(PANEL_MARGIN, 0, PANEL_MARGIN, 0));

		mainContainer.add(upperPanel, upperLayoutData);
		mainContainer.add(palettesPanel, leftLayoutData);
		mainContainer.add(designPanel, centerLayoutData);
		mainContainer.add(rightPanel, rightLayoutData);
		add(mainContainer);
	}

	private void configWindow() {

		setLayout(new FitLayout());
		setLayoutOnChange(true);
		RootPanel.get().addStyleName(DESIGN_BODY_CSS);
		setHeadingText(pmsMessages.titleDesignWindow(page.getName()));
		setMaximizable(true);
		setModal(true);
		setSize(600, 500);
	}

	/**
	 * Returns the Empty Component
	 * @return the EmptyComponent, or null if there isn't a empty component
	 */
	public static Widget getEmptyComponent() {
		Widget widget = null;
		Column c = getMainColumn();

		List<Frame<?>> boxes = c.getFrames();
		Iterator<Frame<?>> itBoxes = boxes.listIterator();
		while (itBoxes.hasNext() && widget == null) {
			Frame<?> b = itBoxes.next();
			if (b instanceof FrameComponent) {
				FrameComponent boxComponent = (FrameComponent) b;
				widget = boxComponent.getEmptyComponent();
			} else if (b instanceof FrameColumn) {
				FrameColumn boxColumn = (FrameColumn) b;
				widget = boxColumn.getEmptyComponent();
			}
		}
		return widget;
	}

	/**
	 * @return the design's main column
	 */
	public static Column getMainColumn() {

		if (designPanel != null) {
			return designPanel.getMainColum();
		} else {
			return null;
		}
	}

	private void addListeners() {

		beforeCloseListener = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				if (dirty) {
					be.setCancelled(true);
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmCloseDesignWindow(),
						new Listener<MessageBoxEvent>() {
							public void handleEvent(MessageBoxEvent we) {
								Button clicked = we.getButtonClicked();
								if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
									onCloseConfirmed();
								}
							}
						}).setModal(true);
				} else {
					onCloseConfirmed();
				}
			}
		};

		addListener(Events.BeforeHide, beforeCloseListener);
	}

	/**
	 * close the design and remove design panel
	 */
	public void onCloseConfirmed() {

		removeDynamicCss();

		// unregister drops
		if (Design.getMainColumn() != null) {
			DesignDNDController.getInstance().unregisterDropController(Design.getMainColumn().getColDropController());
		}

		// remove the body class
		RootPanel.get().removeStyleName(DESIGN_BODY_CSS);

		setFiresEvents(false);
		instance = null;
		DesignDNDController.resetInstance();
		hide();
	}

	/**
	 * Configures the design panel
	 * @param layout
	 * @param page
	 */
	public void config(PageTemplateDTO p, LayoutDTO layout) {
		this.page = p;
		this.pageLayoutDto = layout;

		configWindow();

		// adds the page's css
		addStyleSheets();

		// init the list with the page components
		initializeFrameList();

		// creates the layout window
		createDesignPanel();

		addListeners();
	}

	/**
	 * @return the frameDtoComponents
	 */
	public List<String> getFrameDtoComponents() {
		return frameDtoComponents;
	}

	/**
	 * @return the layoutItemsMap
	 */
	public Map<String, LayoutItemDTO> getLayoutItemsMap() {
		return layoutItemsMap;
	}

	/**
	 * @return the palettesPanel
	 */
	public PalettesPanel getPalettesPanel() {
		return palettesPanel;
	}

	/**
	 * @return the beforeCloseListener
	 */
	public Listener<BaseEvent> getBeforeCloseListener() {
		return beforeCloseListener;
	}

	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the designPanel
	 */
	public DesignPanel getDesignPanel() {
		return designPanel;
	}

	/**
	 * @return the pageLayoutDto
	 */
	public LayoutDTO getPageLayoutDto() {
		return pageLayoutDto;
	}

	/**
	 * @param pageLayoutDto the pageLayoutDto to set
	 */
	public void setPageLayoutDto(LayoutDTO pageLayoutDto) {
		this.pageLayoutDto = pageLayoutDto;
	}

	/**
	 * Builds the collections that keep the current layout
	 * @param layout the new page layout
	 */
	protected void updateLayout(LayoutDTO layout) {
		setPageLayoutDto(layout);
		frameDtoComponents.clear();
		layoutItemsMap.clear();
		initializeFrameList();
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @return the pmsStyles
	 */
	public PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
