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


import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.base.Function;
import com.isotrol.impe3.pbuf.portal.PortalProtos.LayoutPB;
import com.isotrol.impe3.pms.model.LayoutValue;


/**
 * Layout item domain object.
 * @author Andres Rodriguez
 */
public final class LayoutItemObject {
	static Function<LayoutItemObject, UUID> COMPONENT = new Function<LayoutItemObject, UUID>() {
		public UUID apply(LayoutItemObject from) {
			return from.getComponent();
		}
	};

	/** Item id. */
	private final int id;
	/** Column width. */
	private final Integer width;
	/** Frame name. */
	private final String name;
	/** Component in page. */
	private final UUID component;

	/**
	 * Constructor.
	 * @param entry CIPEntry
	 */
	LayoutItemObject(Entry<Integer, LayoutValue> entry) {
		this.id = entry.getKey();
		final LayoutValue v = entry.getValue();
		this.width = v.getWidth();
		this.name = v.getName();
		this.component = v.getComponent();
	}

	int getId() {
		return id;
	}

	boolean isComponent() {
		return component != null;
	}

	UUID getComponent() {
		return component;
	}

	boolean isColumn() {
		return width != null;
	}

	boolean isFrame() {
		return width == null;
	}

	boolean isColumns() {
		return width == null && component == null;
	}

	String getName() {
		return name;
	}

	Integer getWidth() {
		return width;
	}

	LayoutPB export(LayoutObject layout) {
		LayoutPB.Builder b = LayoutPB.newBuilder();
		if (name != null) {
			b.setName(name);
		}
		if (width != null) {
			b.setWidth(width.intValue());
		}
		if (component != null) {
			b.setCipId(component.toString());
		}
		for (LayoutItemObject lio : layout.getChildren(id)) {
			b.addChildren(lio.export(layout));
		}
		return b.build();
	}

}
