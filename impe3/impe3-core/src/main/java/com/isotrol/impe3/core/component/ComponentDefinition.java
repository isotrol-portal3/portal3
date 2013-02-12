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


import static com.google.common.collect.Iterables.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.api.component.CacheMode;
import com.isotrol.impe3.api.component.CacheScope;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentCacheMode;
import com.isotrol.impe3.api.component.ComponentCacheScope;
import com.isotrol.impe3.api.component.ComponentETag;
import com.isotrol.impe3.api.component.ComponentETagMode;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.ExtractAction;
import com.isotrol.impe3.api.component.ExtractCookie;
import com.isotrol.impe3.api.component.ExtractDynHeader;
import com.isotrol.impe3.api.component.ExtractDynLocal;
import com.isotrol.impe3.api.component.ExtractDynQuery;
import com.isotrol.impe3.api.component.ExtractDynSession;
import com.isotrol.impe3.api.component.ExtractHeader;
import com.isotrol.impe3.api.component.ExtractLocal;
import com.isotrol.impe3.api.component.ExtractQuery;
import com.isotrol.impe3.api.component.ExtractSession;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.InjectBase;
import com.isotrol.impe3.api.component.InjectBindingErrors;
import com.isotrol.impe3.api.component.InjectCookie;
import com.isotrol.impe3.api.component.InjectHeader;
import com.isotrol.impe3.api.component.InjectLocal;
import com.isotrol.impe3.api.component.InjectProperty;
import com.isotrol.impe3.api.component.InjectRequest;
import com.isotrol.impe3.api.component.InjectSession;
import com.isotrol.impe3.api.component.Paginated;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.config.ConfigurationException;
import com.isotrol.impe3.core.support.Definition;
import com.isotrol.impe3.core.support.SingleValueSupport;


/**
 * Object that encapsulates the definition of a component.
 * @author Andres Rodriguez.
 * @param <T> Component class.
 */
public final class ComponentDefinition<T extends Component> extends Definition<T> {
	@SuppressWarnings("unchecked")
	private static final ImmutableSet<Class<? extends Annotation>> INJECT_ANNOTATIONS = ImmutableSet.of(Inject.class,
		InjectCookie.class, InjectHeader.class, InjectRequest.class, InjectSession.class, InjectLocal.class,
		InjectBindingErrors.class, InjectProperty.class, InjectBase.class);
	@SuppressWarnings("unchecked")
	private static final ImmutableSet<Class<? extends Annotation>> EXTRACT_ANNOTATIONS = ImmutableSet
		.of(Extract.class, ExtractSession.class, ExtractLocal.class, ExtractQuery.class, ExtractHeader.class,
			ExtractDynHeader.class, ExtractDynLocal.class, ExtractDynSession.class, ExtractDynQuery.class,
			ExtractAction.class, ExtractCookie.class);
	private static final ImmutableSet<Class<? extends Annotation>> ANNOTATIONS = ImmutableSet.copyOf(Iterables.concat(
		INJECT_ANNOTATIONS, EXTRACT_ANNOTATIONS, ImmutableSet.of(Renderer.class)));

	/** Cache. */
	private static final SingleValueSupport<Class<?>, Object> CACHE = SingleValueSupport.create();

	/**
	 * Returns the component definition for the specified component class.
	 * @param componentClass Component class.
	 * @return The component definition.
	 * @throws ComponentException if the configuration class is invalid.
	 */
	public static <T extends Component> ComponentDefinition<T> of(Class<T> componentClass) throws ComponentException {
		Preconditions.checkNotNull(componentClass, "A component class must be provided");
		@SuppressWarnings("unchecked")
		final ComponentDefinition<T> d1 = (ComponentDefinition<T>) CACHE.get(componentClass);
		if (d1 != null) {
			return d1;
		}

		@SuppressWarnings("unchecked")
		final ComponentDefinition<T> d2 = (ComponentDefinition<T>) CACHE.put(componentClass,
			new ComponentDefinition<T>(componentClass));
		return d2;

	}

