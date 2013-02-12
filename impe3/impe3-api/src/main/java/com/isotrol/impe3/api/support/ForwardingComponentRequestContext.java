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

package com.isotrol.impe3.api.support;


import java.net.URI;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Source;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.Listing;


/**
 * Forwarding component request context.
 * @author Andres Rodriguez.
 */
public abstract class ForwardingComponentRequestContext extends ForwardingPageRequestContext implements
	ComponentRequestContext {
	/** Default constructor. */
	public ForwardingComponentRequestContext() {
	}

	protected abstract ComponentRequestContext delegate();

	public UUID getComponentId() {
		return delegate().getComponentId();
	}

	public Map<String, UUID> getRegisteredActions() {
		return delegate().getRegisteredActions();
	}

	public URI getActionURI(String name, Multimap<String, Object> parameters) {
		return delegate().getActionURI(name, parameters);
	}

	public URI getAbsoluteActionURI(String name, Multimap<String, Object> parameters) {
		return delegate().getAbsoluteActionURI(name, parameters);
	}

	public URI getRegisteredActionURI(String name, Multimap<String, Object> parameters) {
		return delegate().getRegisteredActionURI(name, parameters);
	}

	public URI getAbsoluteRegisteredActionURI(String name, Multimap<String, Object> parameters) {
		return delegate().getAbsoluteRegisteredActionURI(name, parameters);
	}

	public ContentKey getContentKey() {
		return delegate().getContentKey();
	}

	public NavigationKey getNavigationKey() {
		return delegate().getNavigationKey();
	}

	public Content getContent() {
		return delegate().getContent();
	}

	public Listing<?> getListing() {
		return delegate().getListing();
	}

	public Pagination getPagination() {
		return delegate().getPagination();
	}

	public TemplateKey getTemplateKey() {
		return delegate().getTemplateKey();
	}

	public TemplateModel getTemplateModel() {
		return delegate().getTemplateModel();
	}

	public ETag getETag() {
		return delegate().getETag();
	}

	public Source getSource() {
		return delegate().getSource();
	}

}
