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

package com.isotrol.impe3.pms.gui.client.widget.comment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Header;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.renderer.LongStringCellRenderer;
import com.isotrol.impe3.gui.common.renderer.SimpleDateTimeRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.widget.DateTimePickerWindow;
import com.isotrol.impe3.pms.gui.api.service.external.ICommentsExternalServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.CommentModelData;
import com.isotrol.impe3.pms.gui.client.error.ExternalServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.CommentsMessages;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;


/**
 * View for Comments Moderation and Management.<br/>
 * 
 * @author Manuel Ruiz
 * 
 */
public class CommentsManagement extends ContentPanel implements IInitializableWidget {

	/**
	 * Pagination size<br/>
	 */
	private static final int PAGE_SIZE = 20;

	/**
	 * Width in pixels for column 'Author'<br/>
	 */
	private static final int COLUMN_AUTHOR_WIDTH = 150;

	/**
	 * Width in pixels for column 'Date'<br/>
	 */
	private static final int COLUMN_DATE_WIDTH = 95;

	/**
	 * Width in pixels for column 'resource id'<br/>
	 */
	private static final int COLUMN_ID_RESOURCE_WIDTH = 30;

	/**
	 * Width in pixels for column 'resource'<br/>
	 */
	private static final int COLUMN_RESOURCE_WIDTH = 200;

	/**
	 * Width in pixels for column 'state'<br/>
	 */
	private static final int COLUMN_STATE_WIDTH = 65;

	/**
	 * Width in pixels for column 'moderated'<br/>
	 */
	private static final int COLUMN_MODERATED_WIDTH = 95;

	/**
	 * Border layout margins.<br/>
	 */
	private static final int MARGINS = 10;

	/**
	 * North panel size inf pixels.<br/>
	 */
	private static final float NORTH_PANEL_HEIGHT = 167;

	/**
	 * The service comments ID.<br/>
	 */
	private String commentsServiceId = null;

	/**
	 * Comments grid.<br/>
	 */
	private Grid<CommentModelData> grid = null;

	/**
	 * The grid page loader.<br/>
	 */
	private BasePagingLoader<PagingLoadResult<CommentModelData>> loader = null;

	/**
	 * Delete button<br/>
	 */
	private Button bDelete = null;

	/**
	 * 'Accept' button<br/>
	 */
	private Button bAccept = null;

	/**
	 * 'Reject' button<br/>
	 */
	private Button bReject = null;

	/**
	 * list for moderation errors in async service call
	 */
	private List<CommentModelData> moderationErrors = null;

	/**
	 * Container for comments filter.<br/>
	 */
	private ContentPanel cpTop = null;

	/**
	 * Container for repository summary info.<br/>
	 */
	private ContentPanel cpCenter = null;

	/**
	 * Id resource filter field
	 */
	private TextField<String> tbIdResource = null;

	private Radio cbNotModerated = null;

	private Radio cbAccepted = null;

	private Radio cbRejected = null;

	private Radio cbAll = null;

	private DateTimePickerWindow initDate = null;

	private DateTimePickerWindow endDate = null;

	/*
	 * Injected deps
	 */
	/**
	 * Renderer for date and time cells.<br/>
	 */
	private SimpleDateTimeRenderer dateTimeRenderer = null;

	/**
	 * Cell renderer for long strings<br/>
	 */
	private LongStringCellRenderer longStringCellRenderer = null;

	/**
	 * Cell renderer for long strings<br/>
	 */
	private InformationCellRenderer infoCellRenderer = null;

	/**
	 * comments service
	 */
	private ICommentsExternalServiceAsync commentsService = null;

	/**
	 * Comments messages bundle.<br/>
	 */
	private CommentsMessages commentsMessages = null;

	/**
	 * GUI Common messages bundle.<br/>
	 */
	private GuiCommonMessages guiCommonMessages = null;

	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttons = null;

	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;

	private boolean initialized = false;

	/**
	 * Error message resolver for {@link #nrService}.<br/>
	 */
	private ExternalServiceErrorMessageResolver emr = null;

	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Generic styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Form layout helper object<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget#init()
	 */
	public CommentsManagement init() {
		assert commentsServiceId != null : getClass().getName()
			+ "#init(): a comments service ID must be set before initialization!";
		initialized = true;

		initThis();

		initComponents();

		PagingLoadConfig firstPageConfig = new BasePagingLoadConfig(0, PAGE_SIZE);
		loader.load(firstPageConfig);

		return this;
	}

