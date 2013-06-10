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


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Indexer;
import nu.xom.Attribute;

import org.apache.lucene.document.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.isotrol.impe3.idx.LocalMappingsService;
import com.isotrol.impe3.idx.feedburner.api.FeedBurnerSchema;
import com.isotrol.impe3.nr.api.ISO9075;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.nr.core.DocumentBuilder;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


/**
 * Feed Flickr Rss timestamp based indexer.
 * @author Emilio Escobar Reyero
 */
public class FeedFlickrIndexer implements Indexer<Long,Object> {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private URL url;
	private LocalMappingsService mappingsService;
	private String defaultContent = "feed";
	private Function<SyndEntryImpl, Document> conversor;
	private boolean splitCategories = false;

	/**
	 * Initializing method, instances conversor function.
	 */
	public void init() {
		this.conversor = new Function<SyndEntryImpl, Document>() {

			public Document apply(SyndEntryImpl input) {
				final DocumentBuilder builder = new DocumentBuilder();

				final nu.xom.Document xml = getXmlDocument(input);

				@SuppressWarnings("unchecked")
				final Set<String> channels = categories(input);

				final String id = ISO9075.encode(input.getUri());
				final UUID nodeType = getContentType(channels, defaultContent);
				if (nodeType == null) {
					return null;
				}
				final Date date = input.getPublishedDate();
				final String title = input.getTitle();
				final String description = input.getDescription().getValue();

				@SuppressWarnings("unchecked")
				final List<SyndContentImpl> contents = input.getContents();

				final Set<UUID> cmapped = mappingsService.getCategories(null, null, channels, xml);
				final Set<String> smapped = mappingsService.getSets(null, null, channels, xml);

				builder.setNodeKey(NodeKey.of(nodeType, id));
				builder.setField(FeedBurnerSchema.ID, id, true, false);
				builder.setTitle(title);
				builder.setDate(date);

				builder.setExpirationDate(Schema.getMaxCalendar());
				builder.setReleaseDate(date);

				if (description != null) {
					builder.setDescription(description);
					builder.setText(description);
				}
				builder.addLocale("es"); // TODO

				for (String set : smapped) {
					builder.addSet(set);
				}

				for (UUID categoryKey : cmapped) {
					builder.addCategory(categoryKey);
				}

				for (String catName : channels) {
					builder.setField(FeedBurnerSchema.CATEGORY, catName, true, false);
				}

				if (contents != null && !contents.isEmpty()) {
					final StringBuilder sb = new StringBuilder();

					for (SyndContentImpl content : contents) {
						sb.append(content.getValue());
					}

					builder.setBytes(sb.toString().getBytes(), true);
				}

				return builder.get().getDocument();
			}

			private nu.xom.Document getXmlDocument(SyndEntryImpl input) {
				final nu.xom.Element item = new nu.xom.Element("item");

				final nu.xom.Element title = new nu.xom.Element("title");
				title.appendChild(input.getTitle());
				item.appendChild(title);

				final nu.xom.Element author = new nu.xom.Element("author");
				author.appendChild(input.getAuthor());
				item.appendChild(author);

				final nu.xom.Element link = new nu.xom.Element("link");
				link.appendChild(input.getLink());
				item.appendChild(link);

				final nu.xom.Element uri = new nu.xom.Element("uri");
				uri.appendChild(input.getUri());
				item.appendChild(uri);

				final nu.xom.Element publishedDate = new nu.xom.Element("publishedDate");
				publishedDate.appendChild(String.valueOf(input.getPublishedDate().getTime()));
				item.appendChild(publishedDate);

				final SyndContent description = input.getDescription();

				if (description != null) {
					final nu.xom.Element desc = new nu.xom.Element("description");
					if (description.getType() != null) {
						desc.addAttribute(new Attribute("type", description.getType()));
					}
					desc.appendChild(description.getValue());
					item.appendChild(desc);
				}

				final List<SyndCategory> categories = input.getCategories();

				if (categories != null) {
					final nu.xom.Element cats = new nu.xom.Element("categories");

					for (SyndCategory category : categories) {
						final nu.xom.Element cat = new nu.xom.Element("category");
						cat.appendChild(category.getName());
						cats.appendChild(cat);
					}

					item.appendChild(cats);
				}

				return new nu.xom.Document(item);
			}

			private Set<String> categories(SyndEntryImpl input) {
				final List<SyndCategoryImpl> categories = input.getCategories();

				if (categories != null && !categories.isEmpty()) {
					final Set<String> channels = splitCategories ? splitChannels(categories) : Sets
						.newHashSet(Collections2.transform(categories, CAT));

					return channels;
				} else {
					final Object others = input.getForeignMarkup();
					if (others instanceof List) {
						final List<Element> elements = (List<Element>) others;
						if (elements == null || elements.isEmpty()) {
							return Sets.newHashSetWithExpectedSize(0);
						}
						final Set<String> channels = Sets.newHashSet();
						for (Element elem : elements) {
							if ("category".equals(elem.getName())) {
								final String c = elem.getTextTrim();
								if (c != null && c.length() > 0) {
									if (splitCategories) {
										final StringTokenizer st = new StringTokenizer(c, " ");

										while (st.hasMoreElements()) {
											channels.add((String) st.nextElement());
										}

									} else {
										channels.add(c.replaceAll(" ", "_"));
									}
								}
							}
						}

						return channels;
					} else {
						return Sets.newHashSetWithExpectedSize(0);
					}
				}

			}

			private Set<String> splitChannels(List<SyndCategoryImpl> categories) {
				final Set<String> channels = Sets.newHashSet();

				for (SyndCategoryImpl category : categories) {
					final String c = category.getName();
					final StringTokenizer st = new StringTokenizer(c, " ");

					while (st.hasMoreElements()) {
						channels.add((String) st.nextElement());
					}

				}

				return channels;
			}

			private UUID getContentType(final Set<String> categories, final String defaultContent) {
				for (String category : categories) {
					final UUID uuid = mappingsService.getContentType(category);
					if (uuid != null) {
						return uuid;
					}
				}
				return mappingsService.getContentType(defaultContent);
			}
		};
	}

