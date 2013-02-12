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

package com.isotrol.impe3.core.component;


import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;


/**
 * Collection of renderers.
 * @author Andres Rodriguez
 * @param <T> Target component type.
 */
public final class Renderers<T extends Component> extends Operators<T> {
	private final ImmutableMap<Class<? extends ComponentRenderer>, RendererMethod> renderers;

	static <T extends Component> Renderers<T> of(Class<T> type, Iterable<Method> methods) {
		return new Renderers<T>(type, filter(methods, Renderer.class));
	}

	private Renderers(final Class<T> type, Iterable<Method> methods) {
		super(type);
		final Map<Class<? extends ComponentRenderer>, RendererMethod> builder = Maps.newHashMap();
		for (Method m : methods) {
			final RendererMethod rm = new RendererMethod(m);
			final Class<? extends ComponentRenderer> rendererType = rm.getRendererType();
			if (builder.containsKey(rendererType)) {
				throw new DuplicateRendererException(type, rendererType);
			}
			builder.put(rendererType, rm);
		}
		this.renderers = ImmutableMap.copyOf(builder);
	}

	@Override
	public boolean isEmpty() {
		return renderers.isEmpty();
	}

	/**
	 * Checks if the collection contains a renderer of the specified type.
	 * @param rendererType Requested type.
	 * @return True if the collection contains a renderer of the specified type.
	 */
	public boolean contains(Class<? extends ComponentRenderer> rendererType) {
		return renderers.containsKey(rendererType);
	}

	/**
	 * Returns the renderer of a certain type.
	 * @param <R> Renderer type.
	 * @param rendererType Renderer type.
	 * @param target Target component.
	 * @param context Render context.
	 * @return The requested renderer.
	 */
	public <R extends ComponentRenderer> R getRenderer(Class<R> rendererType, Object target, RenderContext context) {
		final RendererMethod m = renderers.get(rendererType);
		Preconditions.checkArgument(m != null);
		return rendererType.cast(m.invoke(target, context));
	}

	/**
	 * A renderer method.
	 * @author Andres Rodriguez
	 */
	private class RendererMethod extends Operator {
		RendererMethod(final Method method) {
			super(method, -1);
			final Class<?> returnType = getReturnType();
			if (returnType == ComponentRenderer.class || !ComponentRenderer.class.isAssignableFrom(returnType)) {
				throw new IllegalRendererMethodException(getType(), method);
			}
			final Class<?>[] parameters = method.getParameterTypes();
			if (parameters.length != 1 || parameters[0] != RenderContext.class) {
				throw new IllegalRendererMethodException(getType(), method);
			}
		}

		Class<? extends ComponentRenderer> getRendererType() {
			return getReturnType().asSubclass(ComponentRenderer.class);
		}
	}

}
