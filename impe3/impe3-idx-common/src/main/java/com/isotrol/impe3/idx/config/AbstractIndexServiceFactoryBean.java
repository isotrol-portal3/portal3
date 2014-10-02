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

package com.isotrol.impe3.idx.config;


import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;
import net.sf.lucis.core.Delays;
import net.sf.lucis.core.Factory;
import net.sf.lucis.core.IndexerService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Supplier;


/**
 * Abstract Index service factory bean.
 * @author Andres Rodriguez Chamorro
 */
abstract class AbstractIndexServiceFactoryBean implements FactoryBean<IndexerService>, InitializingBean, DisposableBean, BeanNameAware {
	/** Analyzer. */
	private final Analyzer analyzer;
	/** Delays. */
	private Delays delays = null;
	/** Pasive. */
	private boolean pasive = false;
	/** Bean name. */
	private String name = "IndexService";

	AbstractIndexServiceFactoryBean(Analyzer analyzer) {
		this.analyzer = checkNotNull(analyzer, "The analyzer must be provided");
	}

	public final void setDelays(Delays delays) {
		this.delays = delays;
	}

	public final void setPasive(boolean pasive) {
		this.pasive = pasive;
	}

	public final void setBeanName(String name) {
		this.name = firstNonNull(name, "IndexService");
	}

	final Analyzer getAnalyzer() {
		return analyzer;
	}

	final Delays getDelays() {
		return delays;
	}

	final boolean isPasive() {
		return pasive;
	}

	final Supplier<IndexWriterConfig> getSupplier() {
		return new ConfigSupplier(analyzer);
	}

	final String getName() {
		return name;
	}

	final String getWriterName() {
		return name + "Writer";
	}

	public final boolean isSingleton() {
		return true;
	}

	public final Class<?> getObjectType() {
		return IndexerService.class;
	}

	private static final class ConfigSupplier implements Supplier<IndexWriterConfig> {
		/** Analyzer. */
		private final Analyzer analyzer;

		ConfigSupplier(Analyzer analyzer) {
			this.analyzer = analyzer;
		}

		public IndexWriterConfig get() {
			return new IndexWriterConfig(Factory.get().version(), analyzer);
		}
	}
}
