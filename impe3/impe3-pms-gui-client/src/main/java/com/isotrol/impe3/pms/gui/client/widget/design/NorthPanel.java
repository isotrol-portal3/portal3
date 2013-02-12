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


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.ColumnsFrameDTO;
import com.isotrol.impe3.pms.api.page.ComponentFrameDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.PageErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;


/**
 * North Panel of Design window
 * @author Manuel Ruiz
 * 
 */
public class NorthPanel extends ContentPanel {

	private static final String SAVE_IMAGE = "save-button-icon";
	private static final String SAVE_CLOSE_IMAGE = "save-close-button-icon";
	private static final String CLEAN_IMAGE = "clean-button-icon";
	private static final String PREVIEW_ICON = "preview-button-icon";
	private static final String ALIGN_GROUP_ID = "align";
	private static final String LEFT_ALIGN_CSS = "float-left";
	private static final String RIGHT_ALIGN_CSS = "float-right";
	private static final String CENTER_ALIGN_CSS = "float-none";

	/** page model to design */
	private PageTemplateDTO page = null;
	/** page locator */
	private PageLoc pageLoc = null;

	/** page layout */
	private LayoutDTO pageLayout = null;
	/** indicates if the layout contains a space */
	private boolean containsSpace = false;

	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/** pages service */
	private IPagesServiceAsync pagesService = null;

	/**
	 * Pages error message resover.<br/>
	 */
	private PageErrorMessageResolver emrPages = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;
	
	/**
	 * PMS styles bundle
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * PMS util bundle
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Constructor
	 * @param page
	 * @param layoutDto
	 */
	public NorthPanel() {
		setHeaderVisible(false);
	}

	/**
	 * @param thePage
	 * @param theLayout
	 */
	public void init(PageTemplateDTO thePage, LayoutDTO theLayout) {
		this.page = thePage;
		this.pageLoc = new PageLoc(thePage.getPortalId(), thePage.getDeviceId(), thePage.getId());
		this.pageLayout = theLayout;

		addStyleName("design-north-panel");
		setBodyStyleName("design-north-panel-body");
		setBodyBorder(true);

		addToolBar();
	}

	private void addToolBar() {
		ToolBar toolBar = new ToolBar();

		// restore button
		final Button bRestore = new Button(pmsMessages.labelRestore());
		bRestore.setToolTip(pmsMessages.ttRestoreResume());
		bRestore.setIconStyle(pmsStyles.iconRestoreDesign());
		bRestore.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> restoreListener = new Listener<MessageBoxEvent>() {

					public void handleEvent(MessageBoxEvent be) {
						Button b = be.getButtonClicked();
						if (b.getItemId().equals(Dialog.YES)) {
							restoreDesign();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmRestoreDesignWindow(),
					restoreListener);
			}
		});

