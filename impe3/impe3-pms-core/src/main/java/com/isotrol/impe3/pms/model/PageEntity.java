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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.isotrol.impe3.pms.api.page.PageClass;


/**
 * Entity that represents a page.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "PAGE")
@NamedQueries( {
	@NamedQuery(name = PageEntity.USED, query = "from PortalEntity p join p.current.pages g where p.environment.id = :envId and g.template.id = :id"),
	@NamedQuery(name = PageEntity.DFNS, query = "from PageDfn d where d.page.portal.environment.id = :envId and d.page.id = :id"),
	@NamedQuery(name = PageEntity.BY_DEVICE, query = "from PageEntity p where p.device.id = :id")})
public class PageEntity extends OfPortal {
	/** Is page used in offline. */
	public static final String USED = "page.used";
	/** Definitions of a page. */
	public static final String DFNS = "page.dfns";
	/** By device. */
	public static final String BY_DEVICE = "page.byDevice";
	/** Device. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "DVCE_ID", nullable = false)
	private DeviceEntity device;
	/** Page class. */
	@Enumerated
	@Column(name = "PAGE_CLASS", nullable = false)
	private PageClass pageClass;

	/** Default constructor. */
	public PageEntity() {
	}

	/**
	 * Returns the layout.
	 * @return The layout.
	 */
	public DeviceEntity getDevice() {
		return device;
	}

	/**
	 * Sets the device.
	 * @param device The device.
	 */
	public void setDevice(DeviceEntity device) {
		this.device = device;
	}

	/**
	 * Returns the page class.
	 * @return The page class.
	 */
	public PageClass getPageClass() {
		return pageClass;
	}

	/**
	 * Sets the page class.
	 * @param pageClass The page class.
	 */
	public void setPageClass(PageClass pageClass) {
		this.pageClass = pageClass;
	}
}
