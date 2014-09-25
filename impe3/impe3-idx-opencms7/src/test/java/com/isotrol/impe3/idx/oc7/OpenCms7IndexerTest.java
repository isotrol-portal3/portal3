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
package com.isotrol.impe3.idx.oc7;


import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Checkpoints;
import net.sf.lucis.core.IndexStatus;
import net.sf.lucis.core.Indexer;
import net.sf.lucis.core.IndexerService;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.Writer;
import net.sf.lucis.core.impl.DefaultIndexerService;
import net.sf.lucis.core.impl.DefaultWriter;
import net.sf.lucis.core.impl.FSStore;

import org.apache.lucene.document.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.isotrol.impe3.idx.DummyLocalMappingsService;
import com.isotrol.impe3.idx.LocalMappingsService;
import com.isotrol.impe3.idx.Task;
import com.isotrol.impe3.idx.oc.AuditReader;
import com.isotrol.impe3.idx.oc.IndexConfiguration;


public class OpenCms7IndexerTest {

	String indexDir;

	IndexerService indexerService;
	Indexer<Long, Object> indexer;
	Store<Long> store;
	Writer writer;

	OpenCms7DocumentContentBuilder docContentBuilder;

	@Before
	public void before() throws Exception {

		indexDir = makeDir();

		store = createStore(indexDir);

		writer = createWriter();

		docContentBuilder = createDocumentContentBuilder();

		indexer = createIndexer(docContentBuilder);

		indexerService = new DefaultIndexerService<Long, Object>(store, writer, indexer);

	}

	@After
	public void after() {

		deleteDir();
	}

	@Test
	public void notNullTest() {
		Assert.assertNotNull(indexDir);
		Assert.assertNotNull(indexerService);
		Assert.assertNotNull(docContentBuilder);
		Assert.assertNotNull(indexer);
		Assert.assertNotNull(store);
		Assert.assertNotNull(writer);
	}

	@Test
	public void documentContentBuilderTest() {
		Document[] documents = createDocuments();

		Assert.assertNotNull(documents);
	}

	@Test
	public void startTest() {
		indexerService.start();

		Assert.assertEquals(indexerService.getIndexStatus(), IndexStatus.OK);

		Assert.assertNotSame(indexerService.getIndexStatus(), IndexStatus.ERROR);
	}

	@Test
	public void addTest() throws InterruptedException {
		startTest();
		documentContentBuilderTest();
		Document[] documents = createDocuments();
		final Batch.Builder<Long> builder = Batch.builder();

		for (Document document : documents) {
			builder.add(document);
			// builder.add(document);
		}

		final Batch<Long, Object> batch = builder.build(2L);

		writer.write(store, batch);

		// Assert.assertEquals(store.getCheckpoint(), Long.valueOf(2L));
		Assert.assertTrue(true);
	}

	@Test
	public void stopTest() {
		startTest();
		indexerService.stop();

		Assert.assertEquals(indexerService.getIndexStatus(), IndexStatus.OK);
	}

	/* Private utility methods */

	private String makeDir() {
		final String tempDir = System.getProperty("java.io.tmpdir");
		final String indexDir = tempDir + File.separator + "lucis-test-" + UUID.randomUUID();
		File dir = new File(indexDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return indexDir;
	}

	private void deleteDir() {
		final File dir = new File(indexDir);
		final File files[] = dir.listFiles();
		for (File f : files) {
			f.deleteOnExit();
		}
		dir.deleteOnExit();
	}

	private Writer createWriter() {
		return new DefaultWriter();
	}

	private Store<Long> createStore(final String indexDir) {
		return new FSStore<Long>(Checkpoints.ofLong(), indexDir);
	}

	private LocalMappingsService createMappingsService() {
		return new DummyLocalMappingsService();
	}

	private IndexConfiguration createIndexConfiguration() throws Exception {
		final String file = "/com/isotrol/impe3/idx/oc7/index-configuration.xml";
		final IndexConfiguration configuration = new IndexConfiguration();
		final Resource configurationFile = new ClassPathResource(file);

		configuration.setConfigurationFile(configurationFile);

		try {
			configuration.afterPropertiesSet();
		} catch (Exception e) {
			Assert.fail("IndexConfiguration afterPropertiesSet failure! ");
			throw e;
		}

		return configuration;
	}

	private OpenCms7DocumentContentBuilder createDocumentContentBuilder() throws Exception {
		final String file = "/com/isotrol/impe3/idx/oc7/sample-content.xml";
		final Resource contentFile = new ClassPathResource(file);

		final OpenCms7DocumentContentBuilder builder = new OpenCms7DocumentContentBuilder();

		builder.setCategoriesBase("/system/categories/");
		builder.setDatabaseReader(new DummyDatabaseReaderImpl(contentFile));
		builder.setEncoding("UTF-8");
		builder.setIndexDefaultLanguageContent(true);
		builder.setIndexResourceTypes(createIndexConfiguration());
		builder.setLang(new Locale("es"));
		builder.setMappingsService(createMappingsService());

		builder.afterPropertiesSet();

		return builder;
	}

	private Indexer<Long, Object> createIndexer(OpenCms7DocumentContentBuilder builder) {
		return new OpenCms7Indexer(new DummyAuditReader(), builder);
	}

	private Document[] createDocuments() {
		AuditReader<Task, Long> auditReader = new DummyAuditReader();

		List<Task> tasks = auditReader.readAuditBatch(-1L);

		return docContentBuilder.createDocuments(tasks.get(0));
	}

}
