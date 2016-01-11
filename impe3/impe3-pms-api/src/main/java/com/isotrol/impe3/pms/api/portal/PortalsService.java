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

package com.isotrol.impe3.pms.api.portal;


import java.util.List;

import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;


/**
 * Portals service.
 * @author Andres Rodriguez.
 */
public interface PortalsService {
	/**
	 * Returns all registered portals.
	 * @return All registered portals.
	 */
	PortalTreeDTO getPortals();

	/**
	 * Creates a new portal.
	 * @param dto Name and locale information.
	 * @param parentId Parent portal Id.
	 * @return The template for a new portal.
	 * @throws PortalNotFoundException if the parent portal is not found.
	 */
	String create(PortalNameDTO dto, String parentId) throws PMSException;

	/**
	 * Gets the names and locales of a portal.
	 * @param id ID of the portal.
	 * @return The requested information.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	PortalNameDTO getName(String id) throws PMSException;

	/**
	 * Saves the names and locale information of a portal.
	 * @param dto Information to save.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void setName(PortalNameDTO dto) throws PMSException;

	/**
	 * Export the names and locales of a portal.
	 * @param id Portal Id.
	 * @return URL to download the exported file.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	String exportName(String id) throws PMSException;

	/**
	 * Import the names and locale information of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void importName(String id, String fileId) throws PMSException;

	/**
	 * Gets the detail of a portal.
	 * @param id ID of the portal.
	 * @return The requested detail.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	PortalTemplateDTO get(String id) throws PMSException;

	/**
	 * Saves a portal.
	 * @param dto Portal to save.
	 * @return The saved portal.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void save(PortalDTO dto) throws PMSException;

	/**
	 * Export the configuration of a portal.
	 * @param id Portal Id.
	 * @return URL to download the exported file.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	String exportConfig(String id) throws PMSException;

	/**
	 * Import the configuration of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void importConfig(String id, String fileId) throws PMSException;

	/**
	 * Returns the parent of a portal.
	 * @param id Portal Id.
	 * @return The parent portal.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	PortalParentDTO getParent(String id) throws PMSException;

	/**
	 * Sets the parent of a portal.
	 * @param id Portal Id.
	 * @param parentId Parent portal Id.
	 * @throws PortalNotFoundException if the portal or the parent are not found.
	 * @throws IllegalPortalParentException if requested parent is invalid.
	 */
	void setParent(String id, String parentId) throws PMSException;

	/**
	 * Gets the information arquitecture of a portal.
	 * @param id ID of the portal.
	 * @return The requested detail.
	 * @throws EntityNotFoundException if the portal is not found.
	 */
	PortalIATemplateDTO getIA(String id) throws PMSException;

	/**
	 * Deletes a portal.
	 * @param id Id of the portal.
	 * @return The resulting portal tree.
	 * @throws PortalNotFoundException if the portal is not found.
	 * @throws PortalInUseException if the portal is in use and cannot be deleted.
	 */
	PortalTreeDTO delete(String id) throws PMSException;

	/**
	 * Returns the portal's active bases. The inherited bases are mixed in.
	 * @param portalId Portal Id.
	 * @return The portal's active bases.
	 */
	List<BaseDTO> getAvailableBases(String portalId) throws PMSException;

	/**
	 * Returns a portal's bases.
	 * @param portalId Portal Id.
	 * @return The portal's bases.
	 */
	BasesDTO getBases(String portalId) throws PMSException;

	/**
	 * Sets a portal's bases.
	 * @param portalId Portal Id.
	 * @param bases The portal's bases.
	 * @return The modified portal's bases.
	 */
	BasesDTO setBases(String portalId, List<BaseDTO> bases) throws PMSException;

	/**
	 * Export the bases of a portal.
	 * @param id Portal Id.
	 * @return URL to download the exported file.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	String exportBases(String id) throws PMSException;

	/**
	 * Import the bases of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param remove Whether the existing bases must be removed.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void importBases(String id, String fileId, boolean remove) throws PMSException;

	/**
	 * Returns the portal's active properties. The inherited properties are mixed in.
	 * @param portalId Portal Id.
	 * @return The portal's active properties.
	 */
	List<PropertyDTO> getAvailableProperties(String portalId) throws PMSException;

