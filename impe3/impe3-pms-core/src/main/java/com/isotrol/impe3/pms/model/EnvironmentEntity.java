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


import static com.isotrol.impe3.pms.core.support.EntityFunctions.notDeleted;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Objects;


/**
 * Entity that represents an environment.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "ENVIRONMENT")
@NamedQuery(name = EnvironmentEntity.BY_NAME, query = "from EnvironmentEntity as e where e.name = ?")
public final class EnvironmentEntity extends WithCreatedUpdatedEntity {
	private static final String INTERNAL_SEGMENT = "portal3ir";
	/** Query by name. */
	public static final String BY_NAME = "environment.byName";
	/** Environment name. */
	@Column(name = "NAME", length = Lengths.NAME, unique = true)
	private String name;
	/** Environment description. */
	@Column(name = "DESCRIPTION", length = Lengths.DESCRIPTION)
	private String description;
	/** Current edition. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "EDTN_ID", nullable = true)
	private EditionEntity current;
	/** Internal path segment. */
	@Column(name = "ENVT_INTERNAL_SEGMENT", nullable = true)
	private String internalSegment;
	/** Maximun unsuccessful login attempts. */
	@Column(name = "ENVT_MAX_LOGIN_ATTEMPS", nullable = true)
	private Integer maxLoginAttempts;
	/** Content types version. */
	@Column(name = "COTP_VERSION", nullable = true)
	private Integer contentTypeVersion = 0;
	/** Category version. */
	@Column(name = "CTGY_VERSION", nullable = true)
	private Integer categoryVersion = 0;
	/** Connectors version. */
	@Column(name = "CNNT_VERSION", nullable = true)
	private Integer connectorVersion = 0;
	/** Portals version. */
	@Column(name = "PRTL_VERSION", nullable = true)
	private Integer portalVersion = 0;
	/** Source mappings version. */
	@Column(name = "SMAP_VERSION", nullable = true)
	private Integer mappingVersion = 0;
	/** Routing domain version. */
	@Column(name = "RTDM_VERSION", nullable = true)
	private Integer domainVersion = 0;
	/** Devices version. */
	@Column(name = "DVCE_VERSION", nullable = true)
	private Integer deviceVersion = 0;
	/** Offline version. */
	@Column(name = "OFFLINE_VERSION", nullable = true)
	private Integer offlineVersion = 0;
	/** Online version. */
	@Column(name = "ONLINE_VERSION", nullable = true)
	private Integer onlineVersion = 0;
	/** Editions. */
	@OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
	private Set<EditionEntity> editions;
	/** Content types. */
	@OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
	private Set<ContentTypeEntity> contentTypes;
	/** Categories. */
	@OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
	private Set<CategoryEntity> categories;
	/** Connectors. */
	@OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
	private Set<ConnectorEntity> connectors;
	/** Portals. */
	@OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
	private Set<PortalEntity> portals;
	/** Routing domains. */
	@OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
	private Set<SourceMappingEntity> sourceMappings;
	/** Routing domains. */
	@OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
	private Set<RoutingDomainEntity> routingDomains;

	/** Default constructor. */
	public EnvironmentEntity() {
	}

	/**
	 * Returns the environment name.
	 * @return The environment name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the environment name.
	 * @param name The environment name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the environment description.
	 * @return The environment description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the environment description.
	 * @param description The environment description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the current edition.
	 * @return The current edition.
	 */
	public EditionEntity getCurrent() {
		return current;
	}

	/**
	 * Sets the current edition.
	 * @param current The current edition.
	 */
	public void setCurrent(EditionEntity current) {
		if (!Objects.equal(current, getCurrent())) {
			this.contentTypeVersion = getContentTypeVersion() + 1;
			this.categoryVersion = getCategoryVersion() + 1;
			this.connectorVersion = getConnectorVersion() + 1;
			this.portalVersion = getPortalVersion() + 1;
			this.offlineVersion = getOfflineVersion() + 1;
			this.onlineVersion = getOnlineVersion() + 1;
		}
		this.current = current;
	}

	/**
	 * Returns the current edition id.
	 * @return The current edition id.
	 */
	public UUID getCurrentId() {
		if (current != null) {
			return current.getId();
		}
		return null;
	}

	/**
	 * Returns the internal path segment.
	 * @return The internal path segment.
	 */
	public String getInternalSegment() {
		if (internalSegment == null) {
			return INTERNAL_SEGMENT;
		}
		return internalSegment;
	}

	/**
	 * Sets the internal path segment.
	 * @param internalSegment The internal path segment.
	 */
	public void setInternalSegment(String internalSegment) {
		this.internalSegment = internalSegment;
	}

	/**
	 * Returns the maximun unsuccessful login attempts.
	 * @return The maximun unsuccessful login attempts.
	 */
	public Integer getMaxLoginAttempts() {
		if (maxLoginAttempts == null || maxLoginAttempts < 0) {
			return null;
		}
		return maxLoginAttempts;
	}

	/**
	 * Touches the maximun unsuccessful login attempts.
	 * @param maxLoginAttempts The maximun unsuccessful login attempts.
	 */
	public void setMaxLoginAttempts(Integer maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}

	/**
	 * Returns the current content types version.
	 * @return The current content types version.
	 */
	public int getContentTypeVersion() {
		return contentTypeVersion != null ? contentTypeVersion : 0;
	}

	/**
	 * Returns the current categories version.
	 * @return The current categories version.
	 */
	public int getCategoryVersion() {
		return categoryVersion != null ? categoryVersion : 0;
	}

	/**
	 * Returns the current connectors version.
	 * @return The current connectors version.
	 */
	public int getConnectorVersion() {
		return connectorVersion != null ? connectorVersion : 0;
	}

	/**
	 * Returns the current portal version.
	 * @return The current portal version.
	 */
	public int getPortalVersion() {
		return portalVersion != null ? portalVersion : 0;
	}

	/**
	 * Returns the current source mapping version.
	 * @return The current source mapping version.
	 */
	public int getMappingVersion() {
		return mappingVersion != null ? mappingVersion : 0;
	}

	/**
	 * Returns the current routing domain version.
	 * @return The current routing domain version.
	 */
	public int getDomainVersion() {
		return domainVersion != null ? domainVersion : 0;
	}

	/**
	 * Returns the current devices version.
	 * @return The current devices version.
	 */
	public int getDeviceVersion() {
		return deviceVersion != null ? deviceVersion : 0;
	}

	/**
	 * Returns the current offline version.
	 * @return The current offline version.
	 */
	public int getOfflineVersion() {
		return offlineVersion != null ? offlineVersion : 0;
	}

	/**
	 * Returns the current online version.
	 * @return The current online version.
	 */
	public int getOnlineVersion() {
		return onlineVersion != null ? onlineVersion : 0;
	}

	/**
	 * Touches the current content type version.
	 * @param user The user touching the environment.
	 */
	public void touchContentTypeVersion(UserEntity user) {
		this.contentTypeVersion = getContentTypeVersion() + 1;
		touchPortalVersion(user);
	}

	/**
	 * Touches the current category version.
	 * @param user The user touching the environment.
	 */
	public void touchCategoryVersion(UserEntity user) {
		this.categoryVersion = getCategoryVersion() + 1;
		touchPortalVersion(user);
	}

	/**
	 * Touches the current connector version.
	 * @param user The user touching the environment.
	 */
	public void touchConnectorVersion(UserEntity user) {
		this.connectorVersion = getConnectorVersion() + 1;
		touchOfflineVersion(user);
	}

	/**
	 * Touches the current portal version.
	 * @param user The user touching the environment.
	 */
	public void touchPortalVersion(UserEntity user) {
		this.portalVersion = getPortalVersion() + 1;
		touchOfflineVersion(user);
	}

	/**
	 * Touches the current offline version.
	 * @param user The user touching the environment.
	 */
	public void touchOfflineVersion(UserEntity user) {
		this.offlineVersion = getOfflineVersion() + 1;
		setUpdated(user);
	}

	/**
	 * Touches both the current offline and online versions.
	 * @param user The user touching the environment.
	 */
	private void touchBoth(UserEntity user) {
		this.offlineVersion = getOfflineVersion() + 1;
		this.onlineVersion = getOnlineVersion() + 1;
		setUpdated(user);
	}

	/**
	 * Touches the current source mapping version.
	 * @param user The user touching the environment.
	 */
	public void touchMappingVersion(UserEntity user) {
		this.mappingVersion = getMappingVersion() + 1;
		touchBoth(user);
	}

	/**
	 * Touches the current routing domain version.
	 * @param user The user touching the environment.
	 */
	public void touchDomainVersion(UserEntity user) {
		this.domainVersion = getDomainVersion() + 1;
		touchBoth(user);
	}

	/**
	 * Touches the current devices version.
	 * @param user The user touching the environment.
	 */
	public void touchDeviceVersion(UserEntity user) {
		this.deviceVersion = getDeviceVersion() + 1;
		touchBoth(user);
	}

	/**
	 * Returns the environment editions.
	 * @return The environment editions.
	 */
	public Set<EditionEntity> getEditions() {
		return editions;
	}

	/**
	 * Returns the content types.
	 * @return The content types.
	 */
	public Set<ContentTypeEntity> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Returns the categories.
	 * @return The categories.
	 */
	public Set<CategoryEntity> getCategories() {
		return categories;
	}

	/**
	 * Returns the connectors.
	 * @return The connectors.
	 */
	public Set<ConnectorEntity> getConnectors() {
		return connectors;
	}

	/**
	 * Returns the portals.
	 * @return The portals.
	 */
	public Set<PortalEntity> getPortals() {
		return portals;
	}

	/**
	 * Returns the source mappings.
	 * @return The source mappings.
	 */
	public Set<SourceMappingEntity> getSourceMappings() {
		return sourceMappings;
	}

	/**
	 * Returns the routing domains.
	 * @return The routing domains.
	 */
	public Set<RoutingDomainEntity> getRoutingDomains() {
		return routingDomains;
	}

	/**
	 * Returns the current offline content types.
	 * @return The current offline content types.
	 */
	public Iterable<ContentTypeEntity> getOfflineContentTypes() {
		return notDeleted(contentTypes);
	}

	/**
	 * Returns the current offline categories.
	 * @return The current offline categories.
	 */
	public Iterable<CategoryEntity> getOfflineCategories() {
		return CategoryEntity.BY_ORDER.sortedCopy(notDeleted(getCategories()));
	}

	/**
	 * Returns the current offline category hierarchy.
	 * @return The current offline category hierarchy.
	 */
	public Hierarchy<UUID, CategoryEntity> getOfflineCategoryHierarchy() {
		ImmutableHierarchy.Builder<UUID, CategoryEntity> builder = ImmutableHierarchy.builder();
		for (CategoryEntity category : getOfflineCategories()) {
			final UUID parentId = category.getCurrent().getParentId();
			builder.add(category.getId(), category, parentId);
		}
		return builder.get();
	}

	/**
	 * Returns the current offline connectors.
	 * @return The current offline connectors.
	 */
	public Iterable<ConnectorEntity> getOfflineConnectors() {
		return notDeleted(connectors);
	}

	/**
	 * Returns the current offline portals.
	 * @return The current offline portals.
	 */
	public Iterable<PortalEntity> getOfflinePortals() {
		return notDeleted(portals);
	}

}
