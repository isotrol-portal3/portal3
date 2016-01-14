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

package com.isotrol.impe3.pms.gui.api.service;


import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.PMSException;
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


/**
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public interface IPortalsServiceAsync {

	/**
	 * <br/>
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getPortals()
	 */
	void getPortals(AsyncCallback<PortalTreeDTO> callback);

	/**
	 * Creates a new portal.
	 * @param dto Name and locale information.
	 * @param parentId Parent portal Id.
	 * @param callback The created portal id
	 */
	void create(PortalNameDTO dto, String parentId, AsyncCallback<String> callback);

	/**
	 * Gets the names and locales of a portal.
	 * @param id ID of the portal.
	 * @param callback The requested information.
	 */
	void getName(String id, AsyncCallback<PortalNameDTO> callback);

	/**
	 * Saves the names and locale information of a portal. If the ID is {@code null} it is considered and insertion.
	 * @param dto Information to save.
	 * @param callback
	 */
	void setName(PortalNameDTO dto, AsyncCallback<Void> callback);
	
	/**
	 * Export the names and locales of a portal.
	 * @param id Portal Id.
	 * @param callback URL to download the exported file.
	 */
	void exportName(String id, AsyncCallback<String> callback);

	/**
	 * Import the names and locale information of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param callback
	 */
	void importName(String id, String fileId, AsyncCallback<Void> callback);

	/**
	 * <br/>
	 * @param id
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#get(String)
	 */
	void get(String id, AsyncCallback<PortalTemplateDTO> callback);

	/**
	 * <br/>
	 * @param dto Portal to save.
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#save(PortalDTO)
	 */
	void save(PortalDTO dto, AsyncCallback<Void> callback);
	
	/**
	 * Export the configuration of a portal.
	 * @param id Portal Id.
	 * @param callback URL to download the exported file.
	 */
	void exportConfig(String id, AsyncCallback<String> callback);

	/**
	 * Import the configuration of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param callback
	 */
	void importConfig(String id, String fileId, AsyncCallback<Void> callback);

	/**
	 * Returns the parent of a portal.
	 * @param id Portal Id.
	 * @param callback The parent portal.
	 */
	void getParent(String id, AsyncCallback<PortalParentDTO> callback);

	/**
	 * Sets the parent of a portal.
	 * @param id Portal Id.
	 * @param parentId Parent portal Id.
	 * @param callback
	 */
	void setParent(String id, String parentId, AsyncCallback<Void> callback);

	/**
	 * Gets the information arquitecture of a portal.<br/>
	 * @param id ID of the portal.
	 * @param callback The requested detail.
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getIA(String)
	 */
	void getIA(String id, AsyncCallback<PortalIATemplateDTO> callback);

	/**
	 * <br/>
	 * @param id
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#delete(String)
	 */
	void delete(String id, AsyncCallback<PortalTreeDTO> callback);
	
	/**
	 * Returns the portal's active bases. The inherited bases are mixed in.
	 * @param portalId Portal Id.
	 * @param callback The portal's active bases.
	 */
	void getAvailableBases(String portalId, AsyncCallback<List<BaseDTO>> callback);

	/**
	 * <br/>
	 * @param portalId
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getBases(String)
	 */
	void getBases(String portalId, AsyncCallback<BasesDTO> callback);

	/**
	 * <br/>
	 * @param portalId
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setBases(String, List)
	 */
	void setBases(String portalId, List<BaseDTO> bases, AsyncCallback<BasesDTO> callback);
	
	/**
	 * Export the bases of a portal.
	 * @param id Portal Id.
	 * @param callback URL to download the exported file.
	 */
	void exportBases(String id, AsyncCallback<String> callback);

	/**
	 * Import the bases of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param remove Whether the existing bases must be removed.
	 * @param callback
	 */
	void importBases(String id, String fileId, boolean remove, AsyncCallback<Void> callback);

	/**
	 * Returns the portal's active properties. The inherited properties are mixed in.
	 * @param portalId Portal Id.
	 * @param callback The portal's active properties.
	 */
	void getAvailableProperties(String portalId, AsyncCallback<List<PropertyDTO>> callback);
	
	/**
	 * <br/>
	 * @param portalId
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getProperties(String)
	 */
	void getProperties(String portalId, AsyncCallback<PropertiesDTO> callback);

	/**
	 * <br/>
	 * @param portalId
	 * @param properties
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#setProperties(String, List)
	 */
	void setProperties(String portalId, List<PropertyDTO> properties, AsyncCallback<PropertiesDTO> callback);
	
	/**
	 * Export the properties of a portal.
	 * @param id Portal Id.
	 * @param callback URL to download the exported file.
	 */
	void exportProperties(String id, AsyncCallback<String> callback);

	/**
	 * Import the properties of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param remove Whether the existing bases must be removed.
	 * @param callback
	 */
	void importProperties(String id, String fileId, boolean remove, AsyncCallback<Void> callback);

	/**
	 * Returns the portal configurations. The inherited properties are mixed in.
	 * @param portalId Portal Id.
	 * @return The portal configurations.
	 */
	void getPortalConfigurations(String portalId, AsyncCallback<List<PortalConfigurationSelDTO>> callback);
	
	/**
	 * Return the portal's URLs.
	 * @param portalId Portal Id.
	 * @param callback The portal's URLs.
	 */
	void getURLs(String portalId, AsyncCallback<PortalURLsDTO> callback);
	
	/**
	 * @param portalId
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getOfflineURL(String)
	 */
	void getOfflineURL(String portalId, AsyncCallback<String> callback);

	/**
	 * @param portalId
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#getOnlineURL(String)
	 */
	void getOnlineURL(String portalId, AsyncCallback<String> callback);

	/**
	 * Checks whether a portal may be ready for offline preview.
	 * @param portalId Portal Id.
	 * @return callabck
	 * @see com.isotrol.impe3.pms.api.portal.PortalsService#isOfflineReady(String)
	 */
	void isOfflineReady(String portalId, AsyncCallback<Boolean> callback);
	
	/**
	 * Returns a portal's set filters.
	 * @param portalId Portal Id.
	 * @param callback The portal's set filters.
	 */
	void getSetFilters(String portalId, AsyncCallback<List<SetFilterDTO>> callback);

	/**
	 * Applies a set filters.
	 * @param portalId Portal Id.
	 * @param filter Set filter.
	 * @param callback The portal's set filters.
	 */
	void putSetFilter(String portalId, SetFilterDTO filter, AsyncCallback<List<SetFilterDTO>> callback);

	/**
	 * Removes a set filters.
	 * @param portalId Portal Id.
	 * @param filter Set filter name.
	 * @param callback The portal's set filters.
	 */
	void removeSetFilter(String portalId, String filter, AsyncCallback<List<SetFilterDTO>> callback);

	/**
	 * Clears the set filters.
	 * @param portalId Portal Id.
	 * @param callback The portal's set filters.
	 */
	void clearSetFilters(String portalId, AsyncCallback<List<SetFilterDTO>> callback);
	
	/**
	 * Export the set filters of a portal.
	 * @param id Portal Id.
	 * @param callback URL to download the exported file.
	 */
	void exportSetFilters(String id, AsyncCallback<String> callback);

	/**
	 * Import the set filters of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param remove Whether the existing filters must be removed.
	 * @param callback
	 */
	void importSetFilters(String id, String fileId, boolean remove, AsyncCallback<Void> callback);
	
	/**
	 * Returns the portal devices.
	 * @param id Portal Id.
	 * @param callback Template DTO to manage the portal devices.
	 */
	void getPortalDevices(String id, AsyncCallback<PortalDevicesTemplateDTO> callback);

	/**
	 * Set the portal devices.
	 * @param devices Portal devices DTO.
	 * @param callback
	 */
	void setPortalDevices(PortalDevicesDTO devices, AsyncCallback<Void> callback);
	
	/**
	 * Returns the portal cache configuration.
	 * @param id Portal Id.
	 * @param callback The portal cache configuration DTO.
	 */
	void getPortalCache(String id, AsyncCallback<PortalCacheDTO> callback);

	/**
	 * Set the portal cache configuration.
	 * @param cache The portal cache configuration DTO.
	 * @param callback
	 */
	void setPortalCache(PortalCacheDTO cache, AsyncCallback<Void> callback);
	
	
	void getPortalConfiguration(String portalId, String beanName,AsyncCallback<ConfigurationTemplateDTO> callback);

	void savePortalConfiguration(String id, String bean, List<ConfigurationItemDTO> confsDto, AsyncCallback<ConfigurationTemplateDTO> callback);
	
	//void clearConfiguration(String id, String bean,AsyncCallback<PortalConfigurationSelDTO> callback);
}
