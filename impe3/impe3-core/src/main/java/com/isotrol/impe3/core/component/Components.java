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


import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.or;

import java.util.Locale;
import java.util.UUID;

import javax.xml.transform.Source;

import net.sf.derquinsej.Classes;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Principal;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.CacheMode;
import com.isotrol.impe3.api.component.CacheScope;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.Expires;
import com.isotrol.impe3.api.component.LastModified;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.core.config.ConfigurationDefinition;


/**
 * Utility methods and values related to components.
 * @author Andres Rodriguez
 */
public final class Components {
	private Components() {
		throw new AssertionError();
	}

	/** Predicate to evaluate if a type is a listing. */
	public static final Predicate<Class<?>> IS_LISTING = Classes.extendsOrImplements(Listing.class);
	/** Injectable types (plus one configuration). */
	private static final ImmutableSet<Class<?>> INJECTABLE = ImmutableSet.<Class<?>> of(Locale.class, Device.class,
		Categories.class, Cookies.class, Portal.class, ContentTypes.class, UUID.class, EngineMode.class, Headers.class,
		Route.class, PageKey.class, Principal.class, PrincipalContext.class, ContentLoader.class, URIGenerator.class,
		ComponentRequestContext.class);
	/** Binding Errors Injectable types. */
	public static final ImmutableSet<Class<?>> BE_INJECTABLE = ImmutableSet.<Class<?>> of(Cookies.class,
		RequestParams.class, SessionParams.class, LocalParams.class);
	/** Predicate checking if a class can be injected as a binding error. */
	public static final Predicate<Class<?>> IS_BE_INJECTABLE = in(BE_INJECTABLE);
	/** Bidirectional links. */
	private static final ImmutableSet<Class<?>> LINKS = ImmutableSet.<Class<?>> of(ContentKey.class,
		NavigationKey.class, Content.class, Listing.class, Pagination.class, TemplateKey.class, TemplateModel.class,
		ETag.class, Source.class);
	/** Extractable links. */
	private static final ImmutableSet<Class<?>> EXTRACTABLE = ImmutableSet.<Class<?>> of(LastModified.class,
		Expires.class, CacheMode.class, CacheScope.class, ContentCriteriaTransformer.class);
	/** Predicate checking if a class is a link. */
	public static final Predicate<Class<?>> IS_LINK = in(LINKS);
	/** Predicate checking if a class can be used for extraction. */
	@SuppressWarnings("unchecked")
	public static final Predicate<Class<?>> IS_EXTRACTABLE = or(IS_LISTING, IS_LINK, in(EXTRACTABLE));
	/** Predicate checking if a class can be used for direct injection. */
	@SuppressWarnings("unchecked")
	public static final Predicate<Class<?>> IS_DIRECT_INJECTABLE = or(IS_LINK, IS_LISTING, in(INJECTABLE),
		ConfigurationDefinition.IS_CONFIGURATION, IS_BE_INJECTABLE);
}
