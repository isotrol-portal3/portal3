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

package com.isotrol.impe3.palette.atom.feed.img;


import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.api.component.atom.ATOMFactory;
import com.isotrol.impe3.api.component.atom.ATOMRenderer;
import com.isotrol.impe3.api.component.atom.SkeletalATOMRenderer;
import com.isotrol.impe3.connectors.uri.URIBuilderService;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;


/**
 * ATOM Img Component.
 * @author Emilio Escobar Reyero
 */
public class ImgComponent implements VisualComponent {
	/** Image URI builder. */
	private URIBuilderService imgUri;
	/** File loader. */
	private FileLoader fileLoader;
	/** Module Configuration. */
	private ImgModuleConfig moduleConfig;
	/** Component Configuration. */
	private ImgConfig config;

	/**
	 * Public constructor.
	 */
	public ImgComponent() {
	}

	/* Spring injection. */

	public void setImgUri(URIBuilderService imgUri) {
		this.imgUri = imgUri;
	}

	public void setModuleConfig(ImgModuleConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	public void setFileLoader(FileLoader fileLoader) {
		this.fileLoader = fileLoader;
	}

	/* IMPE injection. */

	@Inject
	public void setConfig(ImgConfig config) {
		this.config = config;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
	}

	String getURI(final RenderContext context) {
		if (config == null) {
			return null;
		}
		FileId file = config.file();
		if (file != null) {
			return context.getURI(file).toASCIIString();
		}
		String name = config.name();
		if (name == null) {
			return null;
		}
		if (moduleConfig != null) {
			file = moduleConfig.bundle();
			if (file != null) {
				try {
					fileLoader.loadFromBundle(file, name);
					return context.getURI(file, name).toASCIIString();
				} catch (Exception e) {}
			}
		}
		if (imgUri != null) {
			return imgUri.getURI(context, name);
		}
		return null;
	}

	String getA(final RenderContext context) {
		if (config == null) {
			return null;
		}
		final Category cg = config.category();
		final ContentType ct = config.contentType();
		PageKey pk = null;
		if (cg != null && ct != null) {
			pk = PageKey.contentType(NavigationKey.category(cg, ct));
		} else if (ct != null) {
			pk = PageKey.contentType(ct);
		} else if (cg != null) {
			pk = PageKey.navigation(cg);
		}
		if (pk != null) {
			return context.getURI(pk).toASCIIString();
		}
		return config.link();
	}

	@Renderer
	public ATOMRenderer atom (final RenderContext context) {
		return new SkeletalATOMRenderer() {
			@Override
			public SyndFeed getFeed(SyndFeed feed) {
				final String link = getA(context);
				final String url = getURI(context);
				final String title = config.title();
				final String description = config.description();
				
				if (link != null || url != null || title != null || description != null ) {
					final SyndImage image = ATOMFactory.image(url, title, description, link);
					feed.setImage(image);
					return feed;
				}
				
				return null;
			}
		};
	}
}
