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

package com.isotrol.impe3.nr.audit.helpers;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.nr.audit.api.AuditEngineMode;
import com.isotrol.impe3.nr.audit.api.AuditOperations;
import com.isotrol.impe3.nr.audit.api.AuditService;
import com.isotrol.impe3.nr.audit.api.AuditTask;
import com.isotrol.impe3.nr.audit.api.AuditValidationMode;
import com.isotrol.impe3.nr.audit.api.AuditedContent;
import com.isotrol.impe3.nr.core.Portal3Document;

public class TaskGenerator {

	
	public static final List<AuditTask> generateTask(AuditService auditService, String indexId,  List<Portal3Document> documents,AuditEngineMode engineMode){
		
		
		final List<AuditTask> contenidos= Lists.newLinkedList();
		
		if (auditService!=null && indexId!=null && engineMode!=null){
			final Collection<AuditedContent> valoresAnteriores;
			final Collection<AuditedContent> valoresIndexados= Lists.newLinkedList();
			valoresAnteriores= auditService.getContents(indexId);
			for (AuditedContent auditedContent : valoresAnteriores) {
				valoresIndexados.add(AuditedContent.simple(auditedContent.getContentId(), auditedContent.getContentTypeId(),ImmutableList.<String>of(), auditedContent.getLocale(), auditedContent.getIndexId(), engineMode, auditedContent.getContentTime()));
			}
			
			for (Portal3Document document : documents) {
				String[] cats= document.getDocument().getValues(Schema.CATEGORY);
				
				final String contentTimeString=document.getDocument().get(Schema.DATE);
				final Long contentTime;
				if (contentTimeString!=null){
					contentTime = Long.parseLong(contentTimeString);
				} else {
					contentTime = null;
				}
				
				AuditedContent content = AuditedContent.withHash(document.getDocument().get(Schema.ID), UUID.fromString(document.getDocument().get(Schema.TYPE)),Lists.newArrayList(cats), document.getDocument().get(Schema.LOCALE), indexId, engineMode, document.getHash().toString(),contentTime);
				AuditedContent simpleContent = AuditedContent.simple(document.getDocument().get(Schema.ID), UUID.fromString(document.getDocument().get(Schema.TYPE)),ImmutableList.<String>of(), document.getDocument().get(Schema.LOCALE), indexId, engineMode, contentTime);
				if (valoresIndexados.remove(simpleContent)){
					if (!valoresAnteriores.remove(content)){
						contenidos.add(new AuditTask(content, AuditOperations.MODIFY,AuditValidationMode.HASH));
					}
				} else {
					contenidos.add(new AuditTask(content, AuditOperations.ADD,AuditValidationMode.HASH));
				}
			}
			for (AuditedContent auditedContent : valoresIndexados) {
				contenidos.add(new AuditTask(auditedContent, AuditOperations.DELETE,AuditValidationMode.HASH));
			}
			
		}
		
		return contenidos;
	}
}
