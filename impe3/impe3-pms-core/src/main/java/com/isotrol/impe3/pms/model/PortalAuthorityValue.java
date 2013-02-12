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


import static com.google.common.base.Objects.equal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;
import com.isotrol.impe3.pms.api.PortalAuthority;


/**
 * Value that represents a granted portal authority.
 * @author Andres Rodriguez
 */
@Embeddable
public class PortalAuthorityValue {
	/** Authority. */
	@Column(name = "PMS_USER_PA", nullable = false, length = Lengths.NAME)
	private PortalAuthority portalAuthority;
	/** Portal. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRTL_ID", nullable = false)
	private PortalEntity portal;

	/** Default constructor. */
	public PortalAuthorityValue() {
	}

	/**
	 * Returns the portal authority.
	 * @return The portal authority.
	 */
	public PortalAuthority getPortalAuthority() {
		return portalAuthority;
	}

	/**
	 * Sets the portal authority.
	 * @param bean The portal authority.
	 */
	public void setPortalAuthority(PortalAuthority portalAuthority) {
		this.portalAuthority = portalAuthority;
	}

	/**
	 * Returns the portal.
	 * @return The portal.
	 */
	public PortalEntity getPortal() {
		return portal;
	}

	/**
	 * Sets the portal.
	 * @param portal The portal.
	 */
	public void setPortal(PortalEntity portal) {
		this.portal = portal;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(portalAuthority, portal);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PortalAuthorityValue) {
			final PortalAuthorityValue v = (PortalAuthorityValue) obj;
			return equal(portalAuthority, v.portalAuthority) && equal(portal, v.portal);
		}
		return false;
	}

}
