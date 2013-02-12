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

package com.isotrol.impe3.core;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.component.ActionContext;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.core.component.ComponentDefinition;
import com.isotrol.impe3.core.component.ComponentType;
import com.isotrol.impe3.core.component.DirectInjectors;
import com.isotrol.impe3.core.component.Renderers;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.StartedModule;


/**
 * A component instance.
 * @author Andres Rodriguez
 */
public final class ComponentInstance {
	private final StartedModule<?> module;
	private final String bean;
	private final ComponentDefinition<?> definition;

	public ComponentInstance(final StartedModule<?> module, final String bean) {
		this.module = checkNotNull(module);
		this.bean = checkNotNull(bean);
		this.definition = module.getModuleDefinition().getComponentProvisions().get(bean).getComponent();
	}

	public ComponentDefinition<?> getDefinition() {
		return definition;
	}

	public ConfigurationDefinition<?> getConfiguration() {
		return definition.getConfiguration();
	}

	/**
	 * Returns the module definition.
	 * @return The module definition.
	 */
	public ModuleDefinition<?> getModuleDefinition() {
		return module.getModuleDefinition();
	}

	public Component create(UUID id, Configuration configuration) {
		final Component c = (Component) module.getProvision(bean);
		final DirectInjectors<?> di = definition.getDirectInjectors();
		di.inject(c, UUID.class, id);
		if (configuration != null) {
			final Class<?> configType = getConfiguration().getType();
			di.inject(c, configType, configType.cast(configuration));
		}
		return c;
	}

	public <R extends ComponentRenderer> R getRenderer(Class<R> rendererType, Object target, RenderContext context) {
		final Renderers<?> renderers = definition.getRenderers();
		if (renderers.contains(rendererType)) {
			return renderers.getRenderer(rendererType, target, context);
		}
		return null;
	}

	/**
	 * Returns the component type in relation with a provided renderer type.
	 * @param rendererType Renderer type.
	 * @return The component type.
	 */
	public <R extends ComponentRenderer> ComponentType getComponentType(Class<R> rendererType) {
		return definition.getComponentType(rendererType);
	}

	/**
	 * Returns an action subresource.
	 * @param context Action context.
	 * @return The requested action sub-resource or {@code null} if not found.
	 */
	public Object getAction(ActionContext context) {
		try {
			return module.getAction(context);
		} catch (RuntimeException e) {
			Loggers.core().error(String.format("Unable to obtain action [%s]", context.getName()), e);
			return null;
		}
	}

}
