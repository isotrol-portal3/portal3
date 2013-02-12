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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.any;

import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchies;
import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.HierarchyVisitor;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComputationException;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.impl.AbstractIdentifiableHierarchy;
import com.isotrol.impe3.pbuf.portal.PortalProtos.CipPB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.CIPValue;
import com.isotrol.impe3.pms.model.PageDfn;


/**
 * Domain object for the tree of CIPs in a page.
 * @author Andres Rodriguez
 */
public final class CIPsObject extends AbstractIdentifiableHierarchy<CIPObject> {
	private static final Ordering<Entry<UUID, CIPValue>> BY_ORDER = Ordering.natural().onResultOf(
		new Function<Entry<UUID, CIPValue>, Integer>() {
			public Integer apply(Entry<UUID, CIPValue> from) {
				return from.getValue().getPosition();
			}
		});

	private final ImmutableHierarchy<UUID, CIPObject> hierarchy;

	/**
	 * Builds a collection from a page definitions.
	 * @param dfn Page definition.
	 * @return The requested collection.
	 */
	public static CIPsObject of(PageDfn dfn) {
		return new CIPsObject(dfn);
	}

	/**
	 * Constructor.
	 * @param dfn Page definition.
	 */
	private CIPsObject(PageDfn dfn) {
		final Iterable<Entry<UUID, CIPValue>> ordered = BY_ORDER.sortedCopy(dfn.getComponents().entrySet());
		ImmutableHierarchy.Builder<UUID, CIPObject> builder = ImmutableHierarchy.builder();
		for (Entry<UUID, CIPValue> entry : ordered) {
			builder.add(entry.getKey(), new CIPObject(entry), entry.getValue().getParent());
		}
		hierarchy = builder.get();
	}

	/**
	 * Copy constructor.
	 * @param hierarchy Source hierarchy.
	 */
	CIPsObject(ImmutableHierarchy<UUID, CIPObject> hierarchy) {
		this.hierarchy = checkNotNull(hierarchy);
	}

	@Override
	protected Hierarchy<UUID, CIPObject> delegate() {
		return hierarchy;
	}

	CIPObject getSpace() {
		try {
			return Iterables.find(values(), CIPObject.SPACE);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	UUID getSpaceId() {
		CIPObject space = getSpace();
		return space != null ? space.getId() : null;
	}

	/**
	 * Returns whether a content type is used by this collection.
	 * @param id Content type id.
	 * @return True if the content type is used by this collection.
	 */
	public boolean isContentTypeUsed(final UUID id) {
		return any(values(), new Predicate<CIPObject>() {
			public boolean apply(CIPObject input) {
				return input.isContentTypeUsed(id);
			}
		});
	}

	/**
	 * Returns whether a category is used by this module.
	 * @param id Category id.
	 * @return True if the category is used by this module.
	 */
	public boolean isCategoryUsed(final UUID id) {
		return any(values(), new Predicate<CIPObject>() {
			public boolean apply(CIPObject input) {
				return input.isCategoryUsed(id);
			}
		});
	}

	List<ComponentInPageTemplateDTO> toTemplate(final ContextPortal ctx) {
		final Function<UUID, ComponentInPageTemplateDTO> toCIP = new Function<UUID, ComponentInPageTemplateDTO>() {
			public ComponentInPageTemplateDTO apply(UUID from) {
				try {
					final CIPObject cip = hierarchy.get(from);
					final ComponentInPageTemplateDTO dto = cip.toTemplate(ctx);
					dto.setChildren(Mappers.list(hierarchy.getChildrenKeys(from), this));
					return dto;
				} catch (PMSException e) {
					throw new ComputationException(e);
				}
			}
		};
		return Mappers.list(hierarchy.getFirstLevelKeys(), toCIP);
	}

	public ImmutableHierarchy<UUID, CIP> start(final BaseModel model, final StartedComponents components) {
		final ImmutableHierarchy.Builder<UUID, CIP> builder = ImmutableHierarchy.builder();
		final HierarchyVisitor<UUID, CIPObject> v = new HierarchyVisitor<UUID, CIPObject>() {
			public void visit(UUID key, CIPObject value, UUID parentKey, int position) {
				if (!value.isSpace()) {
					builder.add(key, value.start(model, components), hierarchy.getParentKey(key));
				}
			}
		};
		Hierarchies.visitDepthFirst(hierarchy, v);
		return builder.get();
	}

	Iterable<CipPB> export(final FileManager fileManager) {
		return Iterables.transform(getFirstLevel(), new Function<CIPObject, CipPB>() {
			public CipPB apply(CIPObject from) {
				return from.export(fileManager, CIPsObject.this);
			}
		});
	}

}