	/** Component configuration. */
	private final ConfigurationDefinition<?> configuration;
	private final DirectInjectors<T> directInjectors;
	private final DirectInjectors<T> bindingErrorsInjectors;
	private final ParameterInjectors<T, Headers> headerInjectors;
	private final ParameterInjectors<T, Cookies> cookieInjectors;
	private final ParameterInjectors<T, RequestParams> requestInjectors;
	private final ParameterInjectors<T, SessionParams> sessionInjectors;
	private final ParameterInjectors<T, LocalParams> localInjectors;
	private final PropertyInjectors<T> propertyInjectors;
	private final BaseInjectors<T> baseInjectors;
	private final Renderers<T> renderers;
	private final DirectExtractors<T> extractors;
	private final LocalExtractors<T> localExtractors;
	private final SessionExtractors<T> sessionExtractors;
	private final QueryExtractors<T> queryExtractors;
	private final HeaderExtractors<T> headerExtractors;
	private final ActionExtractors<T> actionExtractors;
	private final CookieExtractors<T> cookieExtractors;
	private boolean paginated;
	private final ImmutableSet<Class<?>> requiredLinks;
	private final ImmutableSet<Class<?>> providedLinks;
	/** Component type. */
	private ComponentType componentType;
	/** Cache mode. */
	private final CacheMode cacheMode;
	/** Cache scope. */
	private final CacheScope cacheScope;
	/** ETag mode. */
	private final ComponentETagMode eTagMode;

	private ComponentDefinition(Class<T> type) {
		super(type);
		final List<Method> methods = getComponentMethods();
		this.directInjectors = DirectInjectors.direct(type, methods);
		this.configuration = loadConfiguration();
		this.bindingErrorsInjectors = DirectInjectors.bindingErrors(type, methods);
		this.headerInjectors = ParameterInjectors.headers(type, methods);
		this.cookieInjectors = ParameterInjectors.cookies(type, methods);
		this.requestInjectors = ParameterInjectors.request(type, methods);
		this.sessionInjectors = ParameterInjectors.session(type, methods);
		this.localInjectors = ParameterInjectors.local(type, methods);
		this.propertyInjectors = PropertyInjectors.of(type, methods);
		this.baseInjectors = BaseInjectors.of(type, methods);
		this.renderers = Renderers.of(type, methods);
		this.extractors = DirectExtractors.of(type, methods);
		this.localExtractors = LocalExtractors.of(type, methods);
		this.sessionExtractors = SessionExtractors.of(type, methods);
		this.queryExtractors = QueryExtractors.of(type, methods);
		this.headerExtractors = HeaderExtractors.of(type, methods);
		this.actionExtractors = ActionExtractors.of(type, methods);
		this.cookieExtractors = CookieExtractors.of(type, methods);
		this.paginated = type.isAnnotationPresent(Paginated.class);
		final RequiresLink links = type.getAnnotation(RequiresLink.class);
		if (links == null) {
			this.requiredLinks = ImmutableSet.of();
		} else {
			this.requiredLinks = ImmutableSet.copyOf(links.value());
		}
		this.providedLinks = ImmutableSet.copyOf(Sets.union(this.requiredLinks, extractors.typeSet()));
		this.componentType = getComponentType(!renderers.isEmpty());
		// Cache parameters
		this.cacheMode = type.isAnnotationPresent(ComponentCacheMode.class) ? type.getAnnotation(
			ComponentCacheMode.class).value() : CacheMode.ON;
		this.cacheScope = type.isAnnotationPresent(ComponentCacheScope.class) ? type.getAnnotation(
			ComponentCacheScope.class).value() : CacheScope.PUBLIC;
		if (type.isAnnotationPresent(ComponentETag.class)) {
			this.eTagMode = type.getAnnotation(ComponentETag.class).value();
		} else {
			this.eTagMode = this.extractors.contains(ETag.class) ? ComponentETagMode.DEFAULT
				: ComponentETagMode.DISABLED;
		}
	}

	public ConfigurationDefinition<?> getConfiguration() {
		return configuration;
	}

