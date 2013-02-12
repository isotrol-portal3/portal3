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

package com.isotrol.impe3.palette.cloud;


import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.sf.derquinse.lucis.Group;
import net.sf.derquinse.lucis.GroupResult;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.Schema;


/**
 * 
 * @author Emilio Escobar Reyero
 * 
 */
public class ContentTypeCloudComponent extends AbstractCloud {

	public void edit() {

		cloud = new LinkedList<CloudItem>();
		final ContentTypes contentTypes = getContentTypes();
		for (ContentType contentType : contentTypes.values()) {
			cloud.add(createItem(contentType, (int) Math.ceil(Math.random() * 10), 10));
		}
	}

	@Override
	List<String> fields() {
		return Lists.newArrayList(Schema.TYPE);
	}

	@Override
	NodeQuery and() {

		if (getNavigationKey() == null) {
			return null;
		}

		final Category category = getNavigationKey().getCategory();

		if (category == null) {
			return null;
		}

		return NodeQueries.term(Schema.CATEGORY, category.getId());
	}

	@Override
	List<CloudItem> generateCloud(GroupResult result, int totalHits) {
		final Group rootGroup = result.getGroup();

		List<CloudItem> cloud = new LinkedList<CloudItem>();
		for (String groupName : rootGroup.getGroupNames()) {
			final Group group = rootGroup.getGroup(groupName);
			final UUID uuid = UUID.fromString(groupName);
			final ContentType contentType = contentTypes.get(uuid);

			cloud.add(createItem(contentType, group.getHits(), totalHits));
		}

		return cloud;
	}

	@Override
	int calculateTotalHits(GroupResult result) {
		return result.getTotalHits();
	}

	private CloudItem createItem(ContentType contentType, int hits, int totalHits) {
		final NavigationKey nk = NavigationKey.contentType(contentType);
		final PageKey pk = PageKey.navigation(nk);
		final Route r = (route != null) ? route.toPage(pk) : Route.of(false, pk, null, null);
		final URI uri = uriGenerator.getURI(r);
		final Name name = (locale != null) ? contentType.getName().get(locale) : contentType.getDefaultName();

		return new CloudItem(name.getDisplayName(), uri, Math.ceil(hits / totalHits));
	}
}
