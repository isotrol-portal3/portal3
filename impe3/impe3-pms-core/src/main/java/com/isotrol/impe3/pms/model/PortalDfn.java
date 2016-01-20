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

package com.isotrol.impe3.pms.model;


import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


/**
 * Portal definition.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "PORTAL_DFN")
public class PortalDfn extends AbstractRoutableDfn<PortalDfn, PortalEntity, PortalEdition> {
	/** The portal. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRTL_ID", nullable = false)
	private PortalEntity entity;
	/** Routing domain. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "RTDM_ID", nullable = false)
	private RoutingDomainEntity routingDomain;
	/** Portal description. */
	@Column(name = "DESCRIPTION", length = Lengths.DESCRIPTION, nullable = true)
	private String description;
	/** Uses all categories. */
	@Column(name = "ALL_CTGY")
	private boolean allCategories = true;
	/** Uses all categories. */
	@Column(name = "ALL_COTP")
	private boolean allContentTypes = true;
	/** Root category. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "ROOT_CTGY_ID")
	private CategoryEntity root;
	/** Categories. */
	@ElementCollection(fetch = FetchType.LAZY)
	@JoinTable(name = "PORTAL_CATEGORY", joinColumns = @JoinColumn(name = "PRTL_ID"))
	@Column(name = "CTGY_ID")
	private Set<CategoryEntity> categories;
	/** Content types. */
	@ElementCollection(fetch = FetchType.LAZY)
	@JoinTable(name = "PORTAL_CONTENT_TYPE", joinColumns = @JoinColumn(name = "PRTL_ID"))
	@Column(name = "COTP_ID")
	private Set<ContentTypeEntity> contentTypes;
	/** URI bases. */
	@ElementCollection
	@JoinTable(name = "PORTAL_BASE", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@MapKeyColumn(name = "NAME", length = Lengths.NAME)
	@Column(name = "BASE", length = Lengths.DESCRIPTION)
	private Map<String, String> bases;
	/** Portal properties. */
	@ElementCollection
	@JoinTable(name = "PORTAL_PROPERTY", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@MapKeyColumn(name = "PRTL_PROP_NAME", length = Lengths.NAME)
	@Column(name = "PRTL_PROP_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> properties;
	/** Parent portal. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_PRTL_ID")
	private PortalEntity parent;
	/** Tag path segment. */
	@Column(name = "TAG_SEGMENT", length = Lengths.NAME, nullable = true)
	private String tag;
	/** Router Connector. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "ROUTER_CNNT_ID", nullable = true)
	private ConnectorEntity routerConnector;
	/** Router Exported bean. */
	@Column(name = "ROUTER_CNNT_BEAN", nullable = true, length = Lengths.NAME)
	private String routerBean;
	/** Locale Resolver Connector. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCALE_CNNT_ID", nullable = true)
	private ConnectorEntity localeConnector;
	/** Locale Resolver Exported bean. */
	@Column(name = "LOCALE_CNNT_BEAN", nullable = true, length = Lengths.NAME)
	private String localeBean;
	/** Device Resolver Connector. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_CNNT_ID", nullable = true)
	private ConnectorEntity deviceConnector;
	/** Device Resolver Exported bean. */
	@Column(name = "DEVICE_CNNT_BEAN", nullable = true, length = Lengths.NAME)
	private String deviceBean;
	/** Device Capabilities Provider Connector. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVCAP_CNNT_ID", nullable = true)
	private ConnectorEntity deviceCapsConnector;
	/** Device Capabilities Provider Exported bean. */
	@Column(name = "DEVCAP_CNNT_BEAN", nullable = true, length = Lengths.NAME)
	private String deviceCapsBean;
	/** Node Repository Connector. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "NR_CNNT_ID", nullable = true)
	private ConnectorEntity nrConnector;
	/** Node Repository Exported bean. */
	@Column(name = "NR_CNNT_BEAN", nullable = true, length = Lengths.NAME)
	private String nrBean;
	/** Component module instances. */
	@ManyToMany
	@JoinTable(name = "PORTAL_COMPONENT_DFN", joinColumns = {@JoinColumn(name = "PRTL_ID")}, inverseJoinColumns = {@JoinColumn(name = "CMPT_DFN_ID")})
	private Set<ComponentDfn> components;
	/** Pages. */
	@ManyToMany
	@JoinTable(name = "PORTAL_PAGE_DFN", joinColumns = {@JoinColumn(name = "PRTL_ID")}, inverseJoinColumns = {@JoinColumn(name = "PAGE_DFN_ID")})
	private Set<PageDfn> pages;
	/** Default locale. */
	@Column(name = "LOCALE_DEFAULT", nullable = true, length = Lengths.LOCALE)
	private String defaultLocale;
	/** Available localized names. */
	@ElementCollection
	@JoinTable(name = "PORTAL_LOCALE", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@MapKeyColumn(name = "PRTL_LOCALE", length = Lengths.LOCALE)
	@Column(name = "PRTL_NAME", nullable = true, length = Lengths.NAME)
	private Map<String, String> l7dNames;
	/** Available locales. */
	@ElementCollection
	@JoinTable(name = "PORTAL_AVAILABLE_LOCALE", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@Column(name = "PRTL_LOCALE", length = Lengths.LOCALE)
	private Set<String> locales;
	/** Include uncategorized. */
	@Column(name = "PRTL_UNCATEGORIZED", nullable = true)
	private Boolean uncategorized;
	/** Only due nodes. */
	@Column(name = "PRTL_DUE", nullable = true)
	private Boolean due;
	/** Overridden components. */
	@ElementCollection
	@JoinTable(name = "PORTAL_COMPONENT_OVERRIDE", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@MapKeyJoinColumn(name = "PRTL_CMPT_ID")
	private Map<ComponentEntity, OverridenComponentValue> overridenComponents;
	/** Set filters. */
	@ElementCollection
	@JoinTable(name = "PORTAL_SET_FILTER", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@MapKeyColumn(name = "PRTL_SET")
	private Map<String, SetFilterValue> setFilters;
	/** Portal devices. */
	@ElementCollection
	@JoinTable(name = "PORTAL_DEVICE", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@MapKeyJoinColumn(name = "PRDV_DVCE_ID")
	private Map<DeviceEntity, PortalDeviceValue> devices;
	/** Use parent portal devices. */
	@Column(name = "PRTL_PARENT_DEVICES", nullable = true)
	private Boolean inheritedDevices = false;
	/** Cache configuration. */
	private PortalCacheValue cache;
	/** Editions. */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "published")
	private Set<PortalEdition> editions;
	/** Whether to use session-based CSRF. */
	@Column(name = "PRTL_SESSION_CSRF", nullable = true)
	private Boolean sessionCSRF;

	/** Portal Configuration Values. */
	@ElementCollection
	@JoinTable(name = "PORTAL_CONFIGURATION", joinColumns = @JoinColumn(name = "PRTL_ID", nullable = false))
	@MapKeyColumn(name = "CNFG_BEAN_NAME", length = Lengths.DESCRIPTION)
	private Map<String, PortalConfigurationValue> portalConfiguration;
	
	/** Default constructor. */
	public PortalDfn() {
	}

	/**
	 * Returns the portal.
	 * @return The portal.
	 */
	public PortalEntity getEntity() {
		return entity;
	}

	/**
	 * Sets the portal.
	 * @param portal The portal.
	 */
	public void setEntity(PortalEntity portal) {
		this.entity = portal;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Definition#getEditions()
	 */
	@Override
	public Set<PortalEdition> getEditions() {
		return editions;
	}

	/**
	 * Returns the routing domain.
	 * @return The routing domain.
	 */
	public RoutingDomainEntity getRoutingDomain() {
		return routingDomain;
	}

	/**
	 * Sets the routing domain.
	 * @param routingDomain The routing domain.
	 */
	public void setRoutingDomain(RoutingDomainEntity routingDomain) {
		this.routingDomain = routingDomain;
	}

	/**
	 * Returns the portal description.
	 * @return The portal description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the portal description.
	 * @param description The portal description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns true if the portal does not filter categories.
	 * @return Whether the portal does not filter categories.
	 */
	public boolean isAllCategories() {
		return allCategories;
	}

	/**
	 * Sets whether the portal does not filter categories.
	 * @param root True if the portal does not filter categories.
	 */
	public void setAllCategories(boolean allCategories) {
		this.allCategories = allCategories;
	}

	/**
	 * Returns true if the portal does not filter content types.
	 * @return Whether the portal does not filter content types.
	 */
	public boolean isAllContentTypes() {
		return allContentTypes;
	}

	/**
	 * Sets whether the portal does not filter content types.
	 * @param root True if the portal does not filter content types.
	 */
	public void setAllContentTypes(boolean allContentTypes) {
		this.allContentTypes = allContentTypes;
	}

	/**
	 * Returns the root category.
	 * @return The root category.
	 */
	public CategoryEntity getRoot() {
		return root;
	}

	/**
	 * Sets the root category.
	 * @param root The root category.
	 */
	public void setRoot(CategoryEntity root) {
		this.root = root;
	}

	/**
	 * Returns the first level of categories.
	 * @return The first level of categories.
	 */
	public Set<CategoryEntity> getCategories() {
		if (categories == null) {
			categories = Sets.newHashSet();
		}
		return categories;
	}

	/**
	 * Returns the content types.
	 * @return The content types.
	 */
	public Set<ContentTypeEntity> getContentTypes() {
		if (contentTypes == null) {
			contentTypes = Sets.newHashSet();
		}
		return contentTypes;
	}

	/**
	 * Returns the portal bases.
	 * @return The portal bases.
	 */
	public Map<String, String> getBases() {
		if (bases == null) {
			bases = Maps.newHashMap();
		}
		return bases;
	}

	/**
	 * Returns the portal properties.
	 * @return The portal properties.
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = Maps.newHashMap();
		}
		return properties;
	}

	/**
	 * Returns the parent portal.
	 * @return The parent portal.
	 */
	public PortalEntity getParent() {
		return parent;
	}

	/**
	 * Sets the parent portal.
	 * @param parent The parent portal.
	 */
	public void setParent(PortalEntity parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent portal's id.
	 * @return The parent portal's id.
	 */
	public UUID getParentId() {
		final PortalEntity parent = getParent();
		return parent != null ? parent.getId() : null;
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
	 * Returns the router connector.
	 * @return The router connector.
	 */
	public ConnectorEntity getRouterConnector() {
		return routerConnector;
	}

	/**
	 * Sets the router connector.
	 * @param routerConnector The router connector.
	 */
	public void setRouterConnector(ConnectorEntity routerConnector) {
		this.routerConnector = routerConnector;
	}

	/**
	 * Returns the router exported bean.
	 * @return The router exported bean.
	 */
	public String getRouterBean() {
		return routerBean;
	}

	/**
	 * Sets the router exported bean.
	 * @param routerBean The router exported bean.
	 */
	public void setRouterBean(String routerBean) {
		this.routerBean = routerBean;
	}

	/**
	 * Returns the locale resolver connector.
	 * @return The locale resolver connector.
	 */
	public ConnectorEntity getLocaleConnector() {
		return localeConnector;
	}

	/**
	 * Sets the locale resolver connector.
	 * @param localeConnector The locale resolver connector.
	 */
	public void setLocaleConnector(ConnectorEntity localeConnector) {
		this.localeConnector = localeConnector;
	}

	/**
	 * Returns the locale resolver exported bean.
	 * @return The locale resolver exported bean.
	 */
	public String getLocaleBean() {
		return localeBean;
	}

	/**
	 * Sets the locale resolver exported bean.
	 * @param localeBean The locale resolver exported bean.
	 */
	public void setLocaleBean(String localeBean) {
		this.localeBean = localeBean;
	}

	/**
	 * Returns the device resolver connector.
	 * @return The device resolver connector.
	 */
	public ConnectorEntity getDeviceConnector() {
		return deviceConnector;
	}

	/**
	 * Sets the device resolver connector.
	 * @param deviceConnector The device resolver connector.
	 */
	public void setDeviceConnector(ConnectorEntity deviceConnector) {
		this.deviceConnector = deviceConnector;
	}

	/**
	 * Returns the device resolver exported bean.
	 * @return The device resolver exported bean.
	 */
	public String getDeviceBean() {
		return deviceBean;
	}

	/**
	 * Sets the device resolver exported bean.
	 * @param deviceBean The device resolver exported bean.
	 */
	public void setDeviceBean(String deviceBean) {
		this.deviceBean = deviceBean;
	}

	/**
	 * Returns the device capabilities provider connector.
	 * @return The device capabilities provider connector.
	 */
	public ConnectorEntity getDeviceCapsConnector() {
		return deviceCapsConnector;
	}

	/**
	 * Sets the device capabilities provider connector.
	 * @param deviceCapsConnector The device capabilities provider connector.
	 */
	public void setDeviceCapsConnector(ConnectorEntity deviceCapsConnector) {
		this.deviceCapsConnector = deviceCapsConnector;
	}

	/**
	 * Returns the device capabilities provider bean.
	 * @return The device capabilities provider bean.
	 */
	public String getDeviceCapsBean() {
		return deviceCapsBean;
	}

	/**
	 * Sets the device device capabilities exported bean.
	 * @param deviceCapsBean The device device capabilities exported bean.
	 */
	public void setDeviceCapsBean(String deviceCapsBean) {
		this.deviceCapsBean = deviceCapsBean;
	}

	/**
	 * Returns the node repository connector.
	 * @return The node repository connector.
	 */
	public ConnectorEntity getNrConnector() {
		return nrConnector;
	}

	/**
	 * Sets the node repository connector.
	 * @param nrConnector The node repository connector.
	 */
	public void setNrConnector(ConnectorEntity nrConnector) {
		this.nrConnector = nrConnector;
	}

	/**
	 * Returns the node repository exported bean.
	 * @return The node repository exported bean.
	 */
	public String getNrBean() {
		return nrBean;
	}

	/**
	 * Sets the node repository exported bean.
	 * @param nrBean The node repository exported bean.
	 */
	public void setNrBean(String nrBean) {
		this.nrBean = nrBean;
	}

	/**
	 * Returns the component instances.
	 * @return The component instances.
	 */
	public Set<ComponentDfn> getComponents() {
		if (components == null) {
			components = Sets.newHashSet();
		}
		return components;
	}

	/**
	 * Returns the pages.
	 * @return The pages.
	 */
	public Set<PageDfn> getPages() {
		if (pages == null) {
			pages = Sets.newHashSet();
		}
		return pages;
	}

	/**
	 * Returns the default locale.
	 * @return The default locale.
	 */
	public String getDefaultLocale() {
		if (defaultLocale == null) {
			return "es";
		}
		return defaultLocale;
	}

	/**
	 * Sets the default locale.
	 * @param nrBean The default locale.
	 */
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	/**
	 * Returns the available localized names.
	 * @return The available localized names.
	 */
	public Map<String, String> getL7DNames() {
		if (l7dNames == null) {
			l7dNames = Maps.newHashMap();
		}
		return l7dNames;
	}

	/**
	 * Returns the available locales.
	 * @return The available locales.
	 */
	public Set<String> getLocales() {
		if (locales == null) {
			locales = Sets.newHashSet();
		}
		return locales;
	}

	/**
	 * Returns whether to include uncategorized contents.
	 * @return True if uncategorized contents must be included by default.
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
	 * Returns whether parent portal devices are used.
	 * @return True if parent portal devices are used.
	 */
	public boolean isInheritedDevices() {
		return inheritedDevices != null ? inheritedDevices.booleanValue() : false;
	}

	/**
	 * Sets whether parent portal devices are used.
	 * @param inheritedDevices True if parent portal devices are used.
	 */
	public void setInheritedDevices(boolean inheritedDevices) {
		this.inheritedDevices = inheritedDevices;
	}

	/**
	 * Returns the overriden components.
	 * @return The overriden components.
	 */
	public Map<ComponentEntity, OverridenComponentValue> getOverridenComponents() {
		if (overridenComponents == null) {
			overridenComponents = Maps.newHashMap();
		}
		return overridenComponents;
	}

	/**
	 * Returns the set filters.
	 * @return The set filters.
	 */
	public Map<String, SetFilterValue> getSetFilters() {
		if (setFilters == null) {
			setFilters = Maps.newHashMap();
		}
		return setFilters;
	}

	/**
	 * Returns the portal devices.
	 * @return The portal devices.
	 */
	public Map<DeviceEntity, PortalDeviceValue> getDevices() {
		if (devices == null) {
			devices = Maps.newHashMap();
		}
		return devices;
	}

	/**
	 * Returns the cache configuration.
	 * @return The cache configuration.
	 */
	public PortalCacheValue getCache() {
		return cache;
	}

	/**
	 * Sets the cache configuration.
	 * @param cache The cache configuration.
	 */
	public void setCache(PortalCacheValue cache) {
		this.cache = cache;
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

	/**
	 * @return the portalConfiguration
	 */
	public Map<String, PortalConfigurationValue> getPortalConfiguration() {
		if (portalConfiguration == null) {
			portalConfiguration = Maps.newHashMap();
		}
		return portalConfiguration;
	}

	/**
	 * @param portalConfiguration the portalConfiguration to set
	 */
	public void setPortalConfiguration(Map<String, PortalConfigurationValue> portalConfiguration) {
		this.portalConfiguration = portalConfiguration;
	}

	/**
	 * Return active portal configuration value.
	 * @param name Bean name.
	 * @return PortalConfigurationValue
	 */
	public PortalConfigurationValue getActivePortalConfigurationValue(String name) {
		PortalConfigurationValue pcv = null;
		
		pcv = this.getPortalConfiguration().get(name);
		
		// Buscamos en el portal padre una configuracion valida
		if (pcv == null && this.getParentId() != null) {
			pcv = this.getParent().getCurrent().getActivePortalConfigurationValue(name);
		}
		
		return pcv;
	}
	
	
}
