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
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.COMPONENT;

import java.util.UUID;

import com.google.common.base.Objects;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.model.CIPValue;


/**
 * Palette key value.
 * @author Andres Rodriguez.
 */
abstract class PaletteKey {
	private static final Space SPACE = new Space();

	/**
	 * Builds a palette key.
	 * @param k Component key.
	 * @return The requested object.
	 */
	static PaletteKey fromComponentKey(ComponentKey k) throws PMSException {
		COMPONENT.checkNotNull(k, (UUID) null);
		final UUID uuid = COMPONENT.toUUID(k.getInstanceId());
		return new Item(uuid, COMPONENT.checkNotNull(k.getBean(), uuid));
	}

	/**
	 * Builds a palette key.
	 * @param v CIP Value.
	 * @return The requested object.
	 */
	static PaletteKey fromCIPValue(CIPValue v) throws PMSException {
		COMPONENT.checkNotNull(v, (UUID) null);
		if (v.isSpace()) {
			return space();
		}
		return new Item(v.getInstanceId(), v.getBean());
	}

	/**
	 * Builds a palette key.
	 * @param instanceId Component instance id.
	 * @param bean Exported bean name.
	 * @return The requested object.
	 */
	static PaletteKey item(UUID instanceId, String bean) {
		return new Item(instanceId, bean);
	}

	/**
	 * Builds the space palette item.
	 * @return The requested object.
	 */
	public static PaletteKey space() {
		return SPACE;
	}

	private PaletteKey() {
	}

	abstract boolean isSpace();

	/**
	 * Returns the component package instance.
	 * @return The component package instance.
	 */
	abstract UUID getInstanceId();

	/**
	 * Returns the provided component bean.
	 * @return The provided component bean.
	 */
	abstract String getBean();

	abstract ComponentKey toComponentKey();

	private static final class Space extends PaletteKey {
		private Space() {
		}

		@Override
		boolean isSpace() {
			return true;
		}

		/**
		 * Returns the component package instance.
		 * @return The component package instance.
		 */
		UUID getInstanceId() {
			return null;
		}

		/**
		 * Returns the provided component bean.
		 * @return The provided component bean.
		 */
		String getBean() {
			return null;
		}

		@Override
		ComponentKey toComponentKey() {
			return null;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return this == SPACE;
		}

	}

	private static final class Item extends PaletteKey {
		/** Component package instance. */
		private UUID instanceId;
		/** Provided component. */
		private String bean;

		/**
		 * Default constructor.
		 * @param instanceId Component instance id.
		 * @param bean Exported bean name.
		 */
		private Item(UUID instanceId, String bean) {
			this.instanceId = checkNotNull(instanceId);
			this.bean = checkNotNull(bean);
		}

		@Override
		boolean isSpace() {
			return false;
		}

		/**
		 * Returns the component package instance.
		 * @return The component package instance.
		 */
		public UUID getInstanceId() {
			return instanceId;
		}

		/**
		 * Returns the provided component bean.
		 * @return The provided component bean.
		 */
		public String getBean() {
			return bean;
		}

		@Override
		ComponentKey toComponentKey() {
			ComponentKey dto = new ComponentKey();
			dto.setInstanceId(instanceId.toString());
			dto.setBean(bean);
			return dto;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(instanceId, bean);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Item) {
				final Item i = (Item) obj;
				return instanceId.equals(i.instanceId) && bean.equals(i.bean);
			}
			return false;
		}

	}
}
