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
package com.isotrol.impe3.web20.impl;


import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Indexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MembershipSelDTO;
import com.isotrol.impe3.web20.api.MembershipSelFilterDTO;
import com.isotrol.impe3.web20.server.LogTableDTO;
import com.isotrol.impe3.web20.server.LogTableManager;
import com.isotrol.impe3.web20.server.MemberSchema;
import com.isotrol.impe3.web20.server.MembershipManager;


/**
 * 
 * @author Emilio Escobar Reyero
 */
@Service("membersIndexer")
public class MembersIndexer implements Indexer<Long, Object> {

	/** Log table manager. */
	private LogTableManager logTable;
	/** Members service. */
	private MembersService membersService;
	/** Members service. */
	private MembershipManager membershipManager;

	/**
	 * @see net.sf.lucis.core.Indexer#index(java.lang.Object)
	 */
	public Batch<Long, Object> index(Long checkpoint) throws InterruptedException {
		if (checkpoint == null) {
			checkpoint = 0L;
		}
		
		
		final List<LogTableDTO> tasks = logTable.readBatch(checkpoint, "MMBR");
		final Batch.Builder<Long> builder = Batch.builder();

		for (LogTableDTO task : tasks) {
			if (0 == task.getTask()) {
				builder.delete(MemberSchema.ID, String.valueOf(task.getItem()));
			} else {
				try {
					final MemberDTO member = membersService.getById(null, task.getItem());
					final Document doc = docMapper.apply(member);
					addMembership(doc, member.getId());
					builder.update(doc, MemberSchema.ID, String.valueOf(task.getItem()));
				} catch (ServiceException e) {

				}
			}

			checkpoint = task.getId();
		}

		return builder.build(checkpoint);
	}

	private void addMembership(Document doc, String memberId) throws ServiceException {
		PageFilter<MembershipSelFilterDTO> filter = new PageFilter<MembershipSelFilterDTO>();
		filter.setPagination(new PaginationDTO(0, 1024));
		filter.setFilter(new MembershipSelFilterDTO().putId(memberId));
		PageDTO<MembershipSelDTO> page = membershipManager.getMemberships(null, filter);

		for (MembershipSelDTO dto : page.getElements()) {
			doc.add(new Field(MemberSchema.COMMUNITY, dto.getCommunity().getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));		
			doc.add(new Field(MemberSchema.COMMUNITYROL, dto.getCommunity().getId() + ":" + dto.getRole(), Field.Store.YES, Field.Index.NOT_ANALYZED));		
			doc.add(new Field(MemberSchema.COMMUNITYSTATUS, dto.getCommunity().getId() + ":" + dto.isValidated(), Field.Store.YES, Field.Index.NOT_ANALYZED));		
		}
	}
	
	/** Returns lucene Document from MemberDTO */
	public static final Function<MemberDTO, Document> docMapper = new Function<MemberDTO, Document>() {

		public Document apply(MemberDTO input) {
			Document doc = new Document();

			doc.add(new Field(MemberSchema.ID, input.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field(MemberSchema.CODE, input.getCode(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field(MemberSchema.NAME, input.getName(), Field.Store.YES, Field.Index.ANALYZED));
			if (input.getDisplayName() != null) {
				doc.add(new Field(MemberSchema.DISPLAYNAME, input.getDisplayName(), Field.Store.YES,
					Field.Index.ANALYZED));
			}
			if (input.getDisplayName() != null) {
				doc.add(new Field(MemberSchema.DISPLAYNAMEORDER, input.getDisplayName(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			}
			if (input.getEmail() != null) {
				doc.add(new Field(MemberSchema.EMAIL, input.getEmail(), Field.Store.YES, Field.Index.ANALYZED));
			}
			if (input.getDate() != null) {
				doc.add(new Field(MemberSchema.DATE, MemberSchema.dateToString(input.getDate()), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			}
			doc.add(new Field(MemberSchema.BLOCKED, input.isBlocked() ? "TRUE" : "FALSE", Field.Store.YES,
				Field.Index.NOT_ANALYZED));

			Set<String> profiles = input.getProfiles();
			if (profiles != null) {
				for (String profile : profiles) {
					doc.add(new Field(MemberSchema.PROFILE, profile, Field.Store.YES, Field.Index.ANALYZED));
				}
			}

			Map<String, String> properties = input.getProperties();
			if (properties != null) {
				for (String key : properties.keySet()) {
					if(key.equals(MemberSchema.PROPERTY_CENTER)) {
						doc.add(new Field(MemberSchema.property(key) + "ORDER", properties.get(key), Field.Store.YES, Field.Index.NOT_ANALYZED));
					}
					doc.add(new Field(MemberSchema.property(key), properties.get(key), Field.Store.YES, Field.Index.ANALYZED));
				}
			}

			return doc;
		}
	};

	@Autowired
	public void setLogTable(LogTableManager logTable) {
		this.logTable = logTable;
	}

	@Autowired
	public void setMembersService(MembersService membersService) {
		this.membersService = membersService;
	}
	
	@Autowired
	public void setMembershipManager(MembershipManager membershipManager) {
		this.membershipManager = membershipManager;
	}
	
	@Override
	public void afterCommit(Object payload) {
	}
}
