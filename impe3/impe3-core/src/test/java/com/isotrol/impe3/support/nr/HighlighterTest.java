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

package com.isotrol.impe3.support.nr;


import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.derquinse.lucis.Page;
import net.sf.derquinsej.collect.ImmutableHierarchy;
import net.sf.derquinsej.i18n.Unlocalized;
import net.sf.derquinsej.uuid.TimeBasedUUIDGenerator;
import net.sf.derquinsej.uuid.UUIDGenerator;
import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.Writer;
import net.sf.lucis.core.impl.DefaultWriter;
import net.sf.lucis.core.impl.RAMStore;
import net.sf.lucis.core.support.Queryables;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.core.impl.CategoriesFactory;
import com.isotrol.impe3.core.impl.ContentTypesFactory;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.nr.core.DocumentBuilder;
import com.isotrol.impe3.nr.core.NodeRepositoryImpl;


/**
 * Highlight content repository terms.
 * @author Emilio Escobar Reyero
 * 
 */
public class HighlighterTest {
	private static UUIDGenerator generator = new TimeBasedUUIDGenerator();

	private final String[] lorem = new String[] {
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas arcu neque, porta sit amet eleifend in, molestie et orci. Quisque ac elit neque, a convallis metus. Quisque et cursus justo. Nam ultrices nunc quis nulla rhoncus sodales nec at nunc. Donec quis libero quis felis facilisis condimentum. Praesent diam mauris, mattis quis scelerisque a, iaculis ac tortor. Nam vestibulum, ante tristique ultrices tristique, lorem arcu bibendum metus, at eleifend lectus dui ut eros. Donec massa nulla, congue id placerat vitae, dignissim nec augue. Pellentesque sem massa, porttitor eu suscipit nec, volutpat vel lacus. Donec sed malesuada felis.",
		"Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed pharetra dignissim neque. Nullam viverra diam non lacus egestas ac ultrices felis pretium. In tempor, purus vitae facilisis tempus, risus turpis tristique nunc, eu aliquet elit lectus et mi. Duis vitae dapibus eros. Nullam elementum eleifend leo consectetur lacinia. Suspendisse posuere vulputate porttitor. Duis vitae erat nibh. Nulla ullamcorper mi quis mauris malesuada fringilla. Pellentesque nec purus nisi. Mauris varius, velit nec consectetur tristique, turpis nibh malesuada mi, sit amet posuere magna augue nec felis. Integer ligula velit, luctus at scelerisque non, interdum eget leo. Integer ut mi diam. Mauris faucibus, dolor vitae ullamcorper hendrerit, nisi orci elementum diam, et porta elit est vel sapien. Cras imperdiet purus quis nunc lacinia ultrices. Curabitur eu neque quis neque condimentum consequat nec sed mi. Sed in ante vel mauris lacinia egestas. ",
		"Sed pellentesque nunc a dui elementum sollicitudin volutpat nisl sodales. Proin molestie purus iaculis nulla ornare at malesuada metus sagittis. Fusce vehicula, massa rhoncus blandit pulvinar, magna orci commodo mi, vel pretium augue ante vitae est. Aliquam erat volutpat. Nam placerat commodo velit sit amet pharetra. Integer vulputate condimentum sagittis. Vestibulum tincidunt viverra enim, a sodales neque ornare eget. Aliquam id velit orci. Donec ac ipsum vel ipsum aliquam adipiscing id id nunc. Etiam sed hendrerit nunc. Integer at nunc nec dui elementum sodales. Nulla rutrum, ipsum vitae blandit bibendum, eros est pellentesque arcu, sed laoreet mi eros vitae magna. Pellentesque vulputate, lacus et laoreet rutrum, dui mauris vestibulum turpis, eget sagittis urna massa id nunc. Phasellus cursus auctor vestibulum. Aliquam ac nisi sit amet arcu venenatis interdum. Maecenas dui ligula, lacinia id auctor nec, adipiscing ac nisi. ",
		"Mauris vehicula enim sed urna rutrum sit amet dapibus ipsum iaculis. Ut commodo rutrum tortor, ac venenatis libero tincidunt sit amet. Donec ante tortor, auctor vel pulvinar suscipit, dapibus vel lacus. Maecenas ut felis nunc. Morbi vitae convallis tortor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut metus elit, vulputate convallis mattis vel, ullamcorper in urna. Phasellus volutpat faucibus convallis. Nunc eu nisi eu eros bibendum condimentum quis id lectus. Integer leo orci, eleifend in vulputate eu, pharetra non mauris. Sed posuere leo non dolor laoreet sed euismod lacus tincidunt. Pellentesque eget enim et eros ullamcorper vestibulum fermentum in orci. Morbi non sollicitudin lectus. Nunc rutrum rhoncus magna, a varius mi fermentum at. In hac habitasse platea dictumst. Duis bibendum metus ut ante euismod hendrerit. ",
		"Donec tincidunt blandit pulvinar. Aliquam erat volutpat. In sodales justo a dolor feugiat fermentum. Aenean at felis ac magna euismod lobortis in nec magna. Sed pellentesque orci ac ipsum cursus dignissim. Integer non lorem dolor. Cras ornare risus non ipsum fringilla elementum. Integer pulvinar sollicitudin ipsum, id aliquam quam semper et. Mauris nunc metus, mollis sed euismod sit amet, euismod at erat. Aenean nec enim non magna porttitor euismod pretium ac justo. ",
		"Nullam accumsan pulvinar tincidunt. Maecenas vitae commodo lorem. Nam a libero metus, vel aliquam massa. Vivamus euismod imperdiet lacinia. Ut diam ipsum, tempor eget euismod vel, tempus quis nisi. Sed non nisl lectus, ut accumsan orci. Maecenas venenatis lobortis sem, fermentum consequat ligula posuere quis. Vestibulum vitae nisi nec massa mollis laoreet. Aenean arcu erat, tempor sed pulvinar aliquam, faucibus vitae tortor. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse neque nibh, pulvinar a condimentum sit amet, cursus et orci. ",
		"Praesent consectetur dui euismod lorem accumsan ultrices. Suspendisse in sem sapien, nec accumsan urna. In hac habitasse platea dictumst. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed ipsum mi, facilisis quis ullamcorper sit amet, blandit vel augue. Donec eleifend lorem vel turpis fermentum et posuere est vestibulum. Proin at turpis diam, non tempor tellus. Aenean eu purus vitae felis porta consequat id sed nunc. Curabitur rutrum convallis elit, ac pharetra ipsum blandit sollicitudin. Sed volutpat purus id leo egestas vel sagittis mi pulvinar. In hendrerit ultrices nisl, at dictum turpis accumsan eget. Ut tempus, massa et egestas convallis, augue arcu tincidunt mi, sit amet hendrerit turpis neque id metus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla convallis ultrices mi at scelerisque. Aenean vestibulum pellentesque massa, eget faucibus nulla aliquet ac. Quisque tellus orci, sagittis eu rhoncus sit amet, tempus eget lorem. Quisque egestas, odio sit amet ultrices pretium, sem nulla ullamcorper felis, non congue nisl mi nec libero. Sed vitae sapien sit amet nisl lobortis mattis. Quisque convallis adipiscing aliquet. Sed molestie orci nec mauris dignissim rutrum.",
		"Aliquam nec egestas urna. Nulla ligula nisi, gravida at gravida et, suscipit id urna. Etiam cursus nisl porta leo auctor euismod. Vivamus elit risus, mollis id blandit in, cursus id augue. Morbi dapibus luctus fermentum. Duis quis sapien vitae ante scelerisque euismod eu et neque. Nunc vestibulum massa elementum turpis ultricies venenatis. Fusce sagittis nibh porta enim molestie convallis. Ut vestibulum suscipit ante quis facilisis. Nullam nulla nisi, aliquet nec congue at, aliquet non sem. Integer vestibulum dignissim sem egestas luctus.",
		"Vestibulum auctor magna ac est dictum quis mattis nisl viverra. Praesent id erat et est scelerisque porttitor eget ultrices justo. Aliquam nec urna lacus, rutrum suscipit neque. In hac habitasse platea dictumst. Suspendisse diam dolor, euismod ac porta vel, feugiat eget odio. Pellentesque nec neque dolor. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed a lorem metus. Proin sagittis convallis quam at interdum. Fusce vel pretium diam.",
		"Suspendisse cursus hendrerit nunc, quis tempus lacus gravida vitae. Nullam tincidunt placerat tellus non iaculis. Phasellus luctus eros at massa elementum tempor. Morbi eros lorem, lobortis vitae dapibus at, congue sit amet mauris. Aenean at blandit quam. Cras eu tellus eget massa congue consequat. Integer dapibus, elit in imperdiet sodales, felis dui faucibus augue, ac imperdiet libero velit tempor diam. Phasellus et facilisis ipsum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nunc eget leo sapien. Curabitur mollis, neque quis suscipit viverra, nunc nulla mollis odio, nec dapibus justo quam eget metus. Nunc accumsan, velit ac rutrum condimentum, libero lectus pulvinar odio, ut eleifend tellus tortor et odio. Quisque sit amet augue nec dolor consequat congue."};

