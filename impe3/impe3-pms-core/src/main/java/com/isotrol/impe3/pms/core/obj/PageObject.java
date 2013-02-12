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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.isotrol.impe3.api.Identifiables.toStringId;
import static com.isotrol.impe3.pms.core.obj.ObjectFunctions.id;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.Frame;
import com.isotrol.impe3.core.Page;
import com.isotrol.impe3.core.PageMapKey;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagePB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagePB.PageClassPB;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.FillFrameDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.support.PageMapKeySupport;
import com.isotrol.impe3.pms.model.PageDfn;
import com.isotrol.impe3.pms.model.PageEntity;


/**
 * Page domain object.
 * @author Andres Rodriguez
 */
public final class PageObject extends WithDefinitionObject {
	private static final ImmutableBiMap<PageClass, PageClassPB> CLASSES_BIMAP = ImmutableBiMap
		.<PageClass, PageClassPB> builder().put(PageClass.SPECIAL, PageClassPB.SPECIAL)
		.put(PageClass.ERROR, PageClassPB.ERROR).put(PageClass.CATEGORY, PageClassPB.CATEGORY)
		.put(PageClass.TAG, PageClassPB.TAG).put(PageClass.CONTENT, PageClassPB.CONTENT)
		.put(PageClass.TEMPLATE, PageClassPB.TEMPLATE).put(PageClass.MAIN, PageClassPB.MAIN)
		.put(PageClass.DEFAULT, PageClassPB.DEFAULT).put(PageClass.CONTENT_TYPE, PageClassPB.CONTENT_TYPE).build();

	public static final PageClassPB toPageClassPB(PageClass pc) {
		return checkNotNull(CLASSES_BIMAP.get(pc));
	}

	public static final PageClass toPageClass(PageClassPB pc) {
		return checkNotNull(CLASSES_BIMAP.inverse().get(pc));
	}

	static final Function<PageObject, UUID> DEVICE = new Function<PageObject, UUID>() {
		public UUID apply(PageObject from) {
			return from.device;
		}
	};

	static final Function<PageObject, PageClass> PAGE_CLASS = new Function<PageObject, PageClass>() {
		public PageClass apply(PageObject from) {
			return from.pageClass;
		}
	};

	static final Function<PageObject, String> NAME = new Function<PageObject, String>() {
		public String apply(PageObject from) {
			return from.name;
		}
	};

	static final Function<PageObject, UUID> CATEGORY = new Function<PageObject, UUID>() {
		public UUID apply(PageObject from) {
			return from.category;
		}
	};

	static final Function<PageObject, UUID> CONTENT_TYPE = new Function<PageObject, UUID>() {
		public UUID apply(PageObject from) {
			return from.contentType;
		}
	};

	static final Function<PageObject, UUID> TEMPLATE = new Function<PageObject, UUID>() {
		public UUID apply(PageObject from) {
			return from.templateId;
		}
	};

	static final Predicate<PageObject> UMBRELLA = new Predicate<PageObject>() {
		public boolean apply(PageObject input) {
			return input.umbrella;
		}
	};

	static Function<PageObject, Inherited<PageLoc>> map2loc(final UUID portalId) {
		return new Function<PageObject, Inherited<PageLoc>>() {
			public Inherited<PageLoc> apply(PageObject from) {
				return from.toLoc(portalId);
			}

		};
	}

	static final Predicate<PageObject> inherited(final UUID portalId) {
		checkNotNull(portalId);
		return new Predicate<PageObject>() {
			public boolean apply(PageObject input) {
				return input.isInherited(portalId);
			}
		};
	}

	public static Function<PageObject, Inherited<PageSelDTO>> map2sel(final UUID portalId) {
		return new Function<PageObject, Inherited<PageSelDTO>>() {
			public Inherited<PageSelDTO> apply(PageObject from) {
				return from.toSelDTO(portalId);
			}

		};
	}

	/** Page detail loader. */
	private final PageDetailLoader detailLoader;
	/** Portal Id. */
	private final UUID portal;
	/** Definition version. */
	private final int version;
	/** Page key. */
	private final PageMapKey key;
	/** Device Id. */
	private final UUID device;
	private final PageClass pageClass;
	private final String name;
	private final String description;
	private final UUID category;
	private final UUID contentType;
	private final boolean umbrella;
	private final String tag;
	private final UUID templateId;

	/** CIPs. */
	private volatile CIPsObject cips = null;
	/** Layout. */
	private volatile LayoutObject layout = null;

