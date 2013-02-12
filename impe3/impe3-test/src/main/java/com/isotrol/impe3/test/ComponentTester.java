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

package com.isotrol.impe3.test;


import static junit.framework.Assert.assertTrue;

import java.io.OutputStream;

import org.junit.Assert;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.core.component.ComponentDefinition;
import com.isotrol.impe3.core.engine.ComponentInjector;


/**
 * Abstract base class for IMPE3 component.
 * @param <T> Component type.
 * @author Andres Rodriguez
 */
public final class ComponentTester<T extends Component> {
	private final ComponentDefinition<T> definition;
	private final TestModel model;
	private final T component;

	private static HTMLTemplate html = new HTMLTemplate();

	/** Default Constructor. */
	static <T extends Component> ComponentTester<T> of(Class<T> type, T component, final TestModel model) {
		return new ComponentTester<T>(ComponentDefinition.of(type), component, model);
	}

	/** Default Constructor. */
	private ComponentTester(final ComponentDefinition<T> definition, T component, final TestModel model) {
		this.definition = definition;
		this.component = component;
		this.model = model;
	}

	public T getComponent() {
		return component;
	}

	private void inject() {
		ComponentInjector.inject(definition, component, model.getContext());
	}

	/**
	 * Execute.
	 */
	public ComponentResponse execute() throws Exception {
		inject();
		return component.execute();
	}

	/**
	 * Execute and Asserts that component execution was successfull
	 */
	public void executeOk() {
		try {
			assertTrue(ComponentResponse.OK == execute());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Execute in edit mode
	 */
	public void edit() {
		inject();
		((EditModeComponent) component).edit();
		assertTrue(true);
	}

	/**
	 * Do renderization of component execution
	 */
	public void renderHTML() {
		final RenderContext rc = model.getRenderContext();
		final HTMLRenderer renderer = definition.getRenderers().getRenderer(HTMLRenderer.class, component, rc);
		final OutputStream out = System.out;
		html.render(renderer, out, rc);
	}

	/**
	 * Just execute and render
	 */
	public void executeAndRenderHTML() {
		executeOk();
		renderHTML();
	}

	/**
	 * Just execute in edit mode and render
	 */
	public void editAndRenderHTML() {
		edit();
		renderHTML();
	}
}
