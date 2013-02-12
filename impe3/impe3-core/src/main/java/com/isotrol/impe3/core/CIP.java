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

import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.component.ActionContext;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.core.component.ComponentDefinition;
import com.isotrol.impe3.core.component.ComponentType;
import com.isotrol.impe3.core.modules.ModuleDefinition;


/**
 * A component in a page.
 * @author Andres Rodriguez
 */
public final class CIP extends AbstractIdentifiable {
	private final ComponentInstance component;
	private final Configuration configuration;

	public CIP(final UUID id, ComponentInstance component, final Configuration configuration) {
		super(id);
		this.component = checkNotNull(component);
		this.configuration = configuration;
	}

	public Component create() {
		return component.create(getId(), configuration);
	}

	public <R extends ComponentRenderer> R getRenderer(Class<R> rendererType, Object target, RenderContext context) {
		return component.getRenderer(rendererType, target, context);
	}

	/**
	 * Returns the component type in relation with a provided renderer type.
	 * @param rendererType Renderer type.
	 * @return The component type.
	 */
	public <R extends ComponentRenderer> ComponentType getComponentType(Class<R> rendererType) {
		return component.getComponentType(rendererType);
	}

	public ComponentDefinition<?> getDefinition() {
		return component.getDefinition();
	}

	/**
	 * Returns the module definition.
	 * @return The module definition.
	 */
	public ModuleDefinition<?> getModuleDefinition() {
		return component.getModuleDefinition();
	}

	/**
	 * Returns an action subresource.
	 * @param context Action context.
	 * @return The requested action sub-resource or {@code null} if not found.
	 */
	public Object getAction(ActionContext context) {
		return component.getAction(context);
	}

}
