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

package com.isotrol.impe3.api.component;


import java.net.URI;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Source;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.Listing;


/**
 * IMPE3 Component Context.
 * @author Andres Rodriguez
 */
public interface ComponentRequestContext extends PageRequestContext {

	/**
	 * Returns the Id of the component instance in the current page.
	 * @return The component id.
	 */
	UUID getComponentId();

	/**
	 * Returns the registered actions available to this component.
	 * @return A map from action name to component instance id.
	 */
	Map<String, UUID> getRegisteredActions();

	/**
	 * Returns an action URI.
	 * @param name Action name.
	 * @param parameters Action query parameters.
	 * @return The requested URI.
	 */
	URI getActionURI(String name, Multimap<String, Object> parameters);

	/**
	 * Returns an absolute action URI.
	 * @param name Action name.
	 * @param parameters Action query parameters.
	 * @return The requested URI.
	 */
	URI getAbsoluteActionURI(String name, Multimap<String, Object> parameters);
	
	/**
	 * Returns a registered action URI.
	 * @param name Registered action name.
	 * @param parameters Action query parameters.
	 * @return The requested URI.
	 */
	URI getRegisteredActionURI(String name, Multimap<String, Object> parameters);

	/**
	 * Returns an aboslute registered action URI.
	 * @param name Registered action name.
	 * @param parameters Action query parameters.
	 * @return The requested URI.
	 */
	URI getAbsoluteRegisteredActionURI(String name, Multimap<String, Object> parameters);
	
	ContentKey getContentKey();

	NavigationKey getNavigationKey();

	Content getContent();

	Listing<?> getListing();

	Pagination getPagination();
	
	/** Returns the template key. */
	TemplateKey getTemplateKey();
	
	/** Returns the template model. */
	TemplateModel getTemplateModel();
	
	/** Returns the partial ETag of the parent component. */
	ETag getETag();
	
	/** Returns the XML Source. */
	Source getSource();
}
