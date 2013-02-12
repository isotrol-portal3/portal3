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


import static com.isotrol.impe3.pms.core.obj.MessageMappers.provisionPB;

import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.ComponentInstance;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.modules.ComponentProvision;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pbuf.portal.PortalProtos.CipPB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.model.CIPValue;
import com.isotrol.impe3.pms.model.ComponentEntity;


/**
 * CIP domain object.
 * @author Andres Rodriguez
 */
public final class CIPObject extends AbstractIdentifiable {
	static final Predicate<CIPObject> SPACE = new Predicate<CIPObject>() {
		public boolean apply(CIPObject input) {
			return input.isSpace();
		}
	};

	/** Palette key. */
	private final PaletteKey key;
	/** Component Configuration definition. */
	private final ConfigurationDefinition<?> configurationDfn;
	/** Component Configuration. */
	private final ConfigurationObject configuration;
	/** Instance name. */
	private final String name;

	/**
	 * Constructor.
	 * @param entry CIPEntry
	 */
	CIPObject(Entry<UUID, CIPValue> entry) {
		super(entry.getKey());
		final CIPValue v = entry.getValue();
		final ComponentEntity entity = v.getComponent();
		if (v.isSpace()) {
			this.key = PaletteKey.space();
			this.configurationDfn = null;
			this.configuration = null;
		} else {
			this.key = PaletteKey.item(v.getInstanceId(), v.getBean());
			final ModuleDefinition<?> md = entity.getModuleDefinition();
			final String bean = v.getBean();
			final ComponentProvision cp = md.getComponentProvisions().get(bean);
			if (cp == null) {
				String msg = String.format("CIP [%s] references unknown component provision %s#%s", entry.getKey(), md
					.getType().getName(), bean);
				Loggers.core().error(msg);
				throw new IllegalStateException(msg);
			}
			this.configurationDfn = cp.getComponent().getConfiguration();
			this.configuration = ConfigurationObject.of(this.configurationDfn, v.getConfiguration());
		}
		this.name = v.getName();
	}

	public PaletteKey getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public boolean isSpace() {
		return key.isSpace();
	}

	public boolean isSameItem(PaletteItem item) {
		return item != null && key.equals(item.getKey());
	}

	ComponentInPageTemplateDTO toTemplate(ContextPortal ctx) throws PMSException {
		final ComponentInPageTemplateDTO dto = new ComponentInPageTemplateDTO();
		dto.setId(getId().toString().toLowerCase());
		final PaletteItem ref;
		if (isSpace()) {
			ref = PaletteItem.space();
		} else {
			ref = ctx.getPaletteItem(key);
		}
		final PaletteDTO p = ref.toPaletteDTO(ctx.getLocale());
		dto.setComponent(p);
		dto.setName(name);
		if (configuration != null) {
			dto.setConfiguration(configuration.toTemplateDTO(ctx));
		} else if (configurationDfn != null) {
			dto.setConfiguration(ConfigurationObject.template(configurationDfn, ctx));
		}
		return dto;
	}

	/**
	 * Returns whether a content type is used by this CIP.
	 * @param id Content type id.
	 * @return True if the content type is used by this CIP.
	 */
	public boolean isContentTypeUsed(UUID id) {
		return configuration != null ? configuration.isContentTypeUsed(id) : false;
	}

	/**
	 * Returns whether a category is used by this CIP.
	 * @param id Category id.
	 * @return True if the category is used by this CIP.
	 */
	public boolean isCategoryUsed(UUID id) {
		return configuration != null ? configuration.isCategoryUsed(id) : false;
	}

	CIP start(BaseModel model, StartedComponents components) {
		final ComponentInstance ci = new ComponentInstance(components.apply(key.getInstanceId()), key.getBean());
		final Configuration c;
		if (configuration != null) {
			c = configuration.get(model);
		} else if (configurationDfn != null) {
			c = configurationDfn.builder().get();
		} else {
			c = null;
		}
		return new CIP(getId(), ci, c);
	}

	CipPB export(FileManager fileManager, CIPsObject cips) {
		final CipPB.Builder b = CipPB.newBuilder().setCipId(getStringId()).setName(name);
		if (!isSpace()) {
			b.setComponent(provisionPB(key.getInstanceId(), key.getBean()));
			if (configuration != null) {
				b.setConfiguration(configuration.toPB(fileManager));
			}
			for (CIPObject child : cips.getChildren(getId())) {
				b.addChildren(child.export(fileManager, cips));
			}
		}
		return b.build();
	}

}
