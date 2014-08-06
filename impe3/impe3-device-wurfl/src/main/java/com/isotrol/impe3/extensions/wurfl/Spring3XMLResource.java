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

package com.isotrol.impe3.extensions.wurfl;

import java.io.IOException;

import net.sourceforge.wurfl.core.resource.SpringResource;
import net.sourceforge.wurfl.core.resource.WURFLResource;
import net.sourceforge.wurfl.core.resource.WURFLResourceException;
import net.sourceforge.wurfl.core.resource.XMLResource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.context.support.ServletContextResource;

public class Spring3XMLResource extends SpringResource implements WURFLResource {

	
	public Spring3XMLResource(Resource resource) {
		super(resource);
	}
	
	@Override
	protected WURFLResource createDelegate(Resource resource) {
		try {
			
			if(resource instanceof ClassPathResource) {
				return new XMLResource(resource.getInputStream());
			} else if (resource instanceof UrlResource) {
				return new XMLResource(resource.getInputStream());
			} else if (resource instanceof ServletContextResource) {
				return new XMLResource(resource.getInputStream());
			}
			else{
				return new XMLResource(resource.getFile());
			}
		} catch (IOException e) {
			throw new WURFLResourceException(this, e);
		}
	}
	
}
