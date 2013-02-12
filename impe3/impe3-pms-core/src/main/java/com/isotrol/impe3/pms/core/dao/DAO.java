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

package com.isotrol.impe3.pms.core.dao;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.sf.derquinsej.hib3.GeneralDAO;

import org.hibernate.Query;

import com.isotrol.impe3.pms.core.PublishedObject;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.Entity;
import com.isotrol.impe3.pms.model.PageEntity;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.RoutingDomainEntity;
import com.isotrol.impe3.pms.model.SourceMappingEntity;


/**
 * General DAO for the PMS.
 * @author Andres Rodriguez.
 */
public interface DAO extends GeneralDAO {
	/**
	 * Returns the root category of an environment.
	 * @param environmentId Environment ID.
	 * @return The root category or {@code null} if not found.
	 */
	CategoryEntity getRootCategory(UUID environmentId);

	/**
	 * Returns the offline categories of an environment.
	 * @param environmentId Environment Id.
	 * @return The offline categories.
	 */
	Iterable<CategoryEntity> getOfflineCategories(UUID environmentId);

	/**
	 * Returns the offline content types of an environment.
	 * @param environmentId Environment Id.
	 * @return The offline content types.
	 */
	Iterable<ContentTypeEntity> getOfflineContentTypes(UUID environmentId);

	/**
	 * Returns the offline connectors of an environment.
	 * @param environmentId Environment Id.
	 * @return The offline connectors.
	 */
	Iterable<ConnectorEntity> getOfflineConnectors(UUID environmentId);

	/**
	 * Returns the offline portals of an environment.
	 * @param environmentId Environment Id.
	 * @return The offline portals.
	 */
	Iterable<PortalEntity> getOfflinePortals(UUID environmentId);

	/**
	 * Returns the categories for published flag migration.
	 * @return The categories to migrate.
	 */
	Iterable<CategoryEntity> getPFMCategories();

	/**
	 * Returns the content types for published flag migration.
	 * @return The content types to migrate.
	 */
	Iterable<ContentTypeEntity> getPFMContentTypes();

	/**
	 * Returns the connectors for published flag migration.
	 * @return The connectors to migrate.
	 */
	Iterable<ConnectorEntity> getPFMConnectors();

	/**
	 * Returns the portals for published flag migration.
	 * @return The portals to migrate.
	 */
	Iterable<PortalEntity> getPFMPortals();

	/**
	 * Returns the content type editions.
	 * @param environmentId Environment ID.
	 * @return All the content type editions.
	 */
	Iterable<PublishedObject> getContentTypeEditions(UUID environmentId);

	/**
	 * Returns the category editions.
	 * @param environmentId Environment ID.
	 * @return All the category editions.
	 */
	Iterable<PublishedObject> getCategoryEditions(UUID environmentId);

	/**
	 * Returns the connector editions.
	 * @param environmentId Environment ID.
	 * @return All the connector editions.
	 */
	Iterable<PublishedObject> getConnectorEditions(UUID environmentId);

	/**
	 * Returns the portal editions.
	 * @param environmentId Environment ID.
	 * @return All the portal editions.
	 */
	Iterable<PublishedObject> getPortalEditions(UUID environmentId);

	/**
	 * Finds a source mapping by environment and name.
	 * @param envId Environment ID.
	 * @param name Name.
	 * @return The requested source mapping or {@code null} if it is not found.
	 */
	SourceMappingEntity getSourceMappingByName(UUID envId, String name);

	/**
	 * Finds a routing domain by environment and name.
	 * @param envId Environment ID.
	 * @param name Name.
	 * @return The requested routing domain or {@code null} if it is not found.
	 */
	RoutingDomainEntity getRoutingDomainByName(UUID envId, String name);

	/**
	 * Finds a device by name.
	 * @param name Device name.
	 * @return The requested device or {@code null} if it is not found.
	 */
	DeviceEntity getDeviceByName(String name);

	/**
	 * Returns the pages that use a device.
	 * @param deviceId Device used by the pages.
	 * @return The children of the specified categories.
	 */
	List<PageEntity> getPagesByDefaultDevice(UUID deviceId);

	/**
	 * Finds the lastest editions for an environment.
	 * @param envId Environment id.
	 * @param id Edition id.
	 * @return The requested editions.
	 */
	EditionEntity getEditionById(UUID envId, UUID id);

	/**
	 * Finds the lastest editions for an environment.
	 * @param envId Environment id.
	 * @param rows Maximum rows.
	 * @return The requested editions.
	 */
	Iterable<EditionEntity> getLastestEditions(UUID envId, int rows);

	/**
	 * Returns whether an entity-related query has results.
	 * @param query Query to perform.
	 * @param envId Environment id.
	 * @param id Entity id.
	 * @return True if the query returns any row.
	 */
	boolean hasRows(Query query, UUID envId, UUID id);

	/**
	 * Returns whether an entity-related query has results.
	 * @param query Named query to perform.
	 * @param envId Environment id.
	 * @param id Entity id.
	 * @return True if the query returns any row.
	 */
	boolean hasRows(String query, UUID envId, UUID id);

	/**
	 * Returns whether an entity-related query has results.
	 * @param query Query to perform.
	 * @param envId Environment id.
	 * @param entity Entity.
	 * @return True if the query returns any row.
	 */
	boolean hasRows(Query query, UUID envId, Entity entity);

	/**
	 * Returns whether an entity-related query has results.
	 * @param query Named query to perform.
	 * @param envId Environment id.
	 * @param entity Entity.
	 * @return True if the query returns any row.
	 */
	boolean hasRows(String query, UUID envId, Entity entity);

	/**
	 * Returns whether an entity is used.
	 * @param envId Environment id.
	 * @param id Entity id.
	 * @param queries Named queries to perform.
	 * @return True if any of the queries returns any row.
	 */
	boolean isUsed(UUID envId, UUID id, Iterable<String> queries);

	/**
	 * Returns whether an entity is used.
	 * @param envId Environment id.
	 * @param id Entity id.
	 * @param queries Named queries to perform.
	 * @return True if any of the queries returns any row.
	 */
	boolean isUsed(UUID envId, UUID id, String... queries);

	/**
	 * Returns whether an entity is used.
	 * @param envId Environment id.
	 * @param entity Entity.
	 * @param queries Named queries to perform.
	 * @return True if any of the queries returns any row.
	 */
	boolean isUsed(UUID envId, Entity entity, Iterable<String> queries);

	/**
	 * Returns whether an entity is used.
	 * @param envId Environment id.
	 * @param entity Entity.
	 * @param queries Named queries to perform.
	 * @return True if any of the queries returns any row.
	 */
	boolean isUsed(UUID envId, Entity entity, String... queries);

	/**
	 * Returns the ids of the uploaded files older than a certain number of seconds
	 * @param age Minimum age, in seconds.
	 * @return The set of file ids.
	 */
	Set<UUID> getUploadedFiles(int age);

	/**
	 * Returns the files that are used by configuration items.
	 * @return The ids of the used files.
	 */
	Set<UUID> getUsedFiles();
}