	/**
	 * Inits this component propeties.<br/>
	 */
	private void initThis() {
		setHeadingText(commentsMessages.phComments());
		setLayout(new BorderLayout());
		setBorders(false);
		setBodyBorder(false);
		Header header = getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {

		addCommentsFilter();
		addGrid();

		addToolBar();
	}

	/**
	 * Adds the top panel which contains the comments filter: resource, state, moderated, date...<br/>
	 */
	private void addCommentsFilter() {
		cpTop = new ContentPanel(formSupport.getStandardLayout(true));
		cpTop.setBorders(true);
		cpTop.setBodyBorder(false);
		cpTop.setHeadingText(commentsMessages.phCommentsFilter());
		cpTop.setBodyStyle("padding:10px");

		Header header = cpTop.getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());

		tbIdResource = new TextField<String>();
		tbIdResource.setFieldLabel(commentsMessages.labelResourceId());
		cpTop.add(tbIdResource);

		RadioGroup cbGroup = new RadioGroup();
		cbGroup.setOrientation(Orientation.HORIZONTAL);
		cbGroup.setFieldLabel(commentsMessages.labelState());
		cpTop.add(cbGroup);

		cbNotModerated = new Radio();
		cbNotModerated.setBoxLabel(commentsMessages.labelNotModerated());
		cbGroup.add(cbNotModerated);

		cbAccepted = new Radio();
		cbAccepted.setBoxLabel(commentsMessages.labelAccepted());
		cbGroup.add(cbAccepted);

		cbRejected = new Radio();
		cbRejected.setBoxLabel(commentsMessages.labelRejected());
		cbGroup.add(cbRejected);

		cbAll = new Radio();
		cbAll.setBoxLabel(commentsMessages.labelAll());
		cbGroup.add(cbAll);

		HorizontalPanel datesPanel = new HorizontalPanel();
		Html dateLabel = new Html("Rango de fechas:");
		dateLabel.setWidth(138);
		datesPanel.add(dateLabel);
		datesPanel.add(initDate);
		datesPanel.add(endDate);
		cpTop.add(datesPanel);

		ButtonBar bb = new ButtonBar();
		bb.setAlignment(HorizontalAlignment.RIGHT);
		cpTop.setBottomComponent(bb);

		bb.add(buttons.createSearchButton(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				loader.load();
			}
		}));

		bb.add(buttons.createGenericButton(commentsMessages.labelRestore(), "", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				restoreFilterFields();
			}
		}));

		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.NORTH);
		layoutData.setCollapsible(true);
		layoutData.setFloatable(false);
		layoutData.setSize(NORTH_PANEL_HEIGHT);
		layoutData.setMargins(new Margins(MARGINS, MARGINS, 0, MARGINS));
		add(cpTop, layoutData);
	}

	/**
	 * Creates and adds a grid of Comments to the widget.<br/>
	 */
	private void addGrid() {

		cpCenter = new ContentPanel(new FitLayout());
		cpCenter.setBorders(true);
		cpCenter.setBodyBorder(false);
		cpCenter.setHeaderVisible(false);

		CheckBoxSelectionModel<CommentModelData> sm = new CheckBoxSelectionModel<CommentModelData>();
		sm.setSelectionMode(SelectionMode.MULTI);
		ColumnConfig ccCheckbox = sm.getColumn();

		// Column 'resource id'
		ColumnConfig ccIdResource = new ColumnConfig(CommentModelData.PROPERTY_RESOURCE_ID, commentsMessages
			.chResourceId(), COLUMN_ID_RESOURCE_WIDTH);
		ccIdResource.setFixed(true);
		ccIdResource.setRenderer(infoCellRenderer);

		// Column 'resource'
		ColumnConfig ccResource = new ColumnConfig(CommentModelData.PROPERTY_RESOURCE, commentsMessages.chResource(),
			COLUMN_RESOURCE_WIDTH);

		// Column 'Date'
		ColumnConfig ccDate = new ColumnConfig(CommentModelData.PROPERTY_DATE, commentsMessages.chDate(),
			COLUMN_DATE_WIDTH);
		ccDate.setFixed(true);
		ccDate.setRenderer(dateTimeRenderer);

		// Column 'Author'
		ColumnConfig ccAuthor = new ColumnConfig(CommentModelData.PROPERTY_AUTHOR, commentsMessages.chAuthor(),
			COLUMN_AUTHOR_WIDTH);
		ccAuthor.setFixed(true);
		ccAuthor.setRenderer(longStringCellRenderer);

		// Column 'State'
		ColumnConfig ccState = new ColumnConfig(CommentModelData.PROPERTY_VALID, commentsMessages.chState(),
			COLUMN_STATE_WIDTH);
		ccState.setFixed(true);
		ccState.setRenderer(new GridCellRenderer<CommentModelData>() {
			public Object render(CommentModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<CommentModelData> store, Grid<CommentModelData> grid) {
				Boolean valid = model.get(property);
				Boolean moderated = model.getDTO().getLastModeration() != null;
				String msg = "";
				if (!moderated) {
					msg = commentsMessages.msgNotValidated();
				} else if (valid) {
					msg = commentsMessages.msgAccepted();
				} else if (!valid){
					msg = commentsMessages.msgRejected();
				}

				return msg;
			}
		});

		// Column 'Moderated'
		ColumnConfig ccModerated = new ColumnConfig(CommentModelData.PROPERTY_LAST_MODERATION, commentsMessages
			.chModerated(), COLUMN_MODERATED_WIDTH);
		ccModerated.setFixed(true);
		ccModerated.setRenderer(dateTimeRenderer);

		// Column 'Comment'
		ColumnConfig ccComment = new ColumnConfig(CommentModelData.PROPERTY_COMMENT, commentsMessages.chComment(), 500);
		ccComment.setRenderer(longStringCellRenderer);

		ColumnModel columnsModel = new ColumnModel(Arrays.asList(new ColumnConfig[] {ccCheckbox, ccIdResource,
			ccResource, ccDate, ccAuthor, ccState, ccModerated, ccComment}));

		RpcProxy<PagingLoadResult<CommentModelData>> proxy = new RpcProxy<PagingLoadResult<CommentModelData>>() {

			@Override
			protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<CommentModelData>> callback) {
				tryGetComments((PagingLoadConfig) loadConfig, callback);
			}
		};

		loader = new BasePagingLoader<PagingLoadResult<CommentModelData>>(proxy);

		PagingToolBar pagingToolBar = new PagingToolBar(PAGE_SIZE);
		pagingToolBar.bind(loader);
		cpCenter.setBottomComponent(pagingToolBar);

		ListStore<CommentModelData> store = new ListStore<CommentModelData>(loader);
		store.setSortField(CommentModelData.PROPERTY_DATE);
		store.setSortDir(SortDir.DESC);

		grid = new Grid<CommentModelData>(store, columnsModel);
		grid.setSelectionModel(sm);
		grid.getView().setForceFit(true);

		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<CommentModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<CommentModelData> se) {
				onGridSelectionChanged();
			}
		});

		cpCenter.add(grid);
		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.CENTER);
		layoutData.setMargins(new Margins(MARGINS));
		add(cpCenter, layoutData);
	}

	/**
	 * Retrieves the Comments from service.<br/>
	 * @param loadConfig
	 * @param callback
	 */
	private final void tryGetComments(PagingLoadConfig loadConfig,
		final AsyncCallback<PagingLoadResult<CommentModelData>> callback) {

		mask(commentsMessages.mskGetComments());

		AsyncCallback<PageDTO<CommentDTO>> serviceCallback = new AsyncCallback<PageDTO<CommentDTO>>() {

			public void onSuccess(PageDTO<CommentDTO> result) {
				unmask();

				List<CommentDTO> comments = result.getElements();
				List<CommentModelData> lResults = new LinkedList<CommentModelData>();
				for (CommentDTO comment : comments) {
					lResults.add(new CommentModelData(comment));
				}

				PagingLoadResult<CommentModelData> pagingLoadResult = new BasePagingLoadResult<CommentModelData>(
					lResults);
				pagingLoadResult.setOffset(result.getFirst());
				pagingLoadResult.setTotalLength(result.getTotal());

				callback.onSuccess(pagingLoadResult);
			}

			public void onFailure(Throwable caught) {
				unmask();

				getErrorProcessor().processError(caught, getErrorMessageResolver(),
					commentsMessages.msgErrorGetComments());
			}
		};

		commentsService.getResourceComments(commentsServiceId, getRetrievalFilter(loadConfig.getOffset(), loadConfig
			.getLimit()), serviceCallback);
	}

	/**
	 * Configures a base filter for retrieving Comments. This filter should be modified in subclasses for special
	 * retrieval purposes.<br/>
	 * @param offset
	 * @param limit
	 * @return the Comment filter used by the {@link #loader} for the retrieval of Comments of the current page.<br/>
	 * Default implementation configuration is: <ul> <li>Moderated - don't care</li> <li>Validated - don't care</li>
	 * </ul>
	 * 
	 */
	private CommentFilterDTO getRetrievalFilter(int offset, int limit) {
		CommentFilterDTO filterDto = new CommentFilterDTO();

		filterDto.setResourceKey(tbIdResource.getValue());
		filterDto.setPagination(new PaginationDTO(offset, limit));

		Boolean valid = null;
		Boolean moderated = null;

		if (cbAll.getValue() || (cbAccepted.getValue() && cbNotModerated.getValue() && cbRejected.getValue())) {
			valid = null;
			moderated = null;
		} else if (cbNotModerated.getValue()) {
			moderated = false;
		} else if (cbAccepted.getValue()) {
			valid = true;
		} else if (cbRejected.getValue()) {
			valid = false;
		}

		filterDto.setValid(valid);
		filterDto.setModerated(moderated);

		if (initDate.isRendered()) {
			filterDto.setLowDate(initDate.getDateValue());
		}

		if (endDate.isRendered()) {
			filterDto.setHighDate(endDate.getDateValue());
		}

		return filterDto;
	}

	private void restoreFilterFields() {
		tbIdResource.clear();
		cbAccepted.setValue(false);
		cbAll.setValue(false);
		cbNotModerated.setValue(false);
		cbRejected.setValue(false);
		initDate.clear();
		endDate.clear();
	}

	/**
	 * Fired when grid selection has changed.<br/>
	 */
	private void onGridSelectionChanged() {
		CommentModelData selected = getGrid().getSelectionModel().getSelectedItem();
		if (selected != null) {
			if (selected.getDTO().isValid()) {
				bAccept.setEnabled(false);
				bReject.setEnabled(true);
				bDelete.setEnabled(true);
			} else if (!selected.getDTO().isValid()) {
				bAccept.setEnabled(true);
				bReject.setEnabled(false);
				bDelete.setEnabled(true);
			}
		} else {
			bAccept.setEnabled(false);
			bReject.setEnabled(false);
			bDelete.setEnabled(false);
		}
	}

	private void addToolBar() {
		ToolBar toolbar = new ToolBar();

		// Accept:
		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				final List<CommentModelData> selected = getGrid().getSelectionModel().getSelectedItems();
				int n = selected.size();
				String msgConfirmAccept = null;
				if (n == 1) {
					msgConfirmAccept = getCommentsMessages().msgConfirmAcceptOneComment();
				} else if (n > 1) {
					msgConfirmAccept = getCommentsMessages().msgConfirmAcceptManyComments();
				}
				final String msgConfirm = msgConfirmAccept;
				if (!selected.isEmpty()) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							Button clicked = be.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								tryModerateComments(selected, true);
							}
						}
					};
					MessageBox.confirm(getGuiCommonMessages().headerConfirmWindow(), msgConfirm, lConfirm).setModal(
						true);
				}
			};
		};
		bAccept = getButtons().addAcceptButton(toolbar, lAccept);
		bAccept.setToolTip(getCommentsMessages().ttValidateComments());
		bAccept.disable();

		// Reject:
		SelectionListener<ButtonEvent> lReject = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final List<CommentModelData> selected = getGrid().getSelectionModel().getSelectedItems();
				int n = selected.size();
				String msgConfirmReject = null;
				if (n == 1) {
					msgConfirmReject = getCommentsMessages().msgConfirmRejectOneComment();
				} else if (n > 1) {
					msgConfirmReject = getCommentsMessages().msgConfirmRejectManyComments();
				}
				if (!selected.isEmpty()) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							Button clicked = be.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								tryModerateComments(selected, false);
							}
						}
					};
					MessageBox.confirm(getGuiCommonMessages().headerConfirmWindow(), msgConfirmReject, lConfirm)
						.setModal(true);
				}
			}
		};
		bReject = getButtons().addRejectButton(toolbar, lReject);
		bReject.setToolTip(getCommentsMessages().ttDeleteComments());
		bReject.disable();

		// Remove:
		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final List<CommentModelData> selected = getGrid().getSelectionModel().getSelectedItems();
				int n = selected.size();
				String msgConfirmReject = null;
				if (n == 1) {
					msgConfirmReject = getCommentsMessages().msgConfirmRejectOneComment();
				} else if (n > 1) {
					msgConfirmReject = getCommentsMessages().msgConfirmRejectManyComments();
				}
				final String msgConfirm = msgConfirmReject;
				if (!selected.isEmpty()) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							tryDeleteComments(selected);
						}
					};
					MessageBox.confirm(getGuiCommonMessages().headerConfirmWindow(), msgConfirm, lConfirm).setModal(
						true);
				}
			}
		};
		bDelete = getButtons().addDeleteButton(toolbar, lDelete, getCommentsMessages().ttDeleteComments());
		bDelete.disable();

		toolbar.add(new FillToolItem());

		// refresh button
		buttons.addRefreshButton(toolbar, new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				loader.load();
			}
		});

		cpCenter.setTopComponent(toolbar);
	}

	/**
	 * <br/>
	 * @param selected
	 * @param accept
	 */
	private void tryModerateComments(List<CommentModelData> selected, boolean accept) {
		moderationErrors = new ArrayList<CommentModelData>();
		mask(getCommentsMessages().mskModerateComments());

		for (int i = 0; i < selected.size(); i++) {
			moderateComment(selected.get(i), accept, i, selected.size());
		}
	}

	/**
	 * <br/>
	 * @param size
	 * @param current
	 * @param selected
	 */
	private void moderateComment(final CommentModelData commentModelData, boolean accept, final int current,
		final int size) {

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				completeModeration(current, size);
			}

			public void onFailure(Throwable caught) {
				moderationErrors.add(commentModelData);
				completeModeration(current, size);
			}
		};

		getCommentsService().moderate(commentsServiceId, commentModelData.getDTO().getId(), null, accept, callback);
	}

	/**
	 * <br/>
	 * @param current
	 * @param size
	 */
	private void completeModeration(int current, int size) {

		if (current == size - 1) {
			unmask();
			getGrid().getStore().getLoader().load();

			if (!moderationErrors.isEmpty()) {
				MessageBox.alert(getGuiCommonMessages().headerErrorWindow(),
					getCommentsMessages().msgErrorModerateComment(), null).setModal(true);
			}
		}
	}

	/**
	 * Deletes the passed comments.<br/>
	 * 
	 * @param selected
	 */
	private void tryDeleteComments(List<CommentModelData> selected) {
		AsyncCallback<Void> callback = new MassDeleteCallback(selected.size());

		for (int i = 0; i < selected.size(); i++) {
			getCommentsService().delete(commentsServiceId, selected.get(i).getDTO().getId(), callback);
		}
	}

	/**
	 * Callback for processing a group of asynchronous calls. Manages the number of operations sucessfully finished, and
	 * the failed ones.<br/> When all operations have finished, displays info popups.
	 * 
	 * @author Andrei Cojocaru
	 * 
	 */
	private class MassDeleteCallback implements AsyncCallback<Void> {

		/**
		 * Initial number of atomic operations.<br/>
		 */
		private int initial = 0;

		/**
		 * Number of operations left<br/>
		 */
		private int left = 0;

		/**
		 * Number of operations failed.<br/>
		 */
		private int failed = 0;

		/**
		 * <br/>
		 * @param number
		 */
		public MassDeleteCallback(int number) {
			this.initial = number;
			this.left = number;
			this.failed = number;
		}

		public void onFailure(Throwable caught) {
			left--;
			maybeFinish();
		}

		public void onSuccess(Void result) {
			left--;
			failed--;
			maybeFinish();
		}

		/**
		 * Finish batch operation routine.<br/>
		 */
		private void maybeFinish() {
			if (left > 0) { // do nothing
				return;
			}

			unmask();
			switch (failed) {
				case 0: // all operations OK
					String msg;
					if (initial == 1) {
						msg = getCommentsMessages().msgOkDeleteOneComment();
					} else {
						msg = getCommentsMessages().msgOkDeleteManyComments();
					}
					getUtil().info(msg);
					break;
				case 1: // one failed
					msg = getCommentsMessages().msgErrorDeleteOneComment();
					getUtil().error(msg);
					break;
				default: // many failed
					msg = getCommentsMessages().msgErrorDeleteManyComments(Long.toString(failed),
						Long.toString(initial));
					getUtil().error(msg);
			}
			// reload if some success:
			if (failed < initial) {
				getGrid().getStore().getLoader().load();
			}
		}
	}

	/**
	 * @param resourceId the commentsServiceId to set
	 */
	public void setCommentsServiceId(String commentsServiceId) {
		this.commentsServiceId = commentsServiceId;
	}

	/**
	 * Injects the GRM messages bundle.<br/>
	 * @param messages
	 */
	@Inject
	protected final void setMessages(CommentsMessages messages) {
		this.commentsMessages = messages;
	}

	/**
	 * Injects the buttons helper.<br/>
	 * @param buttons
	 */
	@Inject
	protected final void setButtons(Buttons buttons) {
		this.buttons = buttons;
	}

	/**
	 * Injects the long string cell renderer.
	 * @param longStringCellRenderer the longStringCellRenderer to set
	 */
	@Inject
	public void setLongStringCellRenderer(LongStringCellRenderer longStringCellRenderer) {
		this.longStringCellRenderer = longStringCellRenderer;
	}

	/**
	 * Injects the renderer for date and time
	 * @param dateTimeRenderer the dateTimeRenderer to set
	 */
	@Inject
	public void setDateTimeRenderer(SimpleDateTimeRenderer dateTimeRenderer) {
		this.dateTimeRenderer = dateTimeRenderer;
	}

	/**
	 * Injects the GUI Common messages bundle.
	 * @param guiCommonMessages the guiCommonMessages to set
	 */
	@Inject
	public void setGuiCommonMessages(GuiCommonMessages guiCommonMessages) {
		this.guiCommonMessages = guiCommonMessages;
	}

	/**
	 * @param commentsService the commentsService to set
	 */
	@Inject
	public void setCommentsService(ICommentsExternalServiceAsync commentsService) {
		this.commentsService = commentsService;
	}

	/**
	 * Injects the utilities object.<br/>
	 * @param util
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * <br/>
	 * @return the utilities object.
	 */
	protected final Util getUtil() {
		return util;
	}

	/**
	 * @return the resourceId
	 */
	protected final String getCommentsServiceId() {
		return commentsServiceId;
	}

	/**
	 * @return the grid
	 */
	protected final Grid<CommentModelData> getGrid() {
		return grid;
	}

	/**
	 * @return the commentsService
	 */
	protected final ICommentsExternalServiceAsync getCommentsService() {
		return commentsService;
	}

	/**
	 * @return the messages
	 */
	protected final CommentsMessages getCommentsMessages() {
		return commentsMessages;
	}

	/**
	 * @return the guiCommonMessages
	 */
	protected final GuiCommonMessages getGuiCommonMessages() {
		return guiCommonMessages;
	}

	/**
	 * @return the buttons
	 */
	protected final Buttons getButtons() {
		return buttons;
	}

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Injects the error message resolver.
	 * @param emr the emrNr to set
	 */
	@Inject
	public void setEmrNr(ExternalServiceErrorMessageResolver emr) {
		this.emr = emr;
	}

	/**
	 * @return the emr
	 */
	protected ExternalServiceErrorMessageResolver getErrorMessageResolver() {
		return emr;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service errors processor.
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * Injects the generic styles bundle.
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @param infoCellRenderer the infoCellRenderer to set
	 */
	@Inject
	public void setInfoCellRenderer(InformationCellRenderer infoCellRenderer) {
		this.infoCellRenderer = infoCellRenderer;
	}

	/**
	 * @param formSupport the formSupport to set
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * @param initDate the initDate to set
	 */
	@Inject
	public void setInitDate(DateTimePickerWindow initDate) {
		this.initDate = initDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	@Inject
	public void setEndDate(DateTimePickerWindow endDate) {
		this.endDate = endDate;
	}
}
