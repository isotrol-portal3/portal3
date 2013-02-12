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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.collect.Iterables.transform;

import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.isotrol.impe3.pms.core.ContentTypeManager;
import com.isotrol.impe3.pms.core.obj.ContentTypesObject;
import com.isotrol.impe3.pms.model.ContentTypeDfn;
import com.isotrol.impe3.pms.model.ContentTypeEdition;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Content type component implementation.
 * @author Andres Rodriguez.
 */
@Component
public class ContentTypeManagerImpl extends AbstractStateLoaderComponent<ContentTypesObject> implements
	ContentTypeManager {
	private static final Function<ContentTypeEdition, ContentTypeDfn> CONTENT_TYPE = new Function<ContentTypeEdition, ContentTypeDfn>() {
		public ContentTypeDfn apply(ContentTypeEdition from) {
			return from.getPublished();
		};
	};

	/** Loader. */
	private final ContentTypesLoader loader;

	/**
	 * Default constructor.
	 */
	public ContentTypeManagerImpl() {
		this.loader = new ContentTypesLoader();
	}

	@Override
	ContentTypesLoader getLoader() {
		return loader;
	}

	@Override
	int getOfflineVersion(EnvironmentEntity e) {
		return e.getContentTypeVersion();
	}

	private class ContentTypesLoader implements Loader<ContentTypesObject> {

		public ContentTypesObject load(EnvironmentEntity e) {
			return ContentTypesObject.current(getDao().getOfflineContentTypes(e.getId()));
		}

		public ContentTypesObject load(EditionEntity e) {
			return ContentTypesObject.definitions(transform(e.getContentTypes(), CONTENT_TYPE));
		}

		@Override
		public String toString() {
			return "Content Types";
		}
	}

}
