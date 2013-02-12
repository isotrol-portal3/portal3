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

package com.isotrol.impe3.pms.api.minst;


import java.io.Serializable;
import java.util.List;


/**
 * Template DTO for reference to a provision of a module.
 * @author Andres Rodriguez
 */
public class ProvidedTemplateDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 3197974440647401903L;
	/** Current provider. */
	private ProviderDTO current;
	/** Possible providers. */
	private List<ProviderDTO> providers;

	/** Default constructor. */
	public ProvidedTemplateDTO() {
	}

	/**
	 * Returns the current provider.
	 * @return The current provider.
	 */
	public ProviderDTO getCurrent() {
		return current;
	}

	/**
	 * Sets the current provider.
	 * @param current The current provider.
	 */
	public void setCurrent(ProviderDTO current) {
		this.current = current;
	}

	/**
	 * Returns the possible provider.
	 * @return The possible provider.
	 */
	public List<ProviderDTO> getProviders() {
		return providers;
	}

	/**
	 * Sets the possible provider.
	 * @param providers The possible provider.
	 */
	public void setProviders(List<ProviderDTO> providers) {
		this.providers = providers;
	}

	/**
	 * Fills a ProvidedDTO with the data from this template.
	 * @param dto DTO to fill
	 * @return The filled DTO.
	 */
	public <T extends ProvidedDTO> T fill(T dto) {
		if (current == null) {
			return null;
		}
		final String bean = current.getBean();
		final ModuleInstanceSelDTO instance = current.getCurrent();
		if (bean == null || instance == null) {
			return null;
		}
		final String id = instance.getId();
		if (id == null) {
			return null;
		}
		dto.setConnectorId(id);
		dto.setBean(bean);
		return dto;
	}

	/**
	 * Returns a ProvidedDTO filled with the data from this template.
	 * @return The requested DTO.
	 */
	public ProvidedDTO toProvidedDTO() {
		return fill(new ProvidedDTO());
	}
}
