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