	private ContentType contentType(String name) {
		return contentType(generator.get(), name);
	}

	private ContentType contentType(UUID id, String name) {
		final ContentType ct = ContentType.builder().setId(id).setName(Unlocalized.of(Name.of(name, name))).get();
		return ct;
	}

	private Category category(String name, UUID parent) {
		return category(generator.get(), name, parent);
	}

	private Category category(UUID id, String name, UUID parent) {
		final Category c = Category.builder().setId(id).setName(Unlocalized.of(Name.of(name, name))).setVisible(true)
			.setRoutable(true).get();
		return c;
	}

	public ContentRepository setupRepository() throws InterruptedException {
		final Set<ContentType> contentTypesBuilder = Sets.newHashSet();
		final ImmutableHierarchy.Builder<UUID, Category> categoriesBuilder = ImmutableHierarchy.builder();

		final ContentType texto = contentType("Texto");
		contentTypesBuilder.add(texto);

		final ContentType beca = contentType("Beca");
		contentTypesBuilder.add(beca);

		final ContentType normativa = contentType("Normativa");
		contentTypesBuilder.add(normativa);

		final Category root = category("root", null);
		categoriesBuilder.add(root.getId(), root, null);

		final ContentTypes contentTypes = ContentTypesFactory.of(contentTypesBuilder);
		final Categories categories = CategoriesFactory.of(categoriesBuilder.get());

		final Batch.Builder<Long> batchBuilder = Batch.builder();

		for (String str : lorem) {
			DocumentBuilder document = new DocumentBuilder();
			String title = str.substring(0, str.indexOf('.'));

			document.setTitle(title);
			document.setText(str);
			document.setDate(new Date());
			document.addCategory(root.getId());
			document.setDescription(str);
			document.addLocale("es");
			document.setBytes(str.getBytes(), true);
			document.setNodeKey(NodeKey.of(texto.getId(), generator.get().toString()));

			batchBuilder.add(document.get());
		}

		final Batch<Long, Object> batch = batchBuilder.build(1L);
		final Store<Long> store = new RAMStore<Long>();
		final Writer writer = new DefaultWriter();
		writer.write(store, batch);
		final Queryable queryable = Queryables.simple(store);
		final NodeRepository nodeRepository = new NodeRepositoryImpl(queryable);

		final ContentRepository contentRepository = new ContentRepository(nodeRepository, contentTypes, categories);

		return contentRepository;
	}

