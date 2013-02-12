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

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.sf.derquinsej.i18n.BundleLocalized;
import net.sf.derquinsej.i18n.Localized;

import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.isotrol.impe3.core.component.ComponentType;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.modules.ComponentProvision;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PaletteDTO;


/**
 * Palette item domain object.
 * @author Andres Rodriguez.
 */
public abstract class PaletteItem {
	private static final Space SPACE = new Space();
	private static final Localized<String> SPACE_NAME = new BundleLocalized<String>(PaletteItem.class, "space");
	private static final Localized<String> SPACE_DESC = new BundleLocalized<String>(PaletteItem.class, "spaceDesc");

	private static String merge(String s1, String s2) {
		final boolean t1 = StringUtils.hasText(s1);
		final boolean t2 = StringUtils.hasText(s2);
		if (t1 && t2) {
			if (s1.equalsIgnoreCase(s2)) {
				return s1;
			}
			return s1 + " :: " + s2;
		} else if (t1) {
			return s1;
		}
		return s2;
	}

	static final Function<PaletteItem, PaletteDTO> map2dto(final Locale locale) {
		return new Function<PaletteItem, PaletteDTO>() {
			public PaletteDTO apply(PaletteItem from) {
				return from.toPaletteDTO(locale);
			}
		};
	}

	static final Function<PaletteItem, PaletteKey> KEY = new Function<PaletteItem, PaletteKey>() {
		public PaletteKey apply(PaletteItem from) {
			return from.getKey();
		}
	};

	/**
	 * Builds a palette item.
	 * @param connector Connector Object.
	 * @param bean Exported bean name.
	 * @return The requested object.
	 */
	public static PaletteItem item(ComponentObject component, ComponentProvision provision) {
		return new Item(component, provision);
	}

	/**
	 * Builds the space palette item.
	 * @return The requested object.
	 */
	public static PaletteItem space() {
		return SPACE;
	}

	private PaletteItem() {
	}

	public final boolean isSpace() {
		return getKey().isSpace();
	}

	public abstract boolean isVisual();

	public abstract String getDescription(Context0 ctx);

	/**
	 * Returns the component package instance.
	 * @return The component package instance.
	 */
	public final UUID getInstanceId() {
		return getKey().getInstanceId();
	}

	/**
	 * Returns the provided component bean.
	 * @return The provided component bean.
	 */
	public final String getBean() {
		return getKey().getBean();
	}

	public abstract ConfigurationDefinition<?> getConfigurationDefinition();

	abstract PaletteKey getKey();

	final PaletteDTO toPaletteDTO(Locale locale) {
		PaletteDTO dto = new PaletteDTO();
		dto.setKey(getKey().toComponentKey());
		fill(dto, locale);
		return dto;
	}

	abstract void fill(PaletteDTO dto, Locale locale);

	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	final ComponentInPageTemplateDTO newCIPTemplate(Context1 ctx) {
		final ComponentInPageTemplateDTO dto = new ComponentInPageTemplateDTO();
		dto.setId(ctx.newUUID().toString().toLowerCase());
		final PaletteDTO p = toPaletteDTO(ctx.getLocale());
		dto.setComponent(p);
		dto.setName(p.getName());
		dto.setConfiguration(getConfigurationTemplateDTO(ctx));
		final List<ComponentInPageTemplateDTO> children = Lists.newArrayListWithCapacity(0);
		dto.setChildren(children);
		return dto;
	}

	abstract ConfigurationTemplateDTO getConfigurationTemplateDTO(Context1 ctx);

	private static final class Space extends PaletteItem {
		private Space() {
		}

		@Override
		public boolean isVisual() {
			return true;
		}

		@Override
		public String getDescription(Context0 ctx) {
			return SPACE_DESC.get(ctx.getLocale());
		}

		@Override
		PaletteKey getKey() {
			return PaletteKey.space();
		}

		@Override
		void fill(PaletteDTO dto, Locale locale) {
			dto.setName(SPACE_NAME.get(locale));
			dto.setDescription(SPACE_DESC.get(locale));
		}

		@Override
		public boolean equals(Object obj) {
			return this == SPACE;
		}

		@Override
		public ConfigurationDefinition<?> getConfigurationDefinition() {
			return null;
		}

		@Override
		ConfigurationTemplateDTO getConfigurationTemplateDTO(Context1 ctx) {
			return null;
		}

	}

	private static final class Item extends PaletteItem {
		/** Item key. */
		private final PaletteKey key;
		/** Component instance. */
		private final ComponentObject component;
		/** Component provision. */
		private final ComponentProvision provision;

		/**
		 * Default constructor.
		 * @param component Component instance.
		 * @param provision Component provision.
		 */
		private Item(ComponentObject component, ComponentProvision provision) {
			this.component = checkNotNull(component);
			this.provision = checkNotNull(provision);
			this.key = PaletteKey.item(component.getId(), provision.getBeanName());
		}

		@Override
		public boolean isVisual() {
			return (provision.getComponent().getComponentType() == ComponentType.VISUAL);
		}

		@Override
		public String getDescription(Context0 ctx) {
			return component.getDescription();
		}

		@Override
		PaletteKey getKey() {
			return key;
		}

		@Override
		void fill(PaletteDTO dto, Locale locale) {
			String name = component.getName();
			String description = component.getDescription();
			if (!component.getModule().isSimple()) {
				name = merge(name, provision.getName().get(locale));
				description = merge(description, provision.getDescription().get(locale));
			}
			dto.setName(name);
			dto.setDescription(description);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Item) {
				return key.equals(((Item) obj).key);
			}
			return false;
		}

		@Override
		public ConfigurationDefinition<?> getConfigurationDefinition() {
			return provision.getComponent().getConfiguration();
		}

		@Override
		ConfigurationTemplateDTO getConfigurationTemplateDTO(Context1 ctx) {
			ConfigurationDefinition<?> cd = getConfigurationDefinition();
			return cd != null ? ConfigurationObject.template(cd, ctx) : null;
		}
	}
}