	/**
	 * Just call generateBatch method.
	 * @see net.sf.lucis.core.Indexer#index(java.lang.Object)
	 */
	public Batch<Long,Object> index(Long checkpoint) throws InterruptedException {
		if (logger.isDebugEnabled()) {
			logger.debug("[" + url + "] Beggining index checkpoint: {}", checkpoint);
		}

		final Batch<Long,Object> batch = generateBatch(checkpoint == null ? 0L : checkpoint);

		if (logger.isDebugEnabled()) {
			logger.debug("[" + url + "] New index checkpoint at {}", batch.getCheckpoint());
		}

		return batch;
	}

	private Batch<Long,Object> generateBatch(long startPoint) throws InterruptedException {
		if (logger.isTraceEnabled()) {
			logger.trace("[" + url + "] Batch starting at {} position.", startPoint);
		}
		long checkpoint = startPoint;
		final Batch.Builder<Long> builder = Batch.builder();

		final SyndFeed feed = getFeed();

		if (feed != null) {
			final Date pubDate = feed.getPublishedDate();
			final long pubTimestamp = pubDate.getTime();

			if (pubTimestamp > checkpoint) {
				@SuppressWarnings("unchecked")
				List<SyndEntryImpl> entries = feed.getEntries();

				for (SyndEntryImpl entry : entries) {
					final String id = entry.getUri();
					try {
						if (id != null) {
							final Document doc = conversor.apply(entry);
							if (doc != null) {
								builder.update(doc, FeedBurnerSchema.ID, ISO9075.encode(id));
							}
						}
					} catch (Exception e) {
						logger.warn("[" + url + "] Bad entry ", ISO9075.encode(id));
						logger.trace("[" + url + "] Error trace: ", e);
					}
				}
				checkpoint = pubTimestamp;
			}
		}

		if (logger.isTraceEnabled()) {
			logger.trace("[" + url + "] Batch ends at {} ", checkpoint);
		}

		return builder.build(checkpoint);
	}

	private SyndFeed getFeed() {
		try {
			final XmlReader xml = new XmlReader(url);
			final SyndFeedInput input = new SyndFeedInput();
			final SyndFeed feed = input.build(xml);
			return feed;
		} catch (IOException e) {
			if (logger.isTraceEnabled()) {
				logger.trace("[" + url + "] Error entrada/salida leyendo feed: " + url, e);
			} else {
				logger.warn("[" + url + "] Error entrada/salida leyendo feed: " + url);
			}
		} catch (IllegalArgumentException e) {
			if (logger.isTraceEnabled()) {
				logger.trace("[" + url + "] Formato feed no detectado: " + url, e);
			} else {
				logger.warn("[" + url + "] Formato feed no detectado:  " + url);
			}
		} catch (FeedException e) {
			if (logger.isTraceEnabled()) {
				logger.trace("[" + url + "] Feed no parseable: " + url, e);
			} else {
				logger.warn("[" + url + "] Feed no parseable: " + url);
			}
		}
		return null;
	}

	/**
	 * Sets feed url.
	 * @param url feed string url
	 * @throws MalformedURLException throwed by URL constructor.
	 */
	public void setUrl(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	/**
	 * Sets mappings local service helper
	 * @param mappingsService service
	 */
	public void setMappingsService(LocalMappingsService mappingsService) {
		this.mappingsService = mappingsService;
	}

	/**
	 * Sets default content name
	 * @param defaultContent content name
	 */
	public void setDefaultContent(String defaultContent) {
		this.defaultContent = defaultContent;
	}

	public void setSplitCategories(boolean splitCategories) {
		this.splitCategories = splitCategories;
	}

	private static final Function<SyndCategoryImpl, String> CAT = new Function<SyndCategoryImpl, String>() {
		public String apply(SyndCategoryImpl input) {
			final String name = input.getName();

			return name.replaceAll(" ", "_");
		}
	};

	public void afterCommit(Object payload) {
		// TODO Auto-generated method stub
		
	}
}