	@Test
	public void highlighterTermQueryTest() throws InterruptedException {
		final ContentRepository contentRepository = setupRepository();

		final Page<Content> result = contentRepository.getPage(NodeQueries.term(Schema.DESCRIPTION, "lorem"), null,
			true, 0, 3, ImmutableMap.of(Schema.DESCRIPTION, Integer.valueOf(3)));

		Assert.assertNotNull(result);

		Assert.assertTrue(result.getTotalHits() > 0);

		for (Content c : result) {
			Map<String, Collection<String>> highlighted = c.getHighlighted();

			Collection<String> h = highlighted.get(Schema.DESCRIPTION);

			Assert.assertNotNull(h);

			Assert.assertTrue(h.size() > 0);

			for (String s : h) {
				Assert.assertTrue(s.toLowerCase().indexOf("<b>lorem</b>") > -1);
			}
		}
	}

	@Test
	public void highlighterStringQueryTest() throws InterruptedException {
		final ContentRepository contentRepository = setupRepository();

		final Page<Content> result = contentRepository.getPage(
			NodeQueries.string(Schema.DESCRIPTION, "\"lorem ipsum\""), null, true, 0, 3,
			ImmutableMap.of(Schema.DESCRIPTION, Integer.valueOf(3)));

		Assert.assertNotNull(result);
		Assert.assertTrue(result.getTotalHits() > 0);

		for (Content c : result) {
			Map<String, Collection<String>> highlighted = c.getHighlighted();

			Collection<String> h = highlighted.get(Schema.DESCRIPTION);

			Assert.assertNotNull(h);

			Assert.assertTrue(h.size() > 0);

			for (String s : h) {
				Assert.assertTrue(s.toLowerCase().indexOf("<b>lorem</b>") > -1
					|| s.toLowerCase().indexOf("<b>ipsum</b>") > -1);
			}
		}
	}

