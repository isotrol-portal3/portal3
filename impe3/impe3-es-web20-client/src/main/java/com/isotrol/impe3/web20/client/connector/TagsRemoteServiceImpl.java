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
package com.isotrol.impe3.web20.client.connector;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.TagDTO;
import com.isotrol.impe3.web20.api.TagsService;
import com.isotrol.impe3.web20.api.UsedTagDTO;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class TagsRemoteServiceImpl extends WithHessian<TagsService> implements TagsService {

	@Override
	protected Class<TagsService> serviceClass() {
		return TagsService.class;
	}

	@Override
	protected String serviceUrl() {
		return server() + "/tags";
	}
	
	public void addTags(String serviceId, String set, Set<String> tags, boolean valid) throws ServiceException {
		delegate().addTags(serviceId, set, tags, valid);
	}
	
	public void deleteTags(String serviceId, String set, Set<String> tags) throws ServiceException {
		delegate().deleteTags(serviceId, set, tags);
	}
	
	public List<TagDTO> getPendingTags(String serviceId, String set) throws ServiceException {
		return delegate().getPendingTags(serviceId, set);
	}

	public List<UsedTagDTO> getMostUsed(String serviceId, String set, int max) throws ServiceException {
		return delegate().getMostUsed(serviceId, set, max);
	}

	public List<UsedTagDTO> suggest(String serviceId, String set, String prefix, int max) throws ServiceException {
		return delegate().suggest(serviceId, set, prefix, max);
	}

	public void addTag(String serviceId, String set, String tag, boolean valid) throws ServiceException {
		delegate().addTag(serviceId, set, tag, valid);
	}

	public void deleteTag(String serviceId, String set, String tag) throws ServiceException {
		delegate().deleteTag(serviceId, set, tag);
	}

	public List<TagDTO> getTagSet(String serviceId, String set) throws ServiceException {
		return delegate().getTagSet(serviceId, set);
	}

	public void tag(String serviceId, String resource, String set, Set<String> tags, boolean valid)
		throws ServiceException {
		delegate().tag(serviceId, resource, set, tags, valid);
	}

	public void updateTag(String serviceId, String set, String tag, String name) throws ServiceException {
		delegate().updateTag(serviceId, set, tag, name);
	}
}