		final Button cleanButton = new Button(pmsMessages.labelClear());
		cleanButton.setToolTip(pmsMessages.ttCleanDesign());
		cleanButton.setIconStyle(CLEAN_IMAGE);
		cleanButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> cleanListener = new Listener<MessageBoxEvent>() {

					public void handleEvent(MessageBoxEvent be) {
						Button b = be.getButtonClicked();
						if (b.getItemId().equals(Dialog.YES)) {
							removeFramesFromMainColumn();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmClearDesignWindow(),
					cleanListener);
			}
		});

		SplitButton alignButton = new SplitButton(pmsMessages.labelAlign());
		addMenuAlignOptions(alignButton);

		// save button
		Button saveButton = new Button(pmsMessages.labelSave());
		saveButton.setIconStyle(SAVE_IMAGE);
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				getAndSaveLayout(false);
			}
		});

		// save and close button
		Button saveCloseButton = new Button(pmsMessages.labelSaveAndExit());
		saveCloseButton.setIconStyle(SAVE_CLOSE_IMAGE);
		saveCloseButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				getAndSaveLayout(true);
			}
		});

		// button to open the preview window
		Button previewButton = new Button(pmsMessages.labelPreview());
		previewButton.setIconStyle(PREVIEW_ICON);
		previewButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				openPreviewWindow();
			}
		});

		toolBar.add(bRestore);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(cleanButton);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(alignButton);
		toolBar.add(new SeparatorToolItem());
		// Fills to the end to add the buttons in the right side
		toolBar.add(new FillToolItem());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(previewButton);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(saveButton);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(saveCloseButton);

		add(toolBar);
	}

	private void addMenuAlignOptions(SplitButton button) {
		Menu menu = new Menu();
		CheckMenuItem check = new CheckMenuItem(pmsMessages.labelLeft());
		check.setGroup(ALIGN_GROUP_ID);
		check.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent me) {
				alignLeft();
			}
		});
		menu.add(check);

		check = new CheckMenuItem(pmsMessages.labelCenter());
		check.setChecked(true);
		check.setGroup(ALIGN_GROUP_ID);
		check.addSelectionListener(new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				alignCenter();
			}

		});
		menu.add(check);

		check = new CheckMenuItem(pmsMessages.labelRight());
		check.setGroup(ALIGN_GROUP_ID);
		check.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				alignRight();
			}
		});
		menu.add(check);

		button.setMenu(menu);
	}

	/**
	 * Aligns the main column to the left.<br/>
	 */
	private void alignLeft() {
		Design.getMainColumn().removeStyleName(RIGHT_ALIGN_CSS);
		Design.getMainColumn().removeStyleName(CENTER_ALIGN_CSS);
		Design.getMainColumn().addStyleName(LEFT_ALIGN_CSS);
	}

	/**
	 * Aligns the main column to the center.<br/>
	 */
	private void alignCenter() {
		Design.getMainColumn().removeStyleName(LEFT_ALIGN_CSS);
		Design.getMainColumn().removeStyleName(RIGHT_ALIGN_CSS);
		Design.getMainColumn().addStyleName(CENTER_ALIGN_CSS);
	}

	/**
	 * Aligns the main column to the right.<br/>
	 */
	private void alignRight() {
		Design.getMainColumn().removeStyleName(LEFT_ALIGN_CSS);
		Design.getMainColumn().removeStyleName(CENTER_ALIGN_CSS);
		Design.getMainColumn().addStyleName(RIGHT_ALIGN_CSS);
	}

	private void removeFramesFromMainColumn() {
		List<Frame<?>> framesInDesign = Design.getMainColumn().getFrames();
		for (Frame<?> f : framesInDesign) {
			f.removeFrameFromMainColumn();
		}
	}

	/**
	 * Gets the list of frames and try to save it
	 * @param close If true, close the design window
	 */
	private void getAndSaveLayout(final boolean close) {

		Column col = Design.getMainColumn();
		List<Frame<?>> visualFrames = null;
		FrameColumn fillFrame = col.getFillFrame();

		if (fillFrame != null) {
			// save the frames in the fill
			Column fillCol = fillFrame.getColFromFillFrame();
			visualFrames = fillCol.getFrames();
		} else {
			// save all frames
			visualFrames = col.getFrames();
		}

		List<FrameDTO> framesDto = new LinkedList<FrameDTO>();

		try {
			for (Frame<?> visualFrame : visualFrames) {
				if (visualFrame instanceof FrameComponent) {
					FrameComponent fComp = (FrameComponent) visualFrame;
					ComponentFrameDTO frame = visualFrameToFrameCompDTO(fComp);
					framesDto.add(frame);

				} else if (visualFrame instanceof FrameColumn) {
					FrameColumn fCol = (FrameColumn) visualFrame;
					ColumnsFrameDTO cfDto;

					cfDto = visualFrameToFrameColsDTO(fCol);
					framesDto.add(cfDto);
				}
			}

			AsyncCallback<LayoutDTO> callback = new AsyncCallback<LayoutDTO>() {
				public void onFailure(Throwable arg0) {
					Design.getInstance().unmask();
					errorProcessor.processError(arg0, emrPages, pmsMessages.msgErrorSaveLayout());
				}

				public void onSuccess(LayoutDTO arg0) {
					pageLayout = arg0;
					// update the layout in Design
					Design.getInstance().updateLayout(arg0);
					Design.getInstance().unmask();
					util.info(pmsMessages.msgSuccessSaveLayout());
					if (close) {
						// remove the before close listener not to ask if close
						Listener<BaseEvent> listener = Design.getInstance().getBeforeCloseListener();
						Design.getInstance().removeListener(Events.BeforeClose, listener);
						Design.getInstance().onCloseConfirmed();
					}
				}
			};

			if (page.getPageClass().equals(PageClass.TEMPLATE) && !containsSpace) {
				MessageBox.alert(messages.headerErrorWindow(), pmsMessages.msgErrorTemplateHasNoSpace(), null)
					.setModal(true);
			} else {
				Design.getInstance().mask(pmsMessages.mskSavePageLayout());
				pagesService.setLayout(this.pageLoc, framesDto, callback);
				containsSpace = false;
			}

		} catch (DesignException e) {
			MessageBox.alert(messages.headerErrorWindow(), pmsMessages.msgErrorSaveLayoutWithEmptyColumn(), null)
				.setModal(true);
		}
	}

	private ColumnDTO createColumnDto(Column col) throws DesignException {

		ColumnDTO colDto = new ColumnDTO();
		colDto.setWidth(col.getWidth());
		colDto.setName(col.getColumnCss());

		List<FrameDTO> framesDto = new LinkedList<FrameDTO>();
		List<Frame<?>> visualFrames = col.getFrames();

		// it wasn't allowed empty columns
		if (visualFrames.isEmpty()) {
			throw new DesignException();
		}

		for (Frame<?> visualFrame : visualFrames) {
			if (visualFrame instanceof FrameComponent) {
				FrameComponent fComp = (FrameComponent) visualFrame;
				ComponentFrameDTO cfDto = visualFrameToFrameCompDTO(fComp);
				framesDto.add(cfDto);

			} else if (visualFrame instanceof FrameColumn) {
				FrameColumn fCol = (FrameColumn) visualFrame;
				ColumnsFrameDTO cfDto = visualFrameToFrameColsDTO(fCol);
				framesDto.add(cfDto);
			}
		}
		colDto.setFrames(framesDto);

		return colDto;
	}

	private ColumnsFrameDTO visualFrameToFrameColsDTO(FrameColumn col) throws DesignException {

		ColumnsFrameDTO dto = new ColumnsFrameDTO();
		List<ColumnDTO> children = new LinkedList<ColumnDTO>();
		List<Column> columns = col.getColumns();
		for (Column c : columns) {
			children.add(createColumnDto(c));
		}
		dto.setChildren(children);
		dto.setName(col.getCssClass());

		return dto;
	}

	private ComponentFrameDTO visualFrameToFrameCompDTO(FrameComponent visualFrame) {

		if (!containsSpace) {
			containsSpace = visualFrame.isSpace();
		}

		ComponentFrameDTO dto = new ComponentFrameDTO();
		dto.setComponent(visualFrame.getComponent().getDto().getId());
		dto.setName(visualFrame.getCssClass());

		return dto;
	}

	/**
	 * Open the preview window. The design must has been saved.
	 */
	private void openPreviewWindow() {

		if (pageLayout != null) {
			pmsUtil.openPagePreview(pageLoc);
		} else {
			MessageBox.alert(messages.headerErrorWindow(), pmsMessages.msgErrorTemplateNotSaved(), null).setModal(true);
		}

	}

	/**
	 * Removes the frames that haven't still been saved from the design panel. And adds them to the palette
	 */
	private void restoreDesign() {		
		
		// reset components panel
		PalettesPanel palette = Design.getInstance().getPalettesPanel();
		palette.getComponentsTabItem().remove(palette.getComponentsPanel());
		palette.setComponentsPanel(new ComponentsPanel(pageLayout));
				
		// reset design panel
		Design.getInstance().getDesignPanel().reset(pageLayout);
	}

	/**
	 * @return the page
	 */
	public PageTemplateDTO getPage() {
		return page;
	}

	/**
	 * @return the pageLoc
	 */
	public PageLoc getPageLoc() {
		return pageLoc;
	}

	/**
	 * @return the pageLayout
	 */
	public LayoutDTO getPageLayout() {
		return pageLayout;
	}

	/**
	 * Injects the Pages service proxy.
	 * @param pagesService
	 */
	@Inject
	public void setPagesService(IPagesServiceAsync pagesService) {
		this.pagesService = pagesService;
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
	 * Shared objects container
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the pages error message resolver.
	 * @param emrPages the emrPages to set
	 */
	@Inject
	public void setEmrPages(PageErrorMessageResolver emrPages) {
		this.emrPages = emrPages;
	}

	/**
	 * Injects the error processor
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
