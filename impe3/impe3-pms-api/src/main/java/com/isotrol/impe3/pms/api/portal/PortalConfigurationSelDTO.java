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

package com.isotrol.impe3.pms.api.portal;


import com.isotrol.impe3.pms.api.AbstractDescribedWithId;


/**
 * DTO for a portal configuration choice item template.
 * @author Enrique Diaz
 */
public final class PortalConfigurationSelDTO extends AbstractDescribedWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -344929315329715883L;
	
	//** Name. */
	private String beanName;

	private boolean validity;
	
	public enum EstadoHerencia {
	   PROPIO,
	   HEREDADO,
	   SOBREESCRITO;
		
		public String toString() {
			if (this.equals(EstadoHerencia.PROPIO)) {
				return "Propio".toString();
			} else if (this.equals(EstadoHerencia.HEREDADO)) {
				return "Heredado".toString();
			} else {
				return "Sobreescrito".toString();
			}
		}
	}

	private EstadoHerencia inherited;

	
	public PortalConfigurationSelDTO(String beanName, String name, String description, String portalId, boolean validity, EstadoHerencia inherited) {
		super();
		this.setName(name);
		this.setDescription(description);
		this.setId(portalId);
		this.beanName = beanName;
		this.validity = validity;
		this.inherited =inherited;
	}

	/** Default constructor. */
	public PortalConfigurationSelDTO() {
	}

	/**
	 * @return the beanName
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @param beanName the beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public boolean isValidity() {
		return validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public EstadoHerencia getInherited() {
		return inherited;
	}

	public void setInherited(EstadoHerencia inherited) {
		this.inherited = inherited;
	}
	
}