	/**
	 * Returns a portal's properties.
	 * @param portalId Portal Id.
	 * @return The portal's properties.
	 */
	PropertiesDTO getProperties(String portalId) throws PMSException;

	/**
	 * Sets a portal's properties.
	 * @param portalId Portal Id.
	 * @param bases The portal's properties.
	 * @return The modified portal's properties.
	 */
	PropertiesDTO setProperties(String portalId, List<PropertyDTO> properties) throws PMSException;

	/**
	 * Export the properties of a portal.
	 * @param id Portal Id.
	 * @return URL to download the exported file.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	String exportProperties(String id) throws PMSException;

	/**
	 * Import the properties of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param remove Whether the existing properties must be removed.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void importProperties(String id, String fileId, boolean remove) throws PMSException;

	/**
	 * Return the portal's URLs.
	 * @param portalId Portal Id.
	 * @return The portal's URLs.
	 */
	PortalURLsDTO getURLs(String portalId) throws PMSException;

	/**
	 * Returns the offline URL
	 * @param portalId Portal Id.
	 * @return The offline URL.
	 * @throws PMSException
	 */
	String getOfflineURL(String portalId) throws PMSException;

	/**
	 * Returns the online URL
	 * @param portalId Portal Id.
	 * @return The online URL.
	 * @throws PMSException
	 */
	String getOnlineURL(String portalId) throws PMSException;

	/**
	 * Checks whether a portal may be ready for offline preview.
	 * @param portalId Portal Id.
	 * @return True if the portal may be ready for offline preview.
	 * @throws EntityNotFoundException if the portal is not found.
	 */
	boolean isOfflineReady(String portalId) throws PMSException;

	/**
	 * Returns a portal's set filters.
	 * @param portalId Portal Id.
	 * @return The portal's set filters.
	 */
	List<SetFilterDTO> getSetFilters(String portalId) throws PMSException;

	/**
	 * Applies a set filters.
	 * @param portalId Portal Id.
	 * @param filter Set filter.
	 * @return The portal's set filters.
	 */
	List<SetFilterDTO> putSetFilter(String portalId, SetFilterDTO filter) throws PMSException;

	/**
	 * Removes a set filters.
	 * @param portalId Portal Id.
	 * @param filter Set filter name.
	 * @return The portal's set filters.
	 */
	List<SetFilterDTO> removeSetFilter(String portalId, String filter) throws PMSException;

	/**
	 * Clears the set filters.
	 * @param portalId Portal Id.
	 * @return The portal's set filters.
	 */
	List<SetFilterDTO> clearSetFilters(String portalId) throws PMSException;

	/**
	 * Export the set filters of a portal.
	 * @param id Portal Id.
	 * @return URL to download the exported file.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	String exportSetFilters(String id) throws PMSException;

	/**
	 * Import the set filters of a portal.
	 * @param id Portal Id.
	 * @param fileId Uploaded file id.
	 * @param remove Whether the existing filters must be removed.
	 * @throws PortalNotFoundException if the portal is not found.
	 */
	void importSetFilters(String id, String fileId, boolean remove) throws PMSException;

	/**
	 * Returns the portal devices.
	 * @param id Portal Id.
	 * @return Template DTO to manage the portal devices.
	 */
	PortalDevicesTemplateDTO getPortalDevices(String id) throws PMSException;

	/**
	 * Set the portal devices.
	 * @param devices Portal devices DTO.
	 */
	void setPortalDevices(PortalDevicesDTO devices) throws PMSException;

	/**
	 * Returns the portal cache configuration.
	 * @param id Portal Id.
	 * @return The portal cache configuration DTO.
	 */
	PortalCacheDTO getPortalCache(String id) throws PMSException;

	/**
	 * Set the portal cache configuration.
	 * @param cache The portal cache configuration DTO.
	 */
	void setPortalCache(PortalCacheDTO cache) throws PMSException;
	
	/**
	 * Returns the portal configurations. The inherited properties are mixed in.
	 * @param portalId Portal Id.
	 * @return The portal configurations.
	 */
	List<PortalConfigurationSelDTO> getPortalConfigurations(String portalId) throws PMSException;
	
	/**
	 * Returns the portal configuration.
	 * @param portalId Portal Id.
	 * @param beanName Bean Name.
	 * @return The portal configurations.
	 */
	ConfigurationTemplateDTO getPortalConfiguration(String portalId, String beanName) throws PMSException;
}