	@Test
	public void highlighterString2QueryTest() throws InterruptedException {
		final ContentRepository contentRepository = setupRepository();

		final Page<Content> result = contentRepository.getPage(NodeQueries.string(Schema.DESCRIPTION, "lorem ipsum"),
			null, true, 0, 3, ImmutableMap.of(Schema.DESCRIPTION, Integer.valueOf(3)));

		Assert.assertNotNull(result);
		Assert.assertTrue(result.getTotalHits() > 0);

		for (Content c : result) {
			Map<String, Collection<String>> highlighted = c.getHighlighted();

			Collection<String> h = highlighted.get(Schema.DESCRIPTION);

			Assert.assertNotNull(h);

			Assert.assertTrue(h.size() > 0);

			for (String s : h) {
				Assert.assertTrue(s.toLowerCase().indexOf("<b>lorem</b>") > -1
					|| s.toLowerCase().indexOf("<b>ipsum</b>") > -1);
			}
		}
	}

	@Test
	public void highlighterString3QueryTest() throws InterruptedException {
		final ContentRepository contentRepository = setupRepository();

		final Page<Content> result = contentRepository.getPage(
			NodeQueries.any(NodeQueries.string(Schema.DESCRIPTION, "lorem"),
				NodeQueries.string(Schema.DESCRIPTION, "ipsum")), null, true, 0, 3,
			ImmutableMap.of(Schema.DESCRIPTION, Integer.valueOf(3)));

		Assert.assertNotNull(result);
		Assert.assertTrue(result.getTotalHits() > 0);

		for (Content c : result) {
			Map<String, Collection<String>> highlighted = c.getHighlighted();

			Collection<String> h = highlighted.get(Schema.DESCRIPTION);

			Assert.assertNotNull(h);

			Assert.assertTrue(h.size() > 0);

			for (String s : h) {
				Assert.assertTrue(s.toLowerCase().indexOf("<b>lorem</b>") > -1
					|| s.toLowerCase().indexOf("<b>ipsum</b>") > -1);
			}
		}
	}

	@Test
	public void highlighterWildcardQueryTest() throws InterruptedException {
		final ContentRepository contentRepository = setupRepository();

		final Page<Content> result = contentRepository.getPage(
			NodeQueries.any(NodeQueries.string(Schema.DESCRIPTION, "lorem"),
				NodeQueries.wildcard(Schema.DESCRIPTION, "lorem*")), null, true, 0, 3,
			ImmutableMap.of(Schema.DESCRIPTION, Integer.valueOf(3)));

		Assert.assertNotNull(result);
		Assert.assertTrue(result.getTotalHits() > 0);

		for (Content c : result) {
			Map<String, Collection<String>> highlighted = c.getHighlighted();

			Collection<String> h = highlighted.get(Schema.DESCRIPTION);

			Assert.assertNotNull(h);

			Assert.assertTrue(h.size() > 0);

			for (String s : h) {
				Assert.assertTrue(s.toLowerCase().indexOf("<b>lorem</b>") > -1
					|| s.toLowerCase().indexOf("<b>ipsum</b>") > -1);
			}
		}
	}

}
