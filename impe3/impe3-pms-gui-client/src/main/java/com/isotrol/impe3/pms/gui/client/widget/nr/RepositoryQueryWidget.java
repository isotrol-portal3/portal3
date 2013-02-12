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
package com.isotrol.impe3.pms.gui.client.widget.nr;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Header;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.esvc.nr.NodeDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodeRepositoryExternalService;
import com.isotrol.impe3.pms.api.esvc.nr.NodesFilterDTO;
import com.isotrol.impe3.pms.api.esvc.nr.ResultDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.NodeModelData;

/**
 * Widget that manages the queries to the repository.
 * 
 * @author Andrei Cojocaru
 *
 */
public class RepositoryQueryWidget extends ARepositoryWidget {

	/**
	 * Height of the North panel, in pixels.<br/>
	 */
	private static final int NORTH_PANEL_HEIGHT = 134;

	/**
	 * Margin between the panels and the borders<br/>
	 */
	private static final int MARGINS = 10;
	
	/**
	 * Index date formatter.<br/>
	 */
	private DateTimeFormat indexDateFormat = null;
	/**
	 * Human-readable date formatter.<br/>
	 */
	private DateTimeFormat humanDateFormat = null;
	
	/**
	 * Width of the grid column "Description"<br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 500;

	/**
	 * Width of the grid column "Date"<br/>
	 */
	private static final int COLUMN_DATE_WIDTH = 200;
	
	/**
	 * Width of the grid column "Title"<br/>
	 */
	private static final int COLUMN_TITLE_WIDTH = 300;
	
	/**
	 * Width of the grid column <b>ID</b>, in pixels.<br/>
	 */
	private static final int COLUMN_ID_WIDTH = 210;
	
	/**
	 * Results grid.<br/>
	 */
	private Grid<NodeModelData> grid = null;
	
	/**
	 * Filter for the {@link #grid} elements.<br/>
	 */
	private CustomizableStoreFilter<NodeModelData> filter = null;

	
	/**
	 * Contains the query params<br/>
	 */
	private ContentPanel cpQuery = null;
	
	/**
	 * Contains the query results.<br/>
	 */
	private ContentPanel cpResults = null;

	/**
	 * Input for the query words.<br/>
	 */
	private TextField<String> tfWords = null;

	/**
	 * Content Type selector<br/>
	 */
	private ComboBox<ContentTypeSelModelData> cbContentTypes = null;
	
	/**
	 * "Search" button.<br/>
	 */
	private Button bSearch = null;
	
	/**
	 * The queryFilter used in the last query.<br/>
	 */
	private NodesFilterDTO queryFilter = new NodesFilterDTO();
	
	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;
	
	/**
	 * Suuport object for from layed out elements.<br/>
	 */
	private FormSupport formSupport = null;
	
	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Generic styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;
	
