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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.sf.derquinsej.collect.ForwardingHierarchy;
import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.core.Column;
import com.isotrol.impe3.core.Frame;
import com.isotrol.impe3.pbuf.portal.PortalProtos.LayoutPB;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.ColumnsFrameDTO;
import com.isotrol.impe3.pms.api.page.ComponentFrameDTO;
import com.isotrol.impe3.pms.api.page.FillFrameDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.LayoutValue;
import com.isotrol.impe3.pms.model.PageDfn;


/**
 * Domain object for the tree of CIPs in a page.
 * @author Andres Rodriguez
 */
public final class LayoutObject extends ForwardingHierarchy<Integer, LayoutItemObject> {
	private static final Ordering<Entry<Integer, LayoutValue>> BY_ORDER = Ordering.natural().onResultOf(
		new Function<Entry<Integer, LayoutValue>, Integer>() {
			public Integer apply(Entry<Integer, LayoutValue> from) {
				return from.getValue().getPosition();
			}
		});

	private final ImmutableHierarchy<Integer, LayoutItemObject> hierarchy;

	/**
	 * Builds a collection from a page definitions.
	 * @param dfn Page definition.
	 * @return The requested collection.
	 */
	public static LayoutObject of(PageDfn dfn) {
		return new LayoutObject(dfn.getLayout().entrySet());
	}

	/**
	 * Builds a collection from a page definitions.
	 * @param lvs Layout values.
	 * @return The requested collection.
	 */
	public static LayoutObject of(Iterable<Entry<Integer, LayoutValue>> lvs) {
		return new LayoutObject(lvs);
	}

	/**
	 * Constructor.
	 * @param lvs Layout values.
	 */
	private LayoutObject(Iterable<Entry<Integer, LayoutValue>> lvs) {
		final Iterable<Entry<Integer, LayoutValue>> ordered = BY_ORDER.sortedCopy(lvs);
		ImmutableHierarchy.Builder<Integer, LayoutItemObject> builder = ImmutableHierarchy.builder();
		for (Entry<Integer, LayoutValue> entry : ordered) {
			builder.add(entry.getKey(), new LayoutItemObject(entry), entry.getValue().getParent());
		}
		hierarchy = builder.get();
	}

	/**
	 * Copy constructor.
	 * @param hierarchy Source hierarchy.
	 */
	LayoutObject(ImmutableHierarchy<Integer, LayoutItemObject> hierarchy) {
		this.hierarchy = checkNotNull(hierarchy);
	}

	@Override
	protected Hierarchy<Integer, LayoutItemObject> delegate() {
		return hierarchy;
	}

	public boolean isUsed(UUID componentId) {
		return any(transform(values(), LayoutItemObject.COMPONENT), equalTo(checkNotNull(componentId)));
	}

	public List<FrameDTO> toDTO() {
		return new Framer(null, null).getFrames();
	}

	List<FrameDTO> toDTO(UUID spaceId, FillFrameDTO fill) {
		return new Framer(spaceId, fill).getFrames();
	}

	ImmutableList<Frame> start() {
		return new Starter().start();
	}

	Iterable<LayoutPB> export() {
		return Iterables.transform(getFirstLevel(), new Function<LayoutItemObject, LayoutPB>() {
			public LayoutPB apply(LayoutItemObject from) {
				return from.export(LayoutObject.this);
			}
		});
	}

	private final class Framer {
		final UUID spaceId;
		final FillFrameDTO fill;
		final boolean inherited;

		Framer(UUID spaceId, FillFrameDTO fill) {
			this.inherited = fill != null;
			checkArgument(inherited == (spaceId != null));
			this.spaceId = spaceId;
			this.fill = fill;
		}

		Function<Integer, FrameDTO> toFrameDTO = new Function<Integer, FrameDTO>() {
			public FrameDTO apply(Integer from) {
				final LayoutItemObject item = checkNotNull(get(from));
				if (inherited && item.isComponent() && spaceId.equals(item.getComponent())) {
					return fill;
				}
				checkState(item.isFrame());
				final FrameDTO frame;
				if (item.isComponent()) {
					final ComponentFrameDTO dto = new ComponentFrameDTO();
					dto.setComponent(item.getComponent().toString().toLowerCase());
					frame = dto;
				} else {
					final ColumnsFrameDTO dto = new ColumnsFrameDTO();
					dto.setChildren(Mappers.list(getChildrenKeys(from), toColumnDTO));
					frame = dto;
				}
				frame.setName(item.getName());
				frame.setInherited(inherited);
				return frame;
			}
		};

		Function<Integer, ColumnDTO> toColumnDTO = new Function<Integer, ColumnDTO>() {
			public ColumnDTO apply(Integer from) {
				final LayoutItemObject item = checkNotNull(get(from));
				checkState(item.isColumn());
				final ColumnDTO dto = new ColumnDTO();
				dto.setName(item.getName());
				dto.setWidth(item.getWidth());
				dto.setFrames(Mappers.list(getChildrenKeys(from), toFrameDTO));
				dto.setInherited(inherited);
				return dto;
			}
		};

		List<FrameDTO> getFrames() {
			return Mappers.list(getFirstLevelKeys(), toFrameDTO);
		}

	}

	private final class Starter {
		Starter() {
		}

		private Function<Integer, Frame> toFrame = new Function<Integer, Frame>() {
			public Frame apply(Integer from) {
				final LayoutItemObject item = checkNotNull(get(from));
				checkState(item.isFrame());
				if (item.isComponent()) {
					return Frame.component(item.getName(), item.getComponent());
				} else {
					final List<Integer> columnKeys = getChildrenKeys(from);
					if (columnKeys.isEmpty()) {
						return null;
					}
					final Iterable<Column> columns = transform(getChildrenKeys(from), toColumn);
					return Frame.columns(item.getName(), columns);
				}
			}
		};

		private Function<Integer, Column> toColumn = new Function<Integer, Column>() {
			public Column apply(Integer from) {
				final LayoutItemObject item = checkNotNull(get(from));
				checkState(item.isColumn());
				final List<Frame> frames = frames(getChildrenKeys(from));
				return new Column(item.getName(), item.getWidth(), frames);
			}
		};

		private ImmutableList<Frame> frames(Iterable<Integer> from) {
			return ImmutableList.copyOf(filter(transform(from, toFrame), notNull()));
		}

		ImmutableList<Frame> start() {
			return frames(getFirstLevelKeys());
		}

	}

}
