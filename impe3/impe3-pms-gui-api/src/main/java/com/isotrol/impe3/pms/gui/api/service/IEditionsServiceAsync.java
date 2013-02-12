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
import com.isotrol.impe3.pms.api.edition.EditionDTO;


/**
 * Asynchronous interface for the Editions Service.
 * 
 * @author Andrei Cojocaru
 * @see {@link IEditionsService}, {@link com.isotrol.impe3.pms.api.edition.EditionsService EditionsService}
 */
public interface IEditionsServiceAsync {

	/**
	 * Returns the last published editions.<br/> The synchronous service may throw a
	 * {@link com.isotrol.impe3.pms.api.PMSException}
	 * @param callback async callback to process the last published editions.
	 */
	void getLastEditions(AsyncCallback<List<EditionDTO>> callback);

	/**
	 * Creates a new edition with the current offline environment.<br/> The synchronous service may throw a
	 * {@link com.isotrol.impe3.pms.api.PMSException PMSException}
	 * @param callback
	 */
	void publish(AsyncCallback<Void> callback);

	/**
	 * Sets the edition to be used for the online environment.
	 * @param editionId Edition Id.
	 * @param callback
	 */
	void setOnlineEdition(String editionId, AsyncCallback<Void> callback);

	/**
	 * Create a new edition with the current online environment and the offline of the provided portal.
	 * @param portalId Portal to publish.
	 * @param tryParents Whether to publish the parents if needed.
	 * @param callback
	 */
	void publishPortal(String portalId, boolean tryParents, AsyncCallback<Boolean> callback);

}
