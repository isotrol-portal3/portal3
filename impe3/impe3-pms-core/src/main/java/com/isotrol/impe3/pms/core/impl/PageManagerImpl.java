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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.ConfigurationManager;
import com.isotrol.impe3.pms.core.PageManager;
import com.isotrol.impe3.pms.model.CIPValue;
import com.isotrol.impe3.pms.model.LayoutValue;
import com.isotrol.impe3.pms.model.PageDfn;
import com.isotrol.impe3.pms.model.PageEntity;


/**
 * Implementation of PageManager.
 * @author Andres Rodriguez.
 */
@Service
public final class PageManagerImpl extends AbstractContextService implements PageManager {
	@Autowired
	private ConfigurationManager configurationManager;

	/** Default constructor. */
	public PageManagerImpl() {
	}

	private final Function<CIPValue, CIPValue> cvDup = new Function<CIPValue, CIPValue>() {
		public CIPValue apply(CIPValue from) {
			try {
				final CIPValue v = new CIPValue();
				v.setComponent(from.getComponent());
				v.setConfiguration(configurationManager.duplicate(from.getConfiguration()));
				v.setBean(from.getBean());
				v.setName(from.getName());
				v.setParent(from.getParent());
				v.setPosition(from.getPosition());
				return v;
			} catch (PMSException e) {
				throw new IllegalStateException(e); // TODO
			}
		};
	};

	private final Function<LayoutValue, LayoutValue> lvDup = new Function<LayoutValue, LayoutValue>() {
		public LayoutValue apply(LayoutValue from) {
			final LayoutValue v = new LayoutValue();
			v.setWidth(from.getWidth());
			v.setName(from.getName());
			v.setComponent(from.getComponent());
			v.setParent(from.getParent());
			v.setPosition(from.getPosition());
			return v;
		};
	};

	/**
	 * @see com.isotrol.impe3.pms.core.PageManager#copyDfn(com.isotrol.impe3.pms.model.PageDfn,
	 * com.isotrol.impe3.pms.model.PageEntity)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public PageDfn copyDfn(PageDfn from, PageEntity to) {
		final PageDfn dfn = new PageDfn();
		dfn.setPage(to);
		dfn.setName(from.getName());
		dfn.setDescription(from.getDescription());
		dfn.setTag(from.getTag());
		dfn.setCategory(from.getCategory());
		dfn.setContentType(from.getContentType());
		dfn.setUmbrella(from.isUmbrella());
		dfn.setTemplate(from.getTemplate());
		dfn.getComponents().putAll(Maps.transformValues(from.getComponents(), cvDup));
		dfn.getLayout().putAll(Maps.transformValues(from.getLayout(), lvDup));
		try {
			saveNewEntity(dfn);
		} catch (PMSException e) {
			throw new IllegalStateException(e); // TODO
		}
		return dfn;
	}

	private final Duplicator duplicator = new Duplicator() {
		public PageDfn apply(PageDfn from) {
			return copyDfn(from, from.getPage());
		}
	};

	/**
	 * @see com.isotrol.impe3.pms.core.PageManager#getDuplicator()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Duplicator getDuplicator() {
		return duplicator;
	}
}
