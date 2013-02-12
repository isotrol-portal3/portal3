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

package com.isotrol.impe3.pms.core.impl;


import java.util.List;
import java.util.UUID;

import net.sf.derquinse.lucis.Group;
import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.google.common.collect.Lists;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.esvc.nr.ContentTypeCountDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodeDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodeRepositoryDTO;
import com.isotrol.impe3.pms.api.esvc.nr.NodeRepositoryExternalService;
import com.isotrol.impe3.pms.api.esvc.nr.NodesFilterDTO;
import com.isotrol.impe3.pms.api.esvc.nr.ResultDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.core.obj.ContentTypesObject;


/**
 * Implementation of NodeRepositoryExternalService
 * @author Emilio Escobar Reyero
 */
@Service("nodeRepositoryExternalService")
public class NodeRepositoryExternalServiceImpl extends AbstractExternalService<NodeRepository> implements
	NodeRepositoryExternalService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<String> fields = Lists.newArrayList(Schema.TITLE, Schema.DESCRIPTION, Schema.CONTENT_IDX);

	/** Default constructor. */
	public NodeRepositoryExternalServiceImpl() {
		super(NodeRepository.class);
	}

	
	/**
	 * Return node list
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ResultDTO<NodeDTO> getNodes(String repositoryId, NodesFilterDTO filter) throws PMSException {
		final NodeRepository repository = getExternalService(repositoryId);
		final NodeQuery query = (filter == null || (filter.getQuery() == null && filter.getContentTypeId() == null)) ? NodeQueries
			.matchAll()
			: filteredQuery(filter);
		final Page<Node> nodes = repository.getPage(query, null, null, false, 0, 100, null);
		final ContentTypesObject types = loadContentTypes();

		final ResultDTO<NodeDTO> result = new ResultDTO<NodeDTO>();
		final List<NodeDTO> list = Lists.newArrayListWithCapacity(nodes.getItems().size());

		result.setTotal(nodes.getTotalHits());
		result.setMillis(nodes.getTime());

		for (Node node : nodes.getItems()) {
			final ContentTypeSelDTO contentTypeSelDTO = new ContentTypeSelDTO();

			contentTypeSelDTO.setId(node.getNodeKey().getNodeType().toString());

			if (types.containsKey(node.getNodeKey().getNodeType())) {
				contentTypeSelDTO.setName(types.get(node.getNodeKey().getNodeType()).getDefaultName().getDisplayName());
				contentTypeSelDTO.setDescription(types.get(node.getNodeKey().getNodeType()).getDescription());
			}

			final NodeDTO dto = new NodeDTO();
			dto.setId(node.getNodeKey().getNodeId());

			dto.setDescription(node.getDescription());
			dto.setDate(Schema.calendarToString(node.getDate()));
			dto.setTitle(node.getTitle());

			dto.setContentTypeSelDTO(contentTypeSelDTO);

			list.add(dto);
		}

		result.setResults(list);

		return result;
	}

	private ContentTypeSelDTO getContentType(ContentTypesObject types, String id) {
		if (logger.isTraceEnabled()) {
			logger.trace("Trying to recover type for id ->{}<-", id);
		}
		UUID uuid = null;
		try {
			uuid = UUID.fromString(id);
		} catch (RuntimeException e) {
			logger.trace("Problem with id " + id,e);
		}
		if (uuid != null && types.containsKey(uuid)) {
			if (logger.isTraceEnabled()) {
				logger.trace("Content Type found for id ->{}<-", id);
			}
			return types.get(uuid).toSelDTO();
		}
		final ContentTypeSelDTO dto = new ContentTypeSelDTO();
		dto.setId(id);
		dto.setName(id);
		dto.setState(State.NEW);
		return dto;
	}

	@Transactional(rollbackFor = Throwable.class)
	/** Return repository summary */
	public NodeRepositoryDTO getSummary(String repositoryId) throws PMSException {
		if (logger.isTraceEnabled()) {
			logger.trace("Getting summary for repository service with id {}", repositoryId);
		}
		final NodeRepositoryDTO dto = new NodeRepositoryDTO();
		final NodeRepository repository = getExternalService(repositoryId);
		final GroupResult result = repository.groupBy(NodeQueries.matchAll(), null, Lists.newArrayList(Schema.TYPE));
		final Group root = result.getGroup();
		final ContentTypesObject types = loadContentTypes();

		dto.setNodeCount(result.getTotalHits());

		final List<ContentTypeCountDTO> contentTypes = Lists.newArrayListWithCapacity(root.getGroupNames().size());

		for (String name : root.getGroupNames()) {
			final Group g = root.getGroup(name);
			final ContentTypeCountDTO count = new ContentTypeCountDTO();
			count.setContentType(getContentType(types, name));
			count.setCount(g.getHits());
			contentTypes.add(count);
		}

		dto.setContentTypes(contentTypes);

		return dto;
	}

	@Transactional(rollbackFor = Throwable.class)
	/** Return content types */
	public List<ContentTypeSelDTO> getContentTypes(String repositoryId) throws PMSException {
		final ContentTypesObject types = loadContentTypes();

		return types.map2sel();
	}

	private NodeQuery filteredQuery(final NodesFilterDTO filter) {

		if (filter.getQuery() != null && filter.getContentTypeId() != null) {
			return NodeQueries.any(NodeQueries.anyString(filter.getQuery(), fields), NodeQueries.term(Schema.TYPE,
				filter.getContentTypeId()));
		} else if (filter.getQuery() == null && filter.getContentTypeId() != null) {
			return NodeQueries.term(Schema.TYPE, filter.getContentTypeId());
		} else if (filter.getQuery() != null && filter.getContentTypeId() == null) {
			return NodeQueries.anyString(filter.getQuery(), fields);
		}

		return null;
	}

}
