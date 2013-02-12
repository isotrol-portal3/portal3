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

package com.isotrol.impe3.pms.core.dao.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.sf.derquinsej.hib3.GeneralDAOImpl;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.isotrol.impe3.pms.core.PublishedObject;
import com.isotrol.impe3.pms.core.dao.DAO;
import com.isotrol.impe3.pms.model.CategoryEdition;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ConnectorEdition;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.ContentTypeEdition;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.Entity;
import com.isotrol.impe3.pms.model.FileEntity;
import com.isotrol.impe3.pms.model.PageEntity;
import com.isotrol.impe3.pms.model.PortalEdition;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.PublishableEntity;
import com.isotrol.impe3.pms.model.RoutingDomainEntity;
import com.isotrol.impe3.pms.model.SourceMappingEntity;


/**
 * General DAO implementation.
 * @author Andres Rodriguez.
 */
@Repository
public final class DAOImpl extends GeneralDAOImpl implements DAO {
	/**
	 * Constructor.
	 */
	public DAOImpl() {
	}

	/**
	 * @see net.sf.derquinsej.hib3.AbstractDAOImpl#setSessionFactory(org.hibernate.SessionFactory)
	 */
	@Override
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getRootCategory(java.util.UUID)
	 */
	public CategoryEntity getRootCategory(UUID environmentId) {
		final Query q = getNamedQuery(CategoryEntity.ROOT).setParameter(0, environmentId);
		return unique(CategoryEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getOfflineCategories(java.util.UUID)
	 */
	public Iterable<CategoryEntity> getOfflineCategories(UUID environmentId) {
		final Query q = getNamedQuery(CategoryEntity.OFFLINE).setParameter(0, environmentId);
		return list(CategoryEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getOfflineContentTypes(java.util.UUID)
	 */
	public Iterable<ContentTypeEntity> getOfflineContentTypes(UUID environmentId) {
		final Query q = getNamedQuery(ContentTypeEntity.OFFLINE).setParameter(0, environmentId);
		return list(ContentTypeEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getOfflineConnectors(java.util.UUID)
	 */
	public Iterable<ConnectorEntity> getOfflineConnectors(UUID environmentId) {
		final Query q = getNamedQuery(ConnectorEntity.OFFLINE).setParameter(0, environmentId);
		return list(ConnectorEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getOfflinePortals(java.util.UUID)
	 */
	public Iterable<PortalEntity> getOfflinePortals(UUID environmentId) {
		final Query q = getNamedQuery(PortalEntity.OFFLINE).setParameter(0, environmentId);
		return list(PortalEntity.class, q);
	}

	private <T extends PublishableEntity<T, ?, ?>> Iterable<T> getPFM(String queryName, Class<T> type) {
		final Query q = getNamedQuery(queryName).setLockOptions(LockOptions.UPGRADE);
		return list(type, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getPFMCategories()
	 */
	public Iterable<CategoryEntity> getPFMCategories() {
		return getPFM(CategoryEntity.PFM, CategoryEntity.class);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getPFMContentTypes()
	 */
	public Iterable<ContentTypeEntity> getPFMContentTypes() {
		return getPFM(ContentTypeEntity.PFM, ContentTypeEntity.class);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getPFMConnectors()
	 */
	public Iterable<ConnectorEntity> getPFMConnectors() {
		return getPFM(ConnectorEntity.PFM, ConnectorEntity.class);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getPFMPortals()
	 */
	public Iterable<PortalEntity> getPFMPortals() {
		return getPFM(PortalEntity.PFM, PortalEntity.class);
	}

	private Set<PublishedObject> published(Iterable<Object[]> result) {
		Set<PublishedObject> set = Sets.newHashSet();
		for (Object[] row : result) {
			UUID entityId = (UUID) row[0];
			UUID definitionId = (UUID) row[1];
			UUID editionId = (UUID) row[2];
			set.add(PublishedObject.of(entityId, definitionId, editionId));
		}
		return set;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getContentTypeEditions(java.util.UUID)
	 */
	public Iterable<PublishedObject> getContentTypeEditions(UUID environmentId) {
		return published(list(Object[].class, ContentTypeEdition.CURRENT, environmentId));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getCategoryEditions(java.util.UUID)
	 */
	public Iterable<PublishedObject> getCategoryEditions(UUID environmentId) {
		return published(list(Object[].class, CategoryEdition.CURRENT, environmentId));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getConnectorEditions(java.util.UUID)
	 */
	public Iterable<PublishedObject> getConnectorEditions(UUID environmentId) {
		return published(list(Object[].class, ConnectorEdition.CURRENT, environmentId));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getPortalEditions(java.util.UUID)
	 */
	public Iterable<PublishedObject> getPortalEditions(UUID environmentId) {
		return published(list(Object[].class, PortalEdition.CURRENT, environmentId));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getSourceMappingByName(java.util.UUID, java.lang.String)
	 */
	public SourceMappingEntity getSourceMappingByName(UUID envId, String name) {
		checkNotNull(envId);
		checkNotNull(name);
		final Query q = getNamedQuery(SourceMappingEntity.BY_NAME).setString(0, name).setParameter(1, envId);
		return unique(SourceMappingEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getRoutingDomainByName(java.util.UUID, java.lang.String)
	 */
	public RoutingDomainEntity getRoutingDomainByName(UUID envId, String name) {
		checkNotNull(envId);
		checkNotNull(name);
		final Query q = getNamedQuery(RoutingDomainEntity.BY_NAME).setString(0, name).setParameter(1, envId);
		return unique(RoutingDomainEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getDeviceByName(java.lang.String)
	 */
	public DeviceEntity getDeviceByName(String name) {
		checkNotNull(name);
		final Query q = getNamedQuery(DeviceEntity.BY_NAME).setString(0, name);
		return unique(DeviceEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getPagesByDefaultDevice(java.util.UUID)
	 */
	public List<PageEntity> getPagesByDefaultDevice(UUID deviceId) {
		final Query q = getNamedQuery(PageEntity.BY_DEVICE).setParameter("id", checkNotNull(deviceId));
		return list(PageEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getLastestEditions(java.util.UUID, int)
	 */
	public Iterable<EditionEntity> getLastestEditions(UUID envId, int rows) {
		final Query q = getNamedQuery(EditionEntity.LASTEST).setParameter(0, checkNotNull(envId)).setMaxResults(rows);
		return list(EditionEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getEditionById(java.util.UUID, java.util.UUID)
	 */
	public EditionEntity getEditionById(UUID envId, UUID id) {
		final Query q = getNamedQuery(EditionEntity.BY_ID).setParameter(0, id).setParameter(1, envId);
		return unique(EditionEntity.class, q);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#hasRows(org.hibernate.Query, java.util.UUID, java.util.UUID)
	 */
	public boolean hasRows(Query query, UUID envId, UUID id) {
		checkNotNull(query);
		checkNotNull(envId);
		checkNotNull(id);
		query.setParameter("envId", envId).setParameter("id", id).setMaxResults(1);
		return query.iterate().hasNext();
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#hasRows(java.lang.String, java.util.UUID, java.util.UUID)
	 */
	public boolean hasRows(String query, UUID envId, UUID id) {
		return hasRows(getNamedQuery(checkNotNull(query)), envId, id);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#hasRows(org.hibernate.Query, java.util.UUID,
	 * com.isotrol.impe3.pms.model.Entity)
	 */
	public boolean hasRows(Query query, UUID envId, Entity entity) {
		checkNotNull(query);
		checkNotNull(envId);
		checkNotNull(entity);
		query.setParameter("envId", envId).setEntity("entity", entity).setMaxResults(1);
		return query.iterate().hasNext();
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#hasRows(java.lang.String, java.util.UUID,
	 * com.isotrol.impe3.pms.model.Entity)
	 */
	public boolean hasRows(String query, UUID envId, Entity entity) {
		return hasRows(getNamedQuery(checkNotNull(query)), envId, entity);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#isUsed(java.util.UUID, java.util.UUID, java.lang.Iterable)
	 */
	public boolean isUsed(final UUID envId, final UUID id, Iterable<String> queries) {
		final Predicate<String> hasRows = new Predicate<String>() {
			public boolean apply(String input) {
				return hasRows(input, envId, id);
			}
		};
		return Iterables.any(queries, hasRows);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#isUsed(java.util.UUID, java.util.UUID, java.lang.String[])
	 */
	public boolean isUsed(UUID envId, UUID id, String... queries) {
		return isUsed(envId, id, Arrays.asList(queries));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#isUsed(java.util.UUID, com.isotrol.impe3.pms.model.Entity,
	 * java.lang.Iterable)
	 */
	public boolean isUsed(final UUID envId, final Entity entity, final Iterable<String> queries) {
		final Predicate<String> hasRows = new Predicate<String>() {
			public boolean apply(String input) {
				return hasRows(input, envId, entity);
			}
		};
		return Iterables.any(queries, hasRows);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#isUsed(java.util.UUID, com.isotrol.impe3.pms.model.Entity,
	 * java.lang.String[])
	 */
	public boolean isUsed(UUID envId, Entity entity, String... queries) {
		return isUsed(envId, entity, Arrays.asList(queries));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getUploadedFiles(int)
	 */
	public Set<UUID> getUploadedFiles(int age) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, -age);
		@SuppressWarnings("unchecked")
		List<UUID> list = getNamedQuery(FileEntity.FILEID_BEFORE).setParameter(0, c).list();
		return Sets.newHashSet(list);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.dao.DAO#getUsedFiles()
	 */
	public Set<UUID> getUsedFiles() {
		@SuppressWarnings("unchecked")
		List<String> list = getSession().createSQLQuery(
			"SELECT CV_FLDT_ID FROM CONFIGURATION_VALUE WHERE CV_FLDT_ID IS NOT NULL").list();
		Set<UUID> ids = Sets.newHashSet();
		for (String s : list) {
			try {
				ids.add(UUID.fromString(s));
			} catch (Exception e) {}
		}
		return ids;
	}
}
