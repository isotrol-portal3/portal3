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


import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.core.modules.ComponentProvision;
import com.isotrol.impe3.pbuf.portal.PortalProtos.ComponentPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.InheritedComponentPB;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.model.ComponentDfn;
import com.isotrol.impe3.pms.model.OverridenComponentValue;
import com.isotrol.impe3.pms.model.PortalDfn;


/**
 * Component domain object.
 * @author Andres Rodriguez
 */
public class ComponentObject extends ModuleObject {
	static Function<ComponentObject, ComponentPB> map2pb(final FileManager fileManager) {
		return new Function<ComponentObject, ComponentPB>() {
			public ComponentPB apply(ComponentObject from) {
				return from.toPB(fileManager);
			}
		};
	}

	static Function<Inherited, InheritedComponentPB> map2ipb(final FileManager fileManager) {
		return new Function<Inherited, InheritedComponentPB>() {
			public InheritedComponentPB apply(Inherited from) {
				return from.toInheritedPB(fileManager);
			}
		};
	}

	/** Populates a domain object from a definition. */
	public static ComponentObject of(ComponentDfn dfn, PortalDfn portalDfn) {
		return new ComponentObject(dfn, portalDfn);
	}

	/**
	 * Constructor.
	 * @param dfn Definition.
	 */
	private ComponentObject(ComponentDfn dfn, PortalDfn portalDfn) {
		super(dfn, portalDfn);
	}

	/**
	 * Overriding constructor.
	 * @param c Component.
	 * @param o Overriding definition.
	 * @param dfn 
	 */
	private ComponentObject(ComponentObject c, OverridenComponentValue o, PortalDfn dfn) {
		super(c, o, dfn);
	}

	ComponentObject.Inherited override(OverridenComponentValue o, PortalDfn dfn) {
		return new ComponentObject.Inherited(this, o, dfn);
	}

	@Override
	State getState() {
		return State.NEW;
	}

	Iterable<PaletteItem> getPalette() {
		return Iterables.transform(getModule().getComponentProvisions().values(),
			new Function<ComponentProvision, PaletteItem>() {
				public PaletteItem apply(ComponentProvision from) {
					return PaletteItem.item(ComponentObject.this, from);
				}
			});
	}

	ComponentPB toPB(FileManager fm) {
		return ComponentPB.newBuilder().setModule(modulePB(fm)).build();
	}

	public static final class Inherited extends ComponentObject {
		/** Tri-state for configuration. */
		private final Boolean configuration;
		/** Tri-state for dependencies. */
		private final Boolean dependencies;

		/**
		 * Constructor.
		 * @param c Component.
		 * @param o Overriding definition.
		 * @param dfn 
		 */
		Inherited(ComponentObject c, OverridenComponentValue o, PortalDfn dfn) {
			super(c, o, dfn);
			if (c.getModule().getConfiguration() == null) {
				this.configuration = null;
			} else {
				this.configuration = (o != null && o.getConfiguration() != null);
			}
			if (c.getModule().getExternalDependencies().isEmpty()) {
				this.dependencies = null;
			} else {
				this.dependencies = (o != null && o.getDependencySet() != null);
			}
		}

		public Boolean overridesConfiguration() {
			return configuration;
		}

		public Boolean overridesDependencies() {
			return dependencies;
		}

		public boolean isOverriden() {
			return Boolean.TRUE.equals(configuration) || Boolean.TRUE.equals(dependencies);
		}

		public InheritedComponentInstanceSelDTO toInheritedDTO(Context2 ctx) {
			InheritedComponentInstanceSelDTO dto = new InheritedComponentInstanceSelDTO();
			dto.setComponent(toSelDTO(ctx.getRegistry(), ctx.getLocale()));
			dto.setConfiguration(configuration);
			dto.setDependencies(dependencies);
			return dto;
		}

		InheritedComponentPB toInheritedPB(FileManager fileManager) {
			boolean c = Boolean.TRUE.equals(configuration);
			boolean d = Boolean.TRUE.equals(dependencies);
			if (!c && !d) {
				return null;
			}
			InheritedComponentPB.Builder b = InheritedComponentPB.newBuilder().setId(getStringId());
			b.setOverridesConfig(c).setOverridesDeps(d);
			if (c) {
				b.setConfiguration(getConfiguration().toPB(fileManager));
			}
			if (d) {
				b.addAllDependencies(dependenciesPB());
			}
			return b.build();
		}

	}
}
