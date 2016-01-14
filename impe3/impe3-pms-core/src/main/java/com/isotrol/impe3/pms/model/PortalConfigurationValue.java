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


import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.ConfigurationManager;


/**
 * Value that represents a portal configuration value.
 * @author Enrique Diaz
 */
@Embeddable
public class PortalConfigurationValue implements Cloneable {

	/** Configuration */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CNFG_ID", nullable = false)
	private ConfigurationEntity portalConfiguration;
	
	/** Default constructor. */
	public PortalConfigurationValue() {
	}

	/**
	 * Constructor.
	 * @param name The device name.
	 * @param defaultDevice True if the device is the default device.
	 * @param use The device name use.
	 */
	public PortalConfigurationValue(ConfigurationEntity portalConfiguration) {
		this.portalConfiguration = portalConfiguration;
	}

	/**
	 * Clones the value.
	 */
	public PortalConfigurationValue clone() {
		return new PortalConfigurationValue(portalConfiguration);
	}


	/**
	 * @return the portalConfiguration
	 */
	public ConfigurationEntity getPortalConfiguration() {
		return portalConfiguration;
	}

	/**
	 * @param portalConfiguration the portalConfiguration to set
	 */
	public void setPortalConfiguration(ConfigurationEntity portalConfiguration) {
		this.portalConfiguration = portalConfiguration;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(portalConfiguration);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PortalConfigurationValue) {
			final PortalConfigurationValue v = (PortalConfigurationValue) obj;
			return portalConfiguration == v.portalConfiguration;
		}
		return false;
	}

	public PortalConfigurationValue clone(ConfigurationManager cm) throws PMSException {
		if (portalConfiguration == null) {
			return null;
		}
		ConfigurationEntity c = (portalConfiguration != null) ? cm.duplicate(portalConfiguration) : null;
		return new PortalConfigurationValue(c);
	}
}
