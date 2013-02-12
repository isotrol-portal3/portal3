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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Source;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.core.content.TransformerContentLoader;


/**
 * Immutable implementation of ComponentRequestContext.
 * @author Andres Rodriguez
 */
public final class ImmutableComponentRequestContext extends ImmutablePageRequestContext implements
	ComponentRequestContext {
	private final UUID componentId;
	private final ImmutableMap<String, UUID> registeredActions;
	private final ContentKey contentKey;
	private final NavigationKey navigationKey;
	private final Content content;
	private final Listing<?> listing;
	private final Pagination pagination;
	private final TemplateKey templateKey;
	private final TemplateModel templateModel;
	private final ETag tag;
	private final Source source;
	private final ContentCriteriaTransformer criteriaTransformer;
	private final ContentLoader contentLoader;

	ImmutableComponentRequestContext(PageRequestContext context, UUID componentId, Map<String, UUID> registeredActions,
		PageKey pageKey, LocalParams localParams, ContentKey contentKey, NavigationKey navigationKey, Content content,
		Listing<?> listing, Pagination pagination, TemplateKey templateKey, TemplateModel templateModel, ETag tag,
		Source source, ContentCriteriaTransformer criteriaTransformer) {
		super(context, null, localParams, null);
		this.componentId = checkNotNull(componentId);
		this.registeredActions = ImmutableMap.copyOf(registeredActions);
		this.contentKey = contentKey;
		this.navigationKey = navigationKey;
		this.content = content;
		this.listing = listing;
		this.pagination = pagination;
		this.templateKey = templateKey;
		this.templateModel = templateModel;
		this.tag = tag;
		this.source = source;
		this.criteriaTransformer = Objects.firstNonNull(criteriaTransformer, ContentCriteriaTransformer.identity());
		this.contentLoader = TransformerContentLoader.transform(context.getContentLoader(), this.criteriaTransformer);
	}

	public ContentLoader getContentLoader() {
		return contentLoader;
	}

	public UUID getComponentId() {
		return componentId;
	}

	public ImmutableMap<String, UUID> getRegisteredActions() {
		return registeredActions;
	}

	public URI getActionURI(String name, Multimap<String, Object> parameters) {
		return getActionURI(getRoute(), getComponentId(), name, parameters);
	}

	public URI getAbsoluteActionURI(String name, Multimap<String, Object> parameters) {
		return getAbsoluteActionURI(getRoute(), getComponentId(), name, parameters);
	}

	public URI getRegisteredActionURI(String name, Multimap<String, Object> parameters) {
		final UUID uuid;
		if (registeredActions.containsKey(name)) {
			uuid = registeredActions.get(name);
		} else {
			uuid = new UUID(0l, 0L);
		}
		return getActionURI(getRoute(), uuid, name, parameters);
	}

	public URI getAbsoluteRegisteredActionURI(String name, Multimap<String, Object> parameters) {
		final UUID uuid;
		if (registeredActions.containsKey(name)) {
			uuid = registeredActions.get(name);
		} else {
			uuid = new UUID(0l, 0L);
		}
		return getAbsoluteActionURI(getRoute(), uuid, name, parameters);
	}

	public ContentKey getContentKey() {
		if (content != null) {
			return content.getContentKey();
		}
		return contentKey;
	}

	public NavigationKey getNavigationKey() {
		return navigationKey;
	}

	public Content getContent() {
		return content;
	}

	public Listing<?> getListing() {
		return listing;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public TemplateKey getTemplateKey() {
		return templateKey;
	}

	public TemplateModel getTemplateModel() {
		return templateModel;
	}

	public ETag getETag() {
		return tag;
	}

	public Source getSource() {
		return source;
	}

	Builder builder() {
		return new Builder();
	}

	class Builder implements com.isotrol.impe3.api.Builder<ImmutableComponentRequestContext> {
		private UUID componentId;
		private ImmutableMap<String, UUID> registeredActions;
		private LocalParams localParams;
		private ContentKey contentKey;
		private NavigationKey navigationKey;
		private Content content;
		private Listing<?> listing;
		private Pagination pagination;
		private TemplateKey templateKey;
		private TemplateModel templateModel;
		private ETag tag;
		private Source source;
		private ContentCriteriaTransformer criteriaTransformer;
		private boolean changed = false;

		private Builder() {
			this.componentId = ImmutableComponentRequestContext.this.componentId;
			this.registeredActions = ImmutableComponentRequestContext.this.registeredActions;
			this.localParams = getLocalParams();
			this.contentKey = ImmutableComponentRequestContext.this.contentKey;
			this.navigationKey = ImmutableComponentRequestContext.this.navigationKey;
			this.content = ImmutableComponentRequestContext.this.content;
			this.listing = ImmutableComponentRequestContext.this.listing;
			this.pagination = ImmutableComponentRequestContext.this.pagination;
			this.templateKey = ImmutableComponentRequestContext.this.templateKey;
			this.templateModel = ImmutableComponentRequestContext.this.templateModel;
			this.tag = ImmutableComponentRequestContext.this.tag;
			this.source = ImmutableComponentRequestContext.this.source;
			this.criteriaTransformer = ImmutableComponentRequestContext.this.criteriaTransformer;
		}

		public Builder setComponentId(UUID componentId) {
			if (componentId != this.componentId) {
				this.componentId = checkNotNull(componentId);
				changed = true;
			}
			return this;
		}

		public Builder setRegisteredActions(Map<String, UUID> registeredActions) {
			if (registeredActions != this.registeredActions) {
				this.registeredActions = ImmutableMap.copyOf(registeredActions);
				changed = true;
			}
			return this;
		}

		public Builder setLocalParams(LocalParams localParams) {
			if (localParams != this.localParams) {
				this.localParams = checkNotNull(localParams);
				changed = true;
			}
			return this;
		}

		public Builder setContentKey(ContentKey contentKey) {
			if (contentKey != this.contentKey) {
				this.contentKey = contentKey;
				changed = true;
			}
			return this;
		}

		public Builder setNavigationKey(NavigationKey navigationKey) {
			if (navigationKey != this.navigationKey) {
				this.navigationKey = navigationKey;
				changed = true;
			}
			return this;
		}

		public Builder setContent(Content content) {
			if (content != this.content) {
				this.content = content;
				changed = true;
			}
			return this;
		}

		public Builder setListing(Listing<?> listing) {
			if (listing != this.listing) {
				this.listing = listing;
				changed = true;
			}
			return this;
		}

		public Builder setPagination(Pagination pagination) {
			if (pagination != this.pagination) {
				this.pagination = pagination;
				changed = true;
			}
			return this;
		}

		public Builder setTemplateKey(TemplateKey templateKey) {
			if (templateKey != this.templateKey) {
				this.templateKey = templateKey;
				changed = true;
			}
			return this;
		}

		public Builder setTemplateModel(TemplateModel templateModel) {
			if (templateModel != this.templateModel) {
				this.templateModel = templateModel;
				changed = true;
			}
			return this;
		}

		public Builder setETag(ETag tag) {
			if (tag != this.tag) {
				this.tag = tag;
				changed = true;
			}
			return this;
		}

		public Builder setSource(Source source) {
			if (source != this.source) {
				this.source = source;
				changed = true;
			}
			return this;
		}

		public Builder setCriteriaTransformer(ContentCriteriaTransformer criteriaTransformer) {
			if (criteriaTransformer != null) {
				ContentCriteriaTransformer ncct = this.criteriaTransformer.append(criteriaTransformer);
				if (this.criteriaTransformer != ncct) {
					this.criteriaTransformer = ncct;
					changed = true;
				}
			}
			return this;
		}

		public ImmutableComponentRequestContext get() {
			if (!changed) {
				return ImmutableComponentRequestContext.this;
			}
			return new ImmutableComponentRequestContext(ImmutableComponentRequestContext.this, componentId,
				registeredActions, null, localParams, contentKey, navigationKey, content, listing, pagination,
				templateKey, templateModel, tag, source, criteriaTransformer);
		}
	}

}
