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

package com.isotrol.impe3.pms.gui.client.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.BasesDTO;
import com.isotrol.impe3.pms.api.portal.PortalCacheDTO;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalIATemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalParentDTO;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.api.portal.PortalURLsDTO;
import com.isotrol.impe3.pms.api.portal.PropertiesDTO;
import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;


/**
 * Wrapper for the Portals async service with events capabilities
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class PortalsController extends ChangeEventSupport implements IPortalsServiceAsync {

	/**
	 * Async service that does the real work.<br/>
	 */
	private IPortalsServiceAsync service = null;

	/**
	 * Constructor with the real service passed as param.<br/>
	 * @param service
	 */
	public PortalsController(IPortalsServiceAsync service) {
		this.service = service;
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync
	 * #getPortals(com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getPortals(AsyncCallback<PortalTreeDTO> callback) {
		service.getPortals(callback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #get(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void get(String id, AsyncCallback<PortalTemplateDTO> callback) {
		service.get(id, callback);
	}

	/**
	 * Saves the passed template and fires an event of type <i>Add</i> (new item, with no ID) or <i>Update</i> (already
	 * existing item). The resulting portal template can be accessed by calling {@link PmsChangeEvent#getEventInfo()}
	 * <br/>
	 * 
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #save(com.isotrol.impe3.pms.api.portal.PortalDTO,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void save(final PortalDTO dto, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				int type = PmsChangeEvent.ADD;
				if (dto.getId() != null) {
					type = PmsChangeEvent.UPDATE;
				}
				PmsChangeEvent event = new PmsChangeEvent(type, dto);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};

		service.save(dto, realCallback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #getIA(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getIA(String id, AsyncCallback<PortalIATemplateDTO> callback) {
		service.getIA(id, callback);
	}

	/**
	 * Deletes the portal with the passed ID and fires a {@link ChangeEventSource#Remove} event. The resulting portal
	 * tree can be accessed by calling {@link PmsChangeEvent#getEventInfo()}.<br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #delete(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void delete(final String id, final AsyncCallback<PortalTreeDTO> callback) {
		AsyncCallback<PortalTreeDTO> realCallback = new AsyncCallback<PortalTreeDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(PortalTreeDTO portalTree) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.DELETE, portalTree);
				PortalsController.this.notify(event);
				callback.onSuccess(portalTree);
			}
		};

		service.delete(id, realCallback);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #getBases(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getBases(String portalId, AsyncCallback<BasesDTO> callback) {
		service.getBases(portalId, callback);
	}

	/**
	 * Sets the passed bases for the portal with the passed ID, and fires a {@link ChangeEventSource#Update} event.<br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #setBases(java.lang.String, java.util.List,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void setBases(final String portalId, List<BaseDTO> bases, final AsyncCallback<BasesDTO> callback) {
		AsyncCallback<BasesDTO> realCallback = new AsyncCallback<BasesDTO>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(BasesDTO bases) {
				Map<String, Object> info = new HashMap<String, Object>();
				info.put(PmsConstants.BASES_LIST, bases);
				info.put(PmsConstants.PORTAL_ID, portalId);

				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE_BASES, info);
				PortalsController.this.notify(event);
				callback.onSuccess(bases);
			}
		};

		service.setBases(portalId, bases, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #getProperties(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getProperties(String portalId, AsyncCallback<PropertiesDTO> callback) {
		// readonly method: let's forward the callback
		service.getProperties(portalId, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #setProperties(java.lang.String, java.util.List,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void setProperties(final String portalId, final List<PropertyDTO> properties,
		final AsyncCallback<PropertiesDTO> callback) {
		AsyncCallback<PropertiesDTO> realCallback = new AsyncCallback<PropertiesDTO>() {
			public void onSuccess(PropertiesDTO arg0) {
				Map<String, Object> info = new HashMap<String, Object>();
				info.put(PmsConstants.PROPERTIES_LIST, arg0);
				info.put(PmsConstants.PORTAL_ID, portalId);

				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE_PROPERTIES, info);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}

			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}
		};

		service.setProperties(portalId, properties, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getOfflineURL(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getOfflineURL(String portalId, AsyncCallback<String> callback) {

		service.getOfflineURL(portalId, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync #getOnlineURL(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getOnlineURL(String portalId, AsyncCallback<String> callback) {
		service.getOnlineURL(portalId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#isOfflineReady(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void isOfflineReady(String portalId, AsyncCallback<Boolean> callback) {
		service.isOfflineReady(portalId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getName(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getName(String id, AsyncCallback<PortalNameDTO> callback) {
		service.getName(id, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#setName(com.isotrol.impe3.pms.api.portal.PortalNameDTO,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void setName(final PortalNameDTO dto, final AsyncCallback<Void> callback) {

		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE_PORTAL_NAME, dto);
				PortalsController.this.notify(event);
				callback.onSuccess(result);
			}

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		};

		service.setName(dto, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getParent(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getParent(String id, final AsyncCallback<PortalParentDTO> callback) {
		service.getParent(id, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#setParent(java.lang.String, java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void setParent(String id, String parentId, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE, arg0);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.setParent(id, parentId, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#create(com.isotrol.impe3.pms.api.portal.PortalNameDTO,
	 * java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void create(PortalNameDTO dto, String parentId, final AsyncCallback<String> callback) {

		AsyncCallback<String> realCallback = new AsyncCallback<String>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(String id) {
				int type = PmsChangeEvent.ADD;
				PmsChangeEvent event = new PmsChangeEvent(type, id);
				PortalsController.this.notify(event);
				callback.onSuccess(id);
			}
		};

		service.create(dto, parentId, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getAvailableBases(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getAvailableBases(String portalId, AsyncCallback<List<BaseDTO>> callback) {
		service.getAvailableBases(portalId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getAvailableProperties(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getAvailableProperties(String portalId, AsyncCallback<List<PropertyDTO>> callback) {
		service.getAvailableProperties(portalId, callback);
	}

	@Override
	public void getPortalConfigurations(String portalId, AsyncCallback<List<PortalConfigurationSelDTO>> callback) {
		service.getPortalConfigurations(portalId, callback);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#clearSetFilters(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void clearSetFilters(String portalId, final AsyncCallback<List<SetFilterDTO>> callback) {
		AsyncCallback<List<SetFilterDTO>> realCallback = new AsyncCallback<List<SetFilterDTO>>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(List<SetFilterDTO> setFilter) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE_SET_FILTERS, setFilter);
				PortalsController.this.notify(event);
				callback.onSuccess(setFilter);
			}
		};
		service.clearSetFilters(portalId, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getSetFilters(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getSetFilters(String portalId, AsyncCallback<List<SetFilterDTO>> callback) {
		service.getSetFilters(portalId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#putSetFilter(java.lang.String,
	 * com.isotrol.impe3.pms.api.portal.SetFilterDTO, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void putSetFilter(String portalId, SetFilterDTO filter, final AsyncCallback<List<SetFilterDTO>> callback) {
		AsyncCallback<List<SetFilterDTO>> realCallback = new AsyncCallback<List<SetFilterDTO>>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(List<SetFilterDTO> setFilter) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE_SET_FILTERS, setFilter);
				PortalsController.this.notify(event);
				callback.onSuccess(setFilter);
			}
		};
		service.putSetFilter(portalId, filter, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#removeSetFilter(java.lang.String,
	 * java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void removeSetFilter(String portalId, String filter, final AsyncCallback<List<SetFilterDTO>> callback) {
		AsyncCallback<List<SetFilterDTO>> realCallback = new AsyncCallback<List<SetFilterDTO>>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(List<SetFilterDTO> setFilter) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE_SET_FILTERS, setFilter);
				PortalsController.this.notify(event);
				callback.onSuccess(setFilter);
			}
		};
		service.removeSetFilter(portalId, filter, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getURLs(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getURLs(String portalId, AsyncCallback<PortalURLsDTO> callback) {
		service.getURLs(portalId, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#exportBases(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportBases(String id, AsyncCallback<String> callback) {
		service.exportBases(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#exportConfig(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportConfig(String id, AsyncCallback<String> callback) {
		service.exportConfig(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#exportName(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportName(String id, AsyncCallback<String> callback) {
		service.exportName(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#exportProperties(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportProperties(String id, AsyncCallback<String> callback) {
		service.exportProperties(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#exportSetFilters(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void exportSetFilters(String id, AsyncCallback<String> callback) {
		service.exportSetFilters(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#importBases(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importBases(final String id, String fileId, boolean remove, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				Map<String, Object> info = new HashMap<String, Object>();
				info.put(PmsConstants.PORTAL_ID, id);
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, info);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importBases(id, fileId, remove, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#importConfig(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importConfig(String id, String fileId, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, EPortalImportExportType.CONFIGURATION);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importConfig(id, fileId, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#importName(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importName(String id, String fileId, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, EPortalImportExportType.NAMES);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importName(id, fileId, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#importProperties(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importProperties(final String id, String fileId, boolean remove, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				Map<String, Object> info = new HashMap<String, Object>();
				info.put(PmsConstants.PORTAL_ID, id);
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, info);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importProperties(id, fileId, remove, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#importSetFilters(java.lang.String, boolean,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void importSetFilters(String id, String fileId, boolean remove, final AsyncCallback<Void> callback) {
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.IMPORT, EPortalImportExportType.COLLECTIONS);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		service.importSetFilters(id, fileId, remove, realCallback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getPortalDevices(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getPortalDevices(String id, AsyncCallback<PortalDevicesTemplateDTO> callback) {
		service.getPortalDevices(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#setPortalDevices(com.isotrol.impe3.pms.api.portal.PortalDevicesDTO,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void setPortalDevices(PortalDevicesDTO devices, final AsyncCallback<Void> callback) {
		
		AsyncCallback<Void> realCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				callback.onFailure(arg0);
			}

			public void onSuccess(Void arg0) {
				PmsChangeEvent event = new PmsChangeEvent(PmsChangeEvent.UPDATE_PORTAL_DEVICES, arg0);
				PortalsController.this.notify(event);
				callback.onSuccess(arg0);
			}
		};
		
		
		service.setPortalDevices(devices, realCallback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#getPortalCache(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void getPortalCache(String id, AsyncCallback<PortalCacheDTO> callback) {
		service.getPortalCache(id, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync#setPortalCache(com.isotrol.impe3.pms.api.portal.PortalCacheDTO,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	public void setPortalCache(PortalCacheDTO cache, AsyncCallback<Void> callback) {
		service.setPortalCache(cache, callback);
	}

	@Override
	public void getPortalConfiguration(String portalId, String beanName, AsyncCallback<ConfigurationTemplateDTO> callback) {
		service.getPortalConfiguration(portalId, beanName, callback);
	}

	@Override
	public void savePortalConfiguration(String id, String bean, List<ConfigurationItemDTO> confsDto,
			AsyncCallback<ConfigurationTemplateDTO> callback) {
		service.savePortalConfiguration(id, bean, confsDto, callback);		
	}

	/*@Override
	public void clearConfiguration(String id, String bean, AsyncCallback<PortalConfigurationSelDTO> callback) {
		// TODO Auto-generated method stub
		
	}*/
	
	/*@Override
	public void clearConfiguration(String id, String bean,AsyncCallback<PortalConfigurationSelDTO> callback) {
		//service.clearConfiguration(id, bean,callback);	
*/		
	}


