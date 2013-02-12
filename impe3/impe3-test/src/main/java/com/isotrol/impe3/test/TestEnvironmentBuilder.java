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

package com.isotrol.impe3.test;


import static com.isotrol.impe3.core.support.FileLoaderSupport.getMediaType;
import static com.isotrol.impe3.test.TestSupport.uuid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.derquinsej.collect.ImmutableHierarchy;
import net.sf.derquinsej.i18n.Localized;
import net.sf.derquinsej.i18n.Unlocalized;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Builder;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.core.impl.CategoriesFactory;
import com.isotrol.impe3.core.impl.ContentTypesFactory;
import com.isotrol.impe3.core.support.AbstractFileLoader;


/**
 * Builder for IMPE3 Test Environment.
 * @author Andres Rodriguez
 */
public class TestEnvironmentBuilder implements Builder<TestEnvironment> {
	private final Set<ContentType> contentTypesBuilder = Sets.newHashSet();
	private final ImmutableHierarchy.Builder<UUID, Category> categoriesBuilder = ImmutableHierarchy.builder();
	private final ImmutableMap.Builder<UUID, FileData> filesBuilder = ImmutableMap.builder();
	private final Map<String, URI> bases = Maps.newHashMap();
	private EngineMode mode = EngineMode.OFFLINE;

	/** Default Constructor. */
	public TestEnvironmentBuilder() {
	}

	public void setMode(EngineMode mode) {
		this.mode = mode;
	}

	/**
	 * Returns a new content type.
	 * @param id Id.
	 * @param name Localized name.
	 * @return The requested content type.
	 */
	public ContentType contentType(UUID id, Localized<Name> name) {
		final ContentType ct = ContentType.builder().setId(id).setName(name).get();
		contentTypesBuilder.add(ct);
		return ct;
	}

	/**
	 * Returns a new content type.
	 * @param name Localized name.
	 * @return The requested content type.
	 */
	public ContentType contentType(Localized<Name> name) {
		return contentType(uuid(), name);
	}

	/**
	 * Returns a new content type.
	 * @param id Id.
	 * @param name Name.
	 * @return The requested content type.
	 */
	public ContentType contentType(UUID id, Name name) {
		return contentType(id, Unlocalized.of(name));
	}

	/**
	 * Returns a new content type.
	 * @param name Name.
	 * @return The requested content type.
	 */
	public ContentType contentType(Name name) {
		return contentType(uuid(), name);
	}

	/**
	 * Returns a new content type with the same display name and path segment.
	 * @param id Id.
	 * @param name Name.
	 * @return The requested content type.
	 */
	public ContentType contentType(UUID id, String name) {
		return contentType(id, Name.of(name, name));
	}

	/**
	 * Returns a new content type with the same display name and path segment.
	 * @param name Name.
	 * @return The requested content type.
	 */
	public ContentType contentType(String name) {
		return contentType(uuid(), name);
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

	public TestEnvironmentBuilder putBase(String base, String value) {
		bases.put(base, URI.create(value));
		return this;
	}

	/**
	 * Creates a new file.
	 * @param base Base class.
	 * @param resourcePath Resource path.
	 * @return The Id of the created file.
	 */
	private FileId file(Class<?> base, String resourcePath, boolean isBundle) {
		final UUID id = uuid();
		final String[] path = resourcePath.split("/");
		final String name = path[path.length - 1];
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			final InputStream is = base.getResourceAsStream(resourcePath);
			byte[] buffer = new byte[256];
			int read;
			while ((read = is.read(buffer)) > -1) {
				bos.write(buffer, 0, read);
			}
			final FileData data = new FileData(id, name, getMediaType(name), false, isBundle, bos.toByteArray(), false);
			filesBuilder.put(id, data);
			return FileId.of(id, name);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Creates a new file.
	 * @param base Base class.
	 * @param resourcePath Resource path.
	 * @return The Id of the created file.
	 */
	public FileId file(Class<?> base, String resourcePath) {
		return file(base, resourcePath, false);
	}

	/**
	 * Creates a new file bundle.
	 * @param base Base class.
	 * @param resourcePath Resource path.
	 * @return The Id of the created file.
	 */
	public FileId bundle(Class<?> base, String resourcePath) {
		return file(base, resourcePath, true);
	}

	/**
	 * Create a test enviroment
	 */
	public TestEnvironment get() {
		final ContentTypes contentTypes = ContentTypesFactory.of(contentTypesBuilder);
		final Categories categories = CategoriesFactory.of(categoriesBuilder.get());
		final Portal portal = Portal.builder().setId(uuid()).setMode(mode).setDevice(TestSupport.htmlDevice())
			.setName("Test Portal").setContentTypes(contentTypes).setCategories(categories).setBases(bases).get();
		final ImmutableMap<UUID, FileData> files = filesBuilder.build();
		final FileLoader fileLoader = new AbstractFileLoader() {
			@Override
			protected FileData doLoad(UUID id) {
				if (!files.containsKey(id)) {
					throw new IllegalArgumentException();
				}
				return files.get(id);
			}
		};
		final Locale locale = new Locale("es");
		final Device device = new Device(uuid(), DeviceType.HTML, "name", "desc", 980, null, null);
		final URIGenerator urig = new TestURIGenerator(portal);
		return new TestEnvironment(portal, device, locale) {
			public FileLoader getFileLoader() {
				return fileLoader;
			}

			public URIGenerator getURIGenerator() {
				return urig;
			}

		};
	}
}
