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

package com.isotrol.impe3.lucene;


import java.io.Reader;

import net.sf.lucis.core.Factory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


/**
 * Portal standard analyzer.
 * @author Andres Rodriguez
 */
public final class PortalStandardAnalyzer extends Analyzer {
	/** Base analyzer. */
	private final Analyzer base = new StandardAnalyzer(Factory.get().version());

	/**
	 * Constructor.
	 */
	public PortalStandardAnalyzer() {
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return base.tokenStream(fieldName, reader);
	}

}