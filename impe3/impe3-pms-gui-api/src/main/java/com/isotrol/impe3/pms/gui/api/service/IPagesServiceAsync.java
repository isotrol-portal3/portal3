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
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.api.page.ContentPagesDTO;
import com.isotrol.impe3.pms.api.page.DefaultPagesDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageDTO;
import com.isotrol.impe3.pms.api.page.PageDeviceDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;


/**
 * @author Manuel Ruiz
 * 
 */
public interface IPagesServiceAsync {

	/**
	 * Returns the available devices for page definitions.
	 * @param portalId Portal Id.
	 * @param callback
	 */
	void getPageDevices(String portalId, AsyncCallback<List<PageDeviceDTO>> callback);

	/**
	 * Returns the default the pages of a portal for a device.
	 * @param loc Pages locator.
	 * @param callback The default the pages of the portal for the device.
	 */
	void getDefaultPages(PortalPagesLoc loc, AsyncCallback<DefaultPagesDTO> callback);

	/**
	 * Returns the template pages of a portal for a device.
	 * @param loc Pages locator.
	 * @param callback The template pages pages of the portal for the device.
	 */
	void getTemplatePages(PortalPagesLoc loc, AsyncCallback<List<Inherited<PageSelDTO>>> callback);

	/**
	 * Returns the special pages of a portal for a device.
	 * @param loc Pages locator.
	 * @param callback The special pages pages of the portal for the device.
	 */
	void getSpecialPages(PortalPagesLoc loc, AsyncCallback<List<Inherited<PageSelDTO>>> callback);

	/**
	 * Returns the error pages of a portal for a device (excluding default).
	 * @param loc Pages locator.
	 * @param callback The error pages pages of the portal for the device (excluding default).
	 */
	void getErrorPages(PortalPagesLoc loc, AsyncCallback<List<Inherited<PageSelDTO>>> callback);
	
	/**
	 * Returns the category navigation pages of a portal for a device.
	 * @param loc Pages locator.
	 * @param callback The category navigation pages of the portal for the device.
	 */
	void getCategoryPages(PortalPagesLoc loc, AsyncCallback<CategoryPagesDTO> callback);

	/**
	 * Returns the content detail pages of a portal for a device.
	 * @param loc Pages locator.
	 * @param callback The content detail pages of the portal for the device.
	 */
	void getContentPages(PortalPagesLoc loc, AsyncCallback<ContentPagesDTO> callback);

	/**
	 * Returns the content detail pages of a portal for a category and device.
	 * @param loc Pages locator.
	 * @param contentTypeId Content type Id.
	 */
	void getCategoryContentPages(PortalPagesLoc loc, String contentTypeId, AsyncCallback<CategoryPagesDTO> callback);

	/**
	 * Returns the content listing pages of a portal for a device.
	 * @param loc Pages locator.
	 * @param callback The content listing pages of the portal for the device.
	 */
	void getContentTypePages(PortalPagesLoc loc, AsyncCallback<ContentPagesDTO> callback);

	/**
	 * Returns the content listing pages of a portal for a category and device.
	 * @param loc Pages locator.
	 * @param contentTypeId Content type Id.
	 */
	void getCategoryContentTypePages(PortalPagesLoc loc, String contentTypeId, AsyncCallback<CategoryPagesDTO> callback);
	
	/**
	 * @param loc
	 * @param pageClass
	 * @param callback
	 * @see {@link com.isotrol.impe3.pms.api.page.PagesService#newTemplate(PortalPagesLoc, PageClass)}
	 */
	void newTemplate(PortalPagesLoc loc, PageClass pageClass, AsyncCallback<PageTemplateDTO> callback);

	/**
	 * Retrieves the Components palette.<br/>
	 * @param loc Pages locator.
	 * @param callback Callback for processing the Components palette
	 * @see {@link com.isotrol.impe3.pms.api.page.PagesService#getPalette(PortalPagesLoc)} 
	 */
	void getPalette(PortalPagesLoc loc, AsyncCallback<List<PaletteDTO>> callback);
	
	/**
	 * @param loc
	 * @param componentKey
	 * @see com.isotrol.impe3.pms.api.page.PagesService#newComponentTemplate(PortalPagesLoc, ComponentKey)
	 */
	void newComponentTemplate(PortalPagesLoc loc, ComponentKey componentKey,
		AsyncCallback<ComponentInPageTemplateDTO> callback);

	/**
	 * @param loc
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.page.PagesService#get(com.isotrol.impe3.pms.api.page.PageLoc)
	 */
	void get(PageLoc loc, AsyncCallback<PageTemplateDTO> callback);

	/**
	 * @param dto
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.page.PagesService#save(PageDTO)
	 */
	void save(PageDTO dto, AsyncCallback<PageTemplateDTO> callback);

	/**
	 * @param loc
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.page.PagesService#getLayout(com.isotrol.impe3.pms.api.page.PageLoc)
	 */
	void getLayout(PageLoc loc, AsyncCallback<LayoutDTO> callback);

	/**
	 * @param loc
	 * @param frames
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.page.PagesService#setLayout(com.isotrol.impe3.pms.api.page.PageLoc, List)
	 */
	void setLayout(PageLoc loc, List<FrameDTO> frames, AsyncCallback<LayoutDTO> callback);

	/**
	 * @param loc
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.page.PagesService#delete(com.isotrol.impe3.pms.api.page.PageLoc)
	 */
	void delete(PageLoc loc, AsyncCallback<Void> callback);

	/**
	 * @param loc
	 * @param cipId
	 * @param callback
	 * @see com.isotrol.impe3.pms.api.page.PagesService#isCIPinLayout(PageLoc, String)
	 */
	void isCIPinLayout(PageLoc loc, String cipId, AsyncCallback<Boolean> callback);
	
	/**
	 * Export all components.
	 * @param loc Pages locator.
	 * @param callback URL to download the exported file.
	 */
	void exportAll(PortalPagesLoc loc, AsyncCallback<String> callback);

	/**
	 * Import components definitions.
	 * @param loc Pages locator.
	 * @param fileId Uploaded file id.
	 * @param overwrite Whether to overwrite existing connectors.
	 * @param callback
	 */
	void importPages(PortalPagesLoc loc, String fileId, boolean overwrite, AsyncCallback<Void> callback);

}