	/**
	 * Constructor.
	 * @param detailLoader Page detail loader.
	 * @param dfn Definition.
	 */
	public PageObject(PageDetailLoader detailLoader, PageDfn dfn) {
		super(dfn.getPage().getId(), dfn.getId());
		final PageEntity page = dfn.getPage();
		this.portal = page.getPortal().getId();
		this.detailLoader = checkNotNull(detailLoader);
		this.version = dfn.getVersion();
		this.key = PageMapKeySupport.of(dfn);
		this.device = id(dfn.getPage().getDevice());
		this.pageClass = dfn.getPage().getPageClass();
		this.name = dfn.getName();
		this.description = dfn.getDescription();
		this.category = id(dfn.getCategory());
		this.contentType = id(dfn.getContentType());
		this.umbrella = dfn.isUmbrella();
		this.tag = dfn.getTag();
		this.templateId = id(dfn.getTemplate());
	}

	public Key getCacheKey() {
		return new Key(getDefinitionId(), version);
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns the page key.
	 * @return The page key.
	 */
	public PageMapKey getKey() {
		return key;
	}

	/**
	 * Returns the page class.
	 * @return The page class.
	 */
	public PageClass getPageClass() {
		return pageClass;
	}

	/**
	 * Returns the page template id.
	 * @return The page template id.
	 */
	UUID getTemplateId() {
		return templateId;
	}

	public CIPsObject getCIPs() {
		if (cips == null) {
			cips = detailLoader.loadCIPs(this);
		}
		return cips;
	}

	LayoutObject loadLayout() {
		if (layout == null) {
			layout = detailLoader.loadLayout(this);
		}
		return layout;
	}

	CIPsObject getFullCIPs(DevicePagesObject pages) throws PMSException {
		final PageObject template = pages.getTemplate(this);
		if (template == null) {
			return getCIPs();
		}
		final ImmutableHierarchy.Builder<UUID, CIPObject> b = ImmutableHierarchy.builder();
		final CIPsObject tplCIPs = template.getFullCIPs(pages);
		loadCIPs(b, tplCIPs, tplCIPs.getFirstLevel(), null, false);
		return new CIPsObject(b.get());
	}

	private void loadCIPs(ImmutableHierarchy.Builder<UUID, CIPObject> builder, Hierarchy<UUID, CIPObject> h,
		Iterable<CIPObject> cips, UUID parent, boolean own) {
		for (CIPObject cip : cips) {
			if (cip.isSpace() && !own) {
				final CIPsObject ownCIPs = getCIPs();
				loadCIPs(builder, ownCIPs, ownCIPs.getFirstLevel(), parent, true);
			} else {
				checkState(!cip.isSpace() || pageClass == PageClass.TEMPLATE);
				builder.add(cip.getId(), cip, parent);
				if (!cip.isSpace()) {
					loadCIPs(builder, h, h.getChildren(cip.getId()), cip.getId(), own);
				}
			}
		}
	}

	LayoutObject getFullLayout(DevicePagesObject pages) throws PMSException {
		final PageObject template = pages.getTemplate(this);
		if (template == null) {
			return loadLayout();
		}
		final ImmutableHierarchy.Builder<Integer, LayoutItemObject> b = ImmutableHierarchy.builder();
		final LayoutObject tplLayout = template.getFullLayout(pages);
		loadFullLayout(new AtomicInteger(), b, tplLayout, tplLayout.getFirstLevelKeys(), null, template.getCIPs()
			.getSpaceId());
		return new LayoutObject(b.get());
	}

	private void loadFullLayout(AtomicInteger i, ImmutableHierarchy.Builder<Integer, LayoutItemObject> builder,
		Hierarchy<Integer, LayoutItemObject> h, Iterable<Integer> itemKeys, Integer parent, UUID spaceId) {
		for (Integer itemKey : itemKeys) {
			final LayoutItemObject item = checkNotNull(h.get(itemKey), "Should not happend");
			if (spaceId != null && item.isComponent() && item.getComponent().equals(spaceId)) {
				final LayoutObject own = loadLayout();
				loadFullLayout(i, builder, own, own.getFirstLevelKeys(), parent, null);
			} else {
				final int key = i.incrementAndGet();
				builder.add(key, item, parent);
				loadFullLayout(i, builder, h, h.getChildrenKeys(itemKey), key, spaceId);
			}
		}
	}

	Page start(DevicePagesObject pages, BaseModel model, StartedComponents components) throws PMSException {
		ImmutableHierarchy<UUID, CIP> cips = getFullCIPs(pages).start(model, components);
		final ImmutableList<Frame> frames = getFullLayout(pages).start();
		final Device device = model.getDevices().get(this.device);
		return new Page(getId(), device, key, cips, frames);
	}

	/**
	 * Returns whether a content type is used by this module.
	 * @param id Content type id.
	 * @return True if the content type is used by this module.
	 */
	public boolean isContentTypeUsed(UUID id) {
		return (contentType != null && contentType.equals(id)) || getCIPs().isContentTypeUsed(id);
	}

	/**
	 * Returns whether a category is used by this module.
	 * @param id Category id.
	 * @return True if the category is used by this module.
	 */
	public boolean isCategoryUsed(UUID id) {
		return (category != null && category.equals(id)) || getCIPs().isCategoryUsed(id);
	}

	private <T extends PageLoc> T fillLoc(T dto, UUID portalId) {
		dto.setPortalId(toStringId(portalId));
		dto.setDeviceId(toStringId(device));
		dto.setId(getStringId());
		return dto;
	}

	private <T extends PageSelDTO> T fillSel(T dto, UUID portalId) {
		dto.setPageClass(pageClass);
		dto.setName(name);
		dto.setDescription(description);
		return fillLoc(dto, portalId);
	}

	PageLoc toPageLoc(UUID portalId) {
		return fillLoc(new PageLoc(), portalId);
	}

	public Inherited<PageLoc> toLoc(UUID portalId) {
		return new Inherited<PageLoc>(isInherited(portalId), toPageLoc(portalId));
	}

	PageSelDTO toPageSelDTO(UUID portalId) {
		return fillSel(new PageSelDTO(), portalId);
	}

	final boolean isInherited(UUID portalId) {
		return !equal(portalId, this.portal);
	}

	public Inherited<PageSelDTO> toSelDTO(UUID portalId) {
		return new Inherited<PageSelDTO>(isInherited(portalId), toPageSelDTO(portalId));
	}

	PageTemplateDTO toDTO(ContextDevicePages ctx) throws PMSException {
		final PageTemplateDTO dto = ctx.newTemplate(getId());
		dto.setId(getId().toString().toLowerCase());
		dto.setPageClass(pageClass);
		PageObject template = ctx.getPages().getTemplate(this);
		if (template != null) {
			dto.setTemplate(template.toPageSelDTO(ctx.getPortalId()));
		}
		if (category != null) {
			dto.setCategory(ctx.getCategories().load(category).toSelDTO());
		}
		if (contentType != null) {
			dto.setContentType(ctx.getContentTypes().load(contentType).toSelDTO());
		}
		dto.setName(name);
		dto.setDescription(description);
		dto.setUmbrella(umbrella);
		dto.setComponents(getCIPs().toTemplate(ctx));
		return dto;
	}

	List<FrameDTO> getLayout(DevicePagesObject pages) throws PMSException {
		List<FrameDTO> own = loadLayout().toDTO();
		final PageObject template = pages.getTemplate(this);
		if (template == null) {
			return own;
		}
		final FillFrameDTO fill = new FillFrameDTO();
		fill.setInherited(false);
		fill.setFrames(own);
		final UUID spaceId = template.getCIPs().getSpaceId();
		return template.getFullLayout(pages).toDTO(spaceId, fill);
	}

	PagePB export(FileManager fileManager, DevicePagesObject pages) throws PMSException {
		final PagePB.Builder b = PagePB.newBuilder().setPageClass(toPageClassPB(pageClass));
		b.setDeviceId(device.toString()).setUmbrella(umbrella);
		if (description != null) {
			b.setDescription(description);
		}
		if (name != null) {
			b.setName(name);
		}
		if (category != null) {
			b.setCategoryId(category.toString());
		}
		if (contentType != null) {
			b.setContentTypeId(contentType.toString());
		}
		if (tag != null) {
			b.setTagName(tag);
		}
		final PageObject template = pages.getTemplate(this);
		if (template != null) {
			b.setTemplateName(template.getName());
		}
		if (getCIPs() != null) {
			b.addAllCips(getCIPs().export(fileManager));
		}
		if (loadLayout() != null) {
			b.addAllLayout(loadLayout().export());
		}
		return b.build();
	}

	/**
	 * Page item loading cache key.
	 * @author Andres Rodriguez
	 */
	public static final class Key {
		private final UUID id;
		private final int version;
		private final int hash;

		private Key(UUID id, int version) {
			this.id = checkNotNull(id);
			this.version = version;
			this.hash = Objects.hashCode(id, version);
		}

		public UUID getId() {
			return id;
		}

		public Integer getVersion() {
			return version;
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Key) {
				final Key k = (Key) obj;
				return hash == k.hash && equal(version, k.version) && equal(id, k.id);
			}
			return false;
		}

	}

}
