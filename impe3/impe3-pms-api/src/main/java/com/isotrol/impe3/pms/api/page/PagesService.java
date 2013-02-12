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

package com.isotrol.impe3.pms.api.page;


import java.util.List;

import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.InvalidImportFileException;
import com.isotrol.impe3.pms.api.PMSException;


/**
 * Pages service.
 * @author Andres Rodriguez.
 */
public interface PagesService {
	/**
	 * Returns the available devices for page definitions.
	 * @param portalId Portal Id.
	 * @return The list of available devices.
	 * @throws PMSException
	 */
	List<PageDeviceDTO> getPageDevices(String portalId) throws PMSException;

	/**
	 * Returns the default the pages of a portal for a device.
	 * @param loc Pages locator.
	 * @return The default the pages of the portal for the device.
	 */
	DefaultPagesDTO getDefaultPages(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns the template pages of a portal for a device.
	 * @param loc Pages locator.
	 * @return The template pages pages of the portal for the device.
	 */
	List<Inherited<PageSelDTO>> getTemplatePages(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns the special pages of a portal for a device.
	 * @param loc Pages locator.
	 * @return The special pages pages of the portal for the device.
	 */
	List<Inherited<PageSelDTO>> getSpecialPages(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns the error pages of a portal for a device (excluding default).
	 * @param loc Pages locator.
	 * @return The error pages pages of the portal for the device (excluding default).
	 */
	List<Inherited<PageSelDTO>> getErrorPages(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns the category navigation pages of a portal for a device.
	 * @param loc Pages locator.
	 * @return The category navigation pages of the portal for the device.
	 */
	CategoryPagesDTO getCategoryPages(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns the content detail pages of a portal for a device.
	 * @param loc Pages locator.
	 * @return The content detail pages of the portal for the device.
	 */
	ContentPagesDTO getContentPages(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns the content detail pages of a portal for a category and device.
	 * @param loc Pages locator.
	 * @param contentTypeId Content type Id.
	 * @return The content detail pages of the portal for the category and device device.
	 */
	CategoryPagesDTO getCategoryContentPages(PortalPagesLoc loc, String contentTypeId) throws PMSException;

	/**
	 * Returns the content listing pages of a portal for a device.
	 * @param loc Pages locator.
	 * @return The content listing pages of the portal for the device.
	 */
	ContentPagesDTO getContentTypePages(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns the content listing pages of a portal for a category and device.
	 * @param loc Pages locator.
	 * @param contentTypeId Content type Id.
	 * @return The content listing pages of the portal for the category and device device.
	 */
	CategoryPagesDTO getCategoryContentTypePages(PortalPagesLoc loc, String contentTypeId) throws PMSException;

	/**
	 * Creates a template for a new page.
	 * @param loc Pages locator.
	 * @param pageClass Page class.
	 * @return A template for a new page.
	 */
	PageTemplateDTO newTemplate(PortalPagesLoc loc, PageClass pageClass) throws PMSException;

	/**
	 * Returns the component palette.
	 * @param loc Pages locator.
	 * @return The component palette.
	 */
	List<PaletteDTO> getPalette(PortalPagesLoc loc) throws PMSException;

	/**
	 * Returns a new component template for a new component.
	 * @param loc Pages locator.
	 * @param componentKey Component key.
	 * @return The configuration template.
	 */
	ComponentInPageTemplateDTO newComponentTemplate(PortalPagesLoc loc, ComponentKey componentKey) throws PMSException;

	/**
	 * Returns a template to modify a page.
	 * @param loc Page locator.
	 * @return A template.
	 */
	PageTemplateDTO get(PageLoc loc) throws PMSException;

	/**
	 * Saves a page. If the ID is null the operation is considered an insertion. Otherwise, it is considered an update.
	 * @param dto Object to save.
	 * @return The saved object.
	 */
	PageTemplateDTO save(PageDTO dto) throws PMSException;

	/**
	 * Returns a template to modify a page layout.
	 * @param loc Page locator.
	 * @return Page layout template.
	 */
	LayoutDTO getLayout(PageLoc loc) throws PMSException;

	/**
	 * Saves a page layout.
	 * @param loc Page locator.
	 * @param frames Root frame.
	 * @return Page layout template.
	 */
	LayoutDTO setLayout(PageLoc loc, List<FrameDTO> frames) throws PMSException;

	/**
	 * Returns whether a component in page is used in the current layout.
	 * @param loc Page locator.
	 * @param cipId Component in page instance Id.
	 * @return True if the component in page is used in the current layout.
	 * @throws PMSException
	 */
	boolean isCIPinLayout(PageLoc loc, String cipId) throws PMSException;

	/**
	 * Deletes a page.
	 * @param loc Page locator.
	 * @throws PageNotFoundException if the page is not found.
	 * @throws PageInUseException if the page is in use.
	 */
	void delete(PageLoc loc) throws PMSException;

	/**
	 * Export all components.
	 * @param loc Pages locator.
	 * @return URL to download the exported file.
	 */
	String exportAll(PortalPagesLoc loc) throws PMSException;

	/**
	 * Import components definitions.
	 * @param loc Pages locator.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @throws InvalidImportFileException if unable to parse the uploaded file.
	 */
	void importPages(PortalPagesLoc loc, String fileId, boolean overwrite) throws PMSException;

}
