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


import static com.isotrol.impe3.pms.core.support.NotFoundProviders.PORTAL;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.CategoryManager;
import com.isotrol.impe3.pms.core.ConnectorManager;
import com.isotrol.impe3.pms.core.ContentTypeManager;
import com.isotrol.impe3.pms.core.DeviceManager;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.PMSContext;
import com.isotrol.impe3.pms.core.PortalLoader;
import com.isotrol.impe3.pms.core.RoutingDomainManager;
import com.isotrol.impe3.pms.core.SourceMappingManager;
import com.isotrol.impe3.pms.core.obj.CategoriesObject;
import com.isotrol.impe3.pms.core.obj.ConnectorsObject;
import com.isotrol.impe3.pms.core.obj.ContentTypesObject;
import com.isotrol.impe3.pms.core.obj.Context0;
import com.isotrol.impe3.pms.core.obj.Context1;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.IA;
import com.isotrol.impe3.pms.core.obj.RoutingDomainsObject;
import com.isotrol.impe3.pms.core.obj.SourceMappingsObject;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.Entity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.OfEnvironment;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.UserEntity;
import com.isotrol.impe3.pms.model.WithCreated;
import com.isotrol.impe3.pms.model.WithUpdated;


/**
 * Abstract implementation for services requiring context information.
 * @author Andres Rodriguez.
 */
public abstract class AbstractContextService extends AbstractService {
	/** PMS Context. */
	private PMSContext context;
	/** Module registry. */
	private ModuleRegistry registry;
	/** Routing domains manager. */
	@Autowired
	private RoutingDomainManager routingDomainManager;
	/** Device Manager. */
	@Autowired
	private DeviceManager deviceManager;
	/** Content type manager. */
	private ContentTypeManager contentTypeManager;
	/** Category manager. */
	private CategoryManager categoryManager;
	/** Source mapping manager. */
	private SourceMappingManager sourceMappingManager;
	/** Connector manager. */
	private ConnectorManager connectorManager;
	/** Portal loader. */
	private PortalLoader portalLoader;

	/**
	 * Constructor.
	 */
	public AbstractContextService() {
	}

	@Autowired
	public void setContext(PMSContext context) {
		this.context = context;
	}

	@Autowired
	public void setRegistry(ModuleRegistry registry) {
		this.registry = registry;
	}

	@Autowired
	public void setContentTypeManager(ContentTypeManager contentTypeManager) {
		this.contentTypeManager = contentTypeManager;
	}

	@Autowired
	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}

	@Autowired
	public void setSourceMappingManager(SourceMappingManager sourceMappingManager) {
		this.sourceMappingManager = sourceMappingManager;
	}

	@Autowired
	public void setConnectorManager(ConnectorManager connectorManager) {
		this.connectorManager = connectorManager;
	}

	@Autowired
	public void setPortalLoader(PortalLoader portalLoader) {
		this.portalLoader = portalLoader;
	}

	protected final EnvironmentEntity getEnvironment() {
		final UUID id = getEnvironmentId();
		if (id == null) {
			return null;
		}
		return getDao().findById(EnvironmentEntity.class, id, false);
	}

	protected final UUID getEnvironmentId() {
		return context.getEnvironmentId();
	}

	protected final UUID getUserId() {
		return context.getUserId();
	}

	protected final UserEntity loadUser() throws PMSException {
		return load(UserEntity.class, getUserId(), NotFoundProviders.USER);
	}

	protected final Locale getLocale() {
		return context.getLocale();
	}

	protected final ModuleRegistry getRegistry() {
		return registry;
	}

	protected <T extends Entity> T saveNewEntity(T entity) throws PMSException {
		if (entity instanceof WithCreated) {
			((WithCreated) entity).setCreated(loadUser());
		}
		if (entity instanceof WithUpdated) {
			((WithUpdated) entity).setUpdated(loadUser());
		}
		return super.saveNewEntity(entity);
	}

	protected final ContentTypesObject loadContentTypes() {
		return contentTypeManager.loadOffline(getEnvironmentId());
	}

	protected final CategoriesObject loadCategories() {
		return categoryManager.loadOffline(getEnvironmentId());
	}

	protected final IA loadIA() {
		return new IA(loadContentTypes(), loadCategories());
	}

	private RoutingDomainsObject loadDomains() {
		return routingDomainManager.loadOffline(getEnvironmentId());
	}

	protected final SourceMappingsObject loadMappings() {
		return sourceMappingManager.loadOffline(getEnvironmentId());
	}

	protected final ConnectorsObject loadConnectors() {
		return connectorManager.loadOffline(getEnvironmentId());
	}

	protected final CategoryEntity getRootCategory() throws PMSException {
		return categoryManager.getRoot(getEnvironment());
	}

	protected final <T extends OfEnvironment> T loadOfEnvironment(Class<T> type, UUID id, NotFoundProvider nfp)
		throws EntityNotFoundException {
		final T entity = load(type, id, nfp);
		final UUID envId = getEnvironmentId();
		if (envId == null || !envId.equals(entity.getEnvironment().getId())) {
			throw nfp.getNotFoundException(id);
		}
		return entity;
	}

	protected final <T extends OfEnvironment> T loadOfEnvironment(Class<T> type, String id, NotFoundProvider nfp)
		throws EntityNotFoundException {
		return loadOfEnvironment(type, nfp.toUUID(id), nfp);
	}

	protected ContentTypeEntity loadContentType(UUID id) throws EntityNotFoundException {
		return loadOfEnvironment(ContentTypeEntity.class, id, NotFoundProviders.CONTENT_TYPE);
	}

	protected ContentTypeEntity loadContentType(String id) throws EntityNotFoundException {
		return loadOfEnvironment(ContentTypeEntity.class, id, NotFoundProviders.CONTENT_TYPE);
	}

	protected final CategoryEntity loadCategory(UUID id) throws EntityNotFoundException {
		return loadOfEnvironment(CategoryEntity.class, id, NotFoundProviders.CATEGORY);
	}

	protected final CategoryEntity loadCategory(String id) throws EntityNotFoundException {
		return loadOfEnvironment(CategoryEntity.class, id, NotFoundProviders.CATEGORY);
	}

	protected ConnectorEntity loadConnectorEntity(UUID id) throws EntityNotFoundException {
		return loadOfEnvironment(ConnectorEntity.class, id, NotFoundProviders.CONNECTOR);
	}

	protected ConnectorEntity loadConnectorEntity(String id) throws EntityNotFoundException {
		return loadOfEnvironment(ConnectorEntity.class, id, NotFoundProviders.CONNECTOR);
	}

	protected final PortalEntity loadPortal(UUID id) throws EntityNotFoundException {
		return loadOfEnvironment(PortalEntity.class, id, PORTAL);
	}

	protected final PortalEntity loadPortal(String id) throws EntityNotFoundException {
		return loadOfEnvironment(PortalEntity.class, id, PORTAL);
	}

	protected final Context0 loadContext0() {
		final UUID envId = getEnvironmentId();
		return new Context0(getEnvironmentId(), getLocale(), loadDomains(), loadMappings(), deviceManager.load(envId),
			registry, getUUIDGenerator());
	}

	protected final Context1 loadContext1() {
		return new Context1(loadContext0(), loadIA());
	}

	protected final ContextGlobal loadContextGlobal() {
		return new ContextGlobal(loadContext1(), loadConnectors(), portalLoader);
	}

	final void purge() {
		categoryManager.purge();
		connectorManager.purge();
		contentTypeManager.purge();
		sourceMappingManager.purge();
		routingDomainManager.purge();
		portalLoader.purge();
	}
}
