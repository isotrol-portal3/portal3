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


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Entity that represents a portal.
 * @author Andres Rodriguez
 */
@javax.persistence.Entity
@Table(name = "PORTAL")
@NamedQueries({
	@NamedQuery(name = PortalEntity.OFFLINE, query = "from PortalEntity as e inner join fetch e.current inner join fetch e.defaultDevice where e.deleted is false and e.environment.id = ?"),
	@NamedQuery(name = PortalEntity.PFM, query = "from PortalEntity as e where e.everPublished is null")})
public class PortalEntity extends PublishableEntity<PortalEntity, PortalDfn, PortalEdition> {
	/** Query: Offline portals. */
	public static final String OFFLINE = "portal.offline";
	/** Query: Published flag migration. */
	public static final String PFM = "portal.pfm";
	/** Current definition. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "PRTL_DFN_ID", nullable = true)
	private PortalDfn current;
	/** Components version. */
	@Column(name = "CMPT_VERSION", nullable = true)
	private Integer componentVersion = 0;
	/** Page version. */
	@Column(name = "PAGE_VERSION", nullable = true)
	private Integer pageVersion = 0;
	/** Offline version. */
	@Column(name = "OFFLINE_VERSION", nullable = true)
	private Integer offlineVersion = 0;
	/** Definitions. */
	@OneToMany(mappedBy = "entity", fetch = FetchType.LAZY)
	private Set<PortalDfn> definitions;
	/** Default device. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "DVCE_ID", nullable = false)
	private DeviceEntity defaultDevice;

	/** Default constructor. */
	public PortalEntity() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.PublishableEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return "Portal";
	}

	/**
	 * Returns the current definition.
	 * @return The current definition.
	 */
	public PortalDfn getCurrent() {
		return current;
	}

	/**
	 * Sets the current definition.
	 * @param current The current definition.
	 */
	public void setCurrent(PortalDfn current) {
		this.current = current;
	}

	/**
	 * Returns the current components version.
	 * @return The current components version.
	 */
	public int getComponentVersion() {
		return componentVersion != null ? componentVersion : 0;
	}

	/**
	 * Returns the current pages version.
	 * @return The current pages version.
	 */
	public int getPageVersion() {
		return pageVersion != null ? pageVersion : 0;
	}

	/**
	 * Returns the current offline version.
	 * @return The current offline version.
	 */
	public int getOfflineVersion() {
		return offlineVersion != null ? offlineVersion : 0;
	}

	/**
	 * Increments the current offline version.
	 */
	private void touch() {
		this.offlineVersion = getOfflineVersion() + 1;
	}

	/**
	 * Increments the current components version.
	 */
	private void touchComponent() {
		this.componentVersion = getComponentVersion() + 1;
	}

	/**
	 * Increments the current pages version.
	 */
	private void touchPages() {
		this.pageVersion = getPageVersion() + 1;
	}

	/**
	 * Touches the current components version.
	 * @param user The user touching the portal.
	 */
	public void touchComponentVersion(UserEntity user) {
		touchComponent();
		touch();
		getEnvironment().touchPortalVersion(user);
	}

	/**
	 * Touches the current pages version.
	 * @param user The user touching the portal.
	 */
	public void touchPagesVersion(UserEntity user) {
		touchPages();
		touch();
		getEnvironment().touchPortalVersion(user);
	}

	/**
	 * Touches the current component and pages version.
	 * @param user The user touching the portal.
	 */
	public void touchChildrenVersions(UserEntity user) {
		touchComponent();
		touchPages();
		touch();
		getEnvironment().touchPortalVersion(user);
	}

	/**
	 * Touches the current offline version.
	 * @param user The user touching the environment.
	 */
	public void touchOfflineVersion(UserEntity user) {
		touch();
		getEnvironment().touchPortalVersion(user);
	}

	/**
	 * Touches every version.
	 * @param user The user touching the portal.
	 */
	public void touchAll(UserEntity user) {
		touchComponent();
		touchPages();
		touch();
		getEnvironment().touchPortalVersion(user);
	}

	@Override
	public Set<PortalDfn> getDefinitions() {
		return definitions;
	}

	/**
	 * Returns the default device.
	 * @return The default device.
	 */
	public DeviceEntity getDefaultDevice() {
		return defaultDevice;
	}

	/**
	 * Sets the default device.
	 * @param layout The default device.
	 */
	public void setDefaultDevice(DeviceEntity defaultDevice) {
		this.defaultDevice = defaultDevice;
	}

}
