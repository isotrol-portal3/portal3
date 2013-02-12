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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.infarchitecture;

import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.databinding.ADataBoundContentPanel;
import com.isotrol.impe3.pms.api.portal.PortalIATemplateDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.ContentTypesController;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;

/**
 * Represents the Portal Content Types Management.
 * 
 * @author Manuel Ruiz
 * 
 */
public class ContentTypesPanel extends ADataBoundContentPanel<PortalIATemplateDTO> {

	/**
	 * Width for <b>name</b> column, in pixels.<br/>
	 */
	private static final int COLUMNNAMEWIDTH = 160;
	/**
	 * Width for <b>description</b> column, in pixels.<br/>
	 */
	private static final int COLUMNDESCRIPTIONWIDTH = 300;

	/**
	 * Grid height in pixels.<br/>
	 */
	private static final int GRID_HEIGHT = 300;

	/**
	 * Content Types grid.<br/>
	 */
	private Grid<ContentTypeSelModelData> grid = null;
	
	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * Content Types async service proxy<br/>
	 */
	private IContentTypesServiceAsync contentTypesService = null;
	
	/**
	 * Constructor
	 */
	public ContentTypesPanel(PortalIATemplateDTO portalTemplate) {
		super(portalTemplate);
		initComponent();
		initController();
		displayBoundDataValues();
	}

	/**
	 * Inits the controller reference.<br/>
	 */
	private void initController() {

		ContentTypesController ctController = (ContentTypesController) contentTypesService;
		
		ctController.addChangeListener(new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					switch(event.getType()) {
					case PmsChangeEvent.ADD:
						ContentTypeDTO ctDto = event.getEventInfo();
						
						// TODO ContentTypeDTO -> ContentTypeSelDTO
						ContentTypeSelDTO ctSelDto = new ContentTypeSelDTO();
				        ctSelDto.setId(getId());
				        ctSelDto.setState(ctDto.getState());
				        ctSelDto.setRoutable(ctDto.isRoutable());
				        ctSelDto.setDescription(ctDto.getDescription());
				        ctSelDto.setName(ctDto.getDefaultName().getDisplayName());
						
						grid.getStore().add(new ContentTypeSelModelData(ctSelDto));
						break;
					case PmsChangeEvent.DELETE:
						PmsChangeEvent pmsEvent = (PmsChangeEvent) event;
						String removedId = pmsEvent.getEventInfo();
						
						ListStore<ContentTypeSelModelData> store = grid.getStore();
						ContentTypeSelModelData ctModelData = store.findModel(ContentTypeSelModelData.PROPERTY_ID, removedId);
						if(ctModelData != null) {
							store.remove(ctModelData);
						}
						break;
					case PmsChangeEvent.UPDATE:
						ctDto = event.getEventInfo();
						
						String id = ctDto.getId();
						store = grid.getStore();
						ctModelData = store.findModel(ContentTypeSelModelData.PROPERTY_ID,id);
						
						if(ctModelData != null) {
							// TODO ContentTypeDTO -> ContentTypeSelDTO
							ctSelDto = ctModelData.getDTO();
							ctSelDto.setId(ctDto.getId());
							ctSelDto.setState(ctDto.getState());
							ctSelDto.setDescription(ctDto.getDescription());
							ctSelDto.setName(ctDto.getDefaultName().getDisplayName());
							
							store.update(ctModelData);
						}
						break;
					default:
						// nothin'
					}
				}
			}
		});
	}

//	/**
//	 * Requests for the Content Types to service, and refreshes the tree store with the result data.<br/>
//	 */
//	private void tryGetContentTypes() {
//		
//		AsyncCallback<List<ContentTypeSelDTO>> callback = new AsyncCallback<List<ContentTypeSelDTO>>() {
//			public void onFailure(Throwable arg0) {
//				MessageBox.alert(Labels.WINDOW_HEADER_ERROR,
//						"No ha sido posible extraer los tipos de contenido: " + arg0.getLocalizedMessage(),
//						null).setModal(true);
//			}
//			public void onSuccess(List<ContentTypeSelDTO> dtos) {
//				ListStore<ContentTypeSelModelData> store = grid.getStore();
//				store.removeAll();
//				for(ContentTypeSelDTO dto : dtos) {
//					store.add(new ContentTypeSelModelData(dto));
//				}
//			}
//		};
//		PmsSettings settings = PmsSettings.getInstance();
//		IContentTypesServiceAsync ctService = Registry.get(settings.contentTypesServiceKey());
//		ctService.getContentTypes(callback);
//	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {
		setWidth(Constants.HUNDRED_PERCENT);
		setLayout(new FitLayout());
		
		setHeading(pmsMessages.headerContentTypeManagement());
		setHeaderVisible(true);
		
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		CheckBoxSelectionModel<ContentTypeSelModelData> selectionModel = 
			new CheckBoxSelectionModel<ContentTypeSelModelData>();
		configs.add(selectionModel.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId(ContentTypeSelModelData.PROPERTY_NAME);
		column.setHeader(pmsMessages.columnHeaderName());
		column.setWidth(COLUMNNAMEWIDTH);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId(ContentTypeSelModelData.PROPERTY_DESCRIPTION);
		column.setHeader(pmsMessages.columnHeaderDescription());
		column.setWidth(COLUMNDESCRIPTIONWIDTH);
		configs.add(column);

		ListStore<ContentTypeSelModelData> store = new ListStore<ContentTypeSelModelData>();

		ColumnModel cm = new ColumnModel(configs);

		grid = new EditorGrid<ContentTypeSelModelData>(store, cm);
		grid.setSelectionModel(selectionModel);
		grid.addPlugin(selectionModel);
		grid.setHeight(GRID_HEIGHT);
		grid.getView().setForceFit(true);
		grid.setAutoExpandColumn(ContentTypeSelModelData.PROPERTY_DESCRIPTION);
		
		add(grid);
	}
	
	private void displayBoundDataValues() {
		// TODO Auto-generated method stub
		
	}

}
