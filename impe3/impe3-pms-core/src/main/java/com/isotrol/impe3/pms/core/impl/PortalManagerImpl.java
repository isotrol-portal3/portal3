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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.ComponentManager;
import com.isotrol.impe3.pms.core.ConfigurationManager;
import com.isotrol.impe3.pms.core.DependencySetManager;
import com.isotrol.impe3.pms.core.PageManager;
import com.isotrol.impe3.pms.core.PortalManager;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ComponentDfn;
import com.isotrol.impe3.pms.model.ComponentEntity;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.NameValue;
import com.isotrol.impe3.pms.model.OverridenComponentValue;
import com.isotrol.impe3.pms.model.PortalConfigurationValue;
import com.isotrol.impe3.pms.model.PortalDeviceValue;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEdition;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.UserEntity;

import net.sf.derquinsej.collect.Hierarchy;


/**
 * Implementation of PortalManager.
 * @author Andres Rodriguez.
 */
@Component
public final class PortalManagerImpl extends AbstractPortalService<PortalEntity, PortalDfn, PortalEdition> implements
	PortalManager {
	/** Component manager. */
	private ComponentManager componentManager;
	/** Page manager. */
	private PageManager pageManager;
	@Autowired
	private ConfigurationManager configurationManager;
	@Autowired
	private DependencySetManager dependencySetManager;

	/** Default constructor. */
	public PortalManagerImpl() {
	}

	@Autowired
	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	@Autowired
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	private Iterable<UUID> others(PortalsObject portals, PortalEntity entity) {
		return filter(portals.keySet(), not(equalTo(entity.getId())));
	}

	private Iterable<UUID> children(PortalsObject portals, PortalEntity entity) {
		return portals.getDescendantsKeys(entity.getId());
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalManager#touchHierarchy(com.isotrol.impe3.pms.core.obj.PortalsObject,
	 * com.isotrol.impe3.pms.model.PortalEntity)
	 */
	public PortalDfn touchHierarchy(PortalsObject portals, PortalEntity entity) throws PMSException {
		final UserEntity user = loadUser();
		for (UUID id : others(portals, entity)) {
			loadPortal(id).touchAll(user);
		}
		PortalDfn current = entity.getCurrent();
		if (isNewDfnNeeded(current)) {
			current = duplicate(current);
		}
		entity.touchAll(user);
		return current;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalManager#touchOffline(com.isotrol.impe3.pms.model.PortalEntity)
	 */
	public PortalDfn touchOffline(PortalEntity entity) throws PMSException {
		final PortalDfn current = entity.getCurrent();
		final UserEntity user = loadUser();
		if (!isNewDfnNeeded(current)) {
			entity.touchOfflineVersion(user);
			current.setUpdated(user);
			return current;
		} else {
			entity.touchAll(user);
			return duplicate(current);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalManager#touchComponents(com.isotrol.impe3.pms.core.obj.PortalsObject,
	 * com.isotrol.impe3.pms.model.PortalEntity)
	 */
	public PortalDfn touchComponents(PortalsObject portals, PortalEntity entity) throws PMSException {
		final UserEntity user = loadUser();
		for (UUID id : children(portals, entity)) {
			loadPortal(id).touchChildrenVersions(user);
		}
		return touchComponents(user, entity);
	}

	private PortalDfn touchComponents(UserEntity user, PortalEntity entity) throws PMSException {
		entity.touchComponentVersion(user);
		entity.touchChildrenVersions(user);
		final PortalDfn current = entity.getCurrent();
		if (!isNewDfnNeeded(current)) {
			return current;
		} else {
			return duplicate(current);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalManager#touchPages(com.isotrol.impe3.pms.core.obj.PortalsObject,
	 * com.isotrol.impe3.pms.model.PortalEntity)
	 */
	public PortalDfn touchPages(PortalsObject portals, PortalEntity entity) throws PMSException {
		final UserEntity user = loadUser();
		for (UUID id : children(portals, entity)) {
			loadPortal(id).touchChildrenVersions(user);
		}
		return touchPages(user, entity);
	}

	private PortalDfn touchPages(UserEntity user, PortalEntity entity) throws PMSException {
		entity.touchPagesVersion(user);
		entity.touchChildrenVersions(user);
		final PortalDfn current = entity.getCurrent();
		if (!isNewDfnNeeded(current)) {
			return current;
		} else {
			return duplicate(current);
		}
	}

	private PortalDfn duplicate(PortalDfn current) throws PMSException {
		final PortalDfn dfn = new PortalDfn();
		dfn.setRoutingDomain(current.getRoutingDomain());
		dfn.setName(new NameValue(current.getName()));
		dfn.setRoutable(current.isRoutable());
		dfn.setAllCategories(current.isAllCategories());
		dfn.setAllContentTypes(current.isAllContentTypes());
		dfn.setRoot(current.getRoot());
		dfn.getCategories().addAll(current.getCategories());
		dfn.getContentTypes().addAll(current.getContentTypes());
		dfn.getBases().putAll(current.getBases());
		dfn.getProperties().putAll(current.getProperties());
		dfn.setParent(current.getParent());
		dfn.setRouterConnector(current.getRouterConnector());
		dfn.setRouterBean(current.getRouterBean());
		dfn.setLocaleConnector(current.getLocaleConnector());
		dfn.setLocaleBean(current.getLocaleBean());
		dfn.setDeviceConnector(current.getDeviceConnector());
		dfn.setDeviceBean(current.getDeviceBean());
		dfn.setDeviceCapsConnector(current.getDeviceCapsConnector());
		dfn.setDeviceCapsBean(current.getDeviceCapsBean());
		dfn.setNrConnector(current.getNrConnector());
		dfn.setNrBean(current.getNrBean());
		for (ComponentDfn componentDfn : current.getComponents()) {
			dfn.getComponents().add(componentManager.duplicate(componentDfn));
		}
		
		// Portal configuration
		final Map<String, PortalConfigurationValue> portalConfigMap = dfn.getPortalConfiguration();
		for (Entry<String, PortalConfigurationValue> entry : current.getPortalConfiguration().entrySet()) {
			portalConfigMap.put(entry.getKey(), entry.getValue().clone(configurationManager));
		}
		
		dfn.getPages().addAll(Collections2.transform(current.getPages(), pageManager.getDuplicator()));
		dfn.setDefaultLocale(current.getDefaultLocale());
		dfn.getL7DNames().putAll(dfn.getL7DNames());
		dfn.getLocales().addAll(dfn.getLocales());
		dfn.setUncategorized(current.getUncategorized());
		dfn.setDue(current.getDue());
		// Overridden components
		final Map<ComponentEntity, OverridenComponentValue> ocv = dfn.getOverridenComponents();
		for (Entry<ComponentEntity, OverridenComponentValue> entry : current.getOverridenComponents().entrySet()) {
			ocv.put(entry.getKey(), entry.getValue().clone(configurationManager, dependencySetManager));
		}
		dfn.getSetFilters().putAll(current.getSetFilters());
		// Devices
		final Map<DeviceEntity, PortalDeviceValue> devicesMap = dfn.getDevices();
		for (Entry<DeviceEntity, PortalDeviceValue> entry : current.getDevices().entrySet()) {
			devicesMap.put(entry.getKey(), entry.getValue().clone());
		}
		// Cache
		dfn.setCache(current.getCache());
		// Done
		saveNewDfn(current.getEntity(), dfn);
		return dfn;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalManager#getCategories(com.isotrol.impe3.pms.model.PortalDfn)
	 */
	public Hierarchy<UUID, CategoryEntity> getCategories(PortalDfn dfn) {
		// TODO not all
		return getEnvironment().getOfflineCategoryHierarchy();
	}
}
