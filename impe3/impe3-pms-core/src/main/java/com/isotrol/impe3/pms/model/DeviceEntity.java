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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.common.collect.Maps;
import com.isotrol.impe3.api.DeviceType;


/**
 * Entity that represents a device.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "DEVICE")
@NamedQuery(name = DeviceEntity.BY_NAME, query = "from DeviceEntity as e where e.name = ?")
public class DeviceEntity extends WithCreatedUpdatedEntity {
	/** Query by name. */
	public static final String BY_NAME = "device.byName";
	/** Device name. */
	@Column(name = "NAME", length = Lengths.NAME, unique = true)
	private String name;
	/** Device description. */
	@Column(name = "DESCRIPTION", length = Lengths.DESCRIPTION)
	private String description;
	/** Whether the device has layout. */
	@Column(name = "LAYOUT", nullable = false)
	private boolean layout = false;
	/** Layout width. */
	@Column(name = "WIDTH", nullable = true)
	private Integer width;
	/** Parent device. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_DVCE_ID", nullable = true)
	private DeviceEntity parent;
	/** Order in its category level. */
	@Column(name = "LEVEL_ORDER", nullable = true)
	private Integer order;
	/** Device type. */
	@Column(name = "DEVICE_TYPE", length = Lengths.NAME, nullable = true)
	private String type = DeviceType.HTML.toString();
	/** User agent. */
	@Column(name = "DVCE_UA", length = Lengths.DESCRIPTION, nullable = true)
	private String userAgent;
	/** Whether the user agent is a regular expression. */
	@Column(name = "DVCE_UA_RE", nullable = true)
	private Boolean userAgentRE;
	/** Device properties. */
	@ElementCollection
	@JoinTable(name = "DEVICE_PROPERTY", joinColumns = @JoinColumn(name = "DVCE_ID", nullable = false))
	@MapKeyColumn(name = "DVCE_PROP_NAME", length = Lengths.NAME)
	@Column(name = "DVCE_PROP_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> properties;

	/** Default constructor. */
	public DeviceEntity() {
	}

	/**
	 * Returns the device name.
	 * @return The device name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the device name.
	 * @param name The device name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the device description.
	 * @return The device description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the device description.
	 * @param description The device description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the layout width.
	 * @return The layout width.
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * Sets the layout width.
	 * @param width The layout width.
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * Returns the parent device.
	 * @return The parent device.
	 */
	public DeviceEntity getParent() {
		return parent;
	}

	/**
	 * Returns the parent device id.
	 * @return The parent device id.
	 */
	public UUID getParentId() {
		return parent != null ? parent.getId() : null;
	}

	/**
	 * Sets the parent device.
	 * @param parent The parent device.
	 */
	public void setParent(DeviceEntity parent) {
		this.parent = parent;
	}

	/**
	 * Returns the order in its device level.
	 * @return The order in its device level.
	 */
	public int getOrder() {
		return order != null ? order.intValue() : 0;
	}

	/**
	 * Sets the order in its device level.
	 * @param order The order in its device level.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Returns the device type.
	 * @return The device type.
	 */
	public String getType() {
		return type != null ? type : DeviceType.HTML.toString();
	}

	/**
	 * Sets the device name.
	 * @param name The device name.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the user agent.
	 * @return The user agent.
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Sets the user agent.
	 * @param userAgent The user agent.
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Returns whether the user agent is a regular expression.
	 * @return True if the user agent is a regular expression.
	 */
	public boolean isUserAgentRE() {
		return userAgentRE != null ? userAgentRE.booleanValue() : false;
	}

	/**
	 * Sets whether the the user agent is a regular expression.
	 * @param userAgentRE True if the the user agent is a regular expression.
	 */
	public void setUserAgentRE(boolean userAgentRE) {
		this.userAgentRE = userAgentRE;
	}

	/**
	 * Returns the device properties.
	 * @return The device properties.
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = Maps.newHashMap();
		}
		return properties;
	}
}
