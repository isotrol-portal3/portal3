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

package com.isotrol.impe3.core.content;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.isotrol.impe3.api.content.ContentCriteriaTransformer.identity;
import static com.isotrol.impe3.api.content.ContentCriteriaTransformer.reset;

import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.nr.api.NodeRepository;


/**
 * A forwarding content loader with a criteria transformer.
 * @author Andres Rodriguez
 */
public final class TransformerContentLoader extends ForwardingContentLoader {
	/** Content loader. */
	private final ContentLoader contentLoader;
	/** Criteria transformer. */
	private final ContentCriteriaTransformer transformer;

	/**
	 * Creation method.
	 */
	public static ContentLoader transform(ContentLoader contentLoader, ContentCriteriaTransformer transformer) {
		if (contentLoader == null) {
			return null;
		}
		if (transformer == null || transformer == identity() || transformer == reset()) {
			return contentLoader;
		}
		return new TransformerContentLoader(contentLoader, transformer);
	}

	/**
	 * Constructor
	 */
	private TransformerContentLoader(ContentLoader contentLoader, ContentCriteriaTransformer transformer) {
		this.contentLoader = checkNotNull(contentLoader);
		this.transformer = checkNotNull(transformer);
	}

	@Override
	protected ContentLoader delegate() {
		return contentLoader;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#newCriteria()
	 */
	public ContentCriteria newCriteria() {
		final ContentCriteria c = contentLoader.newCriteria();
		transformer.apply(c);
		return c;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#newCriteria(com.isotrol.impe3.api.ContentType)
	 */
	public ContentCriteria newCriteria(ContentType contentType) {
		final ContentCriteria c = contentLoader.newCriteria(contentType);
		transformer.apply(c);
		return c;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#forRepository(com.isotrol.impe3.nr.api.NodeRepository)
	 */
	public ContentLoader forRepository(NodeRepository repository) {
		ContentLoader c = delegate().forRepository(repository);
		return new TransformerContentLoader(c, transformer);
	}

}
