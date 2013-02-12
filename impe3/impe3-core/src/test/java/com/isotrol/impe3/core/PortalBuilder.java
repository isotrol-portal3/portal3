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

package com.isotrol.impe3.core;


import java.util.Set;
import java.util.UUID;

import net.sf.derquinsej.collect.ImmutableHierarchy;
import net.sf.derquinsej.i18n.Localized;
import net.sf.derquinsej.i18n.Unlocalized;

import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.core.impl.CategoriesFactory;
import com.isotrol.impe3.core.impl.ContentTypesFactory;


/**
 * Test support utils.
 * @author Andres Rodriguez
 */
public class PortalBuilder {
	private final Set<ContentType> contentTypesBuilder = Sets.newHashSet();
	private final ImmutableHierarchy.Builder<UUID, Category> categoriesBuilder = ImmutableHierarchy.builder();

	/** Default Constructor. */
	public PortalBuilder() {
	}

	private static UUID uuid() {
		return UUID.randomUUID();
	}

	private static Device device() {
		return new Device(UUID.randomUUID(), DeviceType.HTML, "Test Device", "", 980, null, null);
	}

	/**
	 * Returns a new content type.
	 * @param name Localized name.
	 * @return The requested content type.
	 */
	public ContentType contentType(Localized<Name> name) {
		final ContentType ct = ContentType.builder().setId(uuid()).setName(name).get();
		contentTypesBuilder.add(ct);
		return ct;
	}

	/**
	 * Returns a new content type.
	 * @param name Name.
	 * @return The requested content type.
	 */
	public ContentType contentType(Name name) {
		return contentType(Unlocalized.of(name));
	}

	/**
	 * Returns a new content type with the same display name and path segment.
	 * @param name Name.
	 * @return The requested content type.
	 */
	public ContentType contentType(String name) {
		return contentType(Name.of(name, name));
	}

	/**
	 * Returns a new category with specified id and localized name.
	 * @param id Id.
	 * @param name Localized name.
	 * @param visible If the category is visible.
	 * @param routable If the category is routable.
	 * @param parent Parent category UUID.
	 * @return The requested category.
	 */
	public Category category(UUID id, Localized<Name> name, boolean visible, boolean routable, UUID parent) {
		final Category c = Category.builder().setId(id).setName(name).setVisible(visible).setRoutable(routable).get();
		categoriesBuilder.add(c.getId(), c, parent);
		return c;
	}

	/**
	 * Returns a new category with specified id and localized name.
	 * @param name Localized name.
	 * @param visible If the category is visible.
	 * @param routable If the category is routable.
	 * @param parent Parent category UUID.
	 * @return The requested category.
	 */
	public Category category(Localized<Name> name, boolean visible, boolean routable, UUID parent) {
		return category(uuid(), name, visible, routable, parent);
	}

	/**
	 * Returns a new category with specified parent id id and unlocalized name.
	 * @param id Id.
	 * @param name Name.
	 * @param visible If the category is visible.
	 * @param routable If the category is routable.
	 * @param parent Parent category UUID.
	 * @return The requested category.
	 */
	public Category category(UUID id, Name name, boolean visible, boolean routable, UUID parent) {
		return category(id, Unlocalized.of(name), visible, routable, parent);
	}

	/**
	 * Returns a new category with specified parent id id and unlocalized name.
	 * @param name Name.
	 * @param visible If the category is visible.
	 * @param routable If the category is routable.
	 * @param parent Parent category UUID.
	 * @return The requested category.
	 */
	public Category category(Name name, boolean visible, boolean routable, UUID parent) {
		return category(uuid(), name, visible, routable, parent);
	}

	/**
	 * Returns a new visible and routable category with the same display name and path segment.
	 * @param id Id.
	 * @param name Name.
	 * @return The requested category.
	 */
	public Category category(UUID id, String name, UUID parent) {
		return category(id, Name.of(name, name), true, true, parent);
	}

	/**
	 * Returns a new visible and routable category with the same display name and path segment.
	 * @param name Name.
	 * @return The requested category.
	 */
	public Category category(String name, UUID parent) {
		return category(uuid(), name, parent);
	}

	public ContentTypes getContentTypes() {
		return ContentTypesFactory.of(contentTypesBuilder);
	}

	public Categories getCategories() {
		return CategoriesFactory.of(categoriesBuilder.get());
	}

	public Portal getPortal(EngineMode mode) {
		return Portal.builder().setId(uuid()).setMode(mode).setDevice(device()).setName("Test Portal")
			.setContentTypes(getContentTypes()).setCategories(getCategories()).get();
	}
}
