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
package com.isotrol.impe3.oi.impl;


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
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.oi.api.InterviewDTO;
import com.isotrol.impe3.oi.api.InterviewsService;
import com.isotrol.impe3.oi.server.InterviewSchema;
import com.isotrol.impe3.oi.server.LogTableDTO;
import com.isotrol.impe3.oi.server.LogTableManager;


/**
 * Interviews indexer.
 * @author Emilio Escobar Reyero
 */
@Service("oiInterviewsIndexer")
public class InterviewsIndexer implements Indexer<Long, Object> {

	/** Log table manager. */
	@Autowired
	private LogTableManager logTable;
	/** Interviews service. */
	@Autowired
	private InterviewsService interviewsService;

	/**
	 * @see net.sf.lucis.core.Indexer#index(java.lang.Object)
	 */
	public Batch<Long, Object> index(Long checkpoint) throws InterruptedException {
		if (checkpoint == null) {
			checkpoint = 0L;
		}

		final List<LogTableDTO> tasks = logTable.readBatch(checkpoint, "INTE");
		final Batch.Builder<Long> builder = Batch.builder();

		for (LogTableDTO task : tasks) {
			if (0 == task.getTask()) {
				builder.delete(InterviewSchema.ID, String.valueOf(task.getItem()));
			} else {
				try {
					final InterviewDTO interview = interviewsService.getById(null, task.getItem());
					final Document doc = docMapper.apply(interview);
					builder.update(doc, InterviewSchema.ID, String.valueOf(task.getItem()));
				} catch (ServiceException e) {

				}
			}

			checkpoint = task.getId();
		}

		return builder.build(checkpoint);
	}

	/** Returns lucene Document from MemberDTO */
	public static final Function<InterviewDTO, Document> docMapper = new Function<InterviewDTO, Document>() {

		public Document apply(InterviewDTO input) {
			Document doc = new Document();

			doc.add(new Field(InterviewSchema.ID, input.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));

			if (input.getTitle() != null) {
				doc.add(new Field(InterviewSchema.TITLE, input.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
			}
			if (input.getDescription() != null) {
				doc.add(new Field(InterviewSchema.DESCRIPTION, input.getDescription(), Field.Store.YES,
					Field.Index.ANALYZED));
			}
			if (input.getAuthor() != null) {
				doc.add(new Field(InterviewSchema.AUTHOR, input.getAuthor(), Field.Store.YES, Field.Index.ANALYZED));
			}
			if (input.getInterviewee() != null) {
				doc.add(new Field(InterviewSchema.INTERVIEWEE, input.getInterviewee(), Field.Store.YES,
					Field.Index.ANALYZED));
			}

			doc.add(new Field(InterviewSchema.ALLOWED, input.isNewQuestionsAllowed() ? "TRUE" : "FALSE",
				Field.Store.YES, Field.Index.NOT_ANALYZED));

			if (input.getDate() != null) {
				doc.add(new Field(InterviewSchema.DATE, InterviewSchema.dateToString(input.getDate()), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			}
			if (input.getRelease() != null) {
				doc.add(new Field(InterviewSchema.RELEASE, InterviewSchema.dateToString(input.getRelease()),
					Field.Store.YES, Field.Index.NOT_ANALYZED));
			} else {
				doc.add(new Field(InterviewSchema.RELEASE, InterviewSchema.getMinDateString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			}
			if (input.getExpiration() != null) {
				doc.add(new Field(InterviewSchema.EXPIRATION, InterviewSchema.dateToString(input.getExpiration()),
					Field.Store.YES, Field.Index.NOT_ANALYZED));
			} else {
				doc.add(new Field(InterviewSchema.EXPIRATION, InterviewSchema.getMaxDateString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			}

			Map<String, String> properties = input.getProperties();
			if (properties != null) {
				for (String key : properties.keySet()) {
					doc.add(new Field(InterviewSchema.property(key), properties.get(key), Field.Store.YES,
						Field.Index.ANALYZED));
				}
			}

			Map<String, Set<String>> classes = input.getClasses();
			if (classes != null) {
				for (String set : classes.keySet()) {
					Set<String> classifications = classes.get(set);
					for (String classification : classifications) {
						doc.add(new Field(InterviewSchema.CLASS, set + ":" + classification, Field.Store.YES,
							Field.Index.NOT_ANALYZED));
					}
				}
			}

			return doc;
		}
	};

	public void setLogTable(LogTableManager logTable) {
		this.logTable = logTable;
	}

	public void setInterviewsService(InterviewsService interviewsService) {
		this.interviewsService = interviewsService;
	}
	
	@Override
	public void afterCommit(Object payload) {
	}
}
