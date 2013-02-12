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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.isotrol.impe3.core.support.FileLoaderSupport.getMediaType;
import static com.isotrol.impe3.test.TestSupport.uuid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Builder;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.IAModel;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.core.DocumentBuilder;


/**
 * Test context builder.
 * @author Andres Rodriguez
 */
public final class TestContextBuilder implements IAModel, Builder<TestContext> {
	private final ContentTypes contentTypes;
	private final Categories categories;
	private final TestNodeRepositoryBuilder nrb = new TestNodeRepositoryBuilder();
	private final ImmutableMap.Builder<UUID, FileData> filesBuilder = ImmutableMap.builder();
	private final Map<String, URI> bases = Maps.newHashMap();
	private EngineMode mode = EngineMode.OFFLINE;
	private Locale locale = new Locale("es");
	private Device device = TestSupport.htmlDevice();

	/** Default Constructor. */
	TestContextBuilder(final ContentTypes contentTypes, final Categories categories) {
		this.contentTypes = checkNotNull(contentTypes, "The content types are required");
		this.categories = checkNotNull(categories, "The categories types are required");
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getContentTypes()
	 */
	public ContentTypes getContentTypes() {
		return contentTypes;
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getCategories()
	 */
	public Categories getCategories() {
		return categories;
	}

	/**
	 * Returns a new test document builder. This builder has an unique id (among those created by this builder), and
	 * test title and description.
	 * @param contentType Content type.
	 * @return The created document builder, with node key, title and description.
	 */
	public DocumentBuilder newTestDocument(ContentType contentType) {
		return nrb.newTestBuilder(contentType);
	}

	/**
	 * adds a document to repository builder
	 * @param builder document builder
	 * @param analyzer specific analyzer, could be null.
	 * @return fluid builder
	 */
	public TestContextBuilder add(DocumentBuilder builder, Analyzer analyzer) {
		nrb.add(builder, analyzer);
		return this;
	}

	/**
	 * adds a document to repository builder using default analyzer
	 * @param builder document builder
	 * @return fluid builder
	 */
	public TestContextBuilder add(DocumentBuilder builder) {
		nrb.add(builder);
		return this;
	}

	/**
	 * Adds some simple documents to the repository builder using default analyzer
	 * @param n Number of documents to add.
	 * @param contentType Content type.
	 * @param categories Categories.
	 * @return This builder.
	 */
	public TestContextBuilder add(int n, ContentType contentType, Category... categories) {
		nrb.add(n, contentType, categories);
		return this;
	}

	public TestContextBuilder putBase(String base, String value) {
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

	ImmutableMap<UUID, FileData> getFiles() {
		return filesBuilder.build();
	}

	Function<UUID, NodeRepository> getNodeRepositories() {
		final NodeRepository nr = nrb.get();
		return new Function<UUID, NodeRepository>() {
			public NodeRepository apply(UUID from) {
				return nr;
			}
		};
	}

	Locale getLocale() {
		return locale;
	}

	Device getDevice() {
		return device;
	}

	Portal getPortal() {
		return Portal.builder().setId(uuid()).setMode(mode).setDevice(device).setName("Test Portal")
			.setContentTypes(getContentTypes()).setCategories(getCategories()).setBases(bases).get();
	}

	public TestContext get() {
		return new TestContext(this);
	}
}
