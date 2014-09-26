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

package com.isotrol.impe3.pms.api.portal;


import java.util.Map;

import com.isotrol.impe3.pms.api.AbstractWithId;
import com.isotrol.impe3.pms.api.minst.ProvidedDTO;


/**
 * Portal definition.
 * @author Andres Rodriguez
 */
public class PortalDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -7631510608214952150L;
	/** If the element is routable. */
	private boolean routable;
	/** Tag path segment. */
	private String tag;
	/** Router. */
	private ProvidedDTO router;
	/** Locale resolver. */
	private ProvidedDTO locale;
	/** Device resolver. */
	private ProvidedDTO device;
	/** Current device capabilities provider. */
	private ProvidedDTO deviceCaps;
	/** Current default node repository. */
	private ProvidedDTO nodeRepository;
	/** Default locale. */
	private String defaultLocale;
	/** Available locales. */
	private Map<String, String> locales;
	/** Include uncategorized. */
	private Boolean uncategorized;
	/** Only due nodes. */
	private Boolean due;
	/** Routing domain. */
	private String domain;
	/** Whether to use session-based CSRF. */
	private Boolean sessionCSRF;

	/** Default constructor. */
	public PortalDTO() {
	}

	/**
	 * Returns whether the element is routable.
	 * @return True if the element is routable.
	 */
	public boolean isRoutable() {
		return routable;
	}

	/**
	 * Sets whether the element is routable.
	 * @param routable True if the element is routable.
	 */
	public void setRoutable(boolean routable) {
		this.routable = routable;
	}

	/**
	 * Returns the tag path segment.
	 * @return The tag path segment.
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag path segment.
	 * @param tag The tag path segment.
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Returns the router.
	 * @return The router.
	 */
	public ProvidedDTO getRouter() {
		return router;
	}

	/**
	 * Sets the router.
	 * @param router The router.
	 */
	public void setRouter(ProvidedDTO router) {
		this.router = router;
	}

	/**
	 * Returns the locale resolver.
	 * @return The locale resolver.
	 */
	public ProvidedDTO getLocale() {
		return locale;
	}

	/**
	 * Sets the locale resolver.
	 * @param locale The locale resolver.
	 */
	public void setLocale(ProvidedDTO locale) {
		this.locale = locale;
	}

	/**
	 * Returns the device resolver.
	 * @return The device resolver.
	 */
	public ProvidedDTO getDevice() {
		return device;
	}

	/**
	 * Sets the device resolver.
	 * @param device The device resolver.
	 */
	public void setDevice(ProvidedDTO device) {
		this.device = device;
	}

	/**
	 * Returns the device capabilities provider.
	 * @return The device capabilities provider.
	 */
	public ProvidedDTO getDeviceCaps() {
		return deviceCaps;
	}

	/**
	 * Sets the device resolver.
	 * @param deviceCaps The device resolver.
	 */
	public void setDeviceCaps(ProvidedDTO deviceCaps) {
		this.deviceCaps = deviceCaps;
	}

	/**
	 * Returns the default node repository.
	 * @return The default node repository.
	 */
	public ProvidedDTO getNodeRepository() {
		return nodeRepository;
	}

	/**
	 * Sets the default node repository.
	 * @param nodeRepository The default node repository.
	 */
	public void setNodeRepository(ProvidedDTO nodeRepository) {
		this.nodeRepository = nodeRepository;
	}

	/**
	 * Returns the default locale.
	 * @return The default locale.
	 */
	public String getDefaultLocale() {
		return defaultLocale;
	}

	/**
	 * Sets the default locale.
	 * @param defaultLocale The default locale.
	 */
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	/**
	 * Returns the available locales.
	 * @return The available locales.
	 */
	public Map<String, String> getLocales() {
		return locales;
	}

	/**
	 * Sets the available locales.
	 * @param locales The available locales.
	 */
	public void setLocales(Map<String, String> locales) {
		this.locales = locales;
	}

	/**
	 * Sets whether to include uncategorized contents.
	 * @param uncategorized True if uncategorized contents must be included by default.
	 */
	public Boolean getUncategorized() {
		return uncategorized;
	}

	/**
	 * Sets whether to include uncategorized contents.
	 * @param uncategorized True if uncategorized contents must be included by default.
	 */
	public void setUncategorized(Boolean uncategorized) {
		this.uncategorized = uncategorized;
	}

	/**
	 * Returns whether to include only due contents.
	 * @return True if only due contents must be included by default.
	 */
	public Boolean getDue() {
		return due;
	}

	/**
	 * Sets whether to include only due contents.
	 * @param due True if only due contents must be included by default.
	 */
	public void setDue(Boolean due) {
		this.due = due;
	}

	/**
	 * Returns the routing domain.
	 * @return The routing domain.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the routing domain.
	 * @param domain The routing domain.
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/** Returns whether to use session-based CSRF. */
	public Boolean getSessionCSRF() {
		return sessionCSRF;
	}
	
	/**
	 * Sets whether to use session-based CSRF.
	 * @return True if the portal uses session-based CSRF. 
	 */
	public void setSessionCSRF(Boolean sessionCSRF) {
		this.sessionCSRF = sessionCSRF;
	}
	
}
