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

package com.isotrol.impe3.idx.feedburner;


import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Indexer;

import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.idx.DummyLocalMappingsService;


/**
 * 
 * @author Emilio Escobar Reyero
 * 
 */
public class FeedBurnerIndexerTest {

	// private final String url = "http://feeds.feedburner.com/es/gizmodo?format=xml";
	private final String url = "http://feeds2.feedburner.com/microsiervos/avion?format=xml";

	// private final String url =
	// "http://api.flickr.com/services/feeds/photos_public.gne?tags=plane&lang=es-us&format=rss_200";

	@Test
	public void ok() {
	}

	// @Test
	public void indexTest() throws InterruptedException {
		Indexer<Long,Object> indexer = null;

		try {
			indexer = create();

		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}

		final Batch<Long,Object> batch = indexer.index(null);

		Assert.assertNotNull(batch);
		Assert.assertFalse(batch.isEmpty());

	}

	private Indexer<Long,Object> create() throws Exception {
		FeedBurnerIndexer indexer = new FeedBurnerIndexer();
		indexer.setDefaultContent("feed");
		indexer.setUrl(url);
		indexer.setMappingsService(new DummyLocalMappingsService());

		indexer.init();

		return indexer;
	}

}
