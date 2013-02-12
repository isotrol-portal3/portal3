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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import net.sf.derquinsej.uuid.UUIDGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.PortalTxProvider;
import com.isotrol.impe3.pms.core.ValueLoader;
import com.isotrol.impe3.pms.core.dao.DAO;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.Entity;
import com.isotrol.impe3.pms.model.WithIdVersion;


/**
 * Abstract implementation for services providing UUID generator and general DAO.
 * @author Andres Rodriguez.
 */
public abstract class AbstractService implements PortalTxProvider {
	/** UUID Generator. */
	private UUIDGenerator uuidGenerator;
	/** PMS DAO. */
	private DAO dao;
	/** Transaction manager. */
	@Autowired
	private PlatformTransactionManager txManager;

	/**
	 * Constructor.
	 */
	public AbstractService() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalTxProvider#wrapInTx(com.google.common.base.Supplier, boolean)
	 */
	public final <T> Supplier<T> wrapInTx(Supplier<T> supplier, boolean memoize) {
		Supplier<T> tx = new TransactionalSupplier<T>(supplier);
		if (memoize) {
			return Suppliers.memoize(tx);
		}
		return tx;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PortalTxProvider#getTxSupplier(java.lang.Class, java.util.UUID, java.lang.Object,
	 * com.isotrol.impe3.pms.core.ValueLoader)
	 */
	public final <E extends WithIdVersion, V, P> Supplier<V> getTxSupplier(final Class<E> type, final UUID id,
		final P payload, final ValueLoader<E, V, P> loader) {
		checkNotNull(type);
		checkNotNull(id);
		checkNotNull(loader);
		final Supplier<V> s = new Supplier<V>() {
			public V get() {
				E entity = dao.findById(type, id, false);
				checkNotNull(entity, "Entity of type [%s] id [%s] not found", type.getName(), id);
				return loader.get(entity, payload);
			}

			@Override
			public String toString() {
				return String.format("[%s/%s/%s]", loader, type.getName(), id);
			}
		};
		return wrapInTx(s, true);
	}

	@Autowired
	public void setUuidGenerator(UUIDGenerator uuidGenerator) {
		this.uuidGenerator = uuidGenerator;
	}

	@Autowired
	public void setDao(DAO dao) {
		this.dao = dao;
	}

	protected final UUIDGenerator getUUIDGenerator() {
		return uuidGenerator;
	}

	protected final UUID newUUID() {
		return uuidGenerator.get();
	}

	protected final DAO getDao() {
		return dao;
	}

	/**
	 * Returns the default EntityNotFound provider.
	 * @return The default EntityNotFound provider.
	 */
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.DEFAULT;
	}

	protected final UUID toUUID(String id) throws EntityNotFoundException {
		return getDefaultNFP().toUUID(id);
	}

	protected final <T> T findById(Class<T> type, UUID id) {
		return dao.findById(type, id, false);
	}

	protected final <T> T load(Class<T> type, UUID id, NotFoundProvider nfp) throws EntityNotFoundException {
		if (id == null) {
			throw nfp.getNotFoundException(id);
		}
		final T entity = findById(type, id);
		if (entity == null) {
			throw nfp.getNotFoundException(id);
		}
		return entity;
	}

	protected final <T> T load(Class<T> type, String id, NotFoundProvider nfp) throws EntityNotFoundException {
		return load(type, nfp.toUUID(id), nfp);
	}

	protected final <T> T load(Class<T> type, UUID id) throws EntityNotFoundException {
		return load(type, id, getDefaultNFP());
	}

	protected final <T> T load(Class<T> type, String id) throws EntityNotFoundException {
		return load(type, id, getDefaultNFP());
	}

	protected <T extends Entity> T saveNewEntity(T entity) throws PMSException {
		if (entity.getId() == null) {
			entity.setId(newUUID());
		}
		dao.save(entity);
		return entity;
	}

	/**
	 * Deletes an entity.
	 * @param entity Entity.
	 */
	protected void deleteEntity(Object entity) {
		getDao().delete(entity);
	}

	protected final void flush() {
		dao.flush();
	}

	protected final void sync() {
		flush();
		dao.clear();
	}

	/**
	 * Support class for programmatic transaction management.
	 * @author Andres Rodriguez.
	 */
	private final class TransactionalSupplier<T> implements Supplier<T> {
		/** Target supplier. */
		private final Supplier<T> supplier;

		TransactionalSupplier(Supplier<T> supplier) {
			this.supplier = checkNotNull(supplier);
		}

		public final T get() {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setName(supplier.toString());
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			TransactionStatus status = txManager.getTransaction(def);
			boolean ok = false;
			try {
				T result = supplier.get();
				ok = true;
				return result;
			}
			finally {
				if (ok) {
					txManager.commit(status);
				} else {
					txManager.rollback(status);
				}
			}
		}

	}

}
