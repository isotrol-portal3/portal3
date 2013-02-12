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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;


/**
 * Value representing a request route.
 * @author Andres Rodriguez.
 * 
 */
public final class Route {
	/** Whether this route is through a secure channel, such as HTTPS. */
	private final boolean secure;
	private final PageKey page;
	private final Device device;
	private final Locale locale;

	private Route(boolean secure, final PageKey page, final Device device, final Locale locale) {
		this.secure = secure;
		this.page = page;
		this.device = device;
		this.locale = locale;
	}

	/**
	 * Returns a route to the specified destination.
	 * @param secure Whether this route is through a secure channel, such as HTTPS.
	 * @param page Destination page.
	 * @param device Device performing the request.
	 * @param locale Desired locale.
	 * @return The requested route.
	 */
	public static Route of(boolean secure, PageKey page, Device device, Locale locale) {
		return new Route(secure, page, device, locale);
	}

	/**
	 * Returns a route to the specified destination.
	 * @param secure Whether this route is through a secure channel, such as HTTPS.
	 * @param context Client context.
	 * @param page Destination page.
	 * @return The requested route.
	 */
	public static Route of(boolean secure, ClientRequestContext context, final PageKey page) {
		return of(secure, page, context.getDevice(), context.getLocale());
	}

	/**
	 * Returns a route to the specified destination.
	 * @param context Request context.
	 * @param page Destination page.
	 * @param device Device performing the request.
	 * @param locale Desired locale.
	 * @return The requested route.
	 */
	public static Route of(RequestContext context, final PageKey page) {
		return of(context.isSecure(), page, context.getDevice(), context.getLocale());
	}

	public boolean isSecure() {
		return secure;
	}

	public PageKey getPage() {
		return page;
	}

	public Device getDevice() {
		return device;
	}

	public Locale getLocale() {
		return locale;
	}

	/**
	 * Creates a route with the same parameters of this one but with a different destination page.
	 * @param secure Whether this route is through a secure channel, such as HTTPS.
	 * @param toPage New destination page.
	 * @return The new route.
	 */
	public Route toPage(boolean secure, PageKey toPage) {
		return of(secure, toPage, device, locale);
	}

	/**
	 * Creates a route with the same parameters of this one but with a different destination page.
	 * @param toPage New destination page.
	 * @return The new route.
	 */
	public Route toPage(PageKey toPage) {
		return toPage(secure, toPage);
	}

	/**
	 * Creates a route with the same parameters of this one but with a different destination page.
	 * @param locale New destination locale.
	 * @return The new route.
	 */
	public Route toLocale(Locale locale) {
		checkNotNull(locale, "The destination locale must be provided.");
		if (locale.equals(this.locale)) {
			return this;
		}
		return new Route(this.secure, this.page, this.device, locale);
	}

	/**
	 * Creates a route with the same parameters of this one but with a different destination device.
	 * @param locale New destination locale.
	 * @return The new route.
	 */
	public Route toDevice(Device device) {
		checkNotNull(device, "The destination device must be provided.");
		if (device.equals(this.device)) {
			return this;
		}
		return new Route(this.secure, this.page, device, this.locale);
	}

}
