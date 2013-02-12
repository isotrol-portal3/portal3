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

package com.isotrol.impe3.mappings.jdbc;


import static com.google.common.base.Preconditions.checkNotNull;

import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.isotrol.impe3.mappings.MappingsDTO;
import com.isotrol.impe3.mappings.MappingsService;


/**
 * A MappingsService JDBC implementation that programmatically handles transactions.
 * @author Andres Rodriguez
 */
public class ManualTxMappingsServiceImpl implements MappingsService {
	/** Service implementation. */
	private final MappingsService service;
	/** Transaction template. */
	private final TransactionTemplate transactionTemplate;

	/**
	 * Constructor.
	 * @param manager Transaction Manager.
	 * @param dataSource Data source.
	 */
	public ManualTxMappingsServiceImpl(PlatformTransactionManager manager, DataSource dataSource) {
		checkNotNull(manager, "The transaction manager must be provided");
		checkNotNull(dataSource, "The data source must be provided");
		this.transactionTemplate = new TransactionTemplate(manager);
		this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		final MappingsServiceImpl s = new MappingsServiceImpl();
		s.setDataSource(dataSource);
		s.afterPropertiesSet();
		this.service = s;
	}

	/**
	 * 
	 * @see com.isotrol.impe3.mappings.MappingsService#getMappings(java.lang.String, java.lang.String)
	 */
	public MappingsDTO getMappings(final String environment, final String source) {
		return transactionTemplate.execute(new TransactionCallback<MappingsDTO>() {
			public MappingsDTO doInTransaction(TransactionStatus status) {
				return service.getMappings(environment, source);
			}
		});
	}

	/**
	 * 
	 * @see com.isotrol.impe3.mappings.MappingsService#getMappingsIfModified(java.lang.String, java.lang.String, int)
	 */
	public MappingsDTO getMappingsIfModified(final String environment, final String source, final int version) {
		return transactionTemplate.execute(new TransactionCallback<MappingsDTO>() {
			public MappingsDTO doInTransaction(TransactionStatus status) {
				return service.getMappingsIfModified(environment, source, version);
			}
		});
	}

}