	public DirectInjectors<T> getDirectInjectors() {
		return directInjectors;
	}

	public DirectInjectors<T> getBindingErrorsInjectors() {
		return bindingErrorsInjectors;
	}

	public ParameterInjectors<T, Headers> getHeaderInjectors() {
		return headerInjectors;
	}

	public ParameterInjectors<T, Cookies> getCookieInjectors() {
		return cookieInjectors;
	}

	public ParameterInjectors<T, RequestParams> getRequestInjectors() {
		return requestInjectors;
	}

	public ParameterInjectors<T, SessionParams> getSessionInjectors() {
		return sessionInjectors;
	}

	public ParameterInjectors<T, LocalParams> getLocalInjectors() {
		return localInjectors;
	}

	public PropertyInjectors<T> getPropertyInjectors() {
		return propertyInjectors;
	}

	public BaseInjectors<T> getBaseInjectors() {
		return baseInjectors;
	}

	public Renderers<T> getRenderers() {
		return renderers;
	}

	public DirectExtractors<T> getExtractors() {
		return extractors;
	}

	public LocalExtractors<T> getLocalExtractors() {
		return localExtractors;
	}

	public SessionExtractors<T> getSessionExtractors() {
		return sessionExtractors;
	}

	public QueryExtractors<T> getQueryExtractors() {
		return queryExtractors;
	}

	public HeaderExtractors<T> getHeaderExtractors() {
		return headerExtractors;
	}

	public ActionExtractors<T> getActionExtractors() {
		return actionExtractors;
	}

	public CookieExtractors<T> getCookieExtractors() {
		return cookieExtractors;
	}

	public boolean isPaginated() {
		return paginated;
	}

	public ImmutableSet<Class<?>> getRequiredLinks() {
		return requiredLinks;
	}

	public ImmutableSet<Class<?>> getProvidedLinks() {
		return providedLinks;
	}

	public ComponentType getComponentType() {
		return componentType;
	}

	public CacheMode getCacheMode() {
		return cacheMode;
	}

	public CacheScope getCacheScope() {
		return cacheScope;
	}

	public ComponentETagMode getETagMode() {
		return eTagMode;
	}

	/**
	 * Returns the component type in relation with a provided renderer type.
	 * @param rendererType Renderer type.
	 * @return The component type.
	 */
	public <R extends ComponentRenderer> ComponentType getComponentType(Class<R> rendererType) {
		return getComponentType(renderers.contains(rendererType));
	}

	/* LOADING METHODS. */

	private List<Method> getComponentMethods() {
		final Predicate<Method> valid = new Predicate<Method>() {
			public boolean apply(Method input) {
				boolean added = false;
				for (Class<? extends Annotation> a : ANNOTATIONS) {
					if (input.getAnnotation(a) != null) {
						if (added) {
							throw new IllegalMethodComponentException(getType(), input);
						} else {
							added = true;
						}
					}
				}
				return added;
			}
		};
		return Lists.newLinkedList(filter(getMethods(), valid));
	}

	private ConfigurationDefinition<?> loadConfiguration() {
		ConfigurationDefinition<?> cd = null;
		for (final Class<?> type : directInjectors.getInjectedTypes()) {
			if (ConfigurationDefinition.IS_CONFIGURATION.apply(type)) {
				if (cd != null) {
					throw new DuplicateComponentConfigurationException(getType());
				}
				try {
					cd = ConfigurationDefinition.of(type.asSubclass(Configuration.class));
				} catch (ConfigurationException e) {
					throw new ComponentConfigurationException(getType(), e);
				}
			}
		}
		return cd;
	}

	private ComponentType getComponentType(boolean hasRenderers) {
		final boolean visual = VisualComponent.class.isAssignableFrom(getType());
		if (!visual) {
			if (hasRenderers) {
				return ComponentType.DECORATOR;
			}
			return ComponentType.FUNCTIONAL;
		}
		if (hasRenderers) {
			return ComponentType.VISUAL;
		}
		return ComponentType.FUNCTIONAL;
	}
}