	/**
	 * Default constructor.
	 */
	public RepositoryQueryWidget() {}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.nr.ARepositoryWidget#init()
	 */
	/**
	 * <br/>
	 */
	public RepositoryQueryWidget init() {
		super.init();
		
		this.indexDateFormat = DateTimeFormat.getFormat(messages.indexDateFormat());
		this.humanDateFormat = DateTimeFormat.getFormat(messages.humanDateFormat());
		
		initThis();
		addQueryPanel();
		addResultsPanel();
		addHeaderTools();
		
		tryGetContentTypesAndNodes();
		
		return this;
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setLayout(new BorderLayout());
		setBorders(false);
		setBodyBorder(false);
		setHeading(getNrMessages().menuItem2RepositoryQuery());
		
		Header header = getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());
	}

	/**
	 * Creates and adds the query panel.<br/>
	 */
	private void addQueryPanel() {
		
		cpQuery = new ContentPanel();
//		cpQuery.setAutoHeight(true);
		cpQuery.setBorders(true);
		cpQuery.setBodyBorder(false);
		cpQuery.setLayout(formSupport.getStandardLayout(true));
		cpQuery.setButtonAlign(HorizontalAlignment.LEFT);
		cpQuery.setHeading(getNrMessages().headerQueryParams());
		cpQuery.setBodyStyle("padding:10px");
		
		Header header = cpQuery.getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());

		tfWords = new TextField<String>();
		tfWords.setAllowBlank(true);
		tfWords.setFieldLabel(getNrMessages().labelSearchWords());
		tfWords.addKeyListener(new KeyListener() {
			/* (non-Javadoc)
			 * @see com.extjs.gxt.ui.client.event.KeyListener#componentKeyUp(com.extjs.gxt.ui.client.event.ComponentEvent)
			 */
			/**
			 * Performs the search when ENTER is pressed.<br/>
			 */
			@Override
			public void componentKeyUp(ComponentEvent event) {
				if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
					tryGetNodes(true);
				}
			}
		});
		cpQuery.add(tfWords);
		
		/*
		 * HorizontalPanel w/ Content Types combo & trash can to deselect
		 */
		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(cpQuery, false);
		
		cbContentTypes = new ComboBox<ContentTypeSelModelData>();
		cbContentTypes.setEditable(false);
		cbContentTypes.setFieldLabel(getNrMessages().labelContentType());
		cbContentTypes.setDisplayField(ContentTypeSelModelData.PROPERTY_NAME);
		ListStore<ContentTypeSelModelData> cbStore = new ListStore<ContentTypeSelModelData>();
		cbContentTypes.setStore(cbStore);
		lr[0].add(cbContentTypes);

		buttonsSupport.addTrashIconButton(lr[1], new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				cbContentTypes.setValue(null);
			}
		});
		
		// Search button:
		SelectionListener<ButtonEvent> searchListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				tryGetNodes(true);
			}
		};
		bSearch = new Button(messages.labelSearch(), searchListener);
		cpQuery.addButton(bSearch);
		
		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.NORTH);
		layoutData.setCollapsible(true);
		layoutData.setFloatable(false);
		layoutData.setSize(NORTH_PANEL_HEIGHT);
		layoutData.setMargins(new Margins(MARGINS, MARGINS, 0, MARGINS));
		add(cpQuery, layoutData);
	}
	
	/**
	 * Creates and adds the results panel.<br/>
	 */
	private void addResultsPanel() {
		cpResults = new ContentPanel(new FitLayout());
		cpResults.setBorders(true);
		cpResults.setBodyBorder(false);
		cpResults.setHeading(getNrMessages().headerResults());
		
		Header header = cpResults.getHeader();
		header.addStyleName(styles.noSideBorders());
		header.addStyleName(styles.noTopBorders());
		
		List<ColumnConfig> lColumns = new LinkedList<ColumnConfig>();
		
		ColumnConfig ccId = new ColumnConfig(
				NodeModelData.PROPERTY_ID, 
				getNrMessages().headerNodeId(), 
				COLUMN_ID_WIDTH);
		lColumns.add(ccId);
		
		ColumnConfig ccTitle = new ColumnConfig(
				NodeModelData.PROPERTY_TITLE,
				getNrMessages().headerColumnTitle(),
				COLUMN_TITLE_WIDTH);
		lColumns.add(ccTitle);
		
		ColumnConfig ccDesc = new ColumnConfig(
				NodeModelData.PROPERTY_DESCRIPTION,
				getNrMessages().headerColumnDescription(),
				COLUMN_DESCRIPTION_WIDTH);
		lColumns.add(ccDesc);
		
		ColumnConfig ccDate = new ColumnConfig(
				NodeModelData.PROPERTY_DATE,
				getNrMessages().headerColumnDate(),
				COLUMN_DATE_WIDTH);
		ccDate.setRenderer(new GridCellRenderer<NodeModelData>() {
			public Object render(NodeModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<NodeModelData> store, Grid<NodeModelData> g) {
				String dateTimeString = model.getDTO().getDate();
				String res = "";
				if (dateTimeString == null || dateTimeString.equals("")) {
					res = "???";
				} else {
					res = humanDateFormat.format(indexDateFormat.parse(dateTimeString));
				}
				return res;
			}
		});
		lColumns.add(ccDate);
		
		ColumnModel columnModel = new ColumnModel(lColumns);
		
		ListStore<NodeModelData> store = new ListStore<NodeModelData>();
		
		grid = new Grid<NodeModelData>(store, columnModel);
		
		GridView gridView = grid.getView();
		gridView.setAutoFill(true);
		gridView.setForceFit(true);
		
		grid.setAutoExpandColumn(NodeModelData.PROPERTY_TITLE);
		
		cpResults.add(grid);
		
		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.CENTER);
		layoutData.setMargins(new Margins(MARGINS));
		add(cpResults, layoutData);
	}
	
	/**
	 * Adds a filter and a "Refresh" button to the panel header.<br/>
	 */
	private void addHeaderTools() {
		filter = new CustomizableStoreFilter<NodeModelData>(
				Arrays.asList(new String [] {NodeModelData.PROPERTY_TITLE, NodeModelData.PROPERTY_DESCRIPTION}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		cpResults.getHeader().addTool(filter);
		filter.bind(grid.getStore());

		// "Refresh" button:
		SelectionListener<IconButtonEvent> refreshListener = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetNodes(false);
			}
		};
		buttonsSupport.addRefreshButton(cpResults, refreshListener);
	}
	
	/**
	 * Fills the {@link #grid} with the passed query results.<br/>
	 * @param result
	 */
	private void populateGrid(ResultDTO<NodeDTO> result) {
		List<NodeModelData> models = new LinkedList<NodeModelData>();
		for (NodeDTO dto : result.getResults()) {
			models.add(new NodeModelData(dto));
		}
		
		filter.setValue(null);
		
		ListStore<NodeModelData> store = grid.getStore();
		store.removeAll();
		store.add(models);
	}
	
	/**
	 * Populates the combo store with the passed values.<br/>
	 * @param combos
	 */
	private void populateComboStore(List<ContentTypeSelDTO> dtos) {
		
		List<ContentTypeSelModelData> models = new LinkedList<ContentTypeSelModelData>();
		for (ContentTypeSelDTO ctsDto : dtos) {
			models.add(new ContentTypeSelModelData(ctsDto));
		}
		
		ListStore<ContentTypeSelModelData> store = cbContentTypes.getStore();
		store.removeAll();
		store.add(models);
	}
	
	/**
	 * Calls getNodes() on the remote service, with the current queryFilter settings 
	 * ({@link #tfWords} value for query words, and {@link #cbContentTypes} value for the Content Type).<br/>
	 * @param newQuery if <code>true</code>, applies the input values. 
	 * If <code>false</code>, uses the last queryFilter 
	 */
	private void tryGetNodes(boolean newQuery) {
		getUtil().mask(getNrMessages().mskSearch());
		
		AsyncCallback<ResultDTO<NodeDTO>> callback = new AsyncCallback<ResultDTO<NodeDTO>>() {
			public void onSuccess(ResultDTO<NodeDTO> arg0) {
				populateGrid(arg0);
				getUtil().unmask();
			}
			
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(
						arg0, 
						getNrErrorMessageResolver(), 
						getNrMessages().msgErrorGetNodes());
			}
		};
		
		if (newQuery) {
			queryFilter = new NodesFilterDTO();
			String words = tfWords.getValue();
			if (!Util.emptyString(words)) {
				queryFilter.setQuery(words);
			}
			ContentTypeSelModelData cts = cbContentTypes.getValue();
			if (cts != null) {
				queryFilter.setContentTypeId(cts.getDTO().getId());
			}
		}
		
		getNrService().getNodes(getRepositoryId(), queryFilter, callback);
	}
	
	/**
	 * Calls {@link NodeRepositoryExternalService#getContentTypes(String)}<br/>
	 * On success, populates the {@link #cbContentTypes Content Types combo} store.
	 * On failure, shows an alert message.
	 */
	private void tryGetContentTypesAndNodes() {
		getUtil().mask(getNrMessages().mskSearch());
		
		AsyncCallback<List<ContentTypeSelDTO>> callback = new AsyncCallback<List<ContentTypeSelDTO>>() {
			public void onSuccess(List<ContentTypeSelDTO> arg0) {
				populateComboStore(arg0);
				tryGetNodes(false);
			}
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(
						arg0, 
						getNrErrorMessageResolver(), 
						getNrMessages().msgErrorGetContentTypes());
			}
		};

		getNrService().getContentTypes(getRepositoryId(), callback);
	}

	/**
	 * Injects the button helper<br/>
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the form helper object.<br/>
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * Injects the generic messages bundle.<br/>
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}
	
	/**
	 * Injects the generic styles bundle.
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}
}
