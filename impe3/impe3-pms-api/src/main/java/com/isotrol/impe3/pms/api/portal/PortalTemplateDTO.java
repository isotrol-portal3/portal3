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


import java.util.List;
import java.util.Map;

import com.isotrol.impe3.pms.api.AbstractRoutable;
import com.isotrol.impe3.pms.api.minst.ProvidedDTO;
import com.isotrol.impe3.pms.api.minst.ProvidedTemplateDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;


/**
 * Portal definition.
 * @author Andres Rodriguez
 */
public class PortalTemplateDTO extends AbstractRoutable {
	/** Serial UID. */
	private static final long serialVersionUID = 8607109527490530073L;
	/** Tag path segment. */
	private String tag;
	/** Current router. */
	private ProvidedTemplateDTO router;
	/** Current locale resolver. */
	private ProvidedTemplateDTO locale;
	/** Current device resolver. */
	private ProvidedTemplateDTO device;
	/** Current device capabilities provider. */
	private ProvidedTemplateDTO deviceCaps;
	/** Current default node repository. */
	private ProvidedTemplateDTO nodeRepository;
	/** Default locale. */
	private String defaultLocale;
	/** Available locales. */
	private Map<String, String> locales;
	/** Include uncategorized. */
	private PortalInheritableFlag uncategorized;
	/** Only due nodes. */
	private PortalInheritableFlag due;
	/** Whether to use session-based CSRF. */
	private PortalInheritableFlag sessionCSRF;
	/** Routing domain. */
	private RoutingDomainSelDTO domain;
	/** Available routing domains. */
	private List<RoutingDomainSelDTO> availableDomains;

	/** Default constructor. */
	public PortalTemplateDTO() {
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
	 * Returns the current router.
	 * @return The current router.
	 */
	public ProvidedTemplateDTO getRouter() {
		return router;
	}

	/**
	 * Sets the current router.
	 * @param router The current router.
	 */
	public void setRouter(ProvidedTemplateDTO router) {
		this.router = router;
	}

	/**
	 * Returns the locale resolver.
	 * @return The locale resolver.
	 */
	public ProvidedTemplateDTO getLocale() {
		return locale;
	}

	/**
	 * Sets the locale resolver.
	 * @param locale The locale resolver.
	 */
	public void setLocale(ProvidedTemplateDTO locale) {
		this.locale = locale;
	}

	/**
	 * Returns the device resolver.
	 * @return The device resolver.
	 */
	public ProvidedTemplateDTO getDevice() {
		return device;
	}

	/**
	 * Sets the device capabilities provider.
	 * @param device The device capabilities provider.
	 */
	public void setDevice(ProvidedTemplateDTO device) {
		this.device = device;
	}

	/**
	 * Returns the device capabilities provider.
	 * @return The device capabilities provider.
	 */
	public ProvidedTemplateDTO getDeviceCaps() {
		return deviceCaps;
	}

	/**
	 * Sets the device resolver.
	 * @param deviceCaps The device resolver.
	 */
	public void setDeviceCaps(ProvidedTemplateDTO deviceCaps) {
		this.deviceCaps = deviceCaps;
	}

	/**
	 * Returns the default node repository.
	 * @return The default node repository.
	 */
	public ProvidedTemplateDTO getNodeRepository() {
		return nodeRepository;
	}

	/**
	 * Sets the default node repository.
	 * @param nodeRepository The default node repository.
	 */
	public void setNodeRepository(ProvidedTemplateDTO nodeRepository) {
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
	public PortalInheritableFlag getUncategorized() {
		return uncategorized;
	}

	/**
	 * Sets whether to include uncategorized contents.
	 * @param uncategorized True if uncategorized contents must be included by default.
	 */
	public void setUncategorized(PortalInheritableFlag uncategorized) {
		this.uncategorized = uncategorized;
	}

	/**
	 * Returns whether to include only due contents.
	 * @return Whether if only due contents must be included by default.
	 */
	public PortalInheritableFlag getDue() {
		return due;
	}

	/**
	 * Sets whether to include only due contents.
	 * @param due Whether if only due contents must be included by default.
	 */
	public void setDue(PortalInheritableFlag due) {
		this.due = due;
	}
	
	/** Returns whether to use session-based CSRF. */
	public PortalInheritableFlag getSessionCSRF() {
		return sessionCSRF;
	}
	
	/**
	 * Sets whether to use session-based CSRF.
	 * @return Whether if the portal uses session-based CSRF. 
	 */
	public void setSessionCSRF(PortalInheritableFlag sessionCSRF) {
		this.sessionCSRF = sessionCSRF;
	}

	/**
	 * Returns the routing domain.
	 * @return The routing domain.
	 */
	public RoutingDomainSelDTO getDomain() {
		return domain;
	}

	/**
	 * Sets the routing domain.
	 * @param domain The routing domain.
	 */
	public void setDomain(RoutingDomainSelDTO domain) {
		this.domain = domain;
	}

	/**
	 * Returns the available routing domains.
	 * @return The available routing domains.
	 */
	public List<RoutingDomainSelDTO> getAvailableDomains() {
		return availableDomains;
	}

	/**
	 * Sets the available routing domains.
	 * @param availableDomains The available routing domains.
	 */
	public void setAvailableDomains(List<RoutingDomainSelDTO> availableDomains) {
		this.availableDomains = availableDomains;
	}

	private ProvidedDTO toDTO(ProvidedTemplateDTO template) {
		if (template != null) {
			return template.toProvidedDTO();
		} else {
			return null;
		}
	}
	
	private Boolean toBoolean(PortalInheritableFlag flag) {
		if (flag == null) {
			return null;
		}
		return flag.toBooleanObject();
	}

	public PortalDTO toPortalDTO() {
		final PortalDTO dto = new PortalDTO();
		dto.setId(getId());
		dto.setRoutable(isRoutable());
		dto.setTag(tag);
		dto.setRouter(toDTO(router));
		dto.setLocale(toDTO(locale));
		dto.setDevice(toDTO(device));
		dto.setDeviceCaps(toDTO(deviceCaps));
		dto.setNodeRepository(toDTO(nodeRepository));
		dto.setDefaultLocale(defaultLocale);
		dto.setLocales(locales);
		dto.setUncategorized(toBoolean(uncategorized));
		dto.setDue(toBoolean(due));
		dto.setSessionCSRF(toBoolean(sessionCSRF));
		if (domain != null) {
			dto.setDomain(domain.getId());
		}
		return dto;
	}
}
