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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.CIPResult;
import com.isotrol.impe3.core.Frame;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.Page;
import com.isotrol.impe3.core.PageContext;
import com.isotrol.impe3.core.PageResult;
import com.isotrol.impe3.core.PageResult.Early;
import com.isotrol.impe3.core.PageResult.Ok;
import com.isotrol.impe3.core.component.ComponentDefinition;
import com.isotrol.impe3.core.component.ComponentType;
import com.isotrol.impe3.core.engine.ImmutableComponentRequestContext.Builder;


/**
 * Page instance. This class is NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
public final class PageInstance {
	/** Page context. */
	private final PageContext context;
	private final Map<UUID, Component> components = Maps.newHashMap();
	private final Logger logger = Loggers.core();

	PageInstance(PageContext context) {
		this.context = checkNotNull(context);
		for (CIP cip : context.getPage().getComponents().values()) {
			components.put(cip.getId(), cip.create());
		}
	}

	public Page getPage() {
		return context.getPage();
	}

	public Ok edit() throws Exception {
		return (Ok) new EditRunner().start();
	}

	public <R extends ComponentRenderer> Map<UUID, R> getRenderers(final Class<R> rendererType,
		final Function<CIP, RenderContext> contextFactory) {
		final Map<UUID, R> map = Maps.newLinkedHashMap();
		for (CIP cip : getPage().getOrder()) {
			final UUID cipId = cip.getId();
			final Component c = components.get(cipId);
			final RenderContext context = contextFactory.apply(cip);
			final R renderer = cip.getRenderer(rendererType, c, context);
			if (renderer != null) {
				map.put(cipId, renderer);
			}
		}
		return map;
	}

	public <R extends ComponentRenderer> Iterable<CIP> getVisualComponents(final Class<R> rendererType) {
		final Predicate<CIP> visual = new Predicate<CIP>() {
			public boolean apply(CIP input) {
				return ComponentType.VISUAL == input.getComponentType(rendererType);
			}
		};
		return Iterables.filter(getPage().getOrder(), visual);
	}

	public List<Frame> getLayout() {
		return getPage().getLayout();
	}

	public PageResult run() throws Exception {
		return new Runner().start();
	}

	private abstract class AbstractRunner {
		private final PageResult.Builder b = PageResult.builder(context);

		private AbstractRunner() {
		}

		PageResult start() throws Exception {
			final ComponentResponse componentResponse = runLevel(getPage().getComponents().getFirstLevel(), null);
			if (componentResponse == ComponentResponse.OK) {
				Early early = b.evalutatePreconditions();
				if (early != null) {
					return early;
				}
				return b.ok();
			}
			return b.redirect((ComponentResponse.Redirection) componentResponse);
		}

		private ComponentResponse runLevel(Iterable<CIP> cips, Builder contextBuilder) throws Exception {
			for (CIP cip : cips) {
				final UUID id = cip.getId();
				final ImmutableComponentRequestContext crc;
				if (contextBuilder == null) {
					crc = RequestContexts.firstLevelComponent(context.getContext(), id);
				} else {
					crc = contextBuilder.setComponentId(id).get();
				}
				final ComponentResponse componentResponse = runCIP(cip, crc);
				if (componentResponse != ComponentResponse.OK) {
					return componentResponse;
				}
			}
			return ComponentResponse.OK;
		}

		abstract ComponentResponse doRun(Component component) throws Exception;

		private ComponentResponse runCIP(CIP cip, ImmutableComponentRequestContext context) throws Exception {
			final UUID id = cip.getId();
			final Component component = components.get(id);
			final ComponentDefinition<?> dfn = cip.getDefinition();
			// Component injection
			inject(cip, dfn, component, context);
			// Run the component
			final ComponentResponse componentResponse = execute(cip, component);
			// Extractor
			final Stopwatch watch = new Stopwatch().start();
			Builder builder = null;
			try {
				final ComponentExtractor extractor = ComponentExtractor.create(cip, component, b, context);
				// Extract session params
				extractor.extractSession();
				// If redirection, return.
				if (componentResponse != ComponentResponse.OK) {
					return componentResponse;
				}
				// Extraction
				builder = extractor.extract();
			}
			finally {
				final long t = watch.elapsedMillis();
				if (t > 100) {
					logger.warn(String.format("CIP [%s]-[%s] took [%d] ms to get extracted", cip.getId(), cip
						.getDefinition().getType(), t));
				}
			}
			// Store CIP result
			b.cip(cip.getId(), new CIPResult(cip, component, context));
			// Run children.
			return runLevel(getPage().getComponents().getChildren(id), builder);
		}

		private ComponentResponse execute(CIP cip, Component component) throws Exception {
			final Stopwatch watch = new Stopwatch().start();
			try {
				return doRun(component);
			}
			finally {
				final long t = watch.elapsedMillis();
				if (t > 250) {
					logger.warn(String.format("CIP [%s]-[%s] took [%d] ms to execute", cip.getId(), cip.getDefinition()
						.getType(), t));
				}
			}
		}

		private void inject(CIP cip, ComponentDefinition<?> dfn, Component component, ComponentRequestContext context) {
			final Stopwatch watch = new Stopwatch().start();
			try {
				ComponentInjector.inject(dfn, component, context);
			}
			finally {
				final long t = watch.elapsedMillis();
				if (t > 100) {
					logger.warn(String.format("CIP [%s]-[%s] took [%d] ms to get injected", cip.getId(), cip
						.getDefinition().getType(), t));
				}
			}
		}
	}

	private class Runner extends AbstractRunner {
		Runner() {
		}

		@Override
		ComponentResponse doRun(Component component) throws Exception {
			return component.execute();
		}
	}

	private class EditRunner extends AbstractRunner {
		EditRunner() {
		}

		@Override
		ComponentResponse doRun(Component component) throws Exception {
			try {
				if (component instanceof EditModeComponent) {
					((EditModeComponent) component).edit();
				} else {
					component.execute();
				}
			} catch (Exception e) {
				// TODO: log
			}
			return ComponentResponse.OK;
		}
	}

}
