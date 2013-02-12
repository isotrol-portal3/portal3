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

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.google.common.base.Objects;


/**
 * Value that represents a component in a page.
 * @author Andres Rodriguez
 */
@Embeddable
public class CIPValue implements WithPosition {
	/** Component module. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CMPT_ID", nullable = true)
	private ComponentEntity component;
	/** Component Configuration. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CNFG_ID", nullable = true)
	private ConfigurationEntity configuration;
	/** Exported bean. */
	@Column(name = "CMPT_BEAN", nullable = true, length = Lengths.NAME)
	private String bean;
	/** Instance name. */
	@Column(name = "NAME", nullable = false, length = Lengths.NAME)
	private String name;
	/** Parent component. */
	@Type(type="impeId")
	@Column(name = "PARENT_CIP", length = Lengths.UUID, nullable = true)
	private UUID parent;
	/** Position within parent. */
	@Column(name = "CIP_POSITION", nullable = true)
	private Integer position;

	/** Default constructor. */
	public CIPValue() {
	}

	/**
	 * Returns the component module.
	 * @return The component module.
	 */
	public ComponentEntity getComponent() {
		return component;
	}

	/**
	 * Sets the component module.
	 * @param component The component module.
	 */
	public void setComponent(ComponentEntity component) {
		this.component = component;
	}

	/**
	 * Returns the component instance id.
	 * @return The component instance id.
	 */
	public UUID getInstanceId() {
		return component != null ? component.getId() : null;
	}

	/**
	 * Returns the component configuration.
	 * @return The component configuration.
	 */
	public ConfigurationEntity getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the component configuration.
	 * @param configuration The component configuration.
	 */
	public void setConfiguration(ConfigurationEntity configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns the exported bean.
	 * @return The exported bean.
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the exported bean.
	 * @param bean The exported bean.
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	/**
	 * Returns the instance name.
	 * @return The instance name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the instance name.
	 * @param name The instance name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the parent component.
	 * @return The parent component.
	 */
	public UUID getParent() {
		return parent;
	}

	/**
	 * Sets the parent component.
	 * @param parent The parent component.
	 */
	public void setParent(UUID parent) {
		this.parent = parent;
	}

	/**
	 * Returns the position within parent.
	 * @return The position within parent.
	 */
	public Integer getPosition() {
		return position == null ? 0 : position;
	}

	/**
	 * Sets the position within parent.
	 * @param parent The position within parent.
	 */
	public void setPosition(Integer position) {
		this.position = (position == null) ? 0 : position;
	}
	
	public boolean isSpace() {
		return (component == null || bean == null);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(component, configuration, bean, name, parent);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CIPValue) {
			final CIPValue v = (CIPValue) obj;
			return equal(component, v.component) && equal(configuration, v.configuration) && equal(bean, v.bean)
				&& equal(name, v.name) && equal(parent, v.parent);
		}
		return false;
	}
}
