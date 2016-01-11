package com.isotrol.impe3.pms.gui.client.data.impl;

import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO;

public class PortalConfigurationInstanceSelModelData extends DTOModelData<PortalConfigurationSelDTO> {
	
	
	
	
	
	/**
	 * <br/>
	 * @param dto
	 */
	public PortalConfigurationInstanceSelModelData(PortalConfigurationSelDTO dto) {
		super(dto);
	}
	
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;
	
	public static final String PROPERTY_DESCRIPTION = Constants.PROPERTY_DESCRIPTION;
	
	public static final String PROPERTY_VALIDITY = Constants.PROPERTY_VALIDITY;
	
	public static final String PROPERTY_HERENCY =  Constants.PROPERTY_HERENCY;
	
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_NAME,PROPERTY_DESCRIPTION,
			PROPERTY_VALIDITY,PROPERTY_HERENCY);
	@Override
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	@Override
	protected Object doGet(String property) {
		Object res = null;
		PortalConfigurationSelDTO config = getDTO();

		if (property.equals(PROPERTY_DESCRIPTION)) {
			res = config.getDescription();
		} else if (property.equals(PROPERTY_NAME)) {
			res = config.getName();
		} else if (property.equals(PROPERTY_VALIDITY)) {
			res = config.isValidity();
		} else if (property.equals(PROPERTY_HERENCY)) {
			res = config.isInherited();
		} else {
			throw new IllegalArgumentException("Property not readable: " + property);
		}

		return res;
		
	}

	@Override
	protected Object doSet(String property, Object value) {
		throw new IllegalArgumentException("Property not writable: " + property);
	}


}
